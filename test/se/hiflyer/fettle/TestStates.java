package se.hiflyer.fettle;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static se.mockachino.Mockachino.*;

public class TestStates {

	@Test
	public void simpleStateTransition() {

		StateMachine<States, String> machine = StateMachine.createStateMachineOfEnum(States.class, States.INITIAL);

		machine.addTransition(States.INITIAL, States.ONE, "hej");

		machine.addTransition(States.ONE, States.TWO, "hopp");

		assertEquals(States.INITIAL, machine.getCurrentState());

		machine.fireEvent("hej");

		assertEquals(States.ONE, machine.getCurrentState());

		machine.fireEvent("hej");

		assertEquals(States.ONE, machine.getCurrentState());

		machine.fireEvent("hopp");

		assertEquals(States.TWO, machine.getCurrentState());

	}

	@Test
	public void entryExitActions() {
		StateMachine<States, String> machine = StateMachine.createStateMachineOfEnum(States.class, States.INITIAL);

		machine.addTransition(States.INITIAL, States.ONE, "", BasicConditions.ALWAYS);


		machine.addTransition(States.ONE, States.TWO, "", BasicConditions.ALWAYS);
		Runnable entryAction = mock(Runnable.class);
		machine.addEntryAction(States.ONE, entryAction);
		Runnable exitAction = mock(Runnable.class);
		machine.addExitAction(States.ONE, exitAction);

		machine.fireEvent("foo");

		verifyNever().on(entryAction).run();
		verifyNever().on(exitAction).run();

		machine.fireEvent("");

		verifyOnce().on(entryAction).run();
		verifyNever().on(exitAction).run();

		machine.fireEvent("");

		verifyOnce().on(entryAction).run();
		verifyOnce().on(exitAction).run();
	}

	@Test
	public void simpleStateTransitionUsingNonEnums() {

		TestState initial = new TestState();
		TestState one = new TestState();
		TestState two = new TestState();

		StateMachine<TestState, String> machine = StateMachine.createStateMachine(initial);

		machine.addTransition(initial, one, "", BasicConditions.ALWAYS);

		machine.addTransition(one, two, "", BasicConditions.ALWAYS);

		assertEquals(initial, machine.getCurrentState());

		machine.fireEvent("");

		assertEquals(one, machine.getCurrentState());

		machine.fireEvent("foo");

		assertEquals(one, machine.getCurrentState());

		machine.fireEvent("");

		assertEquals(two, machine.getCurrentState());

	}


	private class TestState {
	}
}
