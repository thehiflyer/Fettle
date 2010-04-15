package se.hiflyer.fettle;

import org.junit.Test;

import static se.mockachino.Mockachino.*;

public class TestTransitionActions {

	@Test
	public void actionsAreRunOnTransitions() {
		StateMachine<States> machine = StateMachine.createStateMachineOfEnum(States.class, States.INITIAL);

		Condition condition1 = mock(Condition.class);
		stubReturn(false).on(condition1).isSatisfied();

		Transition<States> initToOne = new BasicTransition<States>(States.INITIAL, States.ONE, condition1);
		machine.addTransition(initToOne);

		Transition<States> oneToTwo = new BasicTransition<States>(States.ONE, States.TWO, condition1);
		machine.addTransition(oneToTwo);
		Transition<States> twoToOne = new BasicTransition<States>(States.TWO, States.ONE, condition1);
		machine.addTransition(twoToOne);

		Runnable transitionAction1 = mock(Runnable.class);
		machine.addTransitionAction(initToOne, transitionAction1);

		Runnable transitionAction2 = mock(Runnable.class);
		machine.addTransitionAction(twoToOne, transitionAction2);

		machine.update();

		verifyNever().on(transitionAction1).run();
		verifyNever().on(transitionAction2).run();

		stubReturn(true).on(condition1).isSatisfied();

		machine.update();

		verifyOnce().on(transitionAction1).run();
		verifyNever().on(transitionAction2).run();


		machine.update();

		verifyOnce().on(transitionAction1).run();
		verifyNever().on(transitionAction2).run();

		machine.update();

		verifyOnce().on(transitionAction1).run();
		verifyOnce().on(transitionAction2).run();

	}
}
