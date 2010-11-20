package se.hiflyer.fettle.util;

import java.util.Collection;
import java.util.Set;

public interface Multimap<K, V> {
	boolean put(K key, V value);

	Collection<V> get(K key);

	Set<K> keySet();
}
