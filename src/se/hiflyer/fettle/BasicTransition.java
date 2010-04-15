package se.hiflyer.fettle;

public class BasicTransition<T> implements Transition<T> {
	private final T from;
	private final T to;
	private final Condition condition;

	public BasicTransition(T from, T to, Condition condition) {
		this.from = from;
		this.to = to;
		this.condition = condition;
	}

	@Override
	public T getFrom() {
		return from;
	}

	@Override
	public T getTo() {
		return to;
	}

	@Override
	public Condition getCondition() {
		return condition;
	}
}
