package se.fearless.fettle.builder;

public interface Transition<S, E, C> {
	From<S, E, C> from(S fromState);

	From<S, E, C> fromAll();

	Internal<S, E, C> internal(S state);
}
