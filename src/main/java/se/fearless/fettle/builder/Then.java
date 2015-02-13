package se.fearless.fettle.builder;

import se.fearless.fettle.Action;

public interface Then<S, E, C> {
	Then thenPerform(Action<S, E, C> action);
}
