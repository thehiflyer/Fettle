package se.hiflyer.fettle.impl;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import se.hiflyer.fettle.Action;
import se.hiflyer.fettle.Arguments;
import se.hiflyer.fettle.StateMachine;
import se.hiflyer.fettle.TransitionModel;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

public abstract class AbstractTransitionModel<S, E> implements TransitionModel<S, E> {
	protected final Map<S, Map<E, Collection<Transition<S, E>>>> transitionMap;
	protected final Map<E, Collection<Transition<S, E>>> fromAllTransitions;
	protected final Map<S, Collection<Action<S, E>>> exitActions;
	protected final Map<S, Collection<Action<S, E>>> enterActions;
	protected final Class<S> stateClass;
	protected final Class<E> eventClass;

	protected AbstractTransitionModel(Class<S> stateClass, Class<E> eventClass) {
		this.stateClass = stateClass;
		this.eventClass = eventClass;
		transitionMap = createMap(stateClass);
		exitActions = createMap(stateClass);
		enterActions = createMap(stateClass);
		fromAllTransitions = createMap(eventClass);
	}

	protected static <S, T> Map<S, T> createMap(Class<S> state) {
		if (state.isEnum()) {
			return new EnumMap(state);
		} else {
			return Maps.newHashMap();
		}
	}

	@Override
	public boolean fireEvent(StateMachine<S, E> stateMachine, E event, Arguments args) {
		S from = stateMachine.getCurrentState();
		return fireEvent(stateMachine, event, transitionMap.get(from), from, args) ||
				  fireEvent(stateMachine, event, fromAllTransitions, from, args);
	}

	@Override
	public boolean forceSetState(StateMachine<S, E> stateMachine, S forcedState) {
		S from = stateMachine.getCurrentState();
		if (from.equals(forcedState)) {
			return false;
		}
		forceSetState(stateMachine, from, forcedState, null, null, Arguments.NO_ARGS);
		return true;
	}

	private void forceSetState(StateMachine<S, E> stateMachine, S from, S to, Transition<S, E> transition, E event, Arguments args) {
		invoke(exitActions.get(from), from, to, event, args);
		stateMachine.rawSetState(to);
		if (transition != null) {
			transition.onTransition(from, to, event, args);
		}
		invoke(enterActions.get(to), from, to, event, args);
	}

	private boolean fireEvent(StateMachine<S, E> stateMachine, E event, Map<E, Collection<Transition<S, E>>> transitionMap, S from, Arguments args) {
		if (transitionMap == null) {
			return false;
		}
		Collection<Transition<S, E>> transitions = transitionMap.get(event);
		if (transitions == null) {
			return false;
		}
		for (Transition<S, E> transition : transitions) {
			if (transition.isSatisfied(args)) {
				forceSetState(stateMachine, from, transition.getTo(), transition, event, args);
				return true;
			}
		}
		return false;
	}

	private void invoke(Collection<Action<S, E>> actions, S from, S to, E event, Arguments args) {
		if (actions == null) {
			return;
		}
		for (Action<S, E> action : actions) {
			action.onTransition(from, to, event, args);
		}
	}

	public Map<S, Map<E, Collection<Transition<S, E>>>> getStateTransitions() {
		return ImmutableMap.copyOf(transitionMap);
	}

	public Map<E, Collection<Transition<S, E>>> getFromAllTransitions() {
		return ImmutableMap.copyOf(fromAllTransitions);
	}
}

