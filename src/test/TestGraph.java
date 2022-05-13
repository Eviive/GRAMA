package test;

import structure.Graph;
import structure.LinkType;
import structure.LoadGraphException;
import structure.NodeType;

public class TestGraph {
	
	public static void main(String[] args) throws LoadGraphException {
		Graph graph = new Graph("graph.csv");
		
		graph.display();
		System.out.println("\n\n");
		
		graph.display(NodeType.CITY);
		
		System.out.println("\n\nNumber of recreation centers : " + graph.getNumberNodeType(NodeType.RECREATION));
		
		System.out.println("Number of highways : " + graph.getNumberLinkType(LinkType.HIGHWAY));
	}

}