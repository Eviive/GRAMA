package grama.model;

import java.util.Objects;

/**
 * The class representing a link
 * @author VAILLON Albert
 * @author BAUDRY Lilian
 * @version JDK 11.0.13
 */
public final class Link implements Comparable<Link> {
	
	private final LinkType type;
	private final int distance;
	private final Node departure;
	private final Node destination;
	
	/**
	 * Creates a new Link
	 * @param type The type of the Link
	 * @param distance The distance of the Link
	 * @param departure The Node that the Link starts from
	 * @param destination The Node that the Link points to
	 * @throws LoadGraphException If <code>LinkType.typeOf(type)</code> throws a LoadGraphException
	 */
	public Link(char type, int distance, Node departure, Node destination) throws LoadGraphException {
		this.type = LinkType.typeOf(type);
		if (distance < 0) {
			throw new LoadGraphException("Les valuations négatives ne sont pas autorisées dans le graphe");
		}
		this.distance = distance;
		this.departure = departure;
		this.destination = destination;
	}
	
	/**
	 * @return Returns the type of this Link
	 */
	public LinkType getType() {
		return type;
	}
	
	/**
	 * @return Returns the distance of this Link
	 */
	public int getDistance() {
		return distance;
	}
	
	/**
	 * @return Returns the departure of this Link
	 */
	public Node getDeparture() {
		return departure;
	}
	
	/**
	 * @return Returns the destination of this Link
	 */
	public Node getDestination() {
		return destination;
	}
	
	@Override
	public int hashCode(){
		return Objects.hash(type, distance, departure, destination);
	}
	
	@Override
	public String toString() {
		return type.getCharID() + " (" + distance + " km) " + departure.getName() + " <-> " + destination.getName();
	}
	
	@Override
	public boolean equals(Object o){
		if (!(o instanceof Link))
			return false;
		Link e = (Link)o;
		return type == e.type && distance == e.distance && ((departure.equals(e.departure) && destination.equals(e.destination)) || (departure.equals(e.destination) && destination.equals(e.departure)));
	}
	
	@Override
	public int compareTo(Link link) {
		return toString().compareTo(link.toString());
	}
	
}