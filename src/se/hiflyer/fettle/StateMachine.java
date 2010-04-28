package se.hiflyer.fettle;

import se.hiflyer.fettle.util.EnumMultimap;
import se.hiflyer.fettle.util.Multimap;
import se.hiflyer.fettle.util.SetMultimap;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class StateMachine<S, E> {
	private S currentState;
	private final Multimap<S, Transition<S, E>> stateTransitions;
	private final Multimap<S, Action> entryActions;
	private final Multimap<S, Action> exitActions;

	private StateMachine(S initial, Multimap<S, Transition<S, E>> stateTransitions, Multimap<S, Action> entryActions, Multimap<S, Action> exitActions) {
		currentState = initial;
		this.stateTransitions = stateTransitions;
		this.entryActions = entryActions;
		this.exitActions = exitActions;
	}

	public static <S, E> StateMachine<S, E> createStateMachine(S initial) {
		return new StateMachine<S, E>(initial, SetMultimap.<S, Transition<S, E>>create(),
				  SetMultimap.<S, Action>create(), SetMultimap.<S, Action>create());
	}

	public static <S extends Enum<S>, E> StateMachine<S, E> createStateMachineOfEnum(Class<S> clazz, S initial) {
		return new StateMachine<S, E>(initial, EnumMultimap.<S, Transition<S, E>>create(clazz),
				  EnumMultimap.<S, Action>create(clazz), EnumMultimap.<S, Action>create(clazz));
	}


	public void addTransition(S from, S to, E event, Condition condition, List<Action> actions) {
		stateTransitions.put(from, new Transition<S, E>(from, to, condition, event, actions));
	}

	public void addTransition(S from, S to, E event, Condition condition) {
		addTransition(from, to, event, condition, Collections.<Action>emptyList());
	}

	public void addTransition(S from, S to, E event) {
		addTransition(from, to, event, BasicConditions.ALWAYS);
	}

	public S getCurrentState() {
		return currentState;
	}

	private void moveToNewState(Transition<S, E> transition) {
		runActions(exitActions, currentState);
		runActions(transition.getTransitionActions());
		currentState = transition.getTo();
		runActions(entryActions, currentState);
	}

	private void runActions(Collection<Action> actions) {
		for (Action action : actions) {
			action.perform();
		}
	}


	private void runActions(Multimap<S, Action> actionMap, S state) {
		runActions(actionMap.get(state));
	}

	public void addEntryAction(S entryState, Action action) {
		entryActions.put(entryState, action);
	}

	public void addExitAction(S exitState, Action action) {
		exitActions.put(exitState, action);
	}

	public boolean fireEvent(E event) {
		Collection<Transition<S, E>> transitions = stateTransitions.get(currentState);
		for (Transition<S, E> transition : transitions) {
			// TODO: make smart lookup on event instead
			if (transition.getEvent().equals(event)) {
				if (transition.getCondition().isSatisfied()) {
					moveToNewState(transition);
					return true;
				}
			}
		}
		return false;
	}

	public void forceSetState(S forcedState) {
		Transition<S, E> transition = new Transition<S, E>(currentState, forcedState, BasicConditions.ALWAYS, null);
		moveToNewState(transition);
	}
}
