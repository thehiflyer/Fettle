package se.hiflyer.fettle.util;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Set;

public class EnumMultimap<K extends Enum<K>, V> extends AbstractMultimap<K,V> {

	private final Map<K, Set<V>> map;

	private EnumMultimap(Class<K> clazz) {
		map = Maps.newEnumMap(clazz);
	}

	public static <K extends Enum<K>, V> AbstractMultimap<K, V> create(Class<K> clazz) {
		return new EnumMultimap<K,V>(clazz);
	}

	@Override
	protected Map<K, Set<V>> getMap() {
		return map;
	}
}
