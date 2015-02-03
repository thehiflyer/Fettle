package se.fearless.fettle;

import java.util.List;

public interface MutableTransitionModel<S, E, C> extends TransitionModel<S, E, C>, StateMachineTemplate<S, E, C> {

	StateMachineTemplate<S, E, C> createImmutableClone();

	void addTransition(S from, S to, E event, Condition<C> condition, List<Action<S, E, C>> actions);

	void addFromAllTransition(S to, E event, Condition<C> condition, List<Action<S, E, C>> actions);

	void addInternalTransition(S from, S to, E event, Condition<C> condition, List<Action<S, E, C>> actions);

	void addEntryAction(S entryState, Action<S, E, C> action);

	void addExitAction(S exitState, Action<S, E, C> action);
}
