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

	@Test
	public void forceSetState() {
		StateMachine<States, String> machine = StateMachine.createStateMachineOfEnum(States.class, States.INITIAL);
		machine.addTransition(States.INITIAL, States.ONE, "hej");
		machine.addTransition(States.ONE, States.TWO, "hopp");
		machine.addTransition(States.TWO, States.INITIAL, "hej");

		Runnable entryAction1 = mock(Runnable.class);
		machine.addEntryAction(States.INITIAL, entryAction1);
		Runnable exitAction1 = mock(Runnable.class);
		machine.addExitAction(States.INITIAL, exitAction1);

		Runnable entryAction2 = mock(Runnable.class);
		machine.addEntryAction(States.TWO, entryAction2);
		Runnable exitAction2 = mock(Runnable.class);
		machine.addExitAction(States.TWO, exitAction2);

		machine.forceSetState(States.TWO);
		assertEquals(States.TWO, machine.getCurrentState());
		verifyOnce().on(exitAction1).run();
		verifyOnce().on(entryAction2).run();

	}


	private class TestState {
	}
}
