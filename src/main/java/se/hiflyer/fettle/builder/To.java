package se.hiflyer.fettle.builder;

public interface To<S, E> {
	public On<S, E> on(E event);
}
