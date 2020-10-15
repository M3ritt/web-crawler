package assignment3;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Runner {
	private static List<Graph.Vertex> nodes;
	private static List<Graph.Edge> edges;
	
	public static void main(String[] args) {
		nodes = new ArrayList<Graph.Vertex>();
		edges = new ArrayList<Graph.Edge>();
		Graph graph = new Graph(nodes, edges);
		FileWork fw = new FileWork(graph);
		WebsiteWork ww = new WebsiteWork(fw, graph);
//		String searchingFor = "https://en.wikipedia.org/wiki/Computer_engineering";
		//To actually create the graph, need to getWebsite data from a website
//		ww.getWebsiteData("https://en.wikipedia.org/wiki/Hash_table");
		
		fw.readVertex();
		fw.readEdge();
		
		graph = fw.getGraph();
		GUI gui = new GUI(ww, fw, graph);
		gui.startScreen();
	}
}
