package se.hiflyer.fettle;

import org.junit.Test;
import se.hiflyer.fettle.export.DotExporter;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static se.mockachino.Mockachino.*;
import static se.mockachino.matchers.Matchers.any;

public class TestStatesWithTemplate {

	@Test
	public void simpleStateTransition() {
		MutableTransitionModel<States, String> template = MutableTransitionModel.createStateMachineTemplate(States.class);

		template.addTransition(States.INITIAL, States.ONE, "hej", BasicConditions.ALWAYS, Collections.<Action<States, String>>emptyList());

		template.addTransition(States.ONE, States.TWO, "hopp", BasicConditions.ALWAYS, Collections.<Action<States, String>>emptyList());

		StateMachine<States, String> machine = template.createInstance(States.INITIAL);

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
		MutableTransitionModel<States, String> template = MutableTransitionModel.createStateMachineTemplate(States.class);

		template.addTransition(States.INITIAL, States.ONE, "", BasicConditions.ALWAYS, Collections.<Action<States, String>>emptyList());


		template.addTransition(States.ONE, States.TWO, "", BasicConditions.ALWAYS, Collections.<Action<States, String>>emptyList());
		Action<States, String> entryAction = mock(Action.class);
		template.addEntryAction(States.ONE, entryAction);
		Action<States, String> exitAction = mock(Action.class);
		template.addExitAction(States.ONE, exitAction);

		StateMachine<States, String> machine = template.createInstance(States.INITIAL);

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

		MutableTransitionModel<TestState, String> template = MutableTransitionModel.createStateMachineTemplate(TestState.class);

		template.addTransition(initial, one, "", BasicConditions.ALWAYS, Collections.<Action<TestState, String>>emptyList());
		template.addTransition(one, two, "", BasicConditions.ALWAYS, Collections.<Action<TestState, String>>emptyList());

		StateMachine<TestState, String> machine = template.createInstance(initial);

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
		MutableTransitionModel<States, String> template = MutableTransitionModel.createStateMachineTemplate(States.class);

		template.addTransition(States.INITIAL, States.ONE, "hej", BasicConditions.ALWAYS, Collections.<Action<States, String>>emptyList());
		template.addTransition(States.ONE, States.TWO, "hopp", BasicConditions.ALWAYS, Collections.<Action<States, String>>emptyList());
		template.addTransition(States.TWO, States.INITIAL, "hej", BasicConditions.ALWAYS, Collections.<Action<States, String>>emptyList());

		Action<States, String> entryAction1 = mock(Action.class);
		template.addEntryAction(States.INITIAL, entryAction1);
		Action<States, String> exitAction1 = mock(Action.class);
		template.addExitAction(States.INITIAL, exitAction1);

		Action<States, String> entryAction2 = mock(Action.class);
		template.addEntryAction(States.TWO, entryAction2);
		Action<States, String> exitAction2 = mock(Action.class);
		template.addExitAction(States.TWO, exitAction2);

		StateMachine<States, String> machine = template.createInstance(States.INITIAL);

		machine.forceSetState(States.TWO);
		assertEquals(States.TWO, machine.getCurrentState());
		verifyOnce().on(exitAction1).perform(any(States.class), any(States.class), any(String.class), Arguments.NO_ARGS);
		verifyOnce().on(entryAction2).perform(any(States.class), any(States.class), any(String.class), Arguments.NO_ARGS);

	}

	@Test
	public void fromAllTransition() {

		MutableTransitionModel<States, String> template = MutableTransitionModel.createStateMachineTemplate(States.class);

		template.addTransition(States.INITIAL, States.ONE, "hej", BasicConditions.ALWAYS, Collections.<Action<States, String>>emptyList());
		template.addTransition(States.ONE, States.TWO, "hopp", BasicConditions.ALWAYS, Collections.<Action<States, String>>emptyList());
		template.addFromAllTransition(States.INITIAL, "back", BasicConditions.ALWAYS, Collections.<Action<States, String>>emptyList());

		StateMachine<States, String> machine = template.createInstance(States.INITIAL);

		machine.fireEvent("hej");
		machine.fireEvent("hopp");
		machine.fireEvent("back");


		assertEquals(States.INITIAL, machine.getCurrentState());
	}

	@Test
	public void singleFiresBeforeFromAllTransition() {

		MutableTransitionModel<States, String> template = MutableTransitionModel.createStateMachineTemplate(States.class);

		template.addTransition(States.INITIAL, States.ONE, "hej", BasicConditions.ALWAYS, Collections.<Action<States, String>>emptyList());
		template.addTransition(States.ONE, States.TWO, "hopp", BasicConditions.ALWAYS, Collections.<Action<States, String>>emptyList());
		template.addFromAllTransition(States.INITIAL, "hopp", BasicConditions.ALWAYS, Collections.<Action<States, String>>emptyList());


		TemplateBasedStateMachine<States, String> machine = template.createInstance(States.INITIAL);
		machine.fireEvent("hej");
		machine.fireEvent("hopp");

		assertEquals(States.TWO, machine.getCurrentState());

		DotExporter<States, String> exporter = new DotExporter<States, String>(machine, "fromMachine");
		exporter.asDot(System.out, false);
		exporter.asDot(System.out, true);

		DotExporter<States, String> exporter2 = new DotExporter<States, String>(template, "fromTemplate");
		exporter2.asDot(System.out, false);
		exporter2.asDot(System.out, true);

	}


	private class TestState {
	}
}
