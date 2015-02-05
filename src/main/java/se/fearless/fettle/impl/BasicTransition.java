package se.fearless.fettle.impl;

import se.fearless.fettle.Action;
import se.fearless.fettle.Condition;

import java.util.Collection;

public class BasicTransition<S, E, C> extends AbstractTransition<S,E,C> {

	public BasicTransition(S to, Condition<C> condition, Collection<Action<S, E, C>> actions) {
		super(condition, to, actions);
	}
}
