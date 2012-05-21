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

	public TransitionBuilder<S, E> on(E event) {
		this.event = event;
		return this;
	}

	public TransitionBuilder<S, E> from(S fromState) {
		from = fromState;
		return this;
	}

	public TransitionBuilder<S, E> to(S toState) {
		to = toState;
		return this;
	}

	public TransitionBuilder<S, E> when(Condition condition) {
		this.condition = condition;
		return this;
	}

	public TransitionBuilder<S, E> perform(Action<S, E>... actions) {
		this.actions.addAll(GuavaReplacement.newArrayList(actions));
		return this;
	}


	public void addToTransitionModel(MutableTransitionModel<S, E> transitionModel) {
		if (from == null) {
			transitionModel.addFromAllTransition(to, event, condition, actions);
		} else {
			transitionModel.addTransition(from, to, event, condition, actions);
		}
	}
}
