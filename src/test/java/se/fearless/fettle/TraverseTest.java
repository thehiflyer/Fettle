package se.fearless.fettle;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import org.junit.Before;
import org.junit.Test;
import se.fearless.fettle.builder.StateMachineBuilder;
import se.fearless.fettle.impl.Transition;

import java.util.Collection;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TraverseTest {

	private StateMachineBuilder<States, String, Boolean> builder;

	@Before
	public void setUp() throws Exception {
		builder = StateMachineBuilder.create(States.class, String.class);
	}

	@Test
	public void queryWithNoTransitions() throws Exception {
		StateMachine<States, String, Boolean> stateMachine = createStateMachine(builder);

		Map<String, Collection<Transition<States, String, Boolean>>> transitionMap = stateMachine.getPossibleTransitions(States.INITIAL);

		assertTrue(transitionMap.isEmpty());
	}

	@Test
	public void queryWithOneTransition() throws Exception {
		builder.transition().from(States.INITIAL).to(States.ONE).on("foo");
		StateMachine<States, String, Boolean> stateMachine = createStateMachine(builder);

		Map<String, Collection<Transition<States, String, Boolean>>> transitionMap = stateMachine.getPossibleTransitions(States.INITIAL);
		assertEquals(1, transitionMap.size());
		Collection<Transition<States, String, Boolean>> transitions = transitionMap.get("foo");
		Transition<States, String, Boolean> transition = Iterables.getOnlyElement(transitions);
		assertEquals(States.ONE, transition.getTo());
	}

	@Test
	public void queryWithMultipleTransitionsFromOneState() throws Exception {
		builder.transition().from(States.INITIAL).to(States.ONE).on("foo").when(new Condition<Boolean>() {
			@Override
			public boolean isSatisfied(Boolean context) {
				return context;
			}
		});
		builder.transition().from(States.INITIAL).to(States.TWO).on("foo").when(new Condition<Boolean>() {
			@Override
			public boolean isSatisfied(Boolean context) {
				return !context;
			}
		});
		StateMachine<States, String, Boolean> stateMachine = createStateMachine(builder);

		Map<String, Collection<Transition<States, String, Boolean>>> transitionMap = stateMachine.getPossibleTransitions(States.INITIAL);
		assertEquals(1, transitionMap.size());
		Collection<Transition<States, String, Boolean>> transitions = transitionMap.get("foo");

		checkExistenceOfTransitionsTo(States.ONE, transitions);
		checkExistenceOfTransitionsTo(States.TWO, transitions);
	}

	@Test
	public void irrelevantTransitionsAreNotIncluded() throws Exception {
		builder.transition().from(States.INITIAL).to(States.ONE).on("foo");
		builder.transition().from(States.ONE).to(States.TWO).on("foo");
		builder.transition().from(States.THREE).to(States.ONE).on("foo");
		StateMachine<States, String, Boolean> stateMachine = createStateMachine(builder);

		Map<String, Collection<Transition<States, String, Boolean>>> transitionMap = stateMachine.getPossibleTransitions(States.INITIAL);
		assertEquals(1, transitionMap.size());
		Collection<Transition<States, String, Boolean>> transitions = transitionMap.get("foo");
		Transition<States, String, Boolean> transition = Iterables.getOnlyElement(transitions);
		assertEquals(States.ONE, transition.getTo());
	}

	@Test
	public void fromAllTransitionsAreIncluded() throws Exception {
		builder.transition().fromAll().to(States.ONE).on("foo").when(new Condition<Boolean>() {
			@Override
			public boolean isSatisfied(Boolean context) {
				return context;
			}
		});
		builder.transition().fromAll().to(States.TWO).on("foo").when(new Condition<Boolean>() {
			@Override
			public boolean isSatisfied(Boolean context) {
				return !context;
			}
		});
		StateMachine<States, String, Boolean> stateMachine = createStateMachine(builder);

		Map<String, Collection<Transition<States, String, Boolean>>> transitionMap = stateMachine.getPossibleTransitions(States.INITIAL);
		assertEquals(1, transitionMap.size());
		Collection<Transition<States, String, Boolean>> transitions = transitionMap.get("foo");

		checkExistenceOfTransitionsTo(States.ONE, transitions);
		checkExistenceOfTransitionsTo(States.TWO, transitions);
	}

	@Test
	public void fromAllTransitionsAndNormalTransitionsCanCoexist() throws Exception {
		builder.transition().fromAll().to(States.ONE).on("foo").when(new Condition<Boolean>() {
			@Override
			public boolean isSatisfied(Boolean context) {
				return context;
			}
		});
		builder.transition().from(States.INITIAL).to(States.TWO).on("foo").when(new Condition<Boolean>() {
			@Override
			public boolean isSatisfied(Boolean context) {
				return !context;
			}
		});
		StateMachine<States, String, Boolean> stateMachine = createStateMachine(builder);

		Map<String, Collection<Transition<States, String, Boolean>>> transitionMap = stateMachine.getPossibleTransitions(States.INITIAL);
		assertEquals(1, transitionMap.size());
		Collection<Transition<States, String, Boolean>> transitions = transitionMap.get("foo");

		checkExistenceOfTransitionsTo(States.ONE, transitions);
		checkExistenceOfTransitionsTo(States.TWO, transitions);
	}


	private StateMachine<States, String, Boolean> createStateMachine(StateMachineBuilder<States, String, Boolean> builder) {
		StateMachineTemplate<States, String, Boolean> template = builder.buildTransitionModel();
		return template.newStateMachine(States.INITIAL);
	}

	private void checkExistenceOfTransitionsTo(final States state, Collection<Transition<States, String, Boolean>> transitions) {
		Transition<States, String, Boolean> transition1 = Iterables.find(transitions, new Predicate<Transition<States, String, Boolean>>() {
			@Override
			public boolean apply(Transition<States, String, Boolean> input) {
				return input.getTo() == state;
			}
		});
		assertNotNull(transition1);
	}
}
