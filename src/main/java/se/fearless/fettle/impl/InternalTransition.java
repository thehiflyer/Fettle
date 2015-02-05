package se.fearless.fettle.impl;

import se.fearless.fettle.Action;
import se.fearless.fettle.Condition;

import java.util.Collection;

public class InternalTransition<S, E, C> extends AbstractTransition<S, E, C> {
	public InternalTransition(S to, Condition<C> condition, Collection<Action<S, E, C>> actions) {
		super(to, condition, actions);
	}

	@Override
	public boolean shouldExecuteEntryAndExitActions() {
		return false;
	}
}
