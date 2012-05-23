package se.hiflyer.fettle.builder;

public interface From<S, E> {
	To<S, E> to(S toState);
}
