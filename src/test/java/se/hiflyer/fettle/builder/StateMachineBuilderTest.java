package se.hiflyer.fettle.builder;

import org.junit.Test;
import se.hiflyer.fettle.*;
import se.hiflyer.fettle.export.DotExporter;
import se.hiflyer.fettle.impl.AbstractTransitionModel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static se.mockachino.Mockachino.*;
import static se.mockachino.matchers.Matchers.any;

public class StateMachineBuilderTest {

	@Test
	public void testBuilder() {
		StateMachineBuilder<States, String> builder = StateMachineBuilder.create(States.class, String.class);

		builder.transition().on("hej").from(States.INITIAL).to(States.ONE);
		builder.transition().on("hopp").from(States.ONE).to(States.TWO);

		TransitionModel<States, String> transitionModel = builder.buildTransitionModel();
		StateMachine<States, String> machine = transitionModel.newStateMachine(States.INITIAL);

		assertEquals(States.INITIAL, machine.getCurrentState());

		machine.fireEvent("hej");

		assertEquals(States.ONE, machine.getCurrentState());

		machine.fireEvent("hej");

		assertEquals(States.ONE, machine.getCurrentState());

		machine.fireEvent("hopp");

		assertEquals(States.TWO, machine.getCurrentState());
		DotExporter<States, String> exporter = new DotExporter<States, String>((AbstractTransitionModel<States, String>) transitionModel, "test");
		exporter.asDot(System.out, true);
	}

	@Test
	public void fromAllTransition() {

		StateMachineBuilder<States, String> builder = StateMachineBuilder.create(States.class, String.class);


		builder.transition().from(States.INITIAL).to(States.ONE).on("hej");
		builder.transition().from(States.ONE).to(States.TWO).on("hopp");
		builder.transition().to(States.INITIAL).on("back");

		StateMachine<States, String> machine = builder.build(States.INITIAL);

		machine.fireEvent("hej");
		machine.fireEvent("hopp");
		machine.fireEvent("back");


		assertEquals(States.INITIAL, machine.getCurrentState());

	}

	@Test
	public void entryExitActions() {

		StateMachineBuilder<States, String> builder = StateMachineBuilder.create(States.class, String.class);

		builder.transition().from(States.INITIAL).to(States.ONE).on("");

		builder.transition().from(States.ONE).to(States.TWO).on("");
		Action<States, String> entryAction = mock(Action.class);
		builder.onEntry(States.ONE).perform(entryAction);

		Action<States, String> exitAction = mock(Action.class);
		builder.onExit(States.ONE).perform(exitAction);

		StateMachine<States, String> machine = builder.build(States.INITIAL);

		machine.fireEvent("foo");

		verifyNever().on(entryAction).onTransition(any(States.class), any(States.class), any(String.class), Arguments.NO_ARGS, machine);
		verifyNever().on(exitAction).onTransition(any(States.class), any(States.class), any(String.class), Arguments.NO_ARGS, machine);

		machine.fireEvent("");

		verifyOnce().on(entryAction).onTransition(any(States.class), any(States.class), any(String.class), Arguments.NO_ARGS, machine);
		verifyNever().on(exitAction).onTransition(any(States.class), any(States.class), any(String.class), Arguments.NO_ARGS, machine);

		machine.fireEvent("");

		verifyOnce().on(entryAction).onTransition(any(States.class), any(States.class), any(String.class), Arguments.NO_ARGS, machine);
		verifyOnce().on(exitAction).onTransition(any(States.class), any(States.class), any(String.class), Arguments.NO_ARGS, machine);
	}

	@Test
	public void when() throws Exception {
		StateMachineBuilder<States, String> builder = StateMachineBuilder.create(States.class, String.class);

		ConditionImpl condition = new ConditionImpl();
		builder.transition().from(States.INITIAL).to(States.ONE).on("").when(condition);

		StateMachine<States, String> machine = builder.build(States.INITIAL);

		machine.fireEvent("");
		assertEquals(States.INITIAL, machine.getCurrentState());

		condition.pass = true;
		machine.fireEvent("");
		assertEquals(States.ONE, machine.getCurrentState());
	}

	private class ConditionImpl implements Condition {
		boolean pass = false;

		@Override
		public boolean isSatisfied(Arguments args) {
			return pass;
		}
	}

	@Test
	public void perform() throws Exception {
		StateMachineBuilder<States, String> builder = StateMachineBuilder.create(States.class, String.class);
		Action<States, String> action1 = mock(Action.class);
		Action<States, String> action2 = mock(Action.class);

		builder.transition().from(States.INITIAL).to(States.ONE).on("a").perform(action1, action2);

		StateMachine<States, String> machine = builder.build(States.INITIAL);

		verifyNever().on(action1).onTransition(any(States.class), any(States.class), any(String.class), Arguments.NO_ARGS, machine);
		verifyNever().on(action2).onTransition(any(States.class), any(States.class), any(String.class), Arguments.NO_ARGS, machine);
		machine.fireEvent("");

		verifyNever().on(action1).onTransition(any(States.class), any(States.class), any(String.class), Arguments.NO_ARGS, machine);
		verifyNever().on(action2).onTransition(any(States.class), any(States.class), any(String.class), Arguments.NO_ARGS, machine);

		machine.fireEvent("a");

		verifyOnce().on(action1).onTransition(States.INITIAL, States.ONE, "a", Arguments.NO_ARGS, machine);
		verifyOnce().on(action2).onTransition(States.INITIAL, States.ONE, "a", Arguments.NO_ARGS, machine);
	}

	@Test
	public void nullInitialState() throws Exception {
		StateMachineBuilder<States, String> builder = StateMachineBuilder.create(States.class, String.class);
		try {
			builder.build(null);
			fail("Null should not be permitted");
		} catch (IllegalArgumentException e) {

		}
	}
}
