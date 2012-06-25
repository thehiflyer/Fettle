package se.hiflyer.fettle;

public interface Condition<C> {
	boolean isSatisfied(C context);
}
