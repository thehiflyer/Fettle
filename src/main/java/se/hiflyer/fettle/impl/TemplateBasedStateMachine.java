package se.hiflyer.fettle.impl;

import se.hiflyer.fettle.Arguments;
import se.hiflyer.fettle.StateMachine;
import se.hiflyer.fettle.TransitionModel;

import java.util.concurrent.locks.Lock;

public class TemplateBasedStateMachine<S, E> implements StateMachine<S, E> {
	private final TransitionModel<S, E> model;
	private S currentState;
	private final Lock lock;

	public TemplateBasedStateMachine(TransitionModel<S, E> model, S initial, Lock lock) {
		if (initial == null) {
			throw new IllegalArgumentException("Initial state must not be null");
		}
		this.model = model;
		currentState = initial;
		this.lock = lock;
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
		lock.lock();
		try {
			return model.fireEvent(this, event, args);
		} finally {
			lock.unlock();
		}
	}


	@Override
	public void rawSetState(S rawState) {
		lock.lock();
		currentState = rawState;
		lock.unlock();
	}

	@Override
	public boolean forceSetState(S forcedState) {
		lock.lock();
		try {
			return model.forceSetState(this, forcedState);
		} finally {
			lock.unlock();
		}
	}
}
