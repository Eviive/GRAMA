package grama.model;

import grama.comparator.ItineraryComparatorDistance;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
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
						 .collect(Collectors.toList());
	}
	
	public List<Link> getDistinctLinks() {
		return getLinks().stream()
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
	
	public List<Link> getDistinctLinks(LinkType type) {
		return getLinks(type).stream()
							 .distinct()
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
		return getDistinctLinks().size();
	}
	
	/**
	 * @param type The type of <code>Links</code> we will count
	 * @return Returns the number of <code>Links</code> with the right type of this <code>Graph</code>
	 */
	public int getNumberLinks(LinkType type) {
		return getDistinctLinks(type).size();
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
	 * The Dijkstra shortest path resolution algorithm with a number of cities, restaurants and recreations node
	 * @param departure The starting point of the itinerary
	 * @param arrival The end point of the itinerary
	 * @param restaurants The number of restaurants that we must have in the itinerary
	 * @param cities The number of cities that we must have in the itinerary
	 * @param recreations The number of recreations that we must have in the itinerary
	 * @return
	 * @throws ItineraryException 
	 */
	public List<Link> getShortestItinerary(Node departure, Node arrival, int restaurants, int cities, int recreations) throws ItineraryException{
		List<Link> initialPath = getShortestItinerary(departure, arrival);
		if(restaurants==0&&cities==0&&recreations==0){ //check if de numbers are not null
			return initialPath;
		}
		int countRestaurants=this.getNodes(NodeType.RESTAURANT).size();
		int countRecreations=this.getNodes(NodeType.RECREATION).size();
		int countCities = this.getNodes(NodeType.CITY).size();//count the number of each type
		if(restaurants>countRestaurants || cities>countCities || recreations>countRecreations)//throw exception if the number are too high
			throw new ItineraryException("Chiffre trop grand.");
		int restaurantsNumber = restaurants;
		int citiesNumber = cities;
		int recreationsNumber = recreations;
		
		//cehck if th shortest path does contain the required number of each NodeType
		for(Link l : initialPath){
			Node n = l.getDeparture();
			NodeType type = n.getType();
			if(type==NodeType.RECREATION && recreationsNumber>0){
				recreationsNumber--;
			} else if(type==NodeType.CITY && citiesNumber>0){
				citiesNumber--;
			}else if(type==NodeType.RESTAURANT && restaurantsNumber>0){
				restaurantsNumber--;
			}
			if(restaurantsNumber==0&&citiesNumber==0&&recreationsNumber==0){
				return initialPath;
			}
		}//end of verifications
		
		restaurantsNumber = restaurants;
		citiesNumber = cities;
		recreationsNumber = recreations;
		Node from = departure;
		Node n = arrival;
		List<Node> treated = new ArrayList<>(); // treated list of node
		List<Link> finalList = new ArrayList<>(); // final list of node
		int total = restaurants+cities+recreations;
		for(int i = total; i>=0; i--){
			int distance = Integer.MAX_VALUE;
			if(i!=0){//add arrival to treated
				if(!treated.contains(arrival))
					treated.add(arrival);
			if(restaurantsNumber>0){//if restaurants arent all treated
				Node temp = n;
				int tempDistance=distance;
				AtomicInteger d = new AtomicInteger(tempDistance);
				temp = getNearestNode(from, NodeType.RESTAURANT, getNodesFromLinkList(finalList), d);//get the nearest node
				if(distance>d.get()){//if distance is shorter than the previous one 
					n = temp;// the nearest node is this node
					distance = d.get();//the shortest distance is this distance
				}
			}
			if(recreationsNumber>0){//if recreations arent all treated
				Node temp = n;
				int tempDistance=distance;
				AtomicInteger d = new AtomicInteger(tempDistance);
				temp = getNearestNode(from, NodeType.RECREATION, getNodesFromLinkList(finalList), d);//get the nearest node
				if(distance>d.get()){//if distance is shorter than the previous one 
					n = temp;// the nearest node is this node
					distance = d.get();//the shortest distance is this distance
				}
			}
			if(citiesNumber>0){//if cities arent all treated
				Node temp = n;
				int tempDistance=distance;
				AtomicInteger d = new AtomicInteger(tempDistance);
				temp = getNearestNode(from, NodeType.CITY, getNodesFromLinkList(finalList), d);//get the nearest node
				if(distance>d.get()){//if distance is shorter than the previous one 
					n = temp;// the nearest node is this node
					distance = d.get();//the shortest distance is this distance
				}
			}
			if(n.getType()==NodeType.CITY)//updating the count of city, recreation and restaurants found
				citiesNumber--;
			else if(n.getType()==NodeType.RECREATION)
				recreationsNumber--;
			else if(n.getType()==NodeType.RESTAURANT)
				restaurantsNumber--;
			if(i==0)
				n=arrival;
			finalList.addAll(getShortestItinerary(from, n));//adding the itinerary
			treated = getNodesFromLinkList(finalList);//updating the treated list
			from = n;//updating the next departure node
			}
		}
		finalList.addAll(getShortestItinerary(from, arrival));//add the final path to arrival
		return finalList;
	}
	
	/**
	 * Return the nearest node of a type
	 * @param departure The starting node
	 * @param type The type of the node
	 * @param treated the list of node already treated
	 * @param distance the distance of the from the previous node
	 * @return the nearest node of the type <code>type</code>
	 */
	private Node getNearestNode(Node departure, NodeType type, List<Node> treated,AtomicInteger distance) throws ItineraryException{
		Node nearest =null;
		int count=0;
		for(Node node : this.getNodes(type)){ // count the node already trated
			if(treated.contains(node))
				count++;
		}
		if(count==this.getNodes(type).size())//if all the links are treated throw exceptions
			throw new ItineraryException("Chemin inaccessible");
		if(count==this.getNodes(type).size()-1)//if there is only one node left, get that node
			return getFreeNode(type, treated);
		boolean find = false;//while the nearest node is not find
		for(int i =1; !find; i++){
			List<Node> list_vide = new ArrayList<>();
			List<Node> neighbors = departure.getNeighbors(i, list_vide);//get neighbors
			for(Node n : neighbors){
				if(n.getType()==type){
					if(!treated.contains(n) && !n.equals(departure)){//if the type of the node is the good one and it is not equals to departure
						nearest = n;//return that node
						distance.set(i);
						find=true;
					}
				}
			}
		}
		return nearest;
	}
	
	/**
	 * Method to use where there is one or two free node of a type
	 * @param type type of node
	 * @param treated list of treated nodes
	 * @return the free node
	 */
	private Node getFreeNode(NodeType type,List<Node> treated){
		List<Node> list = this.getNodes(type);
		Node node = null;
		for(Node n : list){
			if(!treated.contains(n)){
				node = n;
			}
		}
		return node;
	}
	
	/**
	 * Transform a list of link into a list of node
	 * @param list
	 * @return nodeList
	 */
	private List<Node> getNodesFromLinkList(List<Link> list){
		List<Node> nodes = new ArrayList<>();
		for(Link l : list){
			nodes.add(l.getDeparture());
		}
		return nodes;
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