package structure;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The class representing a Node
 * @author VAILLON Albert
 */
public class Node {
	
	private final NodeType type;
	private final String name;
	private List<Link> linkList = new ArrayList<>();
	
	/**
	 * Creates a new Node
	 * @param type The type of the Node
	 * @param name The name of the Node
	 * @throws LoadGraphException If <code>switchType(type)</code> throws a LoadGraphException
	 */
	public Node(char type, String name) throws LoadGraphException {
		this.type = switchType(type);
		this.name = name;
	}
	
	/**
	 * Converts the character type in its corresponding <code>NodeType</code> constant
	 * @param type The character type
	 * @return Returns the corresponding <code>NodeType</code> constant
	 * @throws LoadGraphException If the type is not valid
	 */
	public static NodeType switchType(char type) throws LoadGraphException {
		switch (type) {
			case 'V':
				return NodeType.CITY;
			case 'R':
				return NodeType.RESTAURANT;
			case 'L':
				return NodeType.RECREATION;
			default:
				throw new LoadGraphException("Switch node type is invalid (should be V, R, or L)");
		}
	}
	
	/**
	 * @return Returns the type of the Node
	 */
	public NodeType getType() {
		return type;
	}
	
	/**
	 * @return Returns the list of the Node's links
	 */
	public List<Link> getNodeLinks() {
		return linkList;
	}
	
	/**
	 * @return Returns the list of the Node's direct neighbors
	 */
	public List<Node> getNeighbors() {
		return linkList.stream()
					   .map(Link::getDestination)
					   .collect(Collectors.toList());
	}
	
	/**
	 * @param type The type of the Nodes we want to get
	 * @return Returns the list of the Node's direct neighbors with the right type
	 */
	public List<Node> getNeighbors(NodeType type) {
		return linkList.stream()
					   .map(Link::getDestination)
					   .filter(node -> node.getType() == type)
					   .collect(Collectors.toList());
	}
	
	/**
	 * Adds a new link to the Node
	 * @param link The link that will be added
	 */
	public void addLink(Link link) {
		linkList.add(link);
	}
	
	/**
	 * Tells us if two Nodes are at two distances of each other or not
	 * @param target The targeted Node
	 * @return Returns <code>true</code> if the Nodes are at two distance from each other, else <code>false</code>
	 */
	public boolean isTwoDistance(Node target) {
		for (Node oneDistanceNeighbors: getNeighbors()) {
			for (Node twoDistanceNeighbors: oneDistanceNeighbors.getNeighbors()) {
				if (twoDistanceNeighbors.equals(target)) {
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Node)) {
			return false;
		}
		Node p = (Node)o;
		return name.equals(p.name) && type == p.type;
	}
	
	@Override
	public String toString() {
		return type + " | " + name;
	}
	
}