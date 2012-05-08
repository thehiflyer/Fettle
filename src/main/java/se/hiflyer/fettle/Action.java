package se.hiflyer.fettle;

public interface Action<S, E> {
	/**
	 * Called when a transition occurs
	 * @param from the state the machine was in before the transition
	 * @param to the state the machine is in after the transition
	 * @param causedBy the event that triggered the transition
	 * @param args the arguments supplied to the transition
	 * @param stateMachine the machine for which the transition occured
	 */
	void onTransition(S from, S to, E causedBy, Arguments args, StateMachine<S, E> stateMachine);
}
