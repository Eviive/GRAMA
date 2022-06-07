package grama.model;

import grama.comparator.ItineraryComparatorDistance;
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
	 * The Dijkstra shortest path resolution algorithm with treated node parameter
	 * @param departure The starting node
	 * @param arrival	The arrival node
	 * @param treated The list of treated node
	 * @return A list of link representing the shortest path between 2 places
	 * @throws ItineraryException 
	 */
	public List<Link> getShortestItinerary(Node departure, Node arrival,List<Node> treated) throws ItineraryException{
		System.out.println("entree fonction treated");
		List<Node> notProcess = new ArrayList<>();
		List<Integer> distances = new ArrayList<>();
		
		List<Node> nodes = getNodes();
		Link previousLink[] = new Link[nodes.size()];
		
		//if(treated.contains(departure))
		//	treated.remove(departure);
		
		// Dijkstra Initialization
		for (Node place : nodeMap.values()){
			if(!treated.contains(place)){
				notProcess.add(place);
			
				if (place == departure)
					distances.add(0);
				else
					distances.add(Integer.MAX_VALUE);
			}
			
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
			
			if (step == null){
				System.out.println("arret sur : " + departure.getName() + " vers " + arrival.getName());
				for(Node caca : treated){
					System.out.println("Noeud traite : " +caca.getName());
				}
				throw new ItineraryException("Ce noeud est inaccessible !");
			}
				
			
			path.add(0, step);
			dest = step.getDeparture();
		}
		
		return path;
	}
	
	/**
	 * 
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
		if(restaurants==0&&cities==0&&recreations==0){ //on verifie si les valeurs ne sont pas egal a 0
			return initialPath;
		}
		List<Node> initialNodePath = new ArrayList<>();// le chemin en node
		List<Node> checked = new ArrayList<>(); // noeud verifie
		List<Node> unchecked = new ArrayList<>(); //noeud pas verifie
		HashMap<Node, Node> nodeNearFromInitialNode = new HashMap<>();//sert a savoir quel nouveau node est proche du quel
		HashMap<Node, Integer> distanceOfTheCheckedNode = new HashMap<>();//sert a savoir quel est la distance de ces noeuds lesp lus proche du chemin
		int restaurantsNumber = restaurants;
		int citiesNumber = cities;
		int recreationsNumber = recreations;
		
		//on regarde si le chemin le plus court ne contient pas ce que l'utilisateur cherche deja
		for(Link l : initialPath){
			Node n = l.getDeparture();
			NodeType type = n.getType();
			if(type==NodeType.RECREATION && recreationsNumber>0){
				checked.add(n);
				nodeNearFromInitialNode.put(n, n);
				distanceOfTheCheckedNode.put(n, 0);
				//recreationsNumber--;
			} else if(type==NodeType.CITY && citiesNumber>0){
				checked.add(n);
				nodeNearFromInitialNode.put(n, n);
				distanceOfTheCheckedNode.put(n, 0);
				//citiesNumber--;
			}else if(type==NodeType.RESTAURANT && restaurantsNumber>0){
				checked.add(n);
				nodeNearFromInitialNode.put(n, n);
				distanceOfTheCheckedNode.put(n, 0);
				//restaurantsNumber--;
			}
			if(restaurantsNumber==0&&citiesNumber==0&&recreationsNumber==0){
				return initialPath;
			}
			initialNodePath.add(n);
		}//fin des verifications 
		
		//INITIALISATION
		for(Node n :this.getNodes()){
			if(!checked.contains(n)){
				NodeType type = n.getType();
				if(type==NodeType.CITY && citiesNumber>0)
					unchecked.add(n);
				if(type==NodeType.RESTAURANT && restaurantsNumber>0)
					unchecked.add(n);
				if(type==NodeType.RECREATION && recreationsNumber>0)
					unchecked.add(n);
			}
			
		}
		//FIN initalisation
		System.out.println("unchecked : " + unchecked.size());
		
		int distance = Integer.MAX_VALUE;
		for(Node unCheckedNode : unchecked){
			Node nearest = unCheckedNode;
			for(Node node : initialNodePath){
				List<Link> itinerary = this.getShortestItinerary(node, unCheckedNode);
				if(itinerary.size()<distance){
					nearest=node;
					if(distance==1)
						break;
				}
			}
			nodeNearFromInitialNode.put(unCheckedNode, nearest);
			distanceOfTheCheckedNode.put(unCheckedNode, distance);
			checked.add(unCheckedNode);
			//unchecked.remove(unCheckedNode);
		}
		unchecked = null; // on supprime la liste
		checked.sort(new ItineraryComparatorDistance(distanceOfTheCheckedNode));// on trie les plus proche
		List<Node> steps = new ArrayList<>();
		restaurantsNumber = restaurants;
		citiesNumber = cities;
		recreationsNumber = recreations;
		System.out.println("nombre resto, ville, loisir" +restaurantsNumber+" " +citiesNumber+" " +recreationsNumber);
		for(Node n : checked){ // on met les noeuds les plus proche dans une liste et on enleve tout les autres
			NodeType type = n.getType();
			if(type==NodeType.RECREATION && recreationsNumber>0){
				steps.add(n);
				recreationsNumber--;
			} else if(type==NodeType.CITY && citiesNumber>0){
				steps.add(n);
				citiesNumber--;
			}else if(type==NodeType.RESTAURANT && restaurantsNumber>0){
				steps.add(n);
				restaurantsNumber--;
			}
		}
		System.out.println("nombre resto, ville, loisir" +restaurantsNumber+" " +citiesNumber+" " +recreationsNumber);
		steps = getNodeListByNearest(steps, nodeNearFromInitialNode, initialNodePath);//list des noeuds trie par distance et par le noeud le plus proche
		
		if(steps.isEmpty()) //////////////////////////////////////////////////ON COMMENCE A FAIRE LCHEMIN
			throw new ItineraryException("Erreur tri");
		List<Link> finalList = new ArrayList<>();
		List<Node> treated = new ArrayList<>();
		for(int i = 0; i < steps.size(); i++){
			Node n = steps.get(i);
			System.out.println(i+"/"+steps.size());
			if(!treated.contains(n))
			if(i==0){
				System.out.println(initialNodePath.get(0).getName()+"/"+n.getName());
				if(nodeNearFromInitialNode.get(n).equals(initialNodePath.get(0)) && !initialNodePath.get(0).equals(n)){
					System.out.println("passe 2");
					finalList.addAll(getShortestItinerary(initialNodePath.get(0),n));
				}else if(!initialNodePath.get(0).equals(nodeNearFromInitialNode.get(n))){
					finalList.addAll(getShortestItinerary(initialNodePath.get(0),nodeNearFromInitialNode.get(n)));
					if(!nodeNearFromInitialNode.get(n).equals(n)){
						System.out.println("passe");
						finalList.addAll(getShortestItinerary(nodeNearFromInitialNode.get(n),n));
					}
				}
				addTreatedNode(treated, getNodesFromLinkList(finalList));
				if(i==steps.size()-1){
					System.out.println("n : " + n.getName() +", initalNodePathDernier : " + initialNodePath.get(initialNodePath.size()-1).getName());
					if(initialNodePath.get(initialNodePath.size()-1).equals(arrival)){
						finalList.addAll(getShortestItinerary(n, initialNodePath.get(initialNodePath.size()-1),treated));
						finalList.addAll(getShortestItinerary(initialNodePath.get(initialNodePath.size()-1), arrival));
					}else{
						finalList.addAll(getShortestItinerary(treated.get(treated.size()-1), arrival));
					}
					
					System.out.println("arrive");
				}
				if(treated.contains(departure))
					System.out.println("YES");
				else
					treated.add(departure);
			}else{
				if(finalList.isEmpty())
					treated.clear();
				System.out.println("else sur " + n.getName());
				Node previousNode = steps.get(i-1);
				System.out.println("le plus proche " + nodeNearFromInitialNode.get(n).getName());
				finalList.addAll(getShortestItinerary(previousNode, nodeNearFromInitialNode.get(n),treated));
				System.out.println("ajout 1");
				addTreatedNode(treated, getNodesFromLinkList(finalList));
				if(!nodeNearFromInitialNode.get(n).equals(n)){
					System.out.println("ajout if");
					finalList.addAll(getShortestItinerary(nodeNearFromInitialNode.get(n),n,treated));
					addTreatedNode(treated, getNodesFromLinkList(finalList));
				}
				
				if(i==steps.size()-1){
					System.out.println("ajout if fin");
					finalList.addAll(getShortestItinerary(n, initialNodePath.get(initialNodePath.size()-1),treated));
					finalList.addAll(getShortestItinerary(initialNodePath.get(initialNodePath.size()-1), arrival, treated));
					System.out.println("arrive");
				}
			}
			addTreatedNode(treated, getNodesFromLinkList(finalList));
		}
		
		return finalList;
	}
	
	private void addTreatedNode(List<Node> treatedList, List<Node> list){
		for(Node n : list){
			treatedList.add(n);
		}
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
		/*if(!list.isEmpty()){
			nodes.add(list.get(list.size()-1).getDestination());
		}*/
		return nodes;
	}
	/**
	 * Sort a list of node by the nearest node from a itinerary (only for the getShortestItinerary function)
	 * @param list The list of sorted Node by distance
	 * @param map The map of wich Node is near of 
	 * @param path
	 * @return 
	 */
	private List<Node> getNodeListByNearest(List<Node> list, HashMap<Node, Node> map, List<Node> path){
		List<Node> sortedList = new ArrayList<>();
		for(Node n : path){
			Node nearest = null;
			for(Node node : list){
				if(map.get(node)==n){
					nearest=node;
					break;
				}
			}
			if(nearest!=null){
				sortedList.add(nearest);
			}
		}
		return sortedList;
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