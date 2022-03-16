package structure;

public class Node {
	
	private final NodeType type;
	private final String name;
	private Link neighbor;
	private Node next;
	
	public Node(char type, String name, Link nodeNeighbor, Node nodeNext) throws LoadGraphException {
		this.type = switchType(type);
		this.name = name;
		this.neighbor = nodeNeighbor;
		this.next = nodeNext;
	}
	
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
	
	public Link getNodeNeighbor() {
		return neighbor;
	}
	
	public Node getNodeNext() {
		return next;
	}
	
	public void setNodeNeighbor(Link nodeNeighbor) {
		this.neighbor = nodeNeighbor;
	}
	
	public void setNodeNext(Node nodeNext) {
		this.next = nodeNext;
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