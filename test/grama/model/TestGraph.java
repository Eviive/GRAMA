package grama.model;

import java.util.List;

/**
 * Test class for Graph
 * @author VAILLON Albert
 * @author BAUDRY Lilian
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
		
		Node albon = graph.getNode("Albon");
		Node portDesAlpes = graph.getNode("Porte des Alpes");
		
		if (albon != null && portDesAlpes != null) {
			try {
				List<Link> shortestPath = graph.getShortestItinerary(portDesAlpes,albon);
				System.out.printf("\nThe shortest distance between Albon and Porte des Alpes is %d km \n",graph.getDistancePath(shortestPath));
				for (Link link: shortestPath) {
					System.out.println("\t" + link.getDeparture()+ " -> " + link.getDestination());
				}
				
			} catch (ItineraryException e) {
				System.err.println(e.getMessage());
			}
		}
	}

}