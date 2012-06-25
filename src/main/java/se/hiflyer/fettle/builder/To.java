package se.hiflyer.fettle.builder;

public interface To<S, E, C> {
	On<S, E, C> on(E event);
}
