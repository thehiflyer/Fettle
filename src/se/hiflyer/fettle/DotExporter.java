package se.hiflyer.fettle;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

public class DotExporter<S, E> {
	private final BasicStateMachine<S, E> stateMachine;
	private final String name;

	public DotExporter(BasicStateMachine<S, E> stateMachine, String name) {
		this.stateMachine = stateMachine;
		this.name = name;
	}

	public void asDot(OutputStream os, boolean includeFromAllTransitions) {
		PrintWriter writer = new PrintWriter(os);
		writer.println("digraph " + name + " {");
		Set<S> states = stateMachine.stateTransitions.keySet();
		Set<S> allStates = new HashSet<S>(states);
		for (S state : states) {
			Collection<Transition<S, E>> transitions = stateMachine.stateTransitions.get(state);
			for (Transition<S, E> transition : transitions) {
				printTransition(writer, transition.getFrom(), transition.getTo(), transition.getEvent(), "");
				allStates.add(transition.getTo());
			}
		}
		if (includeFromAllTransitions) {
			stateMachine.fromAllTransitions.keySet();
			for (E event : stateMachine.fromAllTransitions.keySet()) {
				Transition<S, E> fromAll = stateMachine.fromAllTransitions.get(event);
				for (S state : allStates) {
					printTransition(writer, state, fromAll.getTo(), event, "(fromall)");
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
