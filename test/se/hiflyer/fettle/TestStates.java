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
		Action entryAction = mock(Action.class);
		machine.addEntryAction(States.ONE, entryAction);
		Action exitAction = mock(Action.class);
		machine.addExitAction(States.ONE, exitAction);

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

	@Test
	public void forceSetState() {
		StateMachine<States, String> machine = StateMachine.createStateMachineOfEnum(States.class, States.INITIAL);
		machine.addTransition(States.INITIAL, States.ONE, "hej");
		machine.addTransition(States.ONE, States.TWO, "hopp");
		machine.addTransition(States.TWO, States.INITIAL, "hej");

		Action entryAction1 = mock(Action.class);
		machine.addEntryAction(States.INITIAL, entryAction1);
		Action exitAction1 = mock(Action.class);
		machine.addExitAction(States.INITIAL, exitAction1);

		Action entryAction2 = mock(Action.class);
		machine.addEntryAction(States.TWO, entryAction2);
		Action exitAction2 = mock(Action.class);
		machine.addExitAction(States.TWO, exitAction2);

		machine.forceSetState(States.TWO);
		assertEquals(States.TWO, machine.getCurrentState());
		verifyOnce().on(exitAction1).perform();
		verifyOnce().on(entryAction2).perform();

	}


	private class TestState {
	}
}
