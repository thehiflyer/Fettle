package se.hiflyer.fettle;

import com.googlecode.gentyref.TypeToken;
import org.junit.Test;
import se.hiflyer.fettle.export.DotExporter;
import se.hiflyer.fettle.impl.AbstractTransitionModel;
import se.hiflyer.fettle.impl.MutableTransitionModelImpl;

import java.util.Collections;

import static org.junit.Assert.*;
import static se.mockachino.Mockachino.*;
import static se.mockachino.matchers.Matchers.any;

public class TestStates {

	public static final TypeToken<Action<States,String>> ACTION_TYPE_TOKEN = new TypeToken<Action<States, String>>() {
	};

	@Test
	public void simpleStateTransition() {

		MutableTransitionModelImpl<States, String> model = MutableTransitionModelImpl.create(States.class, String.class);

		model.addTransition(States.INITIAL, States.ONE, "hej", BasicConditions.ALWAYS, Collections.<Action<States, String>>emptyList());
		model.addTransition(States.ONE, States.TWO, "hopp", BasicConditions.ALWAYS, Collections.<Action<States, String>>emptyList());

		StateMachine<States, String> machine = model.newStateMachine(States.INITIAL);

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
		MutableTransitionModelImpl<States, String> model = MutableTransitionModelImpl.create(States.class, String.class);


		model.addTransition(States.INITIAL, States.ONE, "", BasicConditions.ALWAYS, Collections.<Action<States, String>>emptyList());
		model.addTransition(States.ONE, States.TWO, "", BasicConditions.ALWAYS, Collections.<Action<States, String>>emptyList());
		Action<States, String> entryAction = mock(ACTION_TYPE_TOKEN);
		model.addEntryAction(States.ONE, entryAction);
		Action<States, String> exitAction = mock(ACTION_TYPE_TOKEN);
		model.addExitAction(States.ONE, exitAction);

		StateMachine<States, String> machine = model.newStateMachine(States.INITIAL);

		machine.fireEvent("foo");

		verifyNever().on(entryAction).onTransition(any(States.class), any(States.class), any(String.class), Arguments.NO_ARGS, machine);
		verifyNever().on(exitAction).onTransition(any(States.class), any(States.class), any(String.class), Arguments.NO_ARGS, machine);

		machine.fireEvent("");

		verifyOnce().on(entryAction).onTransition(any(States.class), any(States.class), any(String.class), Arguments.NO_ARGS, machine);
		verifyNever().on(exitAction).onTransition(any(States.class), any(States.class), any(String.class), Arguments.NO_ARGS, machine);

		machine.fireEvent("");

		verifyOnce().on(entryAction).onTransition(any(States.class), any(States.class), any(String.class), Arguments.NO_ARGS, machine);
		verifyOnce().on(exitAction).onTransition(any(States.class), any(States.class), any(String.class), Arguments.NO_ARGS, machine);
	}

	@Test
	public void simpleStateTransitionUsingNonEnums() {

		TestState initial = new TestState();
		TestState one = new TestState();
		TestState two = new TestState();

		MutableTransitionModelImpl<TestState, String> model = MutableTransitionModelImpl.create(TestState.class, String.class);

		model.addTransition(initial, one, "", BasicConditions.ALWAYS, Collections.<Action<TestState, String>>emptyList());

		model.addTransition(one, two, "", BasicConditions.ALWAYS, Collections.<Action<TestState, String>>emptyList());
		StateMachine<TestState, String> machine = model.newStateMachine(initial);

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
		MutableTransitionModel<States, String> model = MutableTransitionModelImpl.create(States.class, String.class);

		model.addTransition(States.INITIAL, States.ONE, "hej", BasicConditions.ALWAYS, Collections.<Action<States, String>>emptyList());
		model.addTransition(States.ONE, States.TWO, "hopp", BasicConditions.ALWAYS, Collections.<Action<States, String>>emptyList());
		model.addTransition(States.TWO, States.INITIAL, "hej", BasicConditions.ALWAYS, Collections.<Action<States, String>>emptyList());

		Action<States, String> entryAction1 = mock(ACTION_TYPE_TOKEN);
		model.addEntryAction(States.INITIAL, entryAction1);
		Action<States, String> exitAction1 = mock(ACTION_TYPE_TOKEN);
		model.addExitAction(States.INITIAL, exitAction1);

		Action<States, String> entryAction2 = mock(ACTION_TYPE_TOKEN);
		model.addEntryAction(States.TWO, entryAction2);
		Action<States, String> exitAction2 = mock(ACTION_TYPE_TOKEN);
		model.addExitAction(States.TWO, exitAction2);

		StateMachine<States, String> machine = model.newStateMachine(States.INITIAL);

		boolean switched = machine.forceSetState(States.TWO);
		assertTrue(switched);
		assertEquals(States.TWO, machine.getCurrentState());
		verifyOnce().on(exitAction1).onTransition(any(States.class), any(States.class), any(String.class), Arguments.NO_ARGS, machine);
		verifyOnce().on(entryAction2).onTransition(any(States.class), any(States.class), any(String.class), Arguments.NO_ARGS, machine);

		switched = machine.forceSetState(States.TWO);
		assertFalse(switched);
	}

	@Test
	public void fromAllTransition() {

		MutableTransitionModel<States, String> model = MutableTransitionModelImpl.create(States.class, String.class);

		model.addTransition(States.INITIAL, States.ONE, "hej", BasicConditions.ALWAYS, Collections.<Action<States, String>>emptyList());
		model.addTransition(States.ONE, States.TWO, "hopp", BasicConditions.ALWAYS, Collections.<Action<States, String>>emptyList());
		model.addFromAllTransition(States.INITIAL, "back", BasicConditions.ALWAYS, Collections.<Action<States, String>>emptyList());

		StateMachine<States, String> machine = model.newStateMachine(States.INITIAL);

		machine.fireEvent("hej");
		machine.fireEvent("hopp");
		machine.fireEvent("back");

		assertEquals(States.INITIAL, machine.getCurrentState());
	}

	@Test
	public void singleFiresBeforeFromAllTransition() {

		MutableTransitionModel<States, String> model = MutableTransitionModelImpl.create(States.class, String.class);


		model.addTransition(States.INITIAL, States.ONE, "hej", BasicConditions.ALWAYS, Collections.<Action<States, String>>emptyList());
		model.addTransition(States.ONE, States.TWO, "hopp", BasicConditions.ALWAYS, Collections.<Action<States, String>>emptyList());
		model.addFromAllTransition(States.INITIAL, "hopp", BasicConditions.ALWAYS, Collections.<Action<States, String>>emptyList());

		StateMachine<States, String> machine = model.newStateMachine(States.INITIAL);

		machine.fireEvent("hej");
		machine.fireEvent("hopp");

		assertEquals(States.TWO, machine.getCurrentState());
		DotExporter<States, String> exporter = new DotExporter<States, String>((AbstractTransitionModel<States, String>) model, "test");
		exporter.asDot(System.out, true);
	}


	private class TestState {
	}
}
