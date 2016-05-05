package embedded;

import java.io.File;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.bigdata.blueprints.BigdataGraph;
import com.bigdata.blueprints.BigdataGraphFactory;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.util.wrappers.batch.BatchGraph;

public class Test {
	protected static final Logger log = Logger.getLogger(SampleBlazegraphBlueprintsEmbedded.class);
	private static final String journalFile = "/home/riter/toolkit/blazegraph/testJournal-" + System.currentTimeMillis()
			+ ".jnl";
	
	public static void main(String[] args) throws Exception {
		final File f = new File(journalFile);				
		
		//Make sure were starting with a clean file.
		f.delete();
		final BigdataGraph bigdataGraph = BigdataGraphFactory.create(journalFile);
		BatchGraph graph = BatchGraph.wrap(bigdataGraph, 100);
		
		Vertex vertex1 = graph.getVertex("1");
        if (vertex1 == null) {
        	vertex1 = graph.addVertex("1");
        }
		vertex1.setProperty("name", "张三");
		vertex1.setProperty("age", "张三");
		Vertex vertex2 = graph.getVertex("2");
        if (vertex2 == null) {
        	vertex2 = graph.addVertex("2");
        }
		vertex2.setProperty("name", "李四");
		vertex2.setProperty("age", "李四");

		Edge currentEdge = graph.addEdge("1", vertex1, vertex2, "儿子");
		currentEdge.setProperty("weight", 10);

        graph.commit();
		for(Vertex vertex : bigdataGraph.getVertices()){
			System.out.println(vertex.toString() + " name " + vertex.getProperty("name"));
		}
		
		for(Edge edge : bigdataGraph.getEdges()){
			System.out.println(edge.toString() + " weight " + edge.getProperty("weight"));
		}
	}
}
