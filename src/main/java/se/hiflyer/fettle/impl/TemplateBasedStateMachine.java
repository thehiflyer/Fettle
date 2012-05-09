package se.hiflyer.fettle.impl;

import se.hiflyer.fettle.Arguments;
import se.hiflyer.fettle.StateMachine;
import se.hiflyer.fettle.TransitionModel;

public class TemplateBasedStateMachine<S, E> implements StateMachine<S, E> {
	private final TransitionModel<S, E> model;
	private S currentState;

	public TemplateBasedStateMachine(TransitionModel<S, E> model, S initial) {
		if (initial == null) {
			throw new IllegalArgumentException("Initial state may not be null");
		}
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
	public void rawSetState(S rawState) {
		currentState = rawState;
	}

	@Override
	public boolean forceSetState(S forcedState) {
		return model.forceSetState(this, forcedState);
	}
}
