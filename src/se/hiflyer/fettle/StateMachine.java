package se.hiflyer.fettle;

import se.hiflyer.fettle.util.EnumMultimap;
import se.hiflyer.fettle.util.Multimap;
import se.hiflyer.fettle.util.SetMultimap;

import java.util.Collection;

public class StateMachine<T> {
	private T currentState;
	private final Multimap<T, Transition<T>> stateTransitions;
	private final Multimap<T, Runnable> entryActions;
	private final Multimap<T, Runnable> exitActions;
	private final Multimap<Transition<T>, Runnable> transitionActions = new SetMultimap<Transition<T>, Runnable>();


	private StateMachine(T initial, Multimap<T, Transition<T>> stateTransitions, Multimap<T, Runnable> entryActions, Multimap<T, Runnable> exitActions) {
		currentState = initial;
		this.stateTransitions = stateTransitions;
		this.entryActions = entryActions;
		this.exitActions = exitActions;
	}

	public static <T> StateMachine<T> createStateMachine(T initial) {
		return new StateMachine<T>(initial, SetMultimap.<T, Transition<T>>create(),
				  SetMultimap.<T, Runnable>create(), SetMultimap.<T, Runnable>create());
	}

	public static <T extends Enum<T>> StateMachine<T> createStateMachineOfEnum(Class<T> clazz, T initial) {
		return new StateMachine<T>(initial, EnumMultimap.<T, Transition<T>>create(clazz),
				  EnumMultimap.<T, Runnable>create(clazz), EnumMultimap.<T, Runnable>create(clazz));
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
			if (activeTransition.getCondition().isSatisfied()) {
				makeTransition(activeTransition);
				return;
			}
		}
	}

	private void makeTransition(Transition<T> transition) {

		runActions(exitActions, currentState);
		currentState = transition.getTo();
		runActions(transitionActions, transition);
		runActions(entryActions, currentState);
	}

	private <S> void runActions(Multimap<S, Runnable> actionMap, S state) {
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

	public void addTransitionAction(Transition<T> transition, Runnable action) {
		transitionActions.put(transition, action);
	}
}
