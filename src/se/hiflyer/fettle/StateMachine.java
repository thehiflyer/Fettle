package se.hiflyer.fettle;

public interface StateMachine<S, E> {
	S getCurrentState();

	boolean fireEvent(E event);

	void forceSetState(S forcedState);
}
