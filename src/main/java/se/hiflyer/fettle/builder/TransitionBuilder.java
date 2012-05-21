package se.hiflyer.fettle.builder;

import se.hiflyer.fettle.Action;
import se.hiflyer.fettle.BasicConditions;
import se.hiflyer.fettle.Condition;
import se.hiflyer.fettle.MutableTransitionModel;
import se.hiflyer.fettle.util.GuavaReplacement;

import java.util.List;

public class TransitionBuilder<S, E> {

	private S from;
	private S to;
	private E event;
	private Condition condition = BasicConditions.ALWAYS;
	private final List<Action<S, E>> actions = GuavaReplacement.newArrayList();

	void on(E event) {
		this.event = event;
	}

	public FromBuilder<S, E> from(S fromState) {
		return new FromBuilder<S, E>(this, fromState);
	}

	void setFrom(S fromState) {
		this.from = fromState;
	}

	public FromBuilder<S, E> fromAll() {
		return new FromBuilder<S, E>(this, null);
	}

	void to(S toState) {
		to = toState;
	}

	void when(Condition condition) {
		this.condition = condition;
	}

	void perform(Action<S, E>... actions) {
		this.actions.addAll(GuavaReplacement.newArrayList(actions));
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
