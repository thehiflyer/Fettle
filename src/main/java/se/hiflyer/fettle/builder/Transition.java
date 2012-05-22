package se.hiflyer.fettle.builder;

public interface Transition<S, E> {
	From<S, E> from(S fromState);

	From<S, E> fromAll();
}
