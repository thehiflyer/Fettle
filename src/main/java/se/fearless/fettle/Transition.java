package se.fearless.fettle;

public interface Transition<S, E, C> {
	S getTo();

	boolean isSatisfied(C context);

	void onTransition(S from, S to, E event, C context, StateMachine<S, E, C> statemachine);
}
