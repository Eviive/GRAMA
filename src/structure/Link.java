package structure;

public class Link {
	
	private final LinkType type;
	private final int distance;
	private final Node destination;
	private Link next;
	
	public Link(char type, int distance, Node destination, Link next) throws LoadGraphException {
		this.type = switchType(type);
		if (distance < 0) {
			throw new LoadGraphException("Negative weighting is not authorized in the graph");
		}
		this.destination = destination;
		this.distance = distance;
		this.next = next;
	}
	
	public static LinkType switchType(char type) throws LoadGraphException {
		switch (type) {
			case 'A':
				return LinkType.HIGHWAY;
			case 'N':
				return LinkType.NATIONAL;
			case 'D':
				return LinkType.DEPARTMENTAL;
			default:
				throw new LoadGraphException("Switch link type is invalid (should be H, N, or D)");
		}
	}
	
	public LinkType getType() {
		return type;
	}
	
	public int getDistance() {
		return distance;
	}
	
	public Link getNext() {
		return next;
	}
	
	public void setNext(Link next) {
		this.next = next;
	}
	
	@Override
	public String toString() {
		return type + " | " + distance + " (" + destination + ')';
	}
	
}