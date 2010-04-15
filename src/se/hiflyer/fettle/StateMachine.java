package se.hiflyer.fettle;

import java.util.Collection;

public class StateMachine<T extends Enum<T>> {
	private T currentState;
	private final EnumMultimap<T, Transition<T>> stateTransitions;
	private final EnumMultimap<T, Runnable> entryActions;
	private final EnumMultimap<T, Runnable> exitActions;

	public StateMachine(Class<T> clazz, T initial) {
		currentState = initial;
		stateTransitions = EnumMultimap.create(clazz);
		entryActions = EnumMultimap.create(clazz);
		exitActions = EnumMultimap.create(clazz);
	}

	public void addTransition(Transition<T> transition) {
		stateTransitions.put(transition.getFrom(), transition);
	}

	public T getCurrentState() {
		return currentState;
	}

	public void update() {
		Collection<Transition<T>> activeTransitions = stateTransitions.get(currentState);
		for (Transition<T> activeTransition : activeTransitions) {
			if (activeTransition.getTrigger().isSatisfied()) {
				moveToNewState(activeTransition.getTo());
			}
		}
	}

	private void moveToNewState(T to) {
		runActions(exitActions, currentState);
		currentState = to;
		runActions(entryActions, currentState);
	}

	private void runActions(EnumMultimap<T, Runnable> actionMap, T state) {
		Collection<Runnable> actions = actionMap.get(state);
		for (Runnable action : actions) {
			action.run();
		}
	}

	public void addEntryAction(T entryState, Runnable action) {
		entryActions.put(entryState, action);
	}

	public void addExitAction(T exitState, Runnable action) {
		exitActions.put(exitState, action);
	}
}
