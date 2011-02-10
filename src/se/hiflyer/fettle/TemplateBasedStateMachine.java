package se.hiflyer.fettle;

import se.hiflyer.fettle.util.Multimap;

import java.util.Map;

public class TemplateBasedStateMachine<S, E> implements StateMachine<S, E>, StateMachineInternalsInformer<S, E> {
	private final StateMachineTemplate<S, E> template;
	private S currentState;

	public TemplateBasedStateMachine(StateMachineTemplate<S, E> template, S initial) {
		this.template = template;
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
		Multimap<S,Transition<S,E>> stateTransitions = template.getStateTransitions();
		for (Transition<S, E> transition : stateTransitions.get(currentState)) {
			if (transition.getEvent().equals(event)) {
				if (transition.getCondition().isSatisfied(args)) {
					moveToNewState(transition, event, args);
					return true;
				}
			}
		}
		Transition<S, E> fromAllTransition = template.getFromAllTransitionForEvent(event);
		if (fromAllTransition != null) {
			if (fromAllTransition.getCondition().isSatisfied(args)) {
				moveToNewState(fromAllTransition, event, args);
				return true;
			}
		}
		return false;
	}

	private void moveToNewState(Transition<S, E> transition, E cause, Arguments args) {
		S from = currentState;
		S to = transition.getTo();
		template.runTransitionActions(from, to, cause, args, transition.getTransitionActions());
		currentState = to;
	}	

	@Override
	public void forceSetState(S forcedState) {
		Transition<S, E> transition = new Transition<S, E>(currentState, forcedState, BasicConditions.ALWAYS, null);
		moveToNewState(transition, null, Arguments.NO_ARGS);
	}

	@Override
	public Multimap<S, Transition<S, E>> getStateTransitions() {
		return template.getStateTransitions();
	}

	@Override
	public Map<E, Transition<S, E>> getFromAllTransitions() {
		return template.getFromAllTransitions();
	}

	public StateMachineTemplate<S,E> getTemplate() {
		return template;
	}
}
