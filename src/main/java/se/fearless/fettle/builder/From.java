package se.fearless.fettle.builder;

public interface From<S, E, C> {
	To<S, E, C> to(S toState);
}
