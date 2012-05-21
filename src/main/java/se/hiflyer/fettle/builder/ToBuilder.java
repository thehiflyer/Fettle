package se.hiflyer.fettle.builder;

public class ToBuilder<S, E> {

	private final TransitionBuilder<S, E> transitionBuilder;

	public ToBuilder(TransitionBuilder<S, E> transitionBuilder, S toState) {
		this.transitionBuilder = transitionBuilder;
		transitionBuilder.to(toState);
	}

	public OnBuilder<S, E> on(E event) {
		return new OnBuilder<S, E>(transitionBuilder, event);
	}
}
