package se.fearless.fettle;

public interface Action<S, E, C> {
	/**
	 * Called when a transition occurs
	 * @param from the state the machine was in before the transition
	 * @param to the state the machine is in after the transition
	 * @param causedBy the event that triggered the transition
	 * @param context the context in which the event was fired supplied to the transition
	 * @param stateMachine the machine for which the transition occurred
	 */
	void onTransition(S from, S to, E causedBy, C context, StateMachine<S, E, C> stateMachine);
}
