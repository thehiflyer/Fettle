package se.hiflyer.fettle.util;

import se.hiflyer.fettle.Transition;

import java.util.Collection;
import java.util.Set;

public class TransitionMap<S, E> implements Multimap<S, Transition<S, E>> {
	private final Multimap<S, Transition<S, E>> transitions;

	private TransitionMap(Multimap<S, Transition<S, E>> transitions) {
		this.transitions = transitions;
	}

	public static <S, E> TransitionMap<S, E> create(Class<S> stateClass) {
		return new TransitionMap<S, E>(SetMultimap.<S, Transition<S, E>>create());
	}

	@Override
	public boolean put(S key, Transition<S, E> value) {
		return transitions.put(key, value);
	}

	@Override
	public Collection<Transition<S, E>> get(S key) {
		return transitions.get(key);
	}

	@Override
	public Set<S> keySet() {
		return transitions.keySet();
	}
}
