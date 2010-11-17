package se.hiflyer.fettle.builder;

import org.junit.Test;
import se.hiflyer.fettle.Action;
import se.hiflyer.fettle.StateMachine;
import se.hiflyer.fettle.States;

import static org.junit.Assert.assertEquals;
import static se.mockachino.Mockachino.*;

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
		Action entryAction = mock(Action.class);
		builder.onEntry(States.ONE).perform(entryAction);

		Action exitAction = mock(Action.class);
		builder.onExit(States.ONE).perform(exitAction);

		StateMachine<States, String> machine = builder.build(States.INITIAL);

		machine.fireEvent("foo");

		verifyNever().on(entryAction).perform();
		verifyNever().on(exitAction).perform();

		machine.fireEvent("");

		verifyOnce().on(entryAction).perform();
		verifyNever().on(exitAction).perform();

		machine.fireEvent("");

		verifyOnce().on(entryAction).perform();
		verifyOnce().on(exitAction).perform();
	}

}
