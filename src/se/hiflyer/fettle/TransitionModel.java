package se.hiflyer.fettle;

public interface TransitionModel<S, E> {

	StateMachine<S, E> newStateMachine(S init);

	boolean fireEvent(StateMachine<S, E> machine, E event, Arguments args);

	boolean forceSetState(StateMachine<S, E> stateMachine, S forcedState);
}
