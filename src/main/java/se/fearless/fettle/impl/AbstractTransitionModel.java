package se.fearless.fettle.impl;

import se.fearless.fettle.Action;
import se.fearless.fettle.StateMachine;
import se.fearless.fettle.Transition;
import se.fearless.fettle.TransitionModel;

import java.util.*;

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

	@SuppressWarnings({"unchecked", "rawtypes"})
	protected static <S, T> Map<S, T> createMap(Class<S> state) {
		if (state.isEnum()) {
			return new EnumMap(state);
		} else {
			return new HashMap<>();
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
		if (shouldExecuteEntryAndExitActions(transition)) {
			invoke(exitActions.get(from), from, to, event, context, stateMachine);
		}
		stateMachine.rawSetState(to);
		if (transition != null) {
			transition.onTransition(from, to, event, context, stateMachine);
		}
		if (shouldExecuteEntryAndExitActions(transition)) {
			invoke(enterActions.get(to), from, to, event, context, stateMachine);
		}
	}

	private boolean shouldExecuteEntryAndExitActions(Transition<S, E, C> transition) {
		return transition == null || transition.shouldExecuteEntryAndExitActions();
	}

	private boolean fireEvent(StateMachine<S, E, C> stateMachine, E event, Map<E, Collection<Transition<S, E, C>>> transitionMap, S from, C context) {
		var transitions = transitionMap == null ? null : transitionMap.get(event);
		if (transitions == null) {
			return false;
		}
		return transitions.stream()
			.filter(t -> t.isSatisfied(context))
			.findFirst()
			.map(transition -> {
				forceSetState(stateMachine, from, transition.getTo(), transition, event, context);
				return true;
			})
			.orElse(false);
	}

	private void invoke(Collection<Action<S, E, C>> actions, S from, S to, E event, C context, StateMachine<S, E, C> stateMachine) {
		if (actions != null) {
			actions.forEach(action -> action.onTransition(from, to, event, context, stateMachine));
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
	public Map<E, Collection<? extends Transition<S, E, C>>> getPossibleTransitions(S fromState) {
		Map<E, Collection<? extends Transition<S, E, C>>> map = new HashMap<>();
		var transitions = transitionMap.get(fromState);
		if (transitions != null) {
			map.putAll(transitions);
		}

		fromAllTransitions.forEach((event, transitionCollection) -> {
			@SuppressWarnings("unchecked")
			var existing = (Collection<Transition<S, E, C>>) map.get(event);
			if (existing == null) {
				map.put(event, transitionCollection);
			} else {
				existing.addAll(transitionCollection);
			}
		});
		return map;
	}
}