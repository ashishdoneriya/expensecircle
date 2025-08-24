package com.csetutorials.expensecircle.ds;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class LRUCache<K, V> {

	private final int size;
	private Map<K, V> map = new LinkedHashMap<>();
	private ReentrantLock lock = new ReentrantLock();

	public LRUCache(int size) {
		this.size = size;
	}

	public void put(K key, V value) {
		lock.lock();
		if (map.size() == this.size) {
			map.remove(map.keySet().iterator().next());
		}
		if (map.containsKey(key)) {
			map.remove(key);
		}
		map.put(key, value);
		lock.unlock();
	}

	public V get(K key) {
		lock.lock();
		V value = map.get(key);
		if (value != null) {
			map.remove(key);
			map.put(key, value);
		}
		lock.unlock();
		return value;

	}

}
