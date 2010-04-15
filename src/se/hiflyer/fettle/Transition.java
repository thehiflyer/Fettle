package se.hiflyer.fettle;

public interface Transition<T> {
	Condition getTrigger();

	T getFrom();

	T getTo();
}
