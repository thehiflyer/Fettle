package se.hiflyer.fettle;

import com.google.common.base.Predicate;

public class PredicateCondition<T> implements Condition {

	private final Predicate<? super T> predicate;
	private T input;

	public PredicateCondition(Predicate<? super T> predicate) {
		this.predicate = predicate;
	}

	@Override
	public boolean isSatisfied() {
		return predicate.apply(input);
	}

	public void setInput(T input) {
		this.input = input;
	}
}
