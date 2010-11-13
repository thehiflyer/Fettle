package se.hiflyer.fettle;

import java.util.List;

public interface StateMachine<S, E> {
	void addTransition(S from, S to, E event, Condition condition, List<Action> actions);

	void addTransition(S from, S to, E event, Condition condition, Action action);

	void addTransition(S from, S to, E event, Condition condition);

	void addTransition(S from, S to, E event);

	void addFromAllTransition(S to, E event, Condition condition, List<Action> actions);

	S getCurrentState();

	void addEntryAction(S entryState, Action action);

	void addExitAction(S exitState, Action action);

	boolean fireEvent(E event);

	void forceSetState(S forcedState);
}
