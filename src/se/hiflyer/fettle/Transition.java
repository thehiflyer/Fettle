package se.hiflyer.fettle;

import java.util.Collection;
import java.util.Collections;

public class Transition<T, S> {
	private final T from;
	private final T to;
	private final Condition condition;
	private final S event;
	private final Collection<Runnable> transitionActions;


	public Transition(T from, T to, Condition condition, S event, Collection<Runnable> transitionActions) {
		this.from = from;
		this.to = to;
		this.condition = condition;
		this.event = event;
		this.transitionActions = transitionActions;
	}

	public Transition(T from, T to, Condition condition, S event) {
		this(from, to, condition, event, Collections.<Runnable>emptyList());
	}

	public T getFrom() {
		return from;
	}

	public T getTo() {
		return to;
	}

	public S getEvent() {
		return event;

	}

	public Condition getCondition() {
		return condition;
	}

	public Collection<Runnable> getTransitionActions() {
		return transitionActions;
	}
}
