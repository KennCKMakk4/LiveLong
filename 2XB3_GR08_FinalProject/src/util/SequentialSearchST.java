package util;

import java.util.LinkedList;
import java.lang.Iterable;

// Taken from Lab9-2 in CS2XB3
public class SequentialSearchST<Key, Value> {
	private Node first; // first node in the linked list
	private int N = 0;

	private class Node { // linked-list node
		Key key;
		Value val;
		Node next;

		public Node(Key key, Value val, Node next) {
			this.key = key;
			this.val = val;
			this.next = next;
		}
	}

	// Search for key, return associated
	public Value get(Key key) {
		for (Node x = first; x != null; x = x.next)
			if (key.equals(x.key))
				return x.val;
		return null;
	}

	// Search for key - update value if found, or grow table if new
	public void put(Key key, Value val) {
		if (val == null) {
			delete(key);
			return;
		}

		for (Node x = first; x != null; x = x.next)
			if (key.equals(x.key)) {
				x.val = val;
				return;
			}

		// Search miss, adding new node at the front (first) and pointing to
		// previous first
		first = new Node(key, val, first);
		N++;
	}

	public void delete(Key key) {
		// Deleting if it is the first key in chain
		if (first.key == key)
			first = first.next;

		// If the next key is line is the one to be deleted, redirect current
		// next to the one after
		for (Node x = first; x != null; x = x.next) {
			if (key.equals(x.next.key)) {
				x.next = x.next.next;
				N--;
				return;
			}
		}
	}

	public boolean contains(Key key) {
		return (get(key) != null);
	}

	public boolean isEmpty() {
		return (N == 0);
	}

	public int size() {
		return this.N;
	}

	public Iterable<Key> keys() {
		LinkedList<Key> q = new LinkedList<Key>();
		for (Node x = first; x != null; x = x.next)
			q.add(x.key);
		return q;
	}

}
