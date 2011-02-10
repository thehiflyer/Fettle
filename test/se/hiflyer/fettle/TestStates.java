package se.hiflyer.fettle;

import org.junit.Test;
import se.hiflyer.fettle.builder.StateMachineBuilder;
import se.hiflyer.fettle.export.DotExporter;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static se.mockachino.Mockachino.*;
import static se.mockachino.matchers.Matchers.any;

public class TestStates {

	@Test
	public void simpleStateTransition() {

		StateMachineBuilder<States, String> builder = StateMachineBuilder.create();
		ModifiableStateMachine<States, String> machine = builder.buildModifiable(States.INITIAL);

		machine.addTransition(States.INITIAL, States.ONE, "hej", BasicConditions.ALWAYS, Collections.<Action<States, String>>emptyList());

		machine.addTransition(States.ONE, States.TWO, "hopp", BasicConditions.ALWAYS, Collections.<Action<States, String>>emptyList());

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
		StateMachineBuilder<States, String> builder = StateMachineBuilder.create();
		ModifiableStateMachine<States, String> machine = builder.buildModifiable(States.INITIAL);

		machine.addTransition(States.INITIAL, States.ONE, "", BasicConditions.ALWAYS, Collections.<Action<States, String>>emptyList());


		machine.addTransition(States.ONE, States.TWO, "", BasicConditions.ALWAYS, Collections.<Action<States, String>>emptyList());
		Action<States, String> entryAction = mock(Action.class);
		machine.addEntryAction(States.ONE, entryAction);
		Action<States, String> exitAction = mock(Action.class);
		machine.addExitAction(States.ONE, exitAction);

		machine.fireEvent("foo");

		verifyNever().on(entryAction).perform(any(States.class), any(States.class), any(String.class), Arguments.NO_ARGS);
		verifyNever().on(exitAction).perform(any(States.class), any(States.class), any(String.class), Arguments.NO_ARGS);

		machine.fireEvent("");

		verifyOnce().on(entryAction).perform(any(States.class), any(States.class), any(String.class), Arguments.NO_ARGS);
		verifyNever().on(exitAction).perform(any(States.class), any(States.class), any(String.class), Arguments.NO_ARGS);

		machine.fireEvent("");

		verifyOnce().on(entryAction).perform(any(States.class), any(States.class), any(String.class), Arguments.NO_ARGS);
		verifyOnce().on(exitAction).perform(any(States.class), any(States.class), any(String.class), Arguments.NO_ARGS);
	}

	@Test
	public void simpleStateTransitionUsingNonEnums() {

		TestState initial = new TestState();
		TestState one = new TestState();
		TestState two = new TestState();

		StateMachineBuilder<TestState, String> builder = StateMachineBuilder.create();
		ModifiableStateMachine<TestState, String> machine = builder.buildModifiable(initial);

		machine.addTransition(initial, one, "", BasicConditions.ALWAYS, Collections.<Action<TestState, String>>emptyList());

		machine.addTransition(one, two, "", BasicConditions.ALWAYS, Collections.<Action<TestState, String>>emptyList());

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
		StateMachineBuilder<States, String> builder = StateMachineBuilder.create();
		ModifiableStateMachine<States, String> machine = builder.buildModifiable(States.INITIAL);

		machine.addTransition(States.INITIAL, States.ONE, "hej", BasicConditions.ALWAYS, Collections.<Action<States, String>>emptyList());
		machine.addTransition(States.ONE, States.TWO, "hopp", BasicConditions.ALWAYS, Collections.<Action<States, String>>emptyList());
		machine.addTransition(States.TWO, States.INITIAL, "hej", BasicConditions.ALWAYS, Collections.<Action<States, String>>emptyList());

		Action<States, String> entryAction1 = mock(Action.class);
		machine.addEntryAction(States.INITIAL, entryAction1);
		Action<States, String> exitAction1 = mock(Action.class);
		machine.addExitAction(States.INITIAL, exitAction1);

		Action<States, String> entryAction2 = mock(Action.class);
		machine.addEntryAction(States.TWO, entryAction2);
		Action<States, String> exitAction2 = mock(Action.class);
		machine.addExitAction(States.TWO, exitAction2);

		machine.forceSetState(States.TWO);
		assertEquals(States.TWO, machine.getCurrentState());
		verifyOnce().on(exitAction1).perform(any(States.class), any(States.class), any(String.class), Arguments.NO_ARGS);
		verifyOnce().on(entryAction2).perform(any(States.class), any(States.class), any(String.class), Arguments.NO_ARGS);

	}

	@Test
	public void fromAllTransition() {

		StateMachineBuilder<States, String> builder = StateMachineBuilder.create();
		ModifiableStateMachine<States, String> machine = builder.buildModifiable(States.INITIAL);

		machine.addTransition(States.INITIAL, States.ONE, "hej", BasicConditions.ALWAYS, Collections.<Action<States, String>>emptyList());
		machine.addTransition(States.ONE, States.TWO, "hopp", BasicConditions.ALWAYS, Collections.<Action<States, String>>emptyList());
		machine.addFromAllTransition(States.INITIAL, "back", BasicConditions.ALWAYS, Collections.<Action<States, String>>emptyList());


		machine.fireEvent("hej");
		machine.fireEvent("hopp");
		machine.fireEvent("back");


		assertEquals(States.INITIAL, machine.getCurrentState());

	}

	@Test
	public void singleFiresBeforeFromAllTransition() {

		StateMachineBuilder<States, String> builder = StateMachineBuilder.create();
		ModifiableStateMachine<States, String> machine = builder.buildModifiable(States.INITIAL);

		machine.addTransition(States.INITIAL, States.ONE, "hej", BasicConditions.ALWAYS, Collections.<Action<States, String>>emptyList());
		machine.addTransition(States.ONE, States.TWO, "hopp", BasicConditions.ALWAYS, Collections.<Action<States, String>>emptyList());
		machine.addFromAllTransition(States.INITIAL, "hopp", BasicConditions.ALWAYS, Collections.<Action<States, String>>emptyList());


		machine.fireEvent("hej");
		machine.fireEvent("hopp");

		assertEquals(States.TWO, machine.getCurrentState());
	}



	private class TestState {
	}
}
