package se.hiflyer.fettle.util;

import com.google.common.base.Predicate;
import se.hiflyer.fettle.Arguments;
import se.hiflyer.fettle.Condition;

public class PredicateCondition<T> implements Condition {

	private final Predicate<? super T> predicate;

	public PredicateCondition(Predicate<? super T> predicate) {
		this.predicate = predicate;
	}

	@Override
	public boolean isSatisfied(Arguments args) {
		try {
			T input = (T) args.getFirst();
			return predicate.apply(input);
		} catch (ClassCastException e) {
			return false;
		}
	}
}
