package se.hiflyer.fettle.builder;

import se.hiflyer.fettle.Action;
import se.hiflyer.fettle.Condition;

public class WhenBuilder<S, E> {

	private final TransitionBuilder<S, E> transitionBuilder;

	public WhenBuilder(TransitionBuilder<S, E> transitionBuilder, Condition condition) {
		this.transitionBuilder = transitionBuilder;
		transitionBuilder.when(condition);
	}

	public PerformBuilder<S, E> perform(Action<S, E>... actions) {
		return new PerformBuilder<S, E>(transitionBuilder, actions);
	}
}
