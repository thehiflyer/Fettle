package se.hiflyer.fettle;

import com.google.common.collect.ImmutableMap;
import se.hiflyer.fettle.util.TransitionMap;

public interface StateMachineInternalsInformer<S, E> {
	TransitionMap<S, E> getStateTransitions();

	ImmutableMap<E, Transition<S, E>> getFromAllTransitions();
}
