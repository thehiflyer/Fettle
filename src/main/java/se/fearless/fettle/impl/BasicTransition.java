package se.fearless.fettle.impl;

import se.fearless.fettle.Action;
import se.fearless.fettle.Condition;
import se.fearless.fettle.StateMachine;
import se.fearless.fettle.Transition;
import se.fearless.fettle.util.GuavaReplacement;

import java.util.Collection;

public class BasicTransition<S, E, C> implements Transition<S, E, C> {
	private final S to;
	private final Condition<C> condition;
	private final Collection<Action<S, E, C>> actions = GuavaReplacement.newArrayList();

	public BasicTransition(S to, Condition<C> condition, Collection<Action<S, E, C>> actions) {
		this.to = to;
		this.condition = condition;
		this.actions.addAll(actions);
	}

	@Override
	public S getTo() {
		return to;
	}

	@Override
	public boolean isSatisfied(C context) {
		return condition.isSatisfied(context);
	}

	public Condition<C> getCondition() {
		return condition;
	}

	@Override
	public void onTransition(S from, S to, E event, C context, StateMachine<S, E, C> statemachine) {
		for (Action<S, E, C> action : actions) {
			action.onTransition(from, to, event, context, statemachine);
		}
	}
}
