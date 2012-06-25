package se.hiflyer.fettle;

import java.util.concurrent.locks.Lock;

public interface StateMachineTemplate<S, E, C> {
	/**
	 * Creates a new state machine using the transition model as a template
	 * @param init the state the machine will be in when created
	 * @return a new state machine with the transitions and actions defined in this model
	 */
	StateMachine<S, E, C> newStateMachine(S init);

	StateMachine<S, E, C> newStateMachine(S init, Lock lock);
}
