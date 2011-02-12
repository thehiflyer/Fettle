package se.hiflyer.fettle;

public interface Action<S, E> {
	public void onTransition(S from, S to, E causedBy, Arguments args);
}
