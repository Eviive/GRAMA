package grama.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The class representing a node
 * @author VAILLON Albert
 * @author BAUDRY Lilian
 * @version JDK 11.0.13
 */
public final class Node implements Comparable<Node> {
	
	private final NodeType type;
	private final String name;
	private final List<Link> linkList = new ArrayList<>();
	
	private final double ratioX;
	private final double ratioY;
	
	/**
	 * Creates a new <code>Node</code>
	 * @param type The type of the <code>Node</code>
	 * @param name The name of the <code>Node</code>
	 * @param ratioX A value between 0 and 1 used to determine where to place the node horizontally in the visualization of the graph
	 * @param ratioY A value between 0 and 1 used to determine where to place the node vertically in the visualization of the graph
	 * @throws LoadGraphException If <code>NodeType.typeOf(type)</code> throws a <code>LoadGraphException</code>
	 */
	public Node(char type, String name, double ratioX, double ratioY) throws LoadGraphException {
		this.type = NodeType.typeOf(type);
		this.name = name;
		this.ratioX = ratioX;
		this.ratioY = ratioY;
	}
	
	/**
	 * @return Returns a value between 0 and 1 used to determine where to place the node horizontally in the visualization of the graph
	 */
	public double getRatioX() {
		return ratioX;
	}
	
	/**
	 * @return Returns a value between 0 and 1 used to determine where to place the node vertically in the visualization of the graph
	 */
	public double getRatioY() {
		return ratioY;
	}
	
	/**
	 * @return Returns the type of this <code>Node</code>
	 */
	public NodeType getType() {
		return type;
	}
	
	/**
	 * @return Returns the name of this <code>Node</code>
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return Returns the <code>List</code> of this <code>Node</code>'s <code>Links</code>
	 */
	public List<Link> getNodeLinks() {
		return linkList;
	}
	
	/**
	 * @param types The list of <code>Link</code> types we want
	 * @return Returns the <code>List</code> of all the <code>Links</code> with the right type connected to this Node
	 */
	public List<Link> getNodeLinks(List <LinkType> types) {
		return linkList.stream()
					   .filter(link -> types.contains(link.getType()))
					   .collect(Collectors.toList());
	}
	
	/**
	 * @param neighbor A neighbor of this node
	 * @return The list of paths to the same neighbor
	 * @throws ItineraryException 
	 */
	public List<Link> getPaths(Node neighbor) throws ItineraryException {
		List<Link> paths = linkList.stream()
								   .filter(link -> link.getDestination() == neighbor)
								   .collect(Collectors.toList());
		
		if (paths.isEmpty())
			throw new ItineraryException("Les noeuds ne sont pas reliés");
		
		return paths;
	}
	
	 /**
	 * @param neighbor A neighbor of this node
	 * @return The <code>shortest</code> path to a neighbor node
	 * @throws ItineraryException
	 */
	public Link getShortestPath(Node neighbor) throws ItineraryException {
		int distance = Integer.MAX_VALUE;
		Link path = null;
		
		for (Link route: getPaths(neighbor)) {
			if (route.getDistance() < distance) {
				distance = route.getDistance();
				path = route;
			}
		}
		return path;
	}
	
	/**
	 * @param nodesFilter The <code>List</code> of <code>Node</code> types we want
	 * @param linksFilter The <code>List</code> of <code>Link</code> types we want
	 * @return The directly connected <code>nodes</code> links
	 */
	public List<Node> getNeighbors(List<NodeType> nodesFilter, List<LinkType> linksFilter){
		return linkList.stream()
					   .filter(link -> linksFilter.contains(link.getType()) && nodesFilter.contains(link.getDestination().getType()))
					   .distinct()
					   .map(link -> link.getDestination())
					   .collect(Collectors.toList());

	}
	
	/**
	 * @param nbJumps The number of jumps we have to do
	 * @return Returns the <code>List</code> of all the <code>Nodes</code> you can go to by making <code>nbJumps</code> jumps or less from this <code>Node</code>
	 */
	public List<Node> getNeighbors(int nbJumps){
		return getNeighbors(nbJumps,Arrays.asList(LinkType.values()));
	}
	
	/**
	 * @param nbJumps The number of jumps we have to do
	 * @param linkTypes The types of <code>Links</code> we want to get
	 * @return Returns the <code>List</code> of all the <code>Nodes</code> you can go to by making <code>nbJumps</code> jumps and only going through <code>Links</code> of type <code>linkTypes</code> or less from this <code>Node</code>
	 */
	public List<Node> getNeighbors(int nbJumps, List<LinkType> linkTypes){
		return getNeighborsMap(linkTypes).entrySet().stream()
										 .filter(entree -> entree.getValue() <= nbJumps || entree.getKey() == this )
										 .map(entree -> entree.getKey())
										 .collect(Collectors.toList());
	}
	
