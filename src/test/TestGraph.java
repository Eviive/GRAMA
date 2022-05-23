package test;

import structure.Graph;
import structure.LinkType;
import structure.LoadGraphException;
import structure.NodeType;

/**
 * Test class for Graph
 * @author VAILLON Albert
 * @version JDK 11.0.13
 */
public class TestGraph {
	
	public static void main(String[] args) throws LoadGraphException {
		Graph graph = new Graph();
		graph.load("graph.csv");
		
		graph.display();
		System.out.println("\n\n");
		
		graph.display(NodeType.CITY);
		
		System.out.println("\n\nNumber of recreation centers : " + graph.getNumberNodes(NodeType.RECREATION));
		
		System.out.println("Number of highways : " + graph.getNumberLinks(LinkType.HIGHWAY));
	}

}