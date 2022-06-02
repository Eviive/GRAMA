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
 * @author BAUDRY Lilian
 * @version JDK 11.0.13
 */
public final class Graph {
	
	private HashMap<String, Node> nodeMap = new HashMap<>();
	
	/**
	 * Loads all the file's data in the structure
	 * @param fileName The name of the file where the data is stored
	 * @throws LoadGraphException If the file does not match the following pattern :<blockquote><code>nodeCategory:nodeName|linkCategory:linkDistance:linkDestination|...</code></blockquote>
	 */
	public void load(String fileName) throws LoadGraphException {
		nodeMap.clear();
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
	 * @param name The <code>key</code> of the node hashmap
	 * @return Returns the <code>Node</code> corresponding to the <code>String</code> of the <code>Graph</code>
	 */
	public Node getNode(String name){
		return nodeMap.get(name);
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
	 * @param path A list of link representing a path
	 * @return The total distance in kilometer
	 * @throws ItineraryException 
	 */
	public int getDistancePath(List<Link> path) throws ItineraryException{
		
		int distance = 0;
		
		if (path.isEmpty())
			return distance;
		
		distance += path.get(0).getDistance();
		
		for (int i = 1 ; i < path.size() ; i++){
			if (path.get(i-1).getDestination()!=path.get(i).getDeparture())
				throw new ItineraryException("Chemin invalide");
			distance += path.get(i).getDistance();
		}
		
		return distance;
	}
	
	/**
	 * The Dijkstra shortest path resolution algorithm
	 * @param departure The starting node
	 * @param arrival	The arrival node
	 * @return A list of link representing the shortest path between 2 places
	 * @throws ItineraryException 
	 */
	public List<Link> getShortestItinerary(Node departure, Node arrival) throws ItineraryException{
		
		List<Node> notProcess = new ArrayList<>();
		List<Integer> distances = new ArrayList<>();
		
		List<Node> nodes = getNodes();
		Link previousLink[] = new Link[nodes.size()];
		
		// Dijkstra Initialization
		for (Node place : nodeMap.values()){
			notProcess.add(place);
			
			if (place == departure)
				distances.add(0);
			else
				distances.add(Integer.MAX_VALUE);
		}
		
		// Search until the path is found or inaccessible
		while (!notProcess.isEmpty() || notProcess.contains(arrival) ){
			
			// Definition of the nearest node index
			int indexMin = 0;
			for (int i = 0 ; i < distances.size() ; i++){
				if (distances.get(i) < distances.get(indexMin)){
					indexMin = i;
				}
			}
			
			// Treatment of neighbors not yet treated
			int distance = distances.remove(indexMin);
			Node processing = notProcess.remove(indexMin);
			
			for (Node node : processing.getNeighbors(1, new ArrayList<>())){
				if (notProcess.contains(node)){
					int indice = notProcess.indexOf(node);
					
					// Updates of the shortest distances
					if (distance + processing.getShortestPath(node).getDistance() < distances.get(indice)){
						distances.set(indice, distance + processing.getShortestPath(node).getDistance());
						previousLink[nodes.indexOf(node)] = processing.getShortestPath(node);
					}
				}
			}
		}
		
		// Path construction
		List<Link> path = new ArrayList<>();
		
		Node dest = arrival;
		while (dest != departure){ 
			
			int indice = nodes.indexOf(dest);
			Link step = previousLink[indice];
			
			if (step == null)
				throw new ItineraryException("Ce noeud est inaccessible !");
			
			path.add(0, step);
			dest = step.getDeparture();
		}
		
		return path;
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