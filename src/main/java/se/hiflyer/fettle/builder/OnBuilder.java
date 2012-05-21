package se.hiflyer.fettle.builder;

import se.hiflyer.fettle.Action;
import se.hiflyer.fettle.Condition;

public class OnBuilder<S, E> {

	private final TransitionBuilder<S, E> transitionBuilder;

	public OnBuilder(TransitionBuilder<S, E> transitionBuilder, E event) {
		this.transitionBuilder = transitionBuilder;
		transitionBuilder.on(event);
	}

	public WhenBuilder<S, E> when(Condition condition) {
		return new WhenBuilder<S, E>(transitionBuilder, condition);
	}

	public PerformBuilder<S, E> perform(Action<S, E>... actions) {
		return new PerformBuilder<S, E>(transitionBuilder, actions);
	}
}
