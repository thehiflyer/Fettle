package se.hiflyer.fettle;

public interface Action<S, E> {
	public void perform(S from, S to, E causedBy, Arguments args);
}
