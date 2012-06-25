package se.hiflyer.fettle;

public interface TransitionModel<S, E, C> {

	/**
	 * Fires the event at the state machine. This can result in a state change and trigger actions to be run.
	 * @param machine the machine to fire the event at
	 * @param event the event to fire
	 * @param context the context to use for transition conditions and actions. The context can be used to
	 *                  supply arguments to actions.
	 * @return true if a state change was triggered, false otherwise.
	 * Note that this will return true if a transition to the same state occurs
	 */
	boolean fireEvent(StateMachine<S, E, C> machine, E event, C context);

	/**
	 * Forces the state machine to enter the forcedState even if there are no transitions to that state.
	 * No transition actions are run but exit actions on the current state and entry actions on the new state are run
	 * @param stateMachine the state machine to force the state for
	 * @param forcedState the state the machine will be in after this method
	 * @return true if the state was changed
	 */
	boolean forceSetState(StateMachine<S, E, C> stateMachine, S forcedState);
}
