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
	 * @throws LoadGraphException If <code>NodeType.typeOf(type)</code> throws a <code>LoadGraphException</code>
	 */
	public Node(char type, String name, double ratioX, double ratioY ) throws LoadGraphException {
		this.type = NodeType.typeOf(type);
		this.name = name;
		this.ratioX = ratioX;
		this.ratioY = ratioY;
	}

	public double getRatioX() {
		return ratioX;
	}

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
	
	public List<Node> getNeighbors(int nbJumps){
		return getNeighbors(nbJumps,Arrays.asList(LinkType.values()));
	}
	
	public List<Node> getNeighbors(int nbJumps, List<LinkType> linkTypes){
		
		LinkedList<Node> queue = new LinkedList<>();
		List<Node> visited = new ArrayList<>();
		
		HashMap<Node,Integer> distanceMap = new HashMap<>();

		visited.add(this);
		queue.add(this);
		
		distanceMap.put(this, 0);
		
		while(!queue.isEmpty()){
			
			Node vertex = queue.poll();
			
			for (Link link : vertex.getNodeLinks(linkTypes)){
				Node neighbour = link.getDestination();
				
				if (!(visited.contains(neighbour))){
					visited.add(neighbour);
					queue.add(neighbour);
					distanceMap.put(neighbour,distanceMap.get(vertex)+1);
				}
			}
		}

		return distanceMap.entrySet().stream()
							 .filter(entree -> entree.getValue() <= nbJumps || entree.getKey() == this )
							 .map(entree -> entree.getKey())
							 .collect(Collectors.toList());
		
	}
	
	/**
	 * @param nbJumps The number of jumps we have to do
	 * @param types The types of the <code>Node</code> we want to get
	 * @return Returns the <code>List</code> of all the <code>Nodes</code> of type <code>type</code> you can go to by making <code>nbJumps</code> jumps or less from this <code>Node</code>
	 */
	public List<Node> GetFilteredNeighbors(int nbJumps, List<NodeType> types) {
		return filterByType(Node.this.getNeighbors(nbJumps), types);
	}
	
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
	 * @return Returns <code>true</code> if the <code>Nodes</code> are at two distance from each other
	 */
	public boolean isTwoDistance(Node target) {
		return target.getNodeLinks().stream().anyMatch(link -> linkList.contains(link));
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
		int nbNode = GetFilteredNeighbors(2, types).size();
		int nbTarget = target.GetFilteredNeighbors(2, types).size();
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