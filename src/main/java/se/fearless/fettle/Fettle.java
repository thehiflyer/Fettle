package se.fearless.fettle;

import se.fearless.fettle.builder.StateMachineBuilder;
import se.fearless.fettle.impl.MutableTransitionModelImpl;

/**
 * Utility class to have an easy to find entry point to the framework
 */
public class Fettle {

	private Fettle() {
	}

	/**
	 * Constructs a new transition model that holds the definition for a state machine
	 * @param stateClass the class of the states in the machine
	 * @param eventClass the class of the events triggering state transitions
	 * @param <S> the type of the states in the machine
	 * @param <E> the type of the events triggering state transitions
	 * @return a builder ready to configure with transitions and actions
	 */
	public static <S, E, C> MutableTransitionModel<S, E, C> newTransitionModel(Class<S> stateClass, Class<E> eventClass) {
		return MutableTransitionModelImpl.create(stateClass, eventClass);
	}

	/**
	 * Constructs a new builder object to simplify the construction of state machines
	 * @param stateClass the class of the states in the machine
	 * @param eventClass the class of the events triggering state transitions
	 * @param <S> the type of the states in the machine
	 * @param <E> the type of the events triggering state transitions
	 * @param <C> the type of the context sent to actions and conditions
	 * @return a builder ready to configure with transitions and actions
	 */
	public static <S, E, C> StateMachineBuilder<S, E, C> newBuilder(Class<S> stateClass, Class<E> eventClass) {
		return StateMachineBuilder.create(stateClass, eventClass);
	}
}
