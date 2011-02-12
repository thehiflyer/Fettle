package se.hiflyer.fettle.builder;

import com.google.common.collect.Lists;
import se.hiflyer.fettle.StateMachine;
import se.hiflyer.fettle.impl.MutableTransitionModelImpl;

import java.util.List;

public class StateMachineBuilder<S, E> {
	private final List<TransitionBuilder<S, E>> transitionBuilders = Lists.newArrayList();
	private final List<EntryExitActionBuilder<S, E>> entryExitActions = Lists.newArrayList();
	private final Class<S> stateClass;
	private final Class<E> eventClass;


	private StateMachineBuilder(Class<S> stateClass, Class<E> eventClass) {
		this.stateClass = stateClass;
		this.eventClass = eventClass;
	}

	public static <S, E> StateMachineBuilder<S, E> create(Class<S> stateClass, Class<E> eventClass) {
		return new StateMachineBuilder<S, E>(stateClass, eventClass);
	}

	public TransitionBuilder<S, E> transition() {
		TransitionBuilder<S, E> transition = new TransitionBuilder<S, E>();
		transitionBuilders.add(transition);
		return transition;
	}

	public EntryExitActionBuilder<S, E> onEntry(S state) {
		EntryExitActionBuilder<S, E> actionBuilder = EntryExitActionBuilder.entry(state);
		entryExitActions.add(actionBuilder);
		return actionBuilder;
	}

	public EntryExitActionBuilder<S, E> onExit(S state) {
		EntryExitActionBuilder<S, E> actionBuilder = EntryExitActionBuilder.exit(state);
		entryExitActions.add(actionBuilder);
		return actionBuilder;
	}

	public StateMachine<S, E> build(S initial) {
		@SuppressWarnings("unchecked")
		MutableTransitionModelImpl<S, E> build = buildTransitionModel();
		return build.newStateMachine(initial);
	}

	/**
	 * Builds a state machine template capable of creating many instances all sharing the same
	 * state transitions and actions but all with their own current state.
	 * This is more memory efficient and is good when you need a large number of identical state machine instances.
	 *
	 * @return a state machine template configured with all the transitions and actions specified using this builder
	 */
	public MutableTransitionModelImpl<S, E> buildTransitionModel() {
		MutableTransitionModelImpl<S, E> template = MutableTransitionModelImpl.create(stateClass, eventClass);
		for (TransitionBuilder<S, E> transitionBuilder : transitionBuilders) {
			transitionBuilder.addToMachine(template);
		}
		for (EntryExitActionBuilder<S, E> entryExitAction : entryExitActions) {
			entryExitAction.addToMachine(template);
		}
		return template;
	}


}
