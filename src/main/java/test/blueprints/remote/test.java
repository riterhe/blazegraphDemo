package test.blueprints.remote;

import org.apache.log4j.Logger;

import com.bigdata.blueprints.BigdataGraph;
import com.bigdata.blueprints.BigdataGraphFactory;
import com.bigdata.blueprints.BigdataVertex;
import com.bigdata.bop.joinGraph.rto.Vertex;

public class test {
	public static final Logger log = Logger.getLogger(SampleBlazegraphBlueprintsRemote.class);

    public static final String serviceURL = "http://localhost:9999/";
    
	public static void main(String[] args) {
		final BigdataGraph graph = BigdataGraphFactory.connect(serviceURL);
		String words = "我";
		graph.addVertex(words);
		System.out.println(graph.getVertices().toString());
	}
}