	/**
	 * @param nbJumps The exact number of jumps we have to do
	 * @param linkTypes The types of <code>Links</code> we want to get
	 * @return Returns the <code>List</code> of all the <code>Nodes</code> you can go to by making exactly <code>nbJumps</code> jumps and only going through <code>Links</code> of type <code>linkTypes</code> or less from this <code>Node</code>
	 */
	public List<Node> getExaclyNeighbors(int nbJumps, List<LinkType> linkTypes){
		return getNeighborsMap(linkTypes).entrySet().stream()
										 .filter(entree ->  entree.getValue() == nbJumps || entree.getKey() == this )
										 .map(entree -> entree.getKey())
										 .collect(Collectors.toList());
	}
	
	/**
	 * @param linkTypes The types of <code>Links</code> we want to get
	 * @return Returns the <code>Map</code> of all the <code>Nodes</code> with the minimum number of jumps between them and this <code>Node</code>
	 */
	public HashMap<Node,Integer> getNeighborsMap(List<LinkType> linkTypes){
		
		LinkedList<Node> queue = new LinkedList<>();
		List<Node> visited = new ArrayList<>();
		
		HashMap<Node,Integer> distanceMap = new HashMap<>();

		visited.add(this);
		queue.add(this);
		
		distanceMap.put(this, 0);
		
		while(!queue.isEmpty()){
			
			Node node = queue.poll();
			
			for (Link link : node.getNodeLinks(linkTypes)){
				Node neighbor = link.getDestination();
				
				if (!(visited.contains(neighbor))){
					visited.add(neighbor);
					queue.add(neighbor);
					distanceMap.put(neighbor,distanceMap.get(node)+1);
				}
			}
		}
		return distanceMap;
		
	}
	
	/**
	 * @param nbJumps The number of jumps we have to do
	 * @param types The types of <code>Node</code> we want to get
	 * @return Returns the <code>List</code> of all the <code>Nodes</code> of type <code>type</code> you can go to by making <code>nbJumps</code> jumps or less from this <code>Node</code>
	 */
	public List<Node> getFilteredNeighbors(int nbJumps, List<NodeType> types) {
		return filterByType(Node.this.getNeighbors(nbJumps), types);
	}
	
	/**
	 * @param nodes The <code>List</code> of <code>Nodes</code>
	 * @param types The <code>List</code> of <code>Links</code>
	 * @return Returns a <code>List</code> of <code>Nodes</code> filtered by the <code>List</code> of <code>Links</code>
	 */
	public List<Node> filterByType(List<Node> nodes, List<NodeType> types){
		return nodes.stream()
					.filter(node -> types.contains(node.getType()) || node == this)
					.collect(Collectors.toList());
	}
	
	/**
	 * Adds a new <code>Link</code> to the <code>Node</code>
	 * @param link The <code>Link</code> that will be added
	 */
	public void addLink(Link link) {
		linkList.add(link);
	}
	
	/**
	 * Tells us if this <code>Node</code> is exactly at two distances from the target <code>Node</code>
	 * @param target The targeted <code>Node</code>
	 * @param nodesFilter filter by type of nodes requested
	 * @return Returns <code>true</code> if the <code>Nodes</code> are at two distance from each other
	 */
	public boolean isTwoDistance(Node target, List<NodeType> nodesFilter, List<LinkType> linksFilter) {
		return getNeighbors(nodesFilter, linksFilter).stream()
													 .anyMatch(node -> target.getNeighbors(nodesFilter, linksFilter).contains(node));
	}
	
	/**
	 * Tells us if this <code>Node</code> has more two jumps neighbors of type <code>type</code> than the <code>target Node</code>
	 * @param target The targeted <code>Node</code>
	 * @param type The type of <code>Node</code> we're counting
	 * @return Returns <code>true</code> if this <code>Node</code> has the same number of two jumps neighbors of type <code>type</code> than the <code>target Node</code> or more
	 */
	public int isMoreLinkedToType(Node target, NodeType type) {
		List<NodeType> types = new ArrayList<>();
		types.add(type);
		int nbNode = getFilteredNeighbors(2, types).size();
		int nbTarget = target.getFilteredNeighbors(2, types).size();
		if (nbNode > nbTarget)
			return 1;
		else if (nbNode < nbTarget)
			return -1;
		else
			return 0;
	}
	
	/**
	 * Evaluates if this <code>Node</code> is equal to the specified <code>Object</code>
	 * @param o The <code>Object</code> we want to compare to this <code>Node</code>
	 * @return Returns <code>true</code> if this <code>Node</code> and the <code>Object o</code> are the same
	 */
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Node)) {
			return false;
		}
		Node p = (Node)o;
		return name.equals(p.name) && type == p.type;
	}
	
	/**
	 * Compares this <code>Node</code>'s name to the specified <code>Node</code>'s name
	 * @param node The <code>Node</code> we want to compare to this <code>Node</code>
	 * @return Returns the <code>String</code>'s <code>compareTo()</code> between the two names
	 */
	@Override
	public int compareTo(Node node) {
		return name.compareTo(node.name);
	}
	
	/**
	 * Creates a <code>String</code> object representing this <code>Node</code>
	 * @return Returns a <code>String</code> representation of this <code>Node</code>
	 */
	@Override
	public String toString() {
		return name;
	}
	
}