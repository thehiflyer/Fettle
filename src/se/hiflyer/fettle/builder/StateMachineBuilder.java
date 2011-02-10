package se.hiflyer.fettle.builder;

import com.google.common.collect.Lists;
import se.hiflyer.fettle.MutableStateMachine;
import se.hiflyer.fettle.MutableTemplateBaseStateMachine;
import se.hiflyer.fettle.MutableTransitionModel;
import se.hiflyer.fettle.TemplateBasedStateMachine;

import java.util.List;

public class StateMachineBuilder<S, E> {
	private final List<TransitionBuilder<S, E>> transitionBuilders = Lists.newArrayList();
	private final List<EntryExitActionBuilder<S, E>> entryExitActions = Lists.newArrayList();


	private StateMachineBuilder() {
	}

	public static <S, E> StateMachineBuilder<S, E> create() {
		return new StateMachineBuilder<S, E>();
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

	public TemplateBasedStateMachine<S, E> build(S initial) {
		@SuppressWarnings("unchecked")
		MutableTransitionModel<S, E> build = buildTemplate((Class<S>) initial.getClass());
		return build.createInstance(initial);
	}

	public MutableStateMachine<S, E> buildModifiable(S initial) {
		return new MutableTemplateBaseStateMachine<S, E>(build(initial));
	}

	/**
	 * Builds a state machine template capable of creating many instances all sharing the same
	 * state transitions and actions but all with their own current state.
	 * This is more memory efficient and is good when you need a large number of identical state machine instances.
	 *
	 * @param stateClass the class of the states
	 * @return a state machine template configured with all the transitions and actions specified using this builder
	 */
	public MutableTransitionModel<S, E> buildTemplate(Class<S> stateClass) {
		MutableTransitionModel<S, E> template = MutableTransitionModel.createStateMachineTemplate(stateClass);
		for (TransitionBuilder<S, E> transitionBuilder : transitionBuilders) {
			transitionBuilder.addToMachine(template);
		}
		for (EntryExitActionBuilder<S, E> entryExitAction : entryExitActions) {
			entryExitAction.addToMachine(template);
		}
		return template;
	}


}
