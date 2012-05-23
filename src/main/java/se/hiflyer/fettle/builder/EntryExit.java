package se.hiflyer.fettle.builder;

import se.hiflyer.fettle.Action;

import java.util.List;

public interface EntryExit<S, E> {
	EntryExit<S, E> perform(Action<S, E> action);

	EntryExit<S, E> perform(List<Action<S, E>> actions);
}
