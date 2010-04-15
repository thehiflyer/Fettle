package se.hiflyer.fettle.util;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Set;

public class SetMultimap<K, V> extends AbstractMultimap<K, V> {

	private final Map<K, Set<V>> map = Maps.newHashMap();

	public static <K, V> SetMultimap<K, V> create() {
		return new SetMultimap<K,V>();
	}

	@Override
	protected Map<K, Set<V>> getMap() {
		return map;
	}
}
