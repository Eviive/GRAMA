package structure;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The class representing a graph
 * @author VAILLON Albert
 * @version JDK 11.0.13
 */
public final class Graph {
	
	private HashMap<String, Node> nodeMap;
	
	/**
	 * Loads all the file's data in the structure
	 * @param fileName The name of the file where the data is stored
	 * @throws LoadGraphException If the file does not match the following pattern :<blockquote><code>nodeCategory:nodeName|linkCategory:linkDistance:linkDestination|...</code></blockquote>
	 */
	public void load(String fileName) throws LoadGraphException {
		nodeMap = new HashMap<>();
		try {
			BufferedReader readGraph = new BufferedReader(new FileReader(fileName));	
			// loads all the nodes into the nodeMap ArrayList
			String row;
			while ((row = readGraph.readLine()) != null) {
				String node[] = row.split("\\|")[0].split(":");
				getNodeMap().put(node[1], new Node(node[0].charAt(0), node[1]));
			}
			readGraph.close();

			// loads all the links into their right nodes
			readGraph = new BufferedReader(new FileReader(fileName));
			while ((row = readGraph.readLine()) != null) {
				String elements[] = row.split("\\|");

				// selects the node corresponding to the links
				String[] departure = elements[0].split(":");
				Node nodeDeparture = getNodeMap().get(departure[1]);

				// fills the neighborsList with the neighbors of the node
				for (int i = 1; i < elements.length; i++) {
					String element[] = elements[i].split(":");
					Node destination = getNodeMap().get(element[2]);
					if (destination == null) {
						throw new LoadGraphException("La destination " + element[2] + " en partant de " + nodeDeparture + " n'a pas été trouvé");
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
		if (nodeMap.isEmpty()) {
			throw new LoadGraphException();
		}
	}
	
	/**
	 * @return Returns the <code>HashMap</code> of all the <code>Nodes</code> of this <code>Graph</code>
	 */
	public HashMap<String, Node> getNodeMap() {
		return nodeMap;
	}
	
	/**
	 * @return Returns the <code>List</code> of all the <code>Nodes</code> of this <code>Graph</code>
	 */
	public List<Node> getNodes() {
		return new ArrayList(nodeMap.values());
	}
	
	/**
	 * @param type The type of <code>Node</code> we want
	 * @return Returns the <code>List</code> of all the <code>Nodes</code> with the right type of this <code>Graph</code>
	 */
	public List<Node> getNodes(NodeType type){
		return getNodes().stream()
						 .filter(node -> node.getType() == type)
						 .collect(Collectors.toList());
	}
	
	/**
	 * @return Returns the <code>List</code> of all the <code>Links</code> of this <code>Graph</code>
	 */
	public List<Link> getLinks() {
		return getNodes().stream()
						 .flatMap(node -> node.getNodeLinks().stream())
						 .distinct()
						 .collect(Collectors.toList());
	}
	
	/**
	 * @param type The type of <code>Links</code> we want
	 * @return Returns the <code>List</code> of all the <code>Links</code> of this <code>Graph</code>
	 */
	public List<Link> getLinks(LinkType type) {
		return getLinks().stream()
						 .filter(link -> link.getType() == type)
						 .collect(Collectors.toList());
	}
	
	/**
	 * @return Returns the number of <code>Nodes</code> of this <code>Graph</code>
	 */
	public int getNumberNodes() {
		return getNodes().size();
	}
	
	/**
	 * @param type The type of <code>Nodes</code> we will count
	 * @return Returns the number of <code>Nodes</code> with the right type of this <code>Graph</code>
	 */
	public int getNumberNodes(NodeType type) {
		return getNodes(type).size();
	}
	
	/**
	 * @return Returns the number of <code>Links</code> of this <code>Graph</code>
	 */
	public int getNumberLinks() {
		return getLinks().size() / 2;
	}
	
	/**
	 * @param type The type of <code>Links</code> we will count
	 * @return Returns the number of <code>Links</code> with the right type of this <code>Graph</code>
	 */
	public int getNumberLinks(LinkType type) {
		return getLinks(type).size() / 2;
	}
	
	/**
	 * Displays this <code>Graph</code> in a format close to the source file's
	 */
	public void display() {
		if (nodeMap.isEmpty()) {
			System.out.println("Le graphe est vide");
		} else {
			for (Node node: getNodes()) {
				System.out.printf("%-50s",node);
				
				for (Link link: node.getNodeLinks()) {
					System.out.printf("%-90s",link);
				}
				System.out.println();
			}
		}
	}
	
	/**
	 * Displays all the <code>Nodes</code> with the right type of this <code>Graph</code>
	 * @param type The type of <code>Nodes</code> we will display
	 */
	public void display(NodeType type) {
		List<Node> nodes = getNodes(type);
		if (nodes.isEmpty()) {
			System.out.println("Il n'y a pas de noeuds de type " + type);
		} else {
			System.out.println("Les noeuds de type " + type + " :");
			for (Node node: nodes) {
				System.out.println("\t- " + node);
			}
		}
	}
	
}