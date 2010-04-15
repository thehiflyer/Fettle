package se.hiflyer.fettle;

public interface Transition<T> {
	Condition getCondition();

	T getFrom();

	T getTo();
}
