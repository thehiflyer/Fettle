package se.hiflyer.fettle.impl;

import com.google.common.collect.ImmutableMap;
import se.hiflyer.fettle.Arguments;
import se.hiflyer.fettle.StateMachine;
import se.hiflyer.fettle.TransitionModel;
import se.hiflyer.fettle.impl.MutableTransitionModelImpl;
import se.hiflyer.fettle.impl.Transition;
import se.hiflyer.fettle.util.Multimap;
import se.hiflyer.fettle.util.TransitionMap;

public class TemplateBasedStateMachine<S, E> implements StateMachine<S, E> {
	private final TransitionModel<S, E> model;
	private S currentState;

	public TemplateBasedStateMachine(MutableTransitionModelImpl<S, E> model, S initial) {
		this.model = model;
		currentState = initial;
	}

	@Override
	public S getCurrentState() {
		return currentState;
	}

	@Override
	public boolean fireEvent(E event) {
		return fireEvent(event, Arguments.NO_ARGS);
	}

	@Override
	public boolean fireEvent(E event, Arguments args) {
		return model.fireEvent(this, event, args);
	}


	@Override
	public void forceSetState(S forcedState) {
		currentState = forcedState;
	}
}
