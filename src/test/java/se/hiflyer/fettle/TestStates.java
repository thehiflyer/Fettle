package se.hiflyer.fettle;

import com.googlecode.gentyref.TypeToken;
import org.junit.Test;
import se.hiflyer.fettle.export.DotExporter;
import se.hiflyer.fettle.impl.AbstractTransitionModel;
import se.hiflyer.fettle.impl.MutableTransitionModelImpl;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static se.mockachino.Mockachino.mock;
import static se.mockachino.Mockachino.verifyNever;
import static se.mockachino.Mockachino.verifyOnce;
import static se.mockachino.matchers.Matchers.any;

public class TestStates {

	public static final TypeToken<Action<States,String, Void>> ACTION_TYPE_TOKEN = new TypeToken<Action<States, String, Void>>() {
	};

	@Test
	public void simpleStateTransition() {

		MutableTransitionModelImpl<States, String, Void> model = MutableTransitionModelImpl.create(States.class, String.class);

		model.addTransition(States.INITIAL, States.ONE, "hej", BasicConditions.<Void>always(), Collections.<Action<States, String, Void>>emptyList());
		model.addTransition(States.ONE, States.TWO, "hopp", BasicConditions.<Void>always(), Collections.<Action<States, String, Void>>emptyList());

		StateMachine<States, String, Void> machine = model.newStateMachine(States.INITIAL);

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
		MutableTransitionModelImpl<States, String, Void> model = MutableTransitionModelImpl.create(States.class, String.class);


		model.addTransition(States.INITIAL, States.ONE, "", BasicConditions.<Void>always(), Collections.<Action<States, String, Void>>emptyList());
		model.addTransition(States.ONE, States.TWO, "", BasicConditions.<Void>always(), Collections.<Action<States, String, Void>>emptyList());
		Action<States, String, Void> entryAction = mock(ACTION_TYPE_TOKEN);
		model.addEntryAction(States.ONE, entryAction);
		Action<States, String, Void> exitAction = mock(ACTION_TYPE_TOKEN);
		model.addExitAction(States.ONE, exitAction);

		StateMachine<States, String, Void> machine = model.newStateMachine(States.INITIAL);

		machine.fireEvent("foo");

		verifyNever().on(entryAction).onTransition(any(States.class), any(States.class), any(String.class), any(Void.class), machine);
		verifyNever().on(exitAction).onTransition(any(States.class), any(States.class), any(String.class), any(Void.class), machine);

		machine.fireEvent("");

		verifyOnce().on(entryAction).onTransition(any(States.class), any(States.class), any(String.class), any(Void.class), machine);
		verifyNever().on(exitAction).onTransition(any(States.class), any(States.class), any(String.class), any(Void.class), machine);

		machine.fireEvent("");

		verifyOnce().on(entryAction).onTransition(any(States.class), any(States.class), any(String.class), any(Void.class), machine);
		verifyOnce().on(exitAction).onTransition(any(States.class), any(States.class), any(String.class), any(Void.class), machine);
	}

	@Test
	public void simpleStateTransitionUsingNonEnums() {

		TestState initial = new TestState();
		TestState one = new TestState();
		TestState two = new TestState();

		MutableTransitionModelImpl<TestState, String, Void> model = MutableTransitionModelImpl.create(TestState.class, String.class);

		model.addTransition(initial, one, "", BasicConditions.<Void>always(), Collections.<Action<TestState, String, Void>>emptyList());

		model.addTransition(one, two, "", BasicConditions.<Void>always(), Collections.<Action<TestState, String, Void>>emptyList());
		StateMachine<TestState, String, Void> machine = model.newStateMachine(initial);

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
		MutableTransitionModel<States, String, Void> model = MutableTransitionModelImpl.create(States.class, String.class);

		model.addTransition(States.INITIAL, States.ONE, "hej", BasicConditions.<Void>always(), Collections.<Action<States, String, Void>>emptyList());
		model.addTransition(States.ONE, States.TWO, "hopp", BasicConditions.<Void>always(), Collections.<Action<States, String, Void>>emptyList());
		model.addTransition(States.TWO, States.INITIAL, "hej", BasicConditions.<Void>always(), Collections.<Action<States, String, Void>>emptyList());

		Action<States, String, Void> entryAction1 = mock(ACTION_TYPE_TOKEN);
		model.addEntryAction(States.INITIAL, entryAction1);
		Action<States, String, Void> exitAction1 = mock(ACTION_TYPE_TOKEN);
		model.addExitAction(States.INITIAL, exitAction1);

		Action<States, String, Void> entryAction2 = mock(ACTION_TYPE_TOKEN);
		model.addEntryAction(States.TWO, entryAction2);
		Action<States, String, Void> exitAction2 = mock(ACTION_TYPE_TOKEN);
		model.addExitAction(States.TWO, exitAction2);

		StateMachine<States, String, Void> machine = model.newStateMachine(States.INITIAL);

		boolean switched = machine.forceSetState(States.TWO);
		assertTrue(switched);
		assertEquals(States.TWO, machine.getCurrentState());
		verifyOnce().on(exitAction1).onTransition(any(States.class), any(States.class), any(String.class), any(Void.class), machine);
		verifyOnce().on(entryAction2).onTransition(any(States.class), any(States.class), any(String.class), any(Void.class), machine);

		switched = machine.forceSetState(States.TWO);
		assertFalse(switched);
	}

	@Test
	public void fromAllTransition() {

		MutableTransitionModel<States, String, Void> model = MutableTransitionModelImpl.create(States.class, String.class);

		model.addTransition(States.INITIAL, States.ONE, "hej", BasicConditions.<Void>always(), Collections.<Action<States, String, Void>>emptyList());
		model.addTransition(States.ONE, States.TWO, "hopp", BasicConditions.<Void>always(), Collections.<Action<States, String, Void>>emptyList());
		model.addFromAllTransition(States.INITIAL, "back", BasicConditions.<Void>always(), Collections.<Action<States, String, Void>>emptyList());

		StateMachine<States, String, Void> machine = model.newStateMachine(States.INITIAL);

		machine.fireEvent("hej");
		machine.fireEvent("hopp");
		machine.fireEvent("back");

		assertEquals(States.INITIAL, machine.getCurrentState());
	}

	@Test
	public void singleFiresBeforeFromAllTransition() {

		MutableTransitionModel<States, String, Void> model = MutableTransitionModelImpl.create(States.class, String.class);


		model.addTransition(States.INITIAL, States.ONE, "hej", BasicConditions.<Void>always(), Collections.<Action<States, String, Void>>emptyList());
		model.addTransition(States.ONE, States.TWO, "hopp", BasicConditions.<Void>always(), Collections.<Action<States, String, Void>>emptyList());
		model.addFromAllTransition(States.INITIAL, "hopp", BasicConditions.<Void>always(), Collections.<Action<States, String, Void>>emptyList());

		StateMachine<States, String, Void> machine = model.newStateMachine(States.INITIAL);

		machine.fireEvent("hej");
		machine.fireEvent("hopp");

		assertEquals(States.TWO, machine.getCurrentState());
		DotExporter<States, String, Void> exporter = new DotExporter<States, String, Void>((AbstractTransitionModel<States, String, Void>) model, "test");
		exporter.asDot(System.out, true);
	}


	private class TestState {
	}
}
