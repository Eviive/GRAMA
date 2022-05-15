package test;

import java.util.ArrayList;
import structure.Graph;
import structure.LoadGraphException;
import structure.Node;
import structure.NodeType;

/**
 * Test class for Node
 * @author VAILLON Albert
 * @version JDK 11.0.13
 */
public class TestNode {
	
	public static void main(String[] args) throws LoadGraphException {
		Graph graph = new Graph();
		graph.load("graph.csv");
		
		Node chanas = graph.getNodeMap().get("Chanas");
		if (chanas != null) {
			System.out.println("Chanas' neighbors : " + chanas.getNeighbors(1, new ArrayList<>()) + "\n");
			System.out.println("Chanas' restaurant neighbors : " + chanas.getNeighbors(2, new ArrayList<>(), NodeType.RESTAURANT) + "\n");
			
			Node lyon = graph.getNodeMap().get("Lyon");
			System.out.println("Lyon is at two jumps from Chanas : " + lyon.isTwoDistance(chanas));
			
			System.out.println("Lyon is connected to more two jump cities : " + lyon.isMoreLinkedToType(chanas, NodeType.CITY));
		}
	}
	
}