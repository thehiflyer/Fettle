package se.hiflyer.fettle;

import com.google.common.collect.Lists;
import org.junit.Test;
import se.hiflyer.fettle.builder.StateMachineBuilder;
import se.hiflyer.fettle.impl.MutableTransitionModelImpl;
import se.hiflyer.fettle.impl.Transition;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static se.mockachino.Mockachino.*;
import static se.mockachino.matchers.Matchers.any;

public class TestTransitionActions {

	@Test
	public void testTransitionCreation() {
		States to = States.TWO;
		Condition condition = mock(Condition.class);
		Transition<States, String> transition = new Transition<States, String>(to, condition, Collections.<Action<States,String>>emptyList());

		assertEquals(to, transition.getTo());
		assertEquals(condition, transition.getCondition());
	}

	@Test
	public void actionsAreRunOnTransitions() {
		MutableTransitionModel<States, String> model = MutableTransitionModelImpl.create(States.class, String.class);

		model.addTransition(States.INITIAL, States.ONE, "", BasicConditions.ALWAYS, Collections.<Action<States, String>>emptyList());
		Action<States, String> transitionAction1 = mock(Action.class);
		model.addTransition(States.ONE, States.TWO, "", BasicConditions.ALWAYS, Lists.newArrayList(transitionAction1));
		Action<States, String> transitionAction2 = mock(Action.class);
		model.addTransition(States.TWO, States.ONE, "", BasicConditions.ALWAYS, Lists.newArrayList(transitionAction2));

		StateMachine<States, String> machine = model.newStateMachine(States.INITIAL);

		machine.fireEvent("");

		verifyNever().on(transitionAction1).onTransition(any(States.class), any(States.class), "", Arguments.NO_ARGS);
		verifyNever().on(transitionAction2).onTransition(any(States.class), any(States.class), "", Arguments.NO_ARGS);

		machine.fireEvent("");

		verifyOnce().on(transitionAction1).onTransition(States.ONE, States.TWO, "", Arguments.NO_ARGS);
		verifyNever().on(transitionAction2).onTransition(any(States.class), any(States.class), any(String.class), Arguments.NO_ARGS);

		machine.fireEvent("foo");

		verifyOnce().on(transitionAction1).onTransition(States.ONE, States.TWO, "", Arguments.NO_ARGS);
		verifyNever().on(transitionAction2).onTransition(any(States.class), any(States.class), any(String.class), Arguments.NO_ARGS);

		machine.fireEvent("");

		verifyOnce().on(transitionAction1).onTransition(States.ONE, States.TWO, "", Arguments.NO_ARGS);
		verifyOnce().on(transitionAction2).onTransition(States.TWO, States.ONE, "", Arguments.NO_ARGS);

	}

	@Test
	public void testArguments() throws Exception {
		MutableTransitionModel<States, String> model = MutableTransitionModelImpl.create(States.class, String.class);


		Action<States, String> transitionAction1 = mock(Action.class);
		model.addTransition(States.INITIAL, States.ONE, "", BasicConditions.ALWAYS, Lists.newArrayList(transitionAction1));
		Action<States, String> transitionAction2 = mock(Action.class);
		model.addTransition(States.ONE, States.TWO, "", BasicConditions.ALWAYS, Lists.newArrayList(transitionAction2));

		StateMachine<States, String> machine = model.newStateMachine(States.INITIAL);
		
		Arguments args = new Arguments("arg");
		machine.fireEvent("", args);
		verifyOnce().on(transitionAction1).onTransition(States.INITIAL, States.ONE, "", args);
	}
}
