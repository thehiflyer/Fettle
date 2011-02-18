package se.hiflyer.fettle;

public interface TransitionModel<S, E> {

	/**
	 * Creates a new state machine using the transition model as a template
	 * @param init the state the machine will be in when created
	 * @return a new state machine with the transitions and actions defined in this model
	 */
	StateMachine<S, E> newStateMachine(S init);

	/**
	 * Fires the event at the state machine. This can result in a state change and trigger actions to be run.
	 * This method is only intended to be used by StateMachines
	 * @param machine the machine to fire the event at
	 * @param event the event to fire
	 * @param args the arguments to use for transition conditions and actions
	 * @return true if a state change was triggered, false otherwise.
	 * Note that this will return true if a transition to the same state occurs
	 */
	boolean fireEvent(StateMachine<S, E> machine, E event, Arguments args);

	/**
	 * Forces the state machine to enter the forcedState even if there are no transitions to that state.
	 * No transition actions are run but exit actions on the current state and entry actions on the new state are run
	 * @param stateMachine the state machine to force the state for
	 * @param forcedState the state the machine will be in after this method
	 * @return
	 */
	boolean forceSetState(StateMachine<S, E> stateMachine, S forcedState);
}
