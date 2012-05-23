package se.hiflyer.fettle.impl;

import se.hiflyer.fettle.Action;
import se.hiflyer.fettle.Condition;
import se.hiflyer.fettle.MutableTransitionModel;
import se.hiflyer.fettle.StateMachine;
import se.hiflyer.fettle.StateMachineTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class MutableTransitionModelImpl<S, E> extends AbstractTransitionModel<S, E> implements MutableTransitionModel<S, E> {

	private MutableTransitionModelImpl(Class<S> stateClass, Class<E> eventClass) {
		super(stateClass, eventClass);
	}

	public static <S, E> MutableTransitionModelImpl<S, E> create(Class<S> stateClass, Class<E> eventClass) {
		return new MutableTransitionModelImpl<S, E>(stateClass, eventClass);
	}

	@Override
	public StateMachine<S, E> newStateMachine(S init) {
		return new TemplateBasedStateMachine<S, E>(this, init);
	}

	@Override
	public StateMachineTemplate<S, E> createImmutableClone() {
		return new ImmutableTransitionModel<S, E>(stateClass, eventClass, transitionMap, fromAllTransitions, exitActions, enterActions);
	}

	@Override
	public void addTransition(S from, S to, E event, Condition condition, List<Action<S, E>> actions) {
		Map<E, Collection<Transition<S, E>>> map = transitionMap.get(from);
		if (map == null) {
			map = createMap(eventClass);
			transitionMap.put(from, map);
		}
		Collection<Transition<S, E>> transitions = map.get(event);
		if (transitions == null) {
			transitions = new ArrayList<Transition<S, E>>();
			map.put(event, transitions);
		}
		transitions.add(new Transition<S, E>(to, condition, actions));
	}

	@Override
	public void addFromAllTransition(S to, E event, Condition condition, List<Action<S, E>> actions) {
		Collection<Transition<S, E>> transitions = fromAllTransitions.get(event);
		if (transitions == null) {
			transitions = new ArrayList<Transition<S, E>>();
			fromAllTransitions.put(event, transitions);
		}
		transitions.add(new Transition<S, E>(to, condition, actions));
	}

	@Override
	public void addEntryAction(S entryState, Action<S, E> action) {
		addAction(entryState, action, enterActions);
	}

	private void addAction(S entryState, Action<S, E> action, Map<S, Collection<Action<S, E>>> map) {
		Collection<Action<S, E>> collection = map.get(entryState);
		if (collection == null) {
			collection = new ArrayList<Action<S, E>>();
			map.put(entryState, collection);
		}
		collection.add(action);
	}

	@Override
	public void addExitAction(S exitState, Action<S, E> action) {
		addAction(exitState, action, exitActions);
	}
}
