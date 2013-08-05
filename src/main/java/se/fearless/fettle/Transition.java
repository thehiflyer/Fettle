package se.fearless.fettle;

public interface Transition<S, E, C> {
	S getTo();

	boolean isSatisfied(C context);
}
