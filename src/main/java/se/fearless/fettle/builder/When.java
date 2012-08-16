package se.fearless.fettle.builder;

import se.fearless.fettle.Action;

import java.util.List;

public interface When<S, E, C> {
	void perform(Action<S, E, C> action);

	void perform(List<Action<S, E, C>> actions);
}
