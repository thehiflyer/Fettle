package se.hiflyer.fettle.builder;

import com.google.common.collect.Lists;
import se.hiflyer.fettle.*;

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
		StateMachineTemplate<S, E> build = buildTemplate((Class<S>) initial.getClass());
		return build.createInstance(initial);
	}

	public ModifiableStateMachine<S, E> buildModifiable(S initial) {
		return new ModifiableTemplateBaseStateMachine<S, E>(build(initial));
	}

	public StateMachineTemplate<S, E> buildTemplate(Class<S> stateClass) {
		StateMachineTemplate<S, E> template = StateMachineTemplate.createStateMachineTemplate(stateClass);
		for (TransitionBuilder<S, E> transitionBuilder : transitionBuilders) {
			transitionBuilder.addToMachine(template);
		}
		for (EntryExitActionBuilder<S, E> entryExitAction : entryExitActions) {
			entryExitAction.addToMachine(template);
		}
		return template;
	}


}
