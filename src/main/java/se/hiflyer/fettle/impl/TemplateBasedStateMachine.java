package se.hiflyer.fettle.impl;

import se.hiflyer.fettle.StateMachine;
import se.hiflyer.fettle.TransitionModel;

import java.util.concurrent.locks.Lock;

public class TemplateBasedStateMachine<S, E, C> implements StateMachine<S, E, C> {
	private final TransitionModel<S, E, C> model;
	private S currentState;
	private final Lock lock;

	public TemplateBasedStateMachine(TransitionModel<S, E, C> model, S initial, Lock lock) {
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
		return fireEvent(event, model.getDefaultContext());
	}

	@Override
	public boolean fireEvent(E event, C context) {
		lock.lock();
		try {
			return model.fireEvent(this, event, context);
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
