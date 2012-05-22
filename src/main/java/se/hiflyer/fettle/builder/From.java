package se.hiflyer.fettle.builder;

public interface From<S, E> {
	public To<S, E> to(S toState);
}
