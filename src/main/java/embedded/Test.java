package embedded;

import java.io.File;
import java.net.URLDecoder;
import java.util.List;

import org.apache.log4j.Logger;

import com.bigdata.blueprints.BigdataGraph;
import com.bigdata.blueprints.BigdataGraphFactory;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.NLPTokenizer;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

public class Test {
	protected static final Logger log = Logger.getLogger(SampleBlazegraphBlueprintsEmbedded.class);
	private static final String journalFile = "/home/riter/toolkit/blazegraph/blazegraph-extraction.jnl";
	
	public static void main(String[] args) throws Exception {
		Test test = new Test();
		test.createGraph();
		test.readVertex();
		test.readEdge();
		String[] testLines = {"NTA变更为NTB","NTA更名为NTB","NTA又名NTB","NTA现在更名为NTB"};
		for (int i = 0; i < testLines.length; i++) {
			List<Term> termList = NLPTokenizer.segment(testLines[i]);
			System.out.println(testLines[i] + "结果：" + test.queryGraph(termList));
		}
	}
	
	public void createGraph() throws Exception {
		final File f = new File(journalFile);				
		f.delete();
		final BigdataGraph bigdataGraph = BigdataGraphFactory.create(journalFile);
		System.out.println("\n=======创建数据库======\n");
		//BatchGraph graph = BatchGraph.wrap(bigdataGraph, 1000);
		String[] lines = {"NTA举行揭牌仪式，正式更名为NTB","NTA变更为NTB"};
		for(int lineIndex=0; lineIndex<lines.length; lineIndex++){
			List<Term> termList = NLPTokenizer.segment(lines[lineIndex]);
			System.out.println(termList.toString());
			//[原/b, 北京军区总医院/nt, 举行/v, 揭牌/v, 仪式/n, ，/w, 正式/ad, 更名/vi, 为/p, 陆军总医院/nt]
			//[公司/nt, 拟将/d, 名称/n, 由/p, 山东联创节能新材料股份有限公司/nt, 变更/v, 为/p, 山东联创互联网传媒股份有限公司/nt]
			if (termList.size()<3) {
				continue;
			}
			Term lastTerm = termList.get(0);
			Vertex lastVertex = bigdataGraph.addVertex(lastTerm.word);
			lastVertex.setProperty("word", lastTerm.word);
			lastVertex.setProperty("nature", lastTerm.nature.toString());
			
			for(int i=1; i < termList.size(); i++){
				Term term = termList.get(i);
				Vertex vertex = bigdataGraph.addVertex(term.word);
		        vertex.setProperty("word", term.word);
		        vertex.setProperty("nature", term.nature.toString());
		        
		        String name = lastTerm.word + "-" + term.word;
		        bigdataGraph.addEdge(name, lastVertex, vertex, name);
		        lastTerm = term;
		        lastVertex = vertex;
			}
		}
		bigdataGraph.shutdown();
	}
	
	public void readVertex() throws Exception {
		BigdataGraph bigdataGraph = BigdataGraphFactory.open(journalFile, true);
		System.out.println("\n=======读取数据库顶点======\n");
		for(Vertex vertex : bigdataGraph.getVertices()){
			System.out.println(URLDecoder.decode(vertex.toString()) + vertex.getProperty("nature"));
		}
		bigdataGraph.shutdown();
	}
	
	public void readEdge() throws Exception {
		BigdataGraph bigdataGraph = BigdataGraphFactory.open(journalFile, true);
		System.out.println("\n=======读取数据库边======\n");
		for(Edge edge : bigdataGraph.getEdges()){
			System.out.println(URLDecoder.decode(edge.getId().toString()));
		}
		bigdataGraph.shutdown();
	}
	
	public boolean queryGraph(List<Term> termList) throws Exception {
		BigdataGraph bigdataGraph = BigdataGraphFactory.open(journalFile, true);
		System.out.println("\n=======查询数据库======\n");
		System.out.println(termList.toString());
		//BigdataGraphQuery query = new BigdataGraphQuery(bigdataGraph);
		if (termList.size()<3) {
			return false;
		}
		Term lastTerm = termList.get(0);
		for(int i=1; i<termList.size(); i++){
			Term nowTerm = termList.get(i);
			String name = lastTerm.word + "-" + nowTerm.word;
			lastTerm = nowTerm;
			Edge edge = bigdataGraph.getEdge(name);
			if (edge == null) {
				bigdataGraph.shutdown();
				return false;
			}
		}
		bigdataGraph.shutdown();
		return true;
	}
}
