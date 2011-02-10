package se.hiflyer.fettle;

import com.google.common.collect.Maps;
import se.hiflyer.fettle.util.EnumMultimap;
import se.hiflyer.fettle.util.Multimap;
import se.hiflyer.fettle.util.SetMultimap;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class BasicStateMachine<S, E> implements StateMachine<S, E>, StateMachineConstructor<S, E>, StateMachineInternalsInformer<S,E> {
	private S currentState;
	final Multimap<S, Transition<S, E>> stateTransitions;
	final Map<E, Transition<S, E>> fromAllTransitions;
	private final Multimap<S, Action<S, E>> entryActions;
	private final Multimap<S, Action<S, E>> exitActions;

	private BasicStateMachine(S initial, Multimap<S, Transition<S, E>> stateTransitions, Multimap<S, Action<S, E>> entryActions, Multimap<S, Action<S, E>> exitActions) {
		currentState = initial;
		this.stateTransitions = stateTransitions;
		this.entryActions = entryActions;
		this.exitActions = exitActions;
		fromAllTransitions = Maps.newHashMap();
	}

	@SuppressWarnings(value = "unchecked")
	public static <S, E> BasicStateMachine<S, E> createStateMachine(S initial) {
		if (initial.getClass().isEnum()) {
			return (BasicStateMachine<S, E>) BasicStateMachine.createStateMachineOfEnum((Class<Enum>) ((Enum<?>) initial).getClass(), (Enum<?>) initial);
		}
		return new BasicStateMachine<S, E>(initial, SetMultimap.<S, Transition<S, E>>create(),
				  SetMultimap.<S, Action<S, E>>create(), SetMultimap.<S, Action<S, E>>create());
	}

	private static <S extends Enum<S>, E> StateMachine<S, E> createStateMachineOfEnum(Class<S> clazz, S initial) {
		return new BasicStateMachine<S, E>(initial, EnumMultimap.<S, Transition<S, E>>create(clazz),
				  EnumMultimap.<S, Action<S, E>>create(clazz), EnumMultimap.<S, Action<S, E>>create(clazz));
	}


	@Override
	public void addFromAllTransition(S to, E event, Condition condition, List<Action<S, E>> actions) {
		fromAllTransitions.put(event, new Transition<S, E>(null, to, condition, event, actions));
	}

	@Override
	public void addTransition(S from, S to, E event, Condition condition, List<Action<S, E>> actions) {
		stateTransitions.put(from, new Transition<S, E>(from, to, condition, event, actions));
	}

	@Override
	public S getCurrentState() {
		return currentState;
	}

	private void moveToNewState(Transition<S, E> transition, E cause, Arguments args) {
		S from = currentState;
		S to = transition.getTo();
		runActions(exitActions, currentState, from, to, cause, args);
		runActions(transition.getTransitionActions(), from, to, cause, args);
		currentState = to;
		runActions(entryActions, currentState, from, to, cause, args);
	}

	private void runActions(Collection<Action<S, E>> actions, S from, S to, E cause, Arguments args) {
		for (Action<S, E> action : actions) {
			action.perform(from, to, cause, args);
		}
	}


	private void runActions(Multimap<S, Action<S, E>> actionMap, S state, S from, S to, E cause, Arguments args) {
		runActions(actionMap.get(state), from, to, cause, args);
	}

	@Override
	public void addEntryAction(S entryState, Action<S, E> action) {
		entryActions.put(entryState, action);
	}

	@Override
	public void addExitAction(S exitState, Action<S, E> action) {
		exitActions.put(exitState, action);
	}

	@Override
	public Multimap<S, Transition<S, E>> getStateTransitions() {
		return stateTransitions;
	}

	@Override
	public Map<E, Transition<S, E>> getFromAllTransitions() {
		return fromAllTransitions;
	}


	@Override
	public boolean fireEvent(E event) {
		return fireEvent(event, Arguments.NO_ARGS);
	}

	@Override
	public boolean fireEvent(E event, Arguments args) {
		Collection<Transition<S, E>> transitions = stateTransitions.get(currentState);
		for (Transition<S, E> transition : transitions) {
			// TODO: make smart lookup on event instead
			if (transition.getEvent().equals(event)) {
				if (transition.getCondition().isSatisfied(args)) {
					moveToNewState(transition, event, args);
					return true;
				}
			}
		}
		Transition<S, E> fromAllTransition = fromAllTransitions.get(event);
		if (fromAllTransition != null) {
			if (fromAllTransition.getCondition().isSatisfied(args)) {
				moveToNewState(fromAllTransition, event, args);
				return true;
			}
		}
		return false;
	}

	@Override
	public void forceSetState(S forcedState) {
		Transition<S, E> transition = new Transition<S, E>(currentState, forcedState, BasicConditions.ALWAYS, null);
		moveToNewState(transition, null, Arguments.NO_ARGS);
	}
}
