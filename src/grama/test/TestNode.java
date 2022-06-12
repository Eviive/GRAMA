package grama.test;

import java.util.ArrayList;
import java.util.List;
import grama.model.Graph;
import grama.model.ItineraryException;
import grama.model.LoadGraphException;
import grama.model.Node;
import grama.model.NodeType;

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
		
		if (chanas != null) {
			System.out.println("Chanas' neighbors : " + chanas.getNeighbors(1) + "\n");
			
			List<NodeType> types = new ArrayList<>();
			types.add(NodeType.RESTAURANT);
			System.out.println("Chanas' restaurant neighbors : " + chanas.getFilteredNeighbors(2, types) + "\n");
			
			try {
				System.out.println("Chanas is " + chanas.getShortestPath(graph.getNode("Annonay")).getDistance() + " km from Annonay");
			} catch (ItineraryException e) {
				System.err.println(e.getMessage());
			}
			
			if (lyon != null) {
				System.out.println("Lyon is at two jumps from Chanas : " + lyon.isTwoDistance(chanas));
				System.out.println("Lyon is connected to more two jump cities : " + lyon.isMoreLinkedToType(chanas, NodeType.CITY));
			}
			
			try {
				if (lyon != null && vienne != null)
					System.out.printf("List of routes between Lyon and Vienne : %s\n", lyon.getPaths(vienne));
				
			} catch (ItineraryException e) {
				System.err.println(e.getMessage());
			}

		}
	}
	
}