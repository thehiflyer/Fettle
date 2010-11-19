package se.hiflyer.fettle;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static se.mockachino.Mockachino.*;
import static se.mockachino.matchers.Matchers.any;

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
		ModifiableStateMachine<States, String> machine = BasicStateMachine.createStateMachine(States.INITIAL);

		machine.addTransition(States.INITIAL, States.ONE, "", BasicConditions.ALWAYS, Collections.<Action<States, String>>emptyList());
		Action<States, String> transitionAction1 = mock(Action.class);
		machine.addTransition(States.ONE, States.TWO, "", BasicConditions.ALWAYS, Lists.newArrayList(transitionAction1));
		Action<States, String> transitionAction2 = mock(Action.class);
		machine.addTransition(States.TWO, States.ONE, "", BasicConditions.ALWAYS, Lists.newArrayList(transitionAction2));


		machine.fireEvent("");

		verifyNever().on(transitionAction1).perform(any(States.class), any(States.class), "", Arguments.NO_ARGS);
		verifyNever().on(transitionAction2).perform(any(States.class), any(States.class), "", Arguments.NO_ARGS);

		machine.fireEvent("");

		verifyOnce().on(transitionAction1).perform(States.ONE, States.TWO, "", Arguments.NO_ARGS);
		verifyNever().on(transitionAction2).perform(any(States.class), any(States.class), any(String.class), Arguments.NO_ARGS);

		machine.fireEvent("foo");

		verifyOnce().on(transitionAction1).perform(States.ONE, States.TWO, "", Arguments.NO_ARGS);
		verifyNever().on(transitionAction2).perform(any(States.class), any(States.class), any(String.class), Arguments.NO_ARGS);;

		machine.fireEvent("");

		verifyOnce().on(transitionAction1).perform(States.ONE, States.TWO, "", Arguments.NO_ARGS);
		verifyOnce().on(transitionAction2).perform(States.TWO, States.ONE, "", Arguments.NO_ARGS);

	}

	@Test
	public void testArguments() throws Exception {
		ModifiableStateMachine<States, String> machine = BasicStateMachine.createStateMachine(States.INITIAL);

		Action<States, String> transitionAction1 = mock(Action.class);
		machine.addTransition(States.INITIAL, States.ONE, "", BasicConditions.ALWAYS, Lists.newArrayList(transitionAction1));
		Action<States, String> transitionAction2 = mock(Action.class);
		machine.addTransition(States.ONE, States.TWO, "", BasicConditions.ALWAYS, Lists.newArrayList(transitionAction2));

		Arguments args = new Arguments("arg");
		machine.fireEvent("", args);
		verifyOnce().on(transitionAction1).perform(States.INITIAL, States.ONE, "", args);
	}
}
