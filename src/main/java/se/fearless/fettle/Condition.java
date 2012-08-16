package se.fearless.fettle;

public interface Condition<C> {
	boolean isSatisfied(C context);
}
