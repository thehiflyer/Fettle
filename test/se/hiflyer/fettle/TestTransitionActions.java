package se.hiflyer.fettle;

import com.google.common.collect.Lists;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static se.mockachino.Mockachino.*;

public class TestTransitionActions {

	@Test
	public void testTransitionCreation() {
		States from = States.ONE;
		States to = States.TWO;
		Condition condition = mock(Condition.class);
		String event = "foo";
		Transition<States, String> transition = new Transition<States, String>(from, to, condition, event);

		assertEquals(from, transition.getFrom());
		assertEquals(to, transition.getTo());
		assertEquals(condition, transition.getCondition());
		assertEquals("foo", transition.getEvent());
		assertNotNull(transition.getTransitionActions());
	}

	@Test
	public void actionsAreRunOnTransitions() {
		StateMachine<States, String> machine = StateMachine.createStateMachineOfEnum(States.class, States.INITIAL);

		machine.addTransition(States.INITIAL, States.ONE, "", BasicConditions.ALWAYS);
		Runnable transitionAction1 = mock(Runnable.class);
		machine.addTransition(States.ONE, States.TWO, "", BasicConditions.ALWAYS, Lists.newArrayList(transitionAction1));
		Runnable transitionAction2 = mock(Runnable.class);
		machine.addTransition(States.TWO, States.ONE, "", BasicConditions.ALWAYS, Lists.newArrayList(transitionAction2));


		machine.fireEvent("");

		verifyNever().on(transitionAction1).run();
		verifyNever().on(transitionAction2).run();

		machine.fireEvent("");

		verifyOnce().on(transitionAction1).run();
		verifyNever().on(transitionAction2).run();

		machine.fireEvent("foo");

		verifyOnce().on(transitionAction1).run();
		verifyNever().on(transitionAction2).run();

		machine.fireEvent("");

		verifyOnce().on(transitionAction1).run();
		verifyOnce().on(transitionAction2).run();

	}
}
