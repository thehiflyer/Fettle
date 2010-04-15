package se.hiflyer.fettle;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.*;

public class EnumMultimap<K extends Enum<K>, V> {

	private final Map<K, Set<V>> map;

	private EnumMultimap(Class<K> clazz) {
		map = Maps.newEnumMap(clazz);
	}

	public static <K extends Enum<K>, V> EnumMultimap<K, V> create(Class<K> clazz) {
		return new EnumMultimap<K,V>(clazz);
	}

	public boolean put(K key, V value) {
		Set<V> values = map.get(key);
		if (values == null) {
			values = Sets.newHashSet();
			map.put(key, values);
		}
		return values.add(value);
	}

	public Collection<V> get(K key) {
		Set<V> values = map.get(key);
		if (values == null) {
			return Collections.emptySet();
		}
		return values;
	}

}
