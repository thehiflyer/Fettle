package se.hiflyer.fettle.export;

public class DotExporter<S, E> {
//	private final StateMachineInternalsInformer<S, E> stateMachineInternalsInformer;
	//private final String name;

//	public DotExporter(StateMachineInternalsInformer<S, E> stateMachineInternalsInformer, String name) {
//		this.stateMachineInternalsInformer = stateMachineInternalsInformer;
//		this.name = name;
//	}
//
//	public void asDot(OutputStream os, boolean includeFromAllTransitions) {
//		PrintWriter writer = new PrintWriter(os);
//		writer.println("digraph " + name + " {");
//		Multimap<S,Transition<S,E>> stateTransitions = stateMachineInternalsInformer.getStateTransitions();
//		Set<S> states = stateTransitions.keySet();
//		Set<S> allStates = new HashSet<S>(states);
//		for (S state : states) {
//			for (Transition<S, E> transition : stateTransitions.get(state)) {
//				printTransition(writer, transition.getFrom(), transition.getTo(), transition.getEvent(), "");
//				allStates.add(transition.getTo());
//			}
//		}
//		if (includeFromAllTransitions) {
//			Map<E, Transition<S, E>> fromAllTransitions = stateMachineInternalsInformer.getFromAllTransitions();
//			for (E event : fromAllTransitions.keySet()) {
//				Transition<S, E> fromAll = fromAllTransitions.get(event);
//				for (S state : allStates) {
//					printTransition(writer, state, fromAll.getTo(), event, "(fromall)");
//				}
//			}
//		}
//		writer.println("}");
//		writer.flush();
//	}
//
//	private void printTransition(PrintWriter writer, S from, S to, E cause, String extra) {
//		writer.println("\t" + from + " -> " + to + " [label = \"" + cause + extra + "\"]");
//	}
}
