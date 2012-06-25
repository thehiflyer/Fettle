package se.hiflyer.fettle.builder;

import se.hiflyer.fettle.Action;

import java.util.List;

public interface EntryExit<S, E, C> {
	EntryExit<S, E, C> perform(Action<S, E, C> action);

	EntryExit<S, E, C> perform(List<Action<S, E, C>> actions);
}
