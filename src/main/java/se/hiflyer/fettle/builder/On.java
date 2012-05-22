package se.hiflyer.fettle.builder;

import se.hiflyer.fettle.Action;
import se.hiflyer.fettle.Condition;

public interface On<S, E> {

	public When<S, E> when(Condition condition);

	public void perform(Action<S, E>... actions);
}
