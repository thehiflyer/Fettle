package se.fearless.fettle.impl;

import se.fearless.fettle.Action;
import se.fearless.fettle.Condition;
import se.fearless.fettle.StateMachine;
import se.fearless.fettle.Transition;

import java.util.ArrayList;
import java.util.Collection;

public abstract class AbstractTransition<S, E, C> implements Transition<S, E, C> {
	protected final S to;
	protected final Condition<C> condition;
	protected final Collection<Action<S, E, C>> actions = new ArrayList<>();

	public AbstractTransition(S to, Condition<C> condition, Collection<Action<S, E, C>> actions) {
		this.condition = condition;
		this.to = to;
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
		actions.forEach(action -> action.onTransition(from, to, event, context, statemachine));
	}
}
