package se.hiflyer.fettle;

import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static se.mockachino.Mockachino.*;

public class TestStates {

	@Test
	public void simpleStateTransition() {

		ModifiableStateMachine<States, String> machine = BasicStateMachine.createStateMachine(States.INITIAL);

		machine.addTransition(States.INITIAL, States.ONE, "hej", BasicConditions.ALWAYS, Collections.<Action>emptyList());

		machine.addTransition(States.ONE, States.TWO, "hopp", BasicConditions.ALWAYS, Collections.<Action>emptyList());

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
		ModifiableStateMachine<States, String> machine = BasicStateMachine.createStateMachine(States.INITIAL);

		machine.addTransition(States.INITIAL, States.ONE, "", BasicConditions.ALWAYS, Collections.<Action>emptyList());


		machine.addTransition(States.ONE, States.TWO, "", BasicConditions.ALWAYS, Collections.<Action>emptyList());
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

		ModifiableStateMachine<TestState, String> machine = BasicStateMachine.createStateMachine(initial);

		machine.addTransition(initial, one, "", BasicConditions.ALWAYS, Collections.<Action>emptyList());

		machine.addTransition(one, two, "", BasicConditions.ALWAYS, Collections.<Action>emptyList());

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
		ModifiableStateMachine<States, String> machine = BasicStateMachine.createStateMachine(States.INITIAL);
		machine.addTransition(States.INITIAL, States.ONE, "hej", BasicConditions.ALWAYS, Collections.<Action>emptyList());
		machine.addTransition(States.ONE, States.TWO, "hopp", BasicConditions.ALWAYS, Collections.<Action>emptyList());
		machine.addTransition(States.TWO, States.INITIAL, "hej", BasicConditions.ALWAYS, Collections.<Action>emptyList());

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

	@Test
	public void fromAllTransition() {

		ModifiableStateMachine<States, String> machine = BasicStateMachine.createStateMachine(States.INITIAL);

		machine.addTransition(States.INITIAL, States.ONE, "hej", BasicConditions.ALWAYS, Collections.<Action>emptyList());
		machine.addTransition(States.ONE, States.TWO, "hopp", BasicConditions.ALWAYS, Collections.<Action>emptyList());
		machine.addFromAllTransition(States.INITIAL, "back", BasicConditions.ALWAYS, Collections.<Action>emptyList());


		machine.fireEvent("hej");
		machine.fireEvent("hopp");
		machine.fireEvent("back");


		assertEquals(States.INITIAL, machine.getCurrentState());

	}

	@Test
	public void singleFiresBeforeFromAllTransition() {

		ModifiableStateMachine<States, String> machine = BasicStateMachine.createStateMachine(States.INITIAL);

		machine.addTransition(States.INITIAL, States.ONE, "hej", BasicConditions.ALWAYS, Collections.<Action>emptyList());
		machine.addTransition(States.ONE, States.TWO, "hopp", BasicConditions.ALWAYS, Collections.<Action>emptyList());
		machine.addFromAllTransition(States.INITIAL, "hopp", BasicConditions.ALWAYS, Collections.<Action>emptyList());


		machine.fireEvent("hej");
		machine.fireEvent("hopp");

		assertEquals(States.TWO, machine.getCurrentState());

	}



	private class TestState {
	}
}
