package se.hiflyer.fettle.builder;

import se.hiflyer.fettle.StateMachine;
import se.hiflyer.fettle.StateMachineTemplate;
import se.hiflyer.fettle.impl.MutableTransitionModelImpl;
import se.hiflyer.fettle.util.GuavaReplacement;

import java.util.List;

public class StateMachineBuilder<S, E, C> {
	private final List<TransitionBuilder<S, E, C>> transitionBuilders = GuavaReplacement.newArrayList();
	private final List<EntryExitActionBuilder<S, E, C>> entryExitActions = GuavaReplacement.newArrayList();
	private final Class<S> stateClass;
	private final Class<E> eventClass;


	private StateMachineBuilder(Class<S> stateClass, Class<E> eventClass) {
		this.stateClass = stateClass;
		this.eventClass = eventClass;
	}

	public static <S, E, C> StateMachineBuilder<S, E, C> create(Class<S> stateClass, Class<E> eventClass) {
		return new StateMachineBuilder<S, E, C>(stateClass, eventClass);
	}

	public Transition<S, E, C> transition() {
		TransitionBuilder<S, E, C> transition = new TransitionBuilder<S, E, C>();
		transitionBuilders.add(transition);
		return transition;
	}

	public EntryExit<S, E, C> onEntry(S state) {
		EntryExitActionBuilder<S, E, C> actionBuilder = EntryExitActionBuilder.entry(state);
		entryExitActions.add(actionBuilder);
		return actionBuilder;
	}

	public EntryExit<S, E, C> onExit(S state) {
		EntryExitActionBuilder<S, E, C> actionBuilder = EntryExitActionBuilder.exit(state);
		entryExitActions.add(actionBuilder);
		return actionBuilder;
	}

	/**
	 * Builds a state machine with the transitions and states configured by the builder.
	 * Note that consecutive calls to this method will not share transition models and it's therefore inefficient to
	 * use this method to create large number of state machines. This method can be considered as a short hand for
	 * cases when just one state machine of a certain configuration is needed.
	 * @param initial the state the machine will be in when created
	 * @return a new state machine configured with all the transitions and actions specified using this builder
	 * @throws IllegalArgumentException if the initial state is null
	 */
	public StateMachine<S, E, C> build(S initial) {
		@SuppressWarnings("unchecked")
		StateMachineTemplate<S, E, C> build = buildTransitionModel();
		return build.newStateMachine(initial);
	}

	/**
	 * Builds a state machine template capable of creating many instances all sharing the same
	 * state transitions and actions but all with their own current state.
	 * This is more memory efficient and is good when you need a large number of identical state machine instances.
	 *
	 * @return a state machine template configured with all the transitions and actions specified using this builder
	 */
	public StateMachineTemplate<S, E, C> buildTransitionModel() {
		MutableTransitionModelImpl<S, E, C> template = MutableTransitionModelImpl.create(stateClass, eventClass);
		for (TransitionBuilder<S, E, C> transitionBuilder : transitionBuilders) {
			transitionBuilder.addToTransitionModel(template);
		}
		for (EntryExitActionBuilder<S, E, C> entryExitAction : entryExitActions) {
			entryExitAction.addToMachine(template);
		}
		return template;
	}
}