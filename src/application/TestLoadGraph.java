package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import structure.Link;
import structure.LinkList;
import structure.LoadGraphException;
import structure.Node;
import structure.NodeList;

public class TestLoadGraph {
	
	public static void main(String[] args) {
		File file = new File("graph.csv");
		try {
			NodeList nodeList = new NodeList();
			String row;
			
			BufferedReader readGraph = new BufferedReader(new FileReader(file));
			while ((row = readGraph.readLine()) != null) {
				String node[] = row.split(":")[0].split("/");
				nodeList.append(new Node(node[0].charAt(0), node[1], null, null));
			}
			readGraph.close();
			
			readGraph = new BufferedReader(new FileReader(file));
			while ((row = readGraph.readLine()) != null) {
				String elements[] = row.split(":");
				String node[] = elements[0].split("/");
				LinkList neighborList = new LinkList(nodeList.search(node[0].charAt(0), node[1]));
				for (int i = 1; i < elements.length; i++) {
					String element[] = elements[i].split("/");
					Node destination = nodeList.search(element[2].charAt(0), element[3]);
					neighborList.append(new Link(element[0].charAt(0), Integer.parseInt(element[1]), destination, null));
				}
			}
			readGraph.close();
			
			nodeList.display();
		} catch (IOException | LoadGraphException e) {
			e.printStackTrace(System.err);
		}
	}
	
}