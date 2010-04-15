package se.hiflyer.fettle;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static se.mockachino.Mockachino.*;

public class TestStates {

	@Test
	public void simpleStateTransition() {

		StateMachine<States> machine = StateMachine.createStateMachineOfEnum(States.class, States.INITIAL);

		Condition condition1 = mock(Condition.class);
		stubReturn(true).on(condition1).isSatisfied();

		Transition<States> initToOne = new BasicTransition<States>(States.INITIAL, States.ONE, condition1);
		machine.addTransition(initToOne);

		Condition condition2 = mock(Condition.class);
		stubReturn(false).on(condition2).isSatisfied();

		Transition<States> oneToTwo = new BasicTransition<States>(States.ONE, States.TWO, condition2);
		machine.addTransition(oneToTwo);

		assertEquals(States.INITIAL, machine.getCurrentState());

		machine.update();

		assertEquals(States.ONE, machine.getCurrentState());

		machine.update();

		assertEquals(States.ONE, machine.getCurrentState());

		stubReturn(true).on(condition2).isSatisfied();

		machine.update();

		assertEquals(States.TWO, machine.getCurrentState());

	}

	@Test
	public void entryExitActions() {
		StateMachine<States> machine = StateMachine.createStateMachineOfEnum(States.class, States.INITIAL);

		Condition condition1 = mock(Condition.class);
		stubReturn(false).on(condition1).isSatisfied();

		Transition<States> initToOne = new BasicTransition<States>(States.INITIAL, States.ONE, condition1);
		machine.addTransition(initToOne);

		Transition<States> oneToTwo = new BasicTransition<States>(States.ONE, States.TWO, condition1);
		machine.addTransition(oneToTwo);
		Runnable entryAction = mock(Runnable.class);
		machine.addEntryAction(States.ONE, entryAction);
		Runnable exitAction = mock(Runnable.class);
		machine.addExitAction(States.ONE, exitAction);

		machine.update();

		verifyNever().on(entryAction).run();
		verifyNever().on(exitAction).run();

		stubReturn(true).on(condition1).isSatisfied();

		machine.update();

		verifyOnce().on(entryAction).run();
		verifyNever().on(exitAction).run();

		machine.update();

		verifyOnce().on(entryAction).run();
		verifyOnce().on(exitAction).run();
	}

	@Test
	public void simpleStateTransitionUsingNonEnums() {

		TestState initial = new TestState();
		TestState one = new TestState();
		TestState two = new TestState();

		StateMachine<TestState> machine = StateMachine.createStateMachine(initial);

		Condition condition1 = mock(Condition.class);
		stubReturn(true).on(condition1).isSatisfied();

		Transition<TestState> initToOne = new BasicTransition<TestState>(initial, one, condition1);
		machine.addTransition(initToOne);

		Condition condition2 = mock(Condition.class);
		stubReturn(false).on(condition2).isSatisfied();

		Transition<TestState> oneToTwo = new BasicTransition<TestState>(one, two, condition2);
		machine.addTransition(oneToTwo);

		assertEquals(initial, machine.getCurrentState());

		machine.update();

		assertEquals(one, machine.getCurrentState());

		machine.update();

		assertEquals(one, machine.getCurrentState());

		stubReturn(true).on(condition2).isSatisfied();

		machine.update();

		assertEquals(two, machine.getCurrentState());

	}


	private class TestState {
	}
}
