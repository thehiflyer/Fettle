package se.fearless.fettle.builder;

public interface Internal<S, E, C> {
	On<S, E, C> on(E event);
}
