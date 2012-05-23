package se.hiflyer.fettle.builder;

public interface To<S, E> {
	On<S, E> on(E event);
}
