package se.hiflyer.fettle;

public interface StateMachineTemplate<S, E> {
	/**
	 * Creates a new state machine using the transition model as a template
	 * @param init the state the machine will be in when created
	 * @return a new state machine with the transitions and actions defined in this model
	 */
	StateMachine<S, E> newStateMachine(S init);
}
