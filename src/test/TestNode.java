package test;

import structure.Graph;
import structure.LoadGraphException;
import structure.Node;
import structure.NodeType;

public class TestNode {
	
	public static void main(String[] args) throws LoadGraphException {
		Graph graph = new Graph("graph.csv");
		
		Node chanas = graph.getNodeList().get("Chanas");
		if (chanas != null) {
			System.out.println("Chanas' neighbors : " + chanas.getNeighbors() + "\n");
			System.out.println("Chanas' restaurant neighbors : " + chanas.getNeighbors(NodeType.RESTAURANT) + "\n");
			
			Node lyon = graph.getNodeList().get("Lyon");
			System.out.println("Lyon is at two jumps from Chanas : " + lyon.isTwoDistance(chanas));
		}
	}
	
}