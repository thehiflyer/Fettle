package se.hiflyer.fettle;

import se.hiflyer.fettle.util.Multimap;

import java.util.Map;

public interface StateMachineInternalsInformer<S, E> {
	Multimap<S, Transition<S, E>> getStateTransitions();

	Map<E, Transition<S, E>> getFromAllTransitions();
}
