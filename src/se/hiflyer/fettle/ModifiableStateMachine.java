package se.hiflyer.fettle;

import java.util.List;

public interface ModifiableStateMachine<S, E> extends StateMachine<S,E> {
	void addTransition(S from, S to, E event, Condition condition, List<Action<S, E>> actions);
	void addFromAllTransition(S to, E event, Condition condition, List<Action<S, E>> actions);

	void addEntryAction(S entryState, Action<S, E> action);

	void addExitAction(S exitState, Action<S, E> action);

}
