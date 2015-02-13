package se.fearless.fettle.builder;

import se.fearless.fettle.Action;

public interface When<S, E, C> {
	Then<S, E, C> perform(Action<S, E, C> action);
}
