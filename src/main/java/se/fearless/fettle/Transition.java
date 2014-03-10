package se.fearless.fettle;

public interface Transition<S, C> {
	S getTo();

	boolean isSatisfied(C context);
}
