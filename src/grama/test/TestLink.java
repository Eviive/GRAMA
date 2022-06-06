package grama.test;

import grama.model.Graph;
import grama.model.Link;
import grama.model.LoadGraphException;

/**
 * Test class for Link
 * @author VAILLON Albert
 * @version JDK 11.0.13
 */
public class TestLink {
	
	public static void main(String[] args) throws LoadGraphException {
		Graph graph = new Graph();
		graph.load("graph.csv");
		graph.display();
		
		Link highwayLyonVienne = graph.getNodeMap().get("Lyon").getNodeLinks().get(5);
		if (highwayLyonVienne != null) {
			System.out.println("\n\n" + highwayLyonVienne);
		}
	}
	
}