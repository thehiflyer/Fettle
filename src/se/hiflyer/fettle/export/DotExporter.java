package se.hiflyer.fettle.export;

import se.hiflyer.fettle.impl.AbstractTransitionModel;
import se.hiflyer.fettle.impl.Transition;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DotExporter<S, E> {
	private final AbstractTransitionModel<S, E> model;
	private final String name;

	public DotExporter(AbstractTransitionModel<S, E> model, String name) {
		this.model = model;
		this.name = name;
	}

	public void asDot(OutputStream os, boolean includeFromAllTransitions) {
		PrintWriter writer = new PrintWriter(os);
		writer.println("digraph " + name + " {");
		Map<S, Map<E, Collection<Transition<S, E>>>> stateTransitions = model.getStateTransitions();
		Set<S> states = stateTransitions.keySet();
		Set<S> allStates = new HashSet<S>(states);
		for (S state : states) {
			Map<E, Collection<Transition<S, E>>> transitionsFromState = stateTransitions.get(state);
			for (Map.Entry<E, Collection<Transition<S, E>>> transitions : transitionsFromState.entrySet()) {
				for (Transition<S, E> transition : transitions.getValue()) {
					printTransition(writer, state, transition.getTo(), transitions.getKey(), "");
					allStates.add(transition.getTo());
				}
			}

		}
		if (includeFromAllTransitions) {
			Map<E, Collection<Transition<S, E>>> fromAllTransitions = model.getFromAllTransitions();
			for (E event : fromAllTransitions.keySet()) {
				Collection<Transition<S, E>> fromAll = fromAllTransitions.get(event);
				for (S state : allStates) {
					for (Transition<S, E> transition : fromAll) {
						printTransition(writer, state, transition.getTo(), event, "(fromall)");
					}
				}
			}
		}
		writer.println("}");
		writer.flush();
	}

	private void printTransition(PrintWriter writer, S from, S to, E cause, String extra) {
		writer.println("\t" + from + " -> " + to + " [label = \"" + cause + extra + "\"]");
	}
}
