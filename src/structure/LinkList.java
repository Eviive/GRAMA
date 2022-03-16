package structure;

public class LinkList extends List {
	
	public LinkList(Node head) {
		super(head);
	}
	
	public void append(Link link) {
		Link maillon = getHead().getNodeNeighbor();
		if (maillon != null) {
			while (maillon.getNext() != null) {
				maillon = maillon.getNext();
			}
			maillon.setNext(link);
		} else {
			getHead().setNodeNeighbor(link);
		}
	}
	
}