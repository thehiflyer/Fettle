package se.hiflyer.fettle.util;

import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public abstract class AbstractMultimap<K, V> implements Multimap<K, V> {

	protected abstract Map<K, Set<V>> getMap();

	@Override
	public boolean put(K key, V value) {
		Map<K, Set<V>> map = getMap();
		Set<V> values = map.get(key);
		if (values == null) {
			values = Sets.newHashSet();
			map.put(key, values);
		}
		return values.add(value);
	}

	@Override
	public Collection<V> get(K key) {
		Set<V> values = getMap().get(key);
		if (values == null) {
			return Collections.emptySet();
		}
		return values;
	}

	@Override
	public Set<K> keySet() {
		return getMap().keySet();
	}
}
