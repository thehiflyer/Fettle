package se.hiflyer.fettle;

public class BasicTransition<T extends Enum<T>> implements Transition<T> {
	private final T from;
	private final T to;
	private final Condition condition;

	public BasicTransition(T from, T to, Condition condition) {
		this.from = from;
		this.to = to;
		this.condition = condition;
	}

	public T getFrom() {
		return from;
	}

	public T getTo() {
		return to;
	}

	public Condition getTrigger() {
		return condition;
	}
}
