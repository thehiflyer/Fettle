package se.hiflyer.fettle;

import org.junit.Test;
import se.hiflyer.fettle.BasicTransition;
import se.hiflyer.fettle.Condition;
import se.hiflyer.fettle.StateMachine;
import se.hiflyer.fettle.Transition;

import static org.junit.Assert.assertEquals;
import static se.mockachino.Mockachino.*;

public class TestStates {

	private enum States {
		INITIAL,
		ONE,
		TWO
	}

	@Test
	public void simpleStateTransition() {

		StateMachine<States> machine = new StateMachine<States>(States.class, States.INITIAL);

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
		StateMachine<States> machine = new StateMachine<States>(States.class, States.INITIAL);

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

}
