package util;

import java.util.LinkedList;

// Taken from Lab9-2 of 2XB3
public class SeparateChainingHashST<Key, Value> {
	private int M; // hash table size

	// array of symbol tables
	private SequentialSearchST<Key, Value>[] st;

	public SeparateChainingHashST() {
		this(997);
	}

	@SuppressWarnings("unchecked")
	public SeparateChainingHashST(int M) {
		// Create #M linked lists
		this.M = M;

		// array needs cast b/c java prohibits arrays w/ generics
		st = (SequentialSearchST<Key, Value>[]) new SequentialSearchST[M];
		
		// instantiating array of ST
		for (int i = 0; i < M; i++)
			st[i] = new SequentialSearchST<Key, Value>();

	}

	private int hash(Key key) {
		return (key.hashCode() & 0x7fffffff) % M;
	}

	public Value get(Key key) {
		return (Value) st[hash(key)].get(key);
	}

	public void put(Key key, Value val) {
		st[hash(key)].put(key, val);
	}
	
	
	public Iterable<Key> keys() {
		LinkedList<Key> q = new LinkedList<Key>();
		for(int i = 0; i < M; i++)
			for (Key key : st[i].keys())
				q.add(key);
		return q;
	}

}
