package grama.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author BAUDRY Lilian
 * @version JDK 11.0.13
 */
public final class CounterNodeType {
	
	private Map<NodeType, Integer> mapCounter = new HashMap<>();
	
	public CounterNodeType() {
		reset();
	}
	
	public CounterNodeType(int cities, int retaurants, int recreations) {
		this();
		mapCounter.put(NodeType.CITY,cities);
		mapCounter.put(NodeType.RESTAURANT,retaurants);
		mapCounter.put(NodeType.RECREATION,recreations);
	}
	
	/**
	 * Updates the number of needed types
	 * @param nodes The <code>List</code> of <code>Nodes</code> in which we will count
	 */
	public void update(List<Node> nodes) {
		reset();
		nodes.stream()
			 .distinct()
			 .forEach(node -> incrementType(node.getType()));
	}
	
	/**
	 * Resets all the needed types
	 */
	public void reset() {
		for (NodeType NodeType : NodeType.values()) {
			mapCounter.put(NodeType, 0);
		}
	}
	
	/**
	 * Increments by 1 the type <code>type</code>
	 * @param type The type of <code>Node</code> we want to increment
	 */
	public void incrementType(NodeType type) {
		mapCounter.put(type, mapCounter.get(type)+1);
	}
	
	/**
	 * @return Returns the counter <code>Map</code>
	 */
	public Map<NodeType, Integer> getMapCounter() {
		return mapCounter;
	}
	
	/**
	 * @param type The type we want to get the number from
	 * @return Returns the number of <code>Nodes</code> we need to go through of type <code>type</code>
	 */
	public int getNumber(NodeType type) {
		return mapCounter.get(type);
	}
	
	/**
	 * @param objectif The object representing the number of elements we need to go through
	 * @return Returns the type of <code>Nodes</code> we miss
	 */
	public NodeType getInsufisantType(CounterNodeType objectif) {
		return mapCounter.entrySet().stream()
									.filter(counter -> counter.getValue() < objectif.getMapCounter().get(counter.getKey()))
									.map(counter -> counter.getKey())
									.findFirst().orElse(null);
	}
	
	@Override
	public String toString() {
		return mapCounter.toString();
	}

}
