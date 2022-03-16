package structure;

public abstract class List {
	
	private Node head;
	
	public List() {}
	
	public List(Node head) {
		this.head = head;
	}
	
	public Node getHead() {
		return head;
	}
	
	public void setHead(Node head) {
		this.head = head;
	}
	
	public boolean isEmpty() {
		return head == null;
	}
	
	public Node last() {
		Node maillon = this.head;
		while (maillon.getNodeNext() != null) {
			maillon = maillon.getNodeNext();
		}
		return maillon;
	}
	
}