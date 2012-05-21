package se.hiflyer.fettle.builder;

import se.hiflyer.fettle.Action;

public class PerformBuilder<S, E> {
	public PerformBuilder(TransitionBuilder<S, E> transitionBuilder, Action<S, E>... actions) {
		transitionBuilder.perform(actions);
	}
}
