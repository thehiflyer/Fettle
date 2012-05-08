package se.hiflyer.fettle.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuavaReplacement {
	private GuavaReplacement() {
	}

	public static <T> List<T> newArrayList() {
		return new ArrayList<T>();
	}

	public static <T> List<T> newArrayList(T... elements) {
		List<T> list = new ArrayList<T>();
		Collections.addAll(list, elements);
		return list;
	}

	public static <K, V> Map<K, V> newHashMap() {
		return new HashMap<K, V>();
	}
}
