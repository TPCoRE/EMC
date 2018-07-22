package tpc.mc.emc;

/**
 * A thread-safe unbound deque
 * */
public final class Deque<E> {
	
	private final Node first;
	private final Node last;
	
	/**
	 * Create an instance
	 * */
	public Deque() {
		this.first = new Node();
		this.last = new Node();
		
		//INIT
		this.first.next(this.last);
		this.last.prev(this.first);
	}
	
	/**
	 * Add at the head, null means delete it
	 * */
	public final synchronized void head(E elem) {
		Node first = this.first;
		Node n;
		
		if(elem == null) {
			if(this.empty()) return;
			
			first.next = n = first.next.next;
			n.prev = first;
		} else {
			n = first.next;
			first.next = n.prev = new Node().val(elem).next(n).prev(first);
		}
	}
	
	/**
	 * Add the tail, null means delete it
	 * */
	public final synchronized void tail(E elem) {
		Node last = this.last;
		Node n;
		
		if(elem == null) {
			if(this.empty()) return;
			
			last.prev = n = last.prev.prev;
			n.next = last;
		} else {
			n = last.prev;
			last.prev = n.next = new Node().val(elem).next(last).prev(n);
		}
	}
	
	/**
	 * Have a look at the head of the deque, null for empty
	 * */
	public final synchronized E head() {
		if(this.empty()) return null;
		
		return this.first.next.val;
	}
	
	/**
	 * Have a look at the tail of the deque, null for empty
	 * */
	public final synchronized E tail() {
		if(this.empty()) return null;
		
		return this.last.prev.val;
	}
	
	/**
	 * Check if it is empty
	 * */
	public final synchronized boolean empty() {
		return this.first.next == this.last;
	}
	
	/**
	 * Clean the data
	 * */
	public final synchronized void clear() {
		this.first.next(this.last);
		this.last.prev(this.first);
	}
	
	/**
	 * Box an element
	 * */
	private final class Node {
		
		E val;
		Node next;
		Node prev;
		
		private final Node next(Node ne) {
			this.next = ne;
			return this;
		}
		
		private final Node prev(Node ne) {
			this.prev = ne;
			return this;
		}
		
		private final Node val(E elem) {
			this.val = elem;
			return this;
		}
	}
}
