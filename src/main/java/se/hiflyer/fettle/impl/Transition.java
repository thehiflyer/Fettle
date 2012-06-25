package se.hiflyer.fettle.impl;

import se.hiflyer.fettle.Action;
import se.hiflyer.fettle.Condition;
import se.hiflyer.fettle.StateMachine;
import se.hiflyer.fettle.util.GuavaReplacement;

import java.util.Collection;

public class Transition<S, E, C> {
	private final S to;
	private final Condition<C> condition;
	private final Collection<Action<S, E, C>> actions = GuavaReplacement.newArrayList();

	public Transition(S to, Condition<C> condition, Collection<Action<S, E, C>> actions) {
		this.to = to;
		this.condition = condition;
		this.actions.addAll(actions);
	}

	public S getTo() {
		return to;
	}

	public boolean isSatisfied(C context) {
		return condition.isSatisfied(context);
	}

	public Condition<C> getCondition() {
		return condition;
	}

	public void onTransition(S from, S to, E event, C context, StateMachine<S, E, C> statemachine) {
		for (Action<S, E, C> action : actions) {
			action.onTransition(from, to, event, context, statemachine);
		}
	}
}
