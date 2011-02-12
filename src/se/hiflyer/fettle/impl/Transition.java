package se.hiflyer.fettle.impl;

import com.google.common.collect.Lists;
import se.hiflyer.fettle.Action;
import se.hiflyer.fettle.Arguments;
import se.hiflyer.fettle.Condition;

import java.util.Collection;

public class Transition<S, E> {
	private final S to;
	private final Condition condition;
	private final Collection<Action<S, E>> actions = Lists.newArrayList();

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

	public void onTransition(S from, S to, E event, Arguments args) {
		for (Action<S, E> action : actions) {
			action.onTransition(from, to, event, args);
		}

	}
}
