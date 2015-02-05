package se.fearless.fettle.impl;

import se.fearless.fettle.*;
import se.fearless.fettle.util.GuavaReplacement;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MutableTransitionModelImpl<S, E, C> extends AbstractTransitionModel<S, E, C> implements MutableTransitionModel<S, E, C> {

	private MutableTransitionModelImpl(Class<S> stateClass, Class<E> eventClass, C defaultContext) {
		super(stateClass, eventClass, defaultContext);
	}

	public static <S, E, C> MutableTransitionModelImpl<S, E, C> create(Class<S> stateClass, Class<E> eventClass, C defaultContext) {
		return new MutableTransitionModelImpl<S, E, C>(stateClass, eventClass, defaultContext);
	}

	public static <S, E, C> MutableTransitionModelImpl<S, E, C> create(Class<S> stateClass, Class<E> eventClass) {
		return new MutableTransitionModelImpl<S, E, C>(stateClass, eventClass, null);
	}

	@Override
	public StateMachine<S, E, C> newStateMachine(S init) {
		return newStateMachine(init, new ReentrantLock());
	}


	@Override
	public StateMachine<S, E, C> newStateMachine(S init, Lock lock) {
		return new TemplateBasedStateMachine<S, E, C>(this, init, lock);
	}


	@Override
	public StateMachineTemplate<S, E, C> createImmutableClone() {
		return new ImmutableTransitionModel<S, E, C>(stateClass, eventClass, transitionMap, fromAllTransitions, exitActions, enterActions, defaultContext);
	}

	@Override
	public void addTransition(S from, S to, E event, Condition<C> condition, List<Action<S, E, C>> actions) {
		addTransition(from, event, new BasicTransition<S, E, C>(to, condition, actions));
	}

	@Override
	public void addInternalTransition(S from, S to, E event, Condition<C> condition, List<Action<S, E, C>> actions) {
		addTransition(from, event, new BasicTransition<S, E, C>(to, condition, actions));
	}

	private void addTransition(S from, E event, Transition<S, E, C> transition) {
		Map<E, Collection<Transition<S, E, C>>> map = transitionMap.get(from);
		if (map == null) {
			map = createMap(eventClass);
			transitionMap.put(from, map);
		}
		Collection<Transition<S, E, C>> transitions = map.get(event);
		if (transitions == null) {
			transitions = GuavaReplacement.newArrayList();
			map.put(event, transitions);
		}
		transitions.add(transition);
	}

	@Override
	public void addFromAllTransition(S to, E event, Condition<C> condition, List<Action<S, E, C>> actions) {
		Collection<Transition<S, E, C>> transitions = fromAllTransitions.get(event);
		if (transitions == null) {
			transitions = GuavaReplacement.newArrayList();
			fromAllTransitions.put(event, transitions);
		}
		transitions.add(new BasicTransition<S, E, C>(to, condition, actions));
	}

	@Override
	public void addEntryAction(S entryState, Action<S, E, C> action) {
		addAction(entryState, action, enterActions);
	}

	private void addAction(S entryState, Action<S, E, C> action, Map<S, Collection<Action<S, E, C>>> map) {
		Collection<Action<S, E, C>> collection = map.get(entryState);
		if (collection == null) {
			collection = GuavaReplacement.newArrayList();
			map.put(entryState, collection);
		}
		collection.add(action);
	}

	@Override
	public void addExitAction(S exitState, Action<S, E, C> action) {
		addAction(exitState, action, exitActions);
	}
}
