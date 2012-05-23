package se.hiflyer.fettle.builder;

import se.hiflyer.fettle.Action;
import se.hiflyer.fettle.BasicConditions;
import se.hiflyer.fettle.Condition;
import se.hiflyer.fettle.MutableTransitionModel;
import se.hiflyer.fettle.util.GuavaReplacement;

import java.util.List;

class TransitionBuilder<S, E> implements Transition<S,E>, From<S, E>, To<S, E>, On<S, E>, When<S, E> {

	private S from;
	private S to;
	private E event;
	private Condition condition = BasicConditions.ALWAYS;
	private final List<Action<S, E>> actions = GuavaReplacement.newArrayList();

	public On<S, E> on(E event) {
		this.event = event;
		return this;
	}

	@Override
	public From<S, E> from(S fromState) {
		this.from = fromState;
		return this;
	}

	@Override
	public From<S, E> fromAll() {
		this.from = null;
		return this;
	}

	@Override
	public To<S, E> to(S toState) {
		this.to = toState;
		return this;
	}

	public When<S, E> when(Condition condition) {
		this.condition = condition;
		return this;
	}

	public void perform(List<Action<S, E>> actions) {
		this.actions.addAll(actions);
	}

	public void perform(Action<S, E> action) {
		this.actions.add(action);
	}


	void addToTransitionModel(MutableTransitionModel<S, E> transitionModel) {
		if (event == null) {
			String fromString = from == null ? "anyState" : from.toString();
			handleMissingOn(fromString, to.toString());
		}
		if (from == null) {
			transitionModel.addFromAllTransition(to, event, condition, actions);
		} else {
			transitionModel.addTransition(from, to, event, condition, actions);
		}
	}

	private void handleMissingOn(String from, String to) {
		throw new IllegalStateException(
				String.format("The transition (%s -> %s) has to be performed on an event. " +
						"Use on() to specify on what event the transition should take place", from, to));
	}

}
