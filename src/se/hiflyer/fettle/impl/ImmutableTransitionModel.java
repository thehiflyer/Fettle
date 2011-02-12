package se.hiflyer.fettle.impl;

import se.hiflyer.fettle.Action;
import se.hiflyer.fettle.StateMachine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ImmutableTransitionModel<S, E> extends AbstractTransitionModel<S, E> {

	public ImmutableTransitionModel(Class<S> stateClass, Class<E> eventClass,
											  Map<S, Map<E, Collection<Transition<S, E>>>> transitionMap,
											  Map<E, Collection<Transition<S, E>>> fromAllTransitions,
											  Map<S, Collection<Action<S, E>>> exitActions,
											  Map<S, Collection<Action<S, E>>> enterActions) {
		super(stateClass, eventClass);
		this.exitActions.putAll(copy(exitActions));
		this.enterActions.putAll(copy(enterActions));
		this.transitionMap.putAll(copyTransitions(transitionMap));
		this.fromAllTransitions.putAll(copyTransitions3(fromAllTransitions));
	}

	private Map<E, Collection<Transition<S, E>>> copyTransitions3(Map<E, Collection<Transition<S, E>>> input) {
		Map<E, Collection<Transition<S, E>>> res = new HashMap<E, Collection<Transition<S, E>>>();
		for (Map.Entry<E, Collection<Transition<S, E>>> entry : input.entrySet()) {
			E key = entry.getKey();
			Collection<Transition<S, E>> value = entry.getValue();
			res.put(key, copy(value));
		}
		return res;
	}

	private Map<S, Map<E, Collection<Transition<S, E>>>> copyTransitions(Map<S, Map<E, Collection<Transition<S, E>>>> input) {
		Map<S, Map<E, Collection<Transition<S, E>>>> res = new HashMap<S, Map<E, Collection<Transition<S, E>>>>();
		for (Map.Entry<S, Map<E, Collection<Transition<S, E>>>> entry : input.entrySet()) {
			S key = entry.getKey();
			Map<E, Collection<Transition<S, E>>> value = entry.getValue();
			res.put(key, copyTransitions2(value));
		}
		return res;
	}

	private Map<E, Collection<Transition<S, E>>> copyTransitions2(Map<E, Collection<Transition<S, E>>> input) {
		Map<E, Collection<Transition<S, E>>> res = new HashMap<E, Collection<Transition<S, E>>>();
		for (Map.Entry<E, Collection<Transition<S, E>>> entry : input.entrySet()) {
			E key = entry.getKey();
			Collection<Transition<S, E>> value = entry.getValue();
			res.put(key, copy(value));
		}
		return res;
	}

	private Map<S, Collection<Action<S, E>>> copy(Map<S, Collection<Action<S, E>>> input) {
		Map<S, Collection<Action<S, E>>> res = new HashMap<S, Collection<Action<S, E>>>();
		for (Map.Entry<S, Collection<Action<S, E>>> entry : input.entrySet()) {
			S key = entry.getKey();
			Collection<Action<S, E>> value = entry.getValue();
			res.put(key, copy(value));
		}
		return res;
	}

	private <T> Collection<T> copy(Collection<T> input) {
		Collection<T> res = new ArrayList<T>();
		res.addAll(input);
		return res;
	}

	@Override
	public StateMachine<S, E> newStateMachine(S init) {
		return new TemplateBasedStateMachine<S, E>(this, init);
	}
}
