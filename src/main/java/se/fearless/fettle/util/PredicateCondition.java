package se.fearless.fettle.util;

import se.fearless.fettle.Condition;

import java.util.function.Predicate;

public class PredicateCondition<C> implements Condition<C> {

	private final Predicate<? super C> predicate;

	public PredicateCondition(Predicate<? super C> predicate) {
		this.predicate = predicate;
	}

	@Override
	public boolean isSatisfied(C context) {
		return predicate.test(context);
	}
}
