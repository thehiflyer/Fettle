package se.hiflyer.fettle.builder;

import org.junit.Test;
import se.hiflyer.fettle.*;

import static org.junit.Assert.assertEquals;
import static se.mockachino.Mockachino.*;
import static se.mockachino.matchers.Matchers.any;

public class StateMachineBuilderTest {

	@Test
	public void testBuilder() {
		StateMachineBuilder<States, String> builder = new StateMachineBuilder<States, String>();

		builder.transition().on("hej").from(States.INITIAL).to(States.ONE);
		builder.transition().on("hopp").from(States.ONE).to(States.TWO);

		StateMachine<States, String> machine = builder.build(States.INITIAL);

		assertEquals(States.INITIAL, machine.getCurrentState());

		machine.fireEvent("hej");

		assertEquals(States.ONE, machine.getCurrentState());

		machine.fireEvent("hej");

		assertEquals(States.ONE, machine.getCurrentState());

		machine.fireEvent("hopp");

		assertEquals(States.TWO, machine.getCurrentState());

		DotExporter<States, String> exporter = new DotExporter<States, String>((BasicStateMachine<States,String>) machine, "test");
		exporter.asDot(System.out, false);
	}

	@Test
	public void fromAllTransition() {

		StateMachineBuilder<States, String> builder = new StateMachineBuilder<States, String>();


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

		StateMachineBuilder<States, String> builder = new StateMachineBuilder<States, String>();

		builder.transition().from(States.INITIAL).to(States.ONE).on("");

		builder.transition().from(States.ONE).to(States.TWO).on("");
		Action<States, String> entryAction = mock(Action.class);
		builder.onEntry(States.ONE).perform(entryAction);

		Action<States, String> exitAction = mock(Action.class);
		builder.onExit(States.ONE).perform(exitAction);

		StateMachine<States, String> machine = builder.build(States.INITIAL);

		machine.fireEvent("foo");

		verifyNever().on(entryAction).perform(any(States.class), any(States.class), any(String.class), Arguments.NO_ARGS);
		verifyNever().on(exitAction).perform(any(States.class), any(States.class), any(String.class), Arguments.NO_ARGS);

		machine.fireEvent("");

		verifyOnce().on(entryAction).perform(any(States.class), any(States.class), any(String.class), Arguments.NO_ARGS);
		verifyNever().on(exitAction).perform(any(States.class), any(States.class), any(String.class), Arguments.NO_ARGS);

		machine.fireEvent("");

		verifyOnce().on(entryAction).perform(any(States.class), any(States.class), any(String.class), Arguments.NO_ARGS);
		verifyOnce().on(exitAction).perform(any(States.class), any(States.class), any(String.class), Arguments.NO_ARGS);
	}

}
