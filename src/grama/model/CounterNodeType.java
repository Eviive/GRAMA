package grama.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author BAUDRY Lilian
 * @version JDK 11.0.13
 */
public class CounterNodeType {
	
	private Map<NodeType, Integer> mapCounter = new HashMap<>();
	
	public CounterNodeType(){
		reset();
	}
	
	public CounterNodeType(int cities, int retaurants, int recreations){
		this();
		mapCounter.put(NodeType.CITY,cities);
		mapCounter.put(NodeType.RESTAURANT,retaurants);
		mapCounter.put(NodeType.RECREATION,recreations);
	}
	
	public void update(List<Node> nodes){
		reset();
		nodes.stream()
			 .distinct()
			 .forEach(node -> incrementType(node.getType()));
	}
	
	public void reset(){
		for (NodeType NodeType : NodeType.values()){
			mapCounter.put(NodeType,0);
		}
	}
	
	public void incrementType(NodeType type){
		mapCounter.put(type, mapCounter.get(type)+1);
	}

	public Map<NodeType, Integer> getMapCounter() {
		return mapCounter;
	}
	
	public int getNumber(NodeType type){
		return mapCounter.get(type);
	}
	
	public NodeType getInsufisantType(CounterNodeType objectif){
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
