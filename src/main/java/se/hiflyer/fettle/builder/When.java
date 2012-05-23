package se.hiflyer.fettle.builder;

import se.hiflyer.fettle.Action;

import java.util.List;

public interface When<S, E> {
	void perform(Action<S, E> action);

	void perform(List<Action<S, E>> actions);
}
