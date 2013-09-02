package se.fearless.fettle.impl;

import se.fearless.fettle.Action;
import se.fearless.fettle.StateMachine;
import se.fearless.fettle.StateMachineTemplate;
import se.fearless.fettle.util.GuavaReplacement;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ImmutableTransitionModel<S, E, C> extends AbstractTransitionModel<S, E, C> implements StateMachineTemplate<S, E, C> {

	public ImmutableTransitionModel(Class<S> stateClass, Class<E> eventClass,
											  Map<S, Map<E, Collection<BasicTransition<S, E, C>>>> transitionMap,
											  Map<E, Collection<BasicTransition<S, E, C>>> fromAllTransitions,
											  Map<S, Collection<Action<S, E, C>>> exitActions,
											  Map<S, Collection<Action<S, E, C>>> enterActions, C defaultContext) {
		super(stateClass, eventClass, defaultContext);
		this.exitActions.putAll(copy(exitActions));
		this.enterActions.putAll(copy(enterActions));
		this.transitionMap.putAll(copyTransitions(transitionMap));
		this.fromAllTransitions.putAll(copyTransitions3(fromAllTransitions));
	}

	private Map<E, Collection<BasicTransition<S, E, C>>> copyTransitions3(Map<E, Collection<BasicTransition<S, E, C>>> input) {
		Map<E, Collection<BasicTransition<S, E, C>>> res = GuavaReplacement.newHashMap();
		for (Map.Entry<E, Collection<BasicTransition<S, E, C>>> entry : input.entrySet()) {
			E key = entry.getKey();
			Collection<BasicTransition<S, E, C>> value = entry.getValue();
			res.put(key, copy(value));
		}
		return res;
	}

	private Map<S, Map<E, Collection<BasicTransition<S, E, C>>>> copyTransitions(Map<S, Map<E, Collection<BasicTransition<S, E, C>>>> input) {
		Map<S, Map<E, Collection<BasicTransition<S, E, C>>>> res = GuavaReplacement.newHashMap();
		for (Map.Entry<S, Map<E, Collection<BasicTransition<S, E, C>>>> entry : input.entrySet()) {
			S key = entry.getKey();
			Map<E, Collection<BasicTransition<S, E, C>>> value = entry.getValue();
			res.put(key, copyTransitions2(value));
		}
		return res;
	}

	private Map<E, Collection<BasicTransition<S, E, C>>> copyTransitions2(Map<E, Collection<BasicTransition<S, E, C>>> input) {
		Map<E, Collection<BasicTransition<S, E, C>>> res = GuavaReplacement.newHashMap();
		for (Map.Entry<E, Collection<BasicTransition<S, E, C>>> entry : input.entrySet()) {
			E key = entry.getKey();
			Collection<BasicTransition<S, E, C>> value = entry.getValue();
			res.put(key, copy(value));
		}
		return res;
	}

	private Map<S, Collection<Action<S, E, C>>> copy(Map<S, Collection<Action<S, E, C>>> input) {
		Map<S, Collection<Action<S, E, C>>> res = GuavaReplacement.newHashMap();
		for (Map.Entry<S, Collection<Action<S, E, C>>> entry : input.entrySet()) {
			S key = entry.getKey();
			Collection<Action<S, E, C>> value = entry.getValue();
			res.put(key, copy(value));
		}
		return res;
	}

	private <T> Collection<T> copy(Collection<T> input) {
		Collection<T> res = GuavaReplacement.newArrayList();
		res.addAll(input);
		return res;
	}

	@Override
	public StateMachine<S, E, C> newStateMachine(S init) {
		return newStateMachine(init, new ReentrantLock());
	}

	@Override
	public StateMachine<S, E, C> newStateMachine(S init, Lock lock) {
		return new TemplateBasedStateMachine<S, E, C>(this, init, lock);
	}
}