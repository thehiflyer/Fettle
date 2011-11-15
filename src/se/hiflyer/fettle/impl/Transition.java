package se.hiflyer.fettle.impl;

import se.hiflyer.fettle.Action;
import se.hiflyer.fettle.Arguments;
import se.hiflyer.fettle.Condition;
import se.hiflyer.fettle.StateMachine;
import se.hiflyer.fettle.util.GuavaReplacement;

import java.util.Collection;

public class Transition<S, E> {
	private final S to;
	private final Condition condition;
	private final Collection<Action<S, E>> actions = GuavaReplacement.newArrayList();

	public Transition(S to, Condition condition, Collection<Action<S, E>> actions) {
		this.to = to;
		this.condition = condition;
		this.actions.addAll(actions);
	}

	public S getTo() {
		return to;
	}

	public boolean isSatisfied(Arguments args) {
		return condition.isSatisfied(args);
	}

	public Condition getCondition() {
		return condition;
	}

	public void onTransition(S from, S to, E event, Arguments args, StateMachine<S, E> statemachine) {
		for (Action<S, E> action : actions) {
			action.onTransition(from, to, event, args, statemachine);
		}

	}
}
