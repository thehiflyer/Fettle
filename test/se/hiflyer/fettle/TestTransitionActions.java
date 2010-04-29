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
		StateMachine<States, String> machine = BasicStateMachine.createStateMachineOfEnum(States.class, States.INITIAL);

		machine.addTransition(States.INITIAL, States.ONE, "", BasicConditions.ALWAYS);
		Action transitionAction1 = mock(Action.class);
		machine.addTransition(States.ONE, States.TWO, "", BasicConditions.ALWAYS, Lists.newArrayList(transitionAction1));
		Action transitionAction2 = mock(Action.class);
		machine.addTransition(States.TWO, States.ONE, "", BasicConditions.ALWAYS, Lists.newArrayList(transitionAction2));


		machine.fireEvent("");

		verifyNever().on(transitionAction1).perform();
		verifyNever().on(transitionAction2).perform();

		machine.fireEvent("");

		verifyOnce().on(transitionAction1).perform();
		verifyNever().on(transitionAction2).perform();

		machine.fireEvent("foo");

		verifyOnce().on(transitionAction1).perform();
		verifyNever().on(transitionAction2).perform();

		machine.fireEvent("");

		verifyOnce().on(transitionAction1).perform();
		verifyOnce().on(transitionAction2).perform();

	}
}
