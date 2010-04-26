package se.hiflyer.fettle;

import se.hiflyer.fettle.util.EnumMultimap;
import se.hiflyer.fettle.util.Multimap;
import se.hiflyer.fettle.util.SetMultimap;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class StateMachine<T, S> {
	private T currentState;
	private final Multimap<T, Transition<T, S>> stateTransitions;
	private final Multimap<T, Runnable> entryActions;
	private final Multimap<T, Runnable> exitActions;

	private StateMachine(T initial, Multimap<T, Transition<T, S>> stateTransitions, Multimap<T, Runnable> entryActions, Multimap<T, Runnable> exitActions) {
		currentState = initial;
		this.stateTransitions = stateTransitions;
		this.entryActions = entryActions;
		this.exitActions = exitActions;
	}

	public static <T, S> StateMachine<T, S> createStateMachine(T initial) {
		return new StateMachine<T, S>(initial, SetMultimap.<T, Transition<T, S>>create(),
				  SetMultimap.<T, Runnable>create(), SetMultimap.<T, Runnable>create());
	}

	public static <T extends Enum<T>, S> StateMachine<T, S> createStateMachineOfEnum(Class<T> clazz, T initial) {
		return new StateMachine<T, S>(initial, EnumMultimap.<T, Transition<T, S>>create(clazz),
				  EnumMultimap.<T, Runnable>create(clazz), EnumMultimap.<T, Runnable>create(clazz));
	}


	public void addTransition(T from, T to, S event, Condition condition, List<Runnable> actions) {
		stateTransitions.put(from, new Transition<T, S>(from, to, condition, event, actions));
	}

	public void addTransition(T from, T to, S event, Condition condition) {
		addTransition(from, to, event, condition, Collections.<Runnable>emptyList());
	}

	public void addTransition(T from, T to, S event) {
		addTransition(from, to, event, BasicConditions.ALWAYS);
	}

	public T getCurrentState() {
		return currentState;
	}

	private void moveToNewState(Transition<T, S> transition) {
		runActions(exitActions, currentState);
		runActions(transition.getTransitionActions());
		currentState = transition.getTo();
		runActions(entryActions, currentState);
	}

	private void runActions(Collection<Runnable> actions) {
		for (Runnable action : actions) {
			action.run();
		}
	}


	private void runActions(Multimap<T, Runnable> actionMap, T state) {
		runActions(actionMap.get(state));
	}

	public void addEntryAction(T entryState, Runnable action) {
		entryActions.put(entryState, action);
	}

	public void addExitAction(T exitState, Runnable action) {
		exitActions.put(exitState, action);
	}

	public boolean fireEvent(S event) {
		Collection<Transition<T, S>> transitions = stateTransitions.get(currentState);
		for (Transition<T, S> transition : transitions) {
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

	public void forceSetState(T forcedState) {
		Transition<T, S> transition = new Transition<T, S>(currentState, forcedState, BasicConditions.ALWAYS, null);
		moveToNewState(transition);
	}
}
