package se.hiflyer.fettle;

public class Fettle {

	private Fettle() {
	}

	public static <S, E> MutableTransitionModel<S, E> newTransitionModel(Class<S> stateClass) {
		return MutableTransitionModel.createTransitionModel(stateClass);
	}
}
