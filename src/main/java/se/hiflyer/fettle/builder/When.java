package se.hiflyer.fettle.builder;

import se.hiflyer.fettle.Action;

public interface When<S, E> {
	public void perform(Action<S, E>... actions);
}
