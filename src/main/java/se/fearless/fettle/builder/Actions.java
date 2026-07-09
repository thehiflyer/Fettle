package se.fearless.fettle.builder;

import se.fearless.fettle.Action;

import java.util.List;

public class Actions {
	private Actions() {
	}

	@SafeVarargs
	static <S, E, C> List<Action<S, E, C>> actions(Action<S, E, C>... actions) {
		return List.of(actions);
	}
}
