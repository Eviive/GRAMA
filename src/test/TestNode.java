package test;

import java.util.ArrayList;
import structure.Graph;
import structure.ItineraryException;
import structure.LoadGraphException;
import structure.Node;
import structure.NodeType;

/**
 * Test class for Node
 * @author VAILLON Albert
 * @author BAUDRY Lilian
 * @version JDK 11.0.13
 */
public class TestNode {
	
	public static void main(String[] args) throws LoadGraphException {
		Graph graph = new Graph();
		graph.load("graph.csv");
		
		Node chanas = graph.getNode("Chanas");
		Node lyon = graph.getNode("Lyon");
		Node vienne = graph.getNode("Vienne");
		
		if (chanas != null ) {
			System.out.println("Chanas' neighbors : " + chanas.getNeighbors(1, new ArrayList<>()) + "\n");
			System.out.println("Chanas' restaurant neighbors : " + chanas.getNeighbors(2, new ArrayList<>(), NodeType.RESTAURANT) + "\n");
			
			try{
				System.out.println("Chanas is " + chanas.getShortestPath(graph.getNode("Annonay")).getDistance() + " km from Annonay");
			}catch(ItineraryException e){
				System.err.println(e.getMessage());
			}
			
			if (lyon != null){
				System.out.println("Lyon is at two jumps from Chanas : " + lyon.isTwoDistance(chanas));
				System.out.println("Lyon is connected to more two jump cities : " + lyon.isMoreLinkedToType(chanas, NodeType.CITY));
			}
			
			try{
				if (vienne != null)
					System.out.printf("List of routes between Lyon and Vienne : %s\n",lyon.getPaths(vienne));
			}catch(ItineraryException e){
				System.err.println(e.getMessage());
			}

		}
	}
	
}