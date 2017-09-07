package se.fearless.fettle.builder;

import se.fearless.fettle.Action;
import se.fearless.fettle.BasicConditions;
import se.fearless.fettle.Condition;
import se.fearless.fettle.MutableTransitionModel;
import se.fearless.fettle.util.GuavaReplacement;

import java.util.List;

class TransitionBuilder<S, E, C> implements Transition<S, E, C>, From<S, E, C>, To<S, E, C>, On<S, E, C>, When<S, E, C>, Internal<S, E, C> {

	private final List<Action<S, E, C>> actions = GuavaReplacement.newArrayList();
	private S from;
	private S to;
	private E event;
	private Condition<C> condition = BasicConditions.always();
	private boolean runEntryAndExit = true;

	@Override
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

	@Override
	public Internal<S, E, C> internal(S state) {
		this.from = state;
		this.to = state;
		runEntryAndExit = false;
		return this;
	}

	@Override
	public When<S, E, C> when(Condition<C> condition) {
		this.condition = condition;
		return this;
	}

	@Override
	public void perform(List<Action<S, E, C>> actions) {
		this.actions.addAll(actions);
	}

	@Override
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
			transitionModel.addFromAllTransition(to, event, condition, actions);
		} else if (runEntryAndExit) {
			transitionModel.addTransition(from, to, event, condition, actions);
		} else {
			transitionModel.addInternalTransition(from, to, event, condition, actions);
		}
	}

	private void handleMissingOn(String from, String to) {
		throw new IllegalStateException("The transition (" + from + " -> " + to + ") has to be performed on an event. " +
						"Use on() to specify on what event the transition should take place");
	}
}