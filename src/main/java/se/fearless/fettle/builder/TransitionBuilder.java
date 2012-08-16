package se.fearless.fettle.builder;

import se.fearless.fettle.Action;
import se.fearless.fettle.BasicConditions;
import se.fearless.fettle.Condition;
import se.fearless.fettle.MutableTransitionModel;
import se.fearless.fettle.util.GuavaReplacement;

import java.util.List;

class TransitionBuilder<S, E, C> implements Transition<S, E, C>, From<S, E, C>, To<S, E, C>, On<S, E, C>, When<S, E, C> {

	private S from;
	private S to;
	private E event;
	private Condition<C> condition = BasicConditions.always();
	private final List<Action<S, E, C>> actions = GuavaReplacement.newArrayList();

	public On<S, E, C> on(E event) {
		this.event = event;
		return this;
	}

	@Override
	public From<S, E, C> from(S fromState) {
		this.from = fromState;
		return this;
	}

	@Override
	public From<S, E, C> fromAll() {
		this.from = null;
		return this;
	}

	@Override
	public To<S, E, C> to(S toState) {
		this.to = toState;
		return this;
	}

	public When<S, E, C> when(Condition<C> condition) {
		this.condition = condition;
		return this;
	}

	public void perform(List<Action<S, E, C>> actions) {
		this.actions.addAll(actions);
	}

	public void perform(Action<S, E, C> action) {
		this.actions.add(action);
	}

	@SuppressWarnings("unchecked")
	void addToTransitionModel(MutableTransitionModel<S, E, C> transitionModel) {
		if (event == null) {
			String fromString = from == null ? "anyState" : from.toString();
			handleMissingOn(fromString, to.toString());
		}
		if (from == null) {
			transitionModel.addFromAllTransition(to, event, (Condition<C>) condition, actions);
		} else {
			transitionModel.addTransition(from, to, event, (Condition<C>) condition, actions);
		}
	}

	private void handleMissingOn(String from, String to) {
		throw new IllegalStateException(
				String.format("The transition (%s -> %s) has to be performed on an event. " +
						"Use on() to specify on what event the transition should take place", from, to));
	}
}