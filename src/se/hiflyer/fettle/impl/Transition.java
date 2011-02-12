package se.hiflyer.fettle.impl;

import se.hiflyer.fettle.Action;
import se.hiflyer.fettle.Condition;

import java.util.Collection;
import java.util.Collections;

public class Transition<S, E> {
	private final S from;
	private final S to;
	private final Condition condition;
	private final E event;
	private final Collection<Action<S, E>> transitionActions;


	public Transition(S from, S to, Condition condition, E event, Collection<Action<S, E>> transitionActions) {
		this.from = from;
		this.to = to;
		this.condition = condition;
		this.event = event;
		this.transitionActions = transitionActions;
	}

	public Transition(S from, S to, Condition condition, E event) {
		this(from, to, condition, event, Collections.<Action<S, E>>emptyList());
	}

	public S getFrom() {
		return from;
	}

	public S getTo() {
		return to;
	}

	public E getEvent() {
		return event;

	}

	public Condition getCondition() {
		return condition;
	}

	public Collection<Action<S, E>> getTransitionActions() {
		return transitionActions;
	}
}
