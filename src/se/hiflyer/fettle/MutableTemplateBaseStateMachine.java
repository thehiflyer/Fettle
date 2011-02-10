package se.hiflyer.fettle;

import java.util.List;

public class MutableTemplateBaseStateMachine<S, E> implements MutableStateMachine<S, E> {
	private final TemplateBasedStateMachine<S, E> stateMachine;

	public MutableTemplateBaseStateMachine(TemplateBasedStateMachine<S, E> stateMachine) {
		this.stateMachine = stateMachine;
	}

	@Override
	public S getCurrentState() {
		return stateMachine.getCurrentState();
	}

	@Override
	public boolean fireEvent(E event) {
		return stateMachine.fireEvent(event);
	}

	@Override
	public boolean fireEvent(E event, Arguments args) {
		return stateMachine.fireEvent(event, args);
	}

	@Override
	public void forceSetState(S forcedState) {
		stateMachine.forceSetState(forcedState);
	}

	@Override
	public void addTransition(S from, S to, E event, Condition condition, List<Action<S, E>> actions) {
		stateMachine.getTemplate().addTransition(from, to, event, condition, actions);
	}

	@Override
	public void addFromAllTransition(S to, E event, Condition condition, List<Action<S, E>> actions) {
		stateMachine.getTemplate().addFromAllTransition(to, event, condition, actions);
	}

	@Override
	public void addEntryAction(S entryState, Action<S, E> seAction) {
		stateMachine.getTemplate().addEntryAction(entryState, seAction);
	}

	@Override
	public void addExitAction(S exitState, Action<S, E> seAction) {
		stateMachine.getTemplate().addExitAction(exitState, seAction);
	}
}
