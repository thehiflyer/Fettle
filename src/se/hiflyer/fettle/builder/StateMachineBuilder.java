package se.hiflyer.fettle.builder;

import com.google.common.collect.Lists;
import se.hiflyer.fettle.BasicStateMachine;
import se.hiflyer.fettle.ModifiableStateMachine;
import se.hiflyer.fettle.StateMachine;

import java.util.List;

public class StateMachineBuilder<S, E> {
	private final List<TransitionBuilder<S, E>> transitionBuilders = Lists.newArrayList();
	private final List<EntryExitActionBuilder<S, E>> entryExitActions = Lists.newArrayList();

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
		ModifiableStateMachine<S, E> machine = BasicStateMachine.createStateMachine(initial);
		for (TransitionBuilder<S, E> transitionBuilder : transitionBuilders) {
			transitionBuilder.addToMachine(machine);
		}
		for (EntryExitActionBuilder<S, E> entryExitAction : entryExitActions) {
			entryExitAction.addToMachine(machine);
		}
		return machine;
	}


}
