package se.hiflyer.fettle;

import se.hiflyer.fettle.builder.StateMachineBuilder;
import se.hiflyer.fettle.impl.MutableTransitionModelImpl;

public class Fettle {

	private Fettle() {
	}

	public static <S, E> MutableTransitionModelImpl<S, E> newTransitionModel(Class<S> stateClass, Class<E> eventClass) {
		return MutableTransitionModelImpl.create(stateClass, eventClass);
	}

	public static <S, E> StateMachineBuilder<S, E> newBuilder(Class<S> stateClass, Class<E> eventClass) {
		return StateMachineBuilder.create(stateClass, eventClass);
	}
}
