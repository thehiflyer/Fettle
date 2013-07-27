package se.fearless.fettle.impl;

import com.google.common.collect.Iterables;
import se.fearless.fettle.Action;
import se.fearless.fettle.StateMachine;
import se.fearless.fettle.TransitionModel;
import se.fearless.fettle.util.GuavaReplacement;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public abstract class AbstractTransitionModel<S, E, C> implements TransitionModel<S, E, C> {
	protected final Map<S, Map<E, Collection<Transition<S, E, C>>>> transitionMap;
	protected final Map<E, Collection<Transition<S, E, C>>> fromAllTransitions;
	protected final Map<S, Collection<Action<S, E, C>>> exitActions;
	protected final Map<S, Collection<Action<S, E, C>>> enterActions;
	protected final Class<S> stateClass;
	protected final Class<E> eventClass;
	protected final C defaultContext;


	protected AbstractTransitionModel(Class<S> stateClass, Class<E> eventClass, C defaultContext) {
		this.stateClass = stateClass;
		this.eventClass = eventClass;
		this.defaultContext = defaultContext;
		transitionMap = createMap(stateClass);
		exitActions = createMap(stateClass);
		enterActions = createMap(stateClass);
		fromAllTransitions = createMap(eventClass);
	}

	protected static <S, T> Map<S, T> createMap(Class<S> state) {
		if (state.isEnum()) {
			return new EnumMap(state);
		} else {
			return GuavaReplacement.newHashMap();
		}
	}

	@Override
	public boolean fireEvent(StateMachine<S, E, C> stateMachine, E event, C context) {
		S from = stateMachine.getCurrentState();
		return fireEvent(stateMachine, event, transitionMap.get(from), from, context) ||
				  fireEvent(stateMachine, event, fromAllTransitions, from, context);
	}

	@Override
	public boolean forceSetState(StateMachine<S, E, C> stateMachine, S forcedState) {
		S from = stateMachine.getCurrentState();
		if (from.equals(forcedState)) {
			return false;
		}
		forceSetState(stateMachine, from, forcedState, null, null, null);
		return true;
	}

	private void forceSetState(StateMachine<S, E, C> stateMachine, S from, S to, Transition<S, E, C> transition, E event, C context) {
		invoke(exitActions.get(from), from, to, event, context, stateMachine);
		stateMachine.rawSetState(to);
		if (transition != null) {
			transition.onTransition(from, to, event, context, stateMachine);
		}
		invoke(enterActions.get(to), from, to, event, context, stateMachine);
	}

	private boolean fireEvent(StateMachine<S, E, C> stateMachine, E event, Map<E, Collection<Transition<S, E, C>>> transitionMap, S from, C context) {
		if (transitionMap == null) {
			return false;
		}
		Collection<Transition<S, E, C>> transitions = transitionMap.get(event);
		if (transitions == null) {
			return false;
		}
		for (Transition<S, E, C> transition : transitions) {
			if (transition.isSatisfied(context)) {
				forceSetState(stateMachine, from, transition.getTo(), transition, event, context);
				return true;
			}
		}
		return false;
	}

	private void invoke(Collection<Action<S, E, C>> actions, S from, S to, E event, C context, StateMachine<S, E, C> stateMachine) {
		if (actions == null) {
			return;
		}
		for (Action<S, E, C> action : actions) {
			action.onTransition(from, to, event, context, stateMachine);
		}
	}

	public Map<S, Map<E, Collection<Transition<S, E, C>>>> getStateTransitions() {
		return Collections.unmodifiableMap(transitionMap);
	}

	public Map<E, Collection<Transition<S, E, C>>> getFromAllTransitions() {
		return Collections.unmodifiableMap(fromAllTransitions);
	}

	@Override
	public C getDefaultContext() {
		return defaultContext;
	}

	@Override
	public Map<E, Collection<Transition<S, E, C>>> getPossibleTransitions(S fromState) {
		Map<E, Collection<Transition<S, E, C>>> map = GuavaReplacement.newHashMap();
		Map<E, Collection<Transition<S, E, C>>> transitions = transitionMap.get(fromState);
		if (transitions != null) {
			map.putAll(transitions);
		}

		for (Map.Entry<E, Collection<Transition<S, E, C>>> entry : fromAllTransitions.entrySet()) {
			Collection<Transition<S, E, C>> transitionCollection = map.get(entry.getKey());
			if (transitionCollection == null) {
				transitionCollection = GuavaReplacement.newArrayList();
			}
			transitionCollection.addAll(entry.getValue());
			map.put(entry.getKey(), transitionCollection);
		}
		return map;
	}
}