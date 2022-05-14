package structure;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The class representing the Graph
 * @author VAILLON Albert
 */
public class Graph {
	
	private HashMap<String, Node> nodeList;
	
	public Graph(String fileName) throws LoadGraphException {
		load(fileName);
	}
	
	/**
	 * Loads all the file's data in the structure
	 * @param fileName The name of the file where the data is stored
	 */
	private void load(String fileName) throws LoadGraphException {
		nodeList = new HashMap<>();
		try {
			BufferedReader readGraph = new BufferedReader(new FileReader(fileName));	
			// loads all the nodes into the nodeList ArrayList
			String row;
			while ((row = readGraph.readLine()) != null) {
				String node[] = row.split("\\|")[0].split(":");
				getNodeList().put(node[1], new Node(node[0].charAt(0), node[1]));
			}
			readGraph.close();

			// loads all the links into their right nodes
			readGraph = new BufferedReader(new FileReader(fileName));
			while ((row = readGraph.readLine()) != null) {
				String elements[] = row.split("\\|");

				// selects the node corresponding to the links
				String[] departure = elements[0].split(":");
				Node nodeDeparture = getNodeList().get(departure[1]);

				// fills the neighborsList with the neighbors of the node
				for (int i = 1; i < elements.length; i++) {
					String element[] = elements[i].split(":");
					Node destination = getNodeList().get(element[2]);
					if (destination == null) {
						throw new LoadGraphException("Cannot find the destination " + element[2] + " / " + nodeDeparture);
					}
					nodeDeparture.addLink(new Link(element[0].charAt(0), Integer.parseInt(element[1]), nodeDeparture, destination));
				}
			}
			readGraph.close();
		} catch (LoadGraphException e) {
			throw e;
		} catch (Exception e) {
			throw new LoadGraphException();
		}
		if (nodeList.isEmpty()) {
			throw new LoadGraphException();
		}
	}
	
	/**
	 * @return Returns the list of all the Nodes from the Graph
	 */
	public HashMap<String, Node> getNodeList() {
		return nodeList;
	}
	
	/**
	 * Displays the Graph in a format close to the source file's
	 */
	public void display() {
		if (nodeList.isEmpty()) {
			System.out.println("The graph is empty");
		} else {
			for (Node node: nodeList.values()) {
				System.out.printf("%-50s",node);
				
				List<Link> nodeNeighbors = node.getNodeLinks();
				for (Link link: nodeNeighbors) {
					System.out.printf("%-90s",link);
				}
				System.out.println();
			}
		}
	}
	
	/**
	 * Displays all the Nodes with the right type
	 * @param type The type of Nodes we will display
	 */
	public void display(NodeType type) {
		if (nodeList.isEmpty()) {
			System.out.println("The graph is empty");
		} else {
			List<Node> nodes = nodeList.values()
									   .stream()
									   .filter(node -> node.getType() == type)
									   .collect(Collectors.toList());
			if (nodes.isEmpty()) {
				System.out.println("There are no Nodes of type " + type);
			} else {
				System.out.println("The Nodes of type " + type + " :");
				for (Node node: nodes) {
					System.out.println("\t- " + node);
				}
			}
		}
	}
	
	/**
	 * @return Returns the number of Nodes
	 */
	public int getNumberNodeType() {
		return nodeList.size();
	}
	
	/**
	 * @param type The type of Nodes we will count
	 * @return Returns the number of Nodes with the right type
	 */
	public int getNumberNodeType(NodeType type) {
		Long nbNodes = nodeList.values()
							   .stream()
							   .filter(node -> node.getType() == type)
							   .count();
		return nbNodes.intValue();
	}
	
	/**
	 * @param type The type of Links we will count
	 * @return Returns the number of Links with the right type
	 */
	public int getNumberLinkType(LinkType type) {
		Long nbLinks = nodeList.values()
							   .stream()
							   .flatMap(node -> node.getNodeLinks().stream())
							   .filter(link -> link.getType() == type)
							   .count();
		return nbLinks.intValue() / 2;
	}
	
}