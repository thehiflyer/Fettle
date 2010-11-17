package se.hiflyer.fettle.builder;

import com.google.common.collect.Lists;
import se.hiflyer.fettle.BasicStateMachine;
import se.hiflyer.fettle.StateMachine;

import java.util.List;

public class StateMachineBuilder<S, E> {
	private List<TransitionBuilder<S, E>> transitionBuilders = Lists.newArrayList();

	public TransitionBuilder<S, E> transition() {
		TransitionBuilder<S, E> transition = new TransitionBuilder<S,E>();
		transitionBuilders.add(transition);
		return transition;
	}

	public StateMachine<S, E> build(S initial) {
		StateMachine<S, E> machine = BasicStateMachine.createStateMachine(initial);
		for (TransitionBuilder<S, E> transitionBuilder : transitionBuilders) {
			transitionBuilder.addToMachine(machine);
		}
		return machine;
	}
}
