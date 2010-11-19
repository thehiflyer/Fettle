package se.hiflyer.fettle;

public interface StateMachine<S, E> {
	S getCurrentState();

	boolean fireEvent(E event);

	boolean fireEvent(E event, Arguments args);

	void forceSetState(S forcedState);
}
