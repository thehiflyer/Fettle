package se.fearless.fettle;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.fearless.fettle.builder.StateMachineBuilder;

import java.util.Collection;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TraverseTest {

	private StateMachineBuilder<States, String, Boolean> builder;

	@BeforeEach
	public void setUp() {
		builder = StateMachineBuilder.create(States.class, String.class);
	}

	@Test
	public void queryWithNoTransitions() {
		StateMachine<States, String, Boolean> stateMachine = createStateMachine(builder);

		Map<String, Collection<? extends Transition<States, String, Boolean>>> transitionMap = stateMachine.getPossibleTransitions(States.INITIAL);

		assertTrue(transitionMap.isEmpty());
	}

	@Test
	public void queryWithOneTransition() {
		builder.transition().from(States.INITIAL).to(States.ONE).on("foo");
		StateMachine<States, String, Boolean> stateMachine = createStateMachine(builder);

		Map<String, Collection<? extends Transition<States, String, Boolean>>> transitionMap = stateMachine.getPossibleTransitions(States.INITIAL);
		assertEquals(1, transitionMap.size());
		Collection<? extends Transition<States, String, Boolean>> transitions = transitionMap.get("foo");
		Transition<States, String, Boolean> transition = transitions.stream().findFirst().orElseThrow();
		assertEquals(States.ONE, transition.getTo());
	}

	@Test
	public void queryWithMultipleTransitionsFromOneState() {
		builder.transition().from(States.INITIAL).to(States.ONE).on("foo").when(context -> context);
		builder.transition().from(States.INITIAL).to(States.TWO).on("foo").when(context -> !context);
		StateMachine<States, String, Boolean> stateMachine = createStateMachine(builder);

		Map<String, Collection<? extends Transition<States, String, Boolean>>> transitionMap = stateMachine.getPossibleTransitions(States.INITIAL);
		assertEquals(1, transitionMap.size());
		Collection<? extends Transition<States, String, Boolean>> transitions = transitionMap.get("foo");

		checkExistenceOfTransitionsTo(States.ONE, transitions);
		checkExistenceOfTransitionsTo(States.TWO, transitions);
	}

	@Test
	public void irrelevantTransitionsAreNotIncluded() {
		builder.transition().from(States.INITIAL).to(States.ONE).on("foo");
		builder.transition().from(States.ONE).to(States.TWO).on("foo");
		builder.transition().from(States.THREE).to(States.ONE).on("foo");
		StateMachine<States, String, Boolean> stateMachine = createStateMachine(builder);

		Map<String, Collection<? extends Transition<States, String, Boolean>>> transitionMap = stateMachine.getPossibleTransitions(States.INITIAL);
		assertEquals(1, transitionMap.size());
		Collection<? extends Transition<States, String, Boolean>> transitions = transitionMap.get("foo");
		assertEquals(1, transitions.size());
		Transition<States, String, Boolean> transition = transitions.iterator().next();
		assertEquals(States.ONE, transition.getTo());
	}

	@Test
	public void fromAllTransitionsAreIncluded() {
		builder.transition().fromAll().to(States.ONE).on("foo").when(context -> context);
		builder.transition().fromAll().to(States.TWO).on("foo").when(context -> !context);
		StateMachine<States, String, Boolean> stateMachine = createStateMachine(builder);

		Map<String, Collection<? extends Transition<States, String, Boolean>>> transitionMap = stateMachine.getPossibleTransitions(States.INITIAL);
		assertEquals(1, transitionMap.size());
		Collection<? extends Transition<States, String, Boolean>> transitions = transitionMap.get("foo");

		checkExistenceOfTransitionsTo(States.ONE, transitions);
		checkExistenceOfTransitionsTo(States.TWO, transitions);
	}

	@Test
	public void fromAllTransitionsAndNormalTransitionsCanCoexist() {
		builder.transition().fromAll().to(States.ONE).on("foo").when(context -> context);
		builder.transition().from(States.INITIAL).to(States.TWO).on("foo").when(context -> !context);
		StateMachine<States, String, Boolean> stateMachine = createStateMachine(builder);

		Map<String, Collection<? extends Transition<States, String, Boolean>>> transitionMap = stateMachine.getPossibleTransitions(States.INITIAL);
		assertEquals(1, transitionMap.size());
		Collection<? extends Transition<States, String, Boolean>> transitions = transitionMap.get("foo");

		checkExistenceOfTransitionsTo(States.ONE, transitions);
		checkExistenceOfTransitionsTo(States.TWO, transitions);
	}


	private StateMachine<States, String, Boolean> createStateMachine(StateMachineBuilder<States, String, Boolean> builder) {
		StateMachineTemplate<States, String, Boolean> template = builder.buildTransitionModel();
		return template.newStateMachine(States.INITIAL);
	}

	private void checkExistenceOfTransitionsTo(final States state, Collection<? extends Transition<States, String, Boolean>> transitions) {
		Transition<States, String, Boolean> transition1 = transitions.stream().filter(input -> input.getTo() == state).findFirst().orElseThrow();
		assertNotNull(transition1);
	}
}
