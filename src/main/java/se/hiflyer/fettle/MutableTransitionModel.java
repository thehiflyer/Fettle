package se.hiflyer.fettle;

import java.util.List;

public interface MutableTransitionModel<S, E> extends StateMachineTemplate<S, E> {

	StateMachineTemplate<S, E> createImmutableClone();

	void addTransition(S from, S to, E event, Condition condition, List<Action<S, E>> actions);

	void addFromAllTransition(S to, E event, Condition condition, List<Action<S, E>> actions);

	void addEntryAction(S entryState, Action<S, E> action);

	void addExitAction(S exitState, Action<S, E> action);
}
