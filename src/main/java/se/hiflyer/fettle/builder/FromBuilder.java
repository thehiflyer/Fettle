package se.hiflyer.fettle.builder;

public class FromBuilder<S, E> {

	private final TransitionBuilder<S, E> transitionBuilder;

	public FromBuilder(TransitionBuilder<S, E> transitionBuilder, S fromState) {
		this.transitionBuilder = transitionBuilder;
		transitionBuilder.setFrom(fromState);
	}

	public ToBuilder<S, E> to(S toState) {
		return new ToBuilder<S, E>(transitionBuilder, toState);
	}

}
