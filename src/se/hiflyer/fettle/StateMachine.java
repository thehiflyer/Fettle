package se.hiflyer.fettle;

/**
 * A view of a state machine from a users perspective, i.e it is immutable and already set up.
 *
 * @param <S> the type of the states
 * @param <E> the type of the events that can trigger state changes
 */
public interface StateMachine<S, E> {
	/**
	 * Gets the current state of the machine
	 *
	 * @return the state the machine is currently in
	 */
	S getCurrentState();

	/**
	 * Fires an event at the state machine, possibly triggering a state change
	 *
	 * @param event the event that is fired
	 * @return true if the event resulted in a state change, false otherwise
	 */
	boolean fireEvent(E event);

	/**
	 * Fires an event at the state machine, possibly triggering a state change
	 *
	 * @param event the event that is fired
	 * @param args  the arguments to be sent to any actions that are run on a state change
	 * @return true if the event resulted in a state change, false otherwise
	 */
	boolean fireEvent(E event, Arguments args);

	/**
	 * Sets the state of the state machine to the rawState even if there are no transitions leading to it
	 * No transition, entry or exit actions are run
	 *
	 * @param rawState the state the machine will be in after this method is called
	 */
	void rawSetState(S rawState);

	/**
	 * Forces the state machine to enter the forcedState even if there are no transitions leading to it
	 * No transition actions are run but exit actions on the current state and entry actions on the new state are run
	 *
	 * @param forcedState the state the machine will be in after this method is called
	 * @return true if the state machine changed state
	 */
	boolean forceSetState(S forcedState);
}
