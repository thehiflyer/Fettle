package se.fearless.fettle;

import java.util.List;

public class BasicConditions {

	private BasicConditions() {
	}

	public static <C> Condition<C> always() {
		return context -> true;
	}

	public static <C> Condition<C> and(Condition<C> first, Condition<C> second) {
		return context -> first.isSatisfied(context) && second.isSatisfied(context);
	}

	public static <C> Condition<C> and(List<Condition<C>> conditions) {
		return context -> conditions.stream().allMatch(c -> c.isSatisfied(context));
	}

	public static <C> Condition<C> or(Condition<C> first, Condition<C> second) {
		return context -> first.isSatisfied(context) || second.isSatisfied(context);
	}

	public static <C> Condition<C> or(List<Condition<C>> conditions) {
		return context -> conditions.stream().anyMatch(c -> c.isSatisfied(context));
	}

	public static <C> Condition<C> not(Condition<C> condition) {
		return context -> !condition.isSatisfied(context);
	}

	public static <C> Condition<C> xor(Condition<C> first, Condition<C> second) {
		return context -> first.isSatisfied(context) ^ second.isSatisfied(context);
	}
}

