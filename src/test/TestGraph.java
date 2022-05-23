package test;

import java.util.List;
import structure.Graph;
import structure.ItineraryException;
import structure.Link;
import structure.LinkType;
import structure.LoadGraphException;
import structure.Node;
import structure.NodeType;

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
		
		System.out.println("\n\nNumber of recreation centers : " + graph.getNumberNodeType(NodeType.RECREATION));
		
		System.out.println("Number of highways : " + graph.getNumberLinkType(LinkType.HIGHWAY));
		
		Node albon = graph.getNode("Albon");
		Node portDesAlpes = graph.getNode("Porte des Alpes");
		
		if (albon != null && portDesAlpes!= null){
			try{
				List<Link> shortestPath = graph.getShortestItinerary(portDesAlpes,albon);
				System.out.printf("\nThe shortest distance between Albon and Porte des Alpes is %d km \n",graph.getDistancePath(shortestPath));
				for (Link e : shortestPath){
					System.out.println("\t" + e.getDeparture()+ " -> " + e.getDestination());
				}
				
			}catch (ItineraryException e){
				System.err.println(e.getMessage());
			}
		}
		
	}

}