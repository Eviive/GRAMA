package structure;

public class NodeList extends List {
	
	public void append(Node node) {
		if (!isEmpty()) {
			Node maillon = getHead();
			while (maillon.getNodeNext() != null) {
				maillon = maillon.getNodeNext();
			}
			maillon.setNodeNext(node);
		} else {
			setHead(node);
		}
	}
	
	public Node search(char type, String name) throws LoadGraphException {
		Node cible = new Node(type, name, null, null);
		if (!isEmpty()) {
			Node maillon = getHead();
			while (maillon != null) {
				if (maillon.equals(cible)) {
					return maillon;
				}
				maillon = maillon.getNodeNext();
			}
		}
		throw new LoadGraphException("The following neighbor is not part of the graph : (" + cible + ")");
	}
	
	public void display() {
		if (!isEmpty()) {
			Node maillon = getHead();
			while (maillon != null) {
				System.out.printf("%-22s=>%-4s", maillon, "");
				
				Link neighbor = maillon.getNodeNeighbor();
				while (neighbor != null) {
					if(neighbor.getNext() != null) {
						System.out.printf("%-40s\t", neighbor);
					} else {
						System.out.println(neighbor);
					}
					neighbor = neighbor.getNext();
				}
				maillon = maillon.getNodeNext();
			}
		} else {
			System.out.println("The graph is empty");
		}
	}
	
}