package se.hiflyer.fettle;

import com.google.common.collect.Maps;
import se.hiflyer.fettle.util.EnumMultimap;
import se.hiflyer.fettle.util.Multimap;
import se.hiflyer.fettle.util.SetMultimap;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class MutableTransitionModel<S, E> implements TransitionModel<S, E>, StateMachineInternalsInformer<S, E> {
	private final Multimap<S, Transition<S, E>> stateTransitions;
	private final Map<E, Transition<S, E>> fromAllTransitions;
	private final Multimap<S, Action<S, E>> entryActions;
	private final Multimap<S, Action<S, E>> exitActions;

	private MutableTransitionModel(Multimap<S, Transition<S, E>> stateTransitions, Multimap<S, Action<S, E>> entryActions, Multimap<S, Action<S, E>> exitActions) {
		this.stateTransitions = stateTransitions;
		this.entryActions = entryActions;
		this.exitActions = exitActions;
		fromAllTransitions = Maps.newHashMap();
	}

	@SuppressWarnings("unchecked")
	public static <S, E> MutableTransitionModel<S, E> createStateMachineTemplate(Class<S> stateClass) {
		if (stateClass.isEnum()) {
			return (MutableTransitionModel<S, E>) MutableTransitionModel.createStateMachineTemplateOfEnum((Class<Enum>) stateClass);
		}
		return new MutableTransitionModel<S, E>(SetMultimap.<S, Transition<S, E>>create(), SetMultimap.<S, Action<S, E>>create(), SetMultimap.<S, Action<S, E>>create());
	}

	private static <S extends Enum<S>, E> MutableTransitionModel<S, E> createStateMachineTemplateOfEnum(Class<S> clazz) {
		return new MutableTransitionModel<S, E>(EnumMultimap.<S, Transition<S, E>>create(clazz),
				  EnumMultimap.<S, Action<S, E>>create(clazz), EnumMultimap.<S, Action<S, E>>create(clazz));
	}


	public TemplateBasedStateMachine<S, E> createInstance(S initial) {
		return new TemplateBasedStateMachine<S, E>(this, initial);
	}

	@Override
	public void addFromAllTransition(S to, E event, Condition condition, List<Action<S, E>> actions) {
		fromAllTransitions.put(event, new Transition<S, E>(null, to, condition, event, actions));
	}

	@Override
	public void addTransition(S from, S to, E event, Condition condition, List<Action<S, E>> actions) {
		stateTransitions.put(from, new Transition<S, E>(from, to, condition, event, actions));
	}

	@Override
	public void addEntryAction(S entryState, Action<S, E> action) {
		entryActions.put(entryState, action);
	}

	@Override
	public void addExitAction(S exitState, Action<S, E> action) {
		exitActions.put(exitState, action);
	}

	@Override
	public Multimap<S, Transition<S, E>> getStateTransitions() {
		return stateTransitions;
	}

	@Override
	public Map<E, Transition<S, E>> getFromAllTransitions() {
		return fromAllTransitions;
	}

	public void runTransitionActions(S from, S to, E cause, Arguments args, Collection<Action<S, E>> transitionActions) {
		runActions(exitActions, from, from, to, cause, args);
		runActions(transitionActions, from, to, cause, args);
		runActions(entryActions, to, from, to, cause, args);
	}

	private void runActions(Collection<Action<S, E>> actions, S from, S to, E cause, Arguments args) {
		for (Action<S, E> action : actions) {
			action.perform(from, to, cause, args);
		}
	}


	private void runActions(Multimap<S, Action<S, E>> actionMap, S state, S from, S to, E cause, Arguments args) {
		runActions(actionMap.get(state), from, to, cause, args);
	}

	public Transition<S, E> getFromAllTransitionForEvent(E event) {
		return fromAllTransitions.get(event);
	}
}
