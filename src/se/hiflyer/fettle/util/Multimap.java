package se.hiflyer.fettle.util;

import java.util.Collection;

public interface Multimap<K, V> {
	boolean put(K key, V value);

	Collection<V> get(K key);
}
