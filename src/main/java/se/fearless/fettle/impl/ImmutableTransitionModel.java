package se.fearless.fettle.impl;

import se.fearless.fettle.Action;
import se.fearless.fettle.StateMachine;
import se.fearless.fettle.StateMachineTemplate;
import se.fearless.fettle.Transition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class ImmutableTransitionModel<S, E, C> extends AbstractTransitionModel<S, E, C> implements StateMachineTemplate<S, E, C> {

	public ImmutableTransitionModel(Class<S> stateClass, Class<E> eventClass,
											  Map<S, Map<E, Collection<Transition<S, E, C>>>> transitionMap,
											  Map<E, Collection<Transition<S, E, C>>> fromAllTransitions,
											  Map<S, Collection<Action<S, E, C>>> exitActions,
											  Map<S, Collection<Action<S, E, C>>> enterActions, C defaultContext) {
		super(stateClass, eventClass, defaultContext);
		this.exitActions.putAll(copy(exitActions));
		this.enterActions.putAll(copy(enterActions));
		this.transitionMap.putAll(copyTransitions(transitionMap));
		this.fromAllTransitions.putAll(copyTransitions3(fromAllTransitions));
	}

	private Map<E, Collection<Transition<S, E, C>>> copyTransitions3(Map<E, Collection<Transition<S, E, C>>> input) {
		return input.entrySet().stream()
			.collect(Collectors.toMap(
				Map.Entry::getKey,
				e -> new ArrayList<>(e.getValue())
			));
	}

	private Map<S, Map<E, Collection<Transition<S, E, C>>>> copyTransitions(Map<S, Map<E, Collection<Transition<S, E, C>>>> input) {
		return input.entrySet().stream()
			.collect(Collectors.toMap(
				Map.Entry::getKey,
				e -> copyTransitions2(e.getValue())
			));
	}

	private Map<E, Collection<Transition<S, E, C>>> copyTransitions2(Map<E, Collection<Transition<S, E, C>>> input) {
		return input.entrySet().stream()
			.collect(Collectors.toMap(
				Map.Entry::getKey,
				e -> new ArrayList<>(e.getValue())
			));
	}

	private Map<S, Collection<Action<S, E, C>>> copy(Map<S, Collection<Action<S, E, C>>> input) {
		return input.entrySet().stream()
			.collect(Collectors.toMap(
				Map.Entry::getKey,
				e -> new ArrayList<>(e.getValue())
			));
	}

	@Override
	public StateMachine<S, E, C> newStateMachine(S init) {
		return newStateMachine(init, new ReentrantLock());
	}

	@Override
	public StateMachine<S, E, C> newStateMachine(S init, Lock lock) {
		return new TemplateBasedStateMachine<>(this, init, lock);
	}
}