package structure;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The class representing a node
 * @author VAILLON Albert
 * @version JDK 11.0.13
 */
public final class Node {
	
	private final NodeType type;
	private final String name;
	private final List<Link> linkList = new ArrayList<>();
	
	/**
	 * Creates a new <code>Node</code>
	 * @param type The type of the <code>Node</code>
	 * @param name The name of the <code>Node</code>
	 * @throws LoadGraphException If <code>NodeType.typeOf(type)</code> throws a <code>LoadGraphException</code>
	 */
	public Node(char type, String name) throws LoadGraphException {
		this.type = NodeType.typeOf(type);
		this.name = name;
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
	 * @param nbJumps The number of jumps we have to do
	 * @param results The <code>List</code> in which the results will go
	 * @return Returns the <code>List</code> of all the <code>Nodes</code> you can go to by making <code>nbJumps</code> jumps or less from this <code>Node</code>
	 */
	public List<Node> getNeighbors(int nbJumps, List<Node> results) {
		--nbJumps;
		
		// if nbJumps is less than 0 you can't go anywhere but where you are
		if (nbJumps < 0) {
			results.add(this);
			return results;
		}
		
		// the List of all the neighbors of this Node
		List<Node> neighbors = linkList.stream()
									   .map(link -> link.getDestination())
									   .collect(Collectors.toList());
		
		// if it's not the last jump (nbJumps == 0) then do getNeighbors() on all of them
		if (nbJumps > 0) {
			for (Node neighbor: neighbors) {
				neighbor.getNeighbors(nbJumps, results)
						.stream()
						.filter(node -> !results.contains(node))
						.forEach(node -> results.add(node));
			}
			return results;
		// if it's the last jump (nbJumps == 0) then we add the neighbors to the results List if they're not already in it
		} else {
			neighbors.stream()
					 .filter(node -> !results.contains(node))
					 .forEach(node -> results.add(node));
			return results;
		}
	}
	
	/**
	 * @param nbJumps The number of jumps we have to do
	 * @param results The <code>List</code> in which the results will go
	 * @param type The type of the <code>Nodes</code> we want to get
	 * @return Returns the <code>List</code> of all the <code>Nodes</code> of type <code>type</code> you can go to by making <code>nbJumps</code> jumps or less from this <code>Node</code>
	 */
	public List<Node> getNeighbors(int nbJumps, List<Node> results, NodeType type) {
		return getNeighbors(nbJumps, results).stream()
											 .filter(node -> node.getType() == type)
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
	 * Tells us if this <code>Node</code> is at two distances from the target <code>Node</code>
	 * @param target The targeted <code>Node</code>
	 * @return Returns <code>true</code> if the <code>Nodes</code> are at two distance from each other
	 */
	public boolean isTwoDistance(Node target) {
		return getNeighbors(2, new ArrayList<>()).contains(target);
	}
	
	/**
	 * Tells us if this <code>Node</code> has more two jumps neighbors of type <code>type</code> than the <code>target Node</code>
	 * @param target The targeted <code>Node</code>
	 * @param type The type of <code>Nodes</code> we're counting
	 * @return Returns <code>true</code> if this <code>Node</code> has the same number of two jumps neighbors of type <code>type</code> than the <code>target Node</code> or more
	 */
	public boolean isMoreLinkedToType(Node target, NodeType type) {
		int nbNodeCities = getNeighbors(2, new ArrayList<>(), type).size();
		int nbTargetCities = target.getNeighbors(2, new ArrayList<>(), type).size();
		return nbNodeCities >= nbTargetCities;
	}
	
	/**
	 * Compares this <code>Node</code> to the specified <code>Object</code>
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
	 * Creates a <code>String</code> object representing this <code>Node</code>
	 * @return Returns a <code>String</code> representation of this <code>Node</code>
	 */
	@Override
	public String toString() {
		return type + " | " + name;
	}
	
}