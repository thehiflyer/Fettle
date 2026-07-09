package se.fearless.fettle.export;

import se.fearless.fettle.Transition;
import se.fearless.fettle.impl.AbstractTransitionModel;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.nio.charset.StandardCharsets.UTF_8;

public class DotExporter<S, E, C> {
	private final AbstractTransitionModel<S, E, C> model;
	private final String name;

	public DotExporter(AbstractTransitionModel<S, E, C> model, String name) {
		this.model = model;
		this.name = name;
	}

	public void asDot(OutputStream os, boolean includeFromAllTransitions) {
		PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(os, UTF_8)));
		writer.println("digraph " + name + " {");
		Map<S, Map<E, Collection<Transition<S, E, C>>>> stateTransitions = model.getStateTransitions();
		Set<S> allStates = new HashSet<>();

		stateTransitions.forEach((state, transitionsFromState) ->
			transitionsFromState.forEach((event, transitions) ->
				transitions.forEach(transition -> {
					printTransition(writer, state, transition.getTo(), event, "");
					allStates.add(transition.getTo());
				})
			)
		);

		if (includeFromAllTransitions) {
			Map<E, Collection<Transition<S, E, C>>> fromAllTransitions = model.getFromAllTransitions();
			fromAllTransitions.forEach((event, fromAll) ->
				allStates.forEach(state ->
					fromAll.forEach(transition ->
						printTransition(writer, state, transition.getTo(), event, "(fromall)")
					)
				)
			);
		}
		writer.println("}");
		writer.flush();
	}

	private void printTransition(PrintWriter writer, S from, S to, E cause, String extra) {
		writer.println("\t" + from + " -> " + to + " [label = \"" + cause + extra + "\"]");
	}
}
