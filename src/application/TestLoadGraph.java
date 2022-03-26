package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import structure.Graph;
import structure.Node;
import structure.NodeType;
import structure.Link;
import structure.LinkType;
import structure.LoadGraphException;

public class TestLoadGraph {
	
	public static void main(String[] args) {
		Graph graph = new Graph();
		File file = new File("graph.csv");
		try {
			BufferedReader readGraph = new BufferedReader(new FileReader(file));
			// loads all the nodes into the nodeList ArrayList
			String row;
			while ((row = readGraph.readLine()) != null) {
				String node[] = row.split(":")[0].split("/");
				graph.getNodeList().put(node[1], new Node(node[0].charAt(0), node[1]));
			}
			readGraph.close();
			
			// loads all the links into their right nodes
			readGraph = new BufferedReader(new FileReader(file));
			while ((row = readGraph.readLine()) != null) {
				String elements[] = row.split(":");
				
				// selects the node corresponding to the links
				String[] depart = elements[0].split("/");
				Node nodeStart = graph.getNodeList().get(depart[1]);
				
				// fills the neighborsList with the neighbors of the node
				for (int i = 1; i < elements.length; i++) {
					String element[] = elements[i].split("/");
					Node destination = graph.getNodeList().get(element[3]);
					nodeStart.addLink(new Link(element[0].charAt(0), Integer.parseInt(element[1]), destination));
				}
			}
			readGraph.close();
			
			graph.display();
		} catch (IOException | LoadGraphException e) {
			e.printStackTrace(System.err);
		}
		
		Node iut = graph.getNodeList().get("IUT");
		Node dijon = graph.getNodeList().get("DIJON");
		System.out.println("\nNumber of cities : " + graph.getNumberNodeType(NodeType.RECREATION));
		System.out.println("IUT's direct neighbors : " + iut.getNeighbors());
		System.out.println("IUT's direct restaurant neighbors : " + iut.getNeighbors(NodeType.RESTAURANT));
		System.out.println("Are IUT and Dijon at two distance from each other : " + iut.isTwoDistance(dijon));
	}
	
}