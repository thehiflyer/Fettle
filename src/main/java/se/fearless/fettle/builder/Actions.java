package se.fearless.fettle.builder;

import se.fearless.fettle.Action;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Actions {
	private Actions() {
	}

	static <S, E, C> List<Action<S, E, C>> actions(Action<S, E, C> action) {
		return Collections.singletonList(action);
	}

	static <S, E, C> List<Action<S, E, C>> actions(Action<S, E, C> action1, Action<S, E, C> action2) {
		return Arrays.asList(action1, action2);
	}

	static <S, E, C> List<Action<S, E, C>> actions(Action<S, E, C> action1, Action<S, E, C> action2, Action<S, E, C> action3) {
		return Arrays.asList(action1, action2, action3);
	}

	static <S, E, C> List<Action<S, E, C>> actions(Action<S, E, C> action1, Action<S, E, C> action2, Action<S, E, C> action3, Action<S, E, C> action4) {
		return Arrays.asList(action1, action2, action3, action4);
	}

	static <S, E, C> List<Action<S, E, C>> actions(Action<S, E, C> action1, Action<S, E, C> action2, Action<S, E, C> action3, Action<S, E, C> action4,
	                                               Action<S, E, C> action5) {
		return Arrays.asList(action1, action2, action3, action4, action5);
	}
}
