package structure;

/**
 * The class representing a Link
 * @author VAILLON Albert
 */
public class Link {
	
	private final LinkType type;
	private final int distance;
	private final Node destination;
	
	/**
	 * Creates a new Link
	 * @param type The type of the Link
	 * @param distance The distance of the Link
	 * @param destination The Node that the Link points to
	 * @throws LoadGraphException If <code>switchType(type)</code> throws a LoadGraphException
	 */
	public Link(char type, int distance, Node destination) throws LoadGraphException {
		this.type = switchType(type);
		if (distance < 0) {
			throw new LoadGraphException("Negative weighting is not authorized in the graph");
		}
		this.distance = distance;
		this.destination = destination;
	}
	
	/**
	 * Converts the character type in its corresponding <code>LinkType</code> constant
	 * @param type The character type
	 * @return Returns the corresponding <code>LinkType</code> constant
	 * @throws LoadGraphException If the type is not valid
	 */
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
	
	/**
	 * @return Returns the type of the Link
	 */
	public LinkType getType() {
		return type;
	}
	
	/**
	 * @return Returns the type of the Link
	 */
	public Node getDestination() {
		return destination;
	}
	
	@Override
	public String toString() {
		return type + " | " + distance + " (" + destination + ')';
	}
	
}