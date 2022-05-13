package test;

import structure.Graph;
import structure.Link;
import structure.LoadGraphException;

public class TestLink {
	
	public static void main(String[] args) throws LoadGraphException {
		Graph graph = new Graph("graph.csv");
		graph.display();
		
		Link highwayLyonVienne = graph.getNodeList().get("Lyon").getNodeLinks().get(5);
		if (highwayLyonVienne != null) {
			System.out.println("\n\n" + highwayLyonVienne);
		}
	}
	
}