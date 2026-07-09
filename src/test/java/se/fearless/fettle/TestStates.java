package se.fearless.fettle;

import org.junit.jupiter.api.Test;
import se.fearless.fettle.export.DotExporter;
import se.fearless.fettle.impl.AbstractTransitionModel;
import se.fearless.fettle.impl.MutableTransitionModelImpl;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class TestStates {

	@Test
	public void simpleStateTransition() {
		MutableTransitionModelImpl<States, String, Void> model = MutableTransitionModelImpl.create(States.class, String.class);

		model.addTransition(States.INITIAL, States.ONE, "hej", BasicConditions.always(), Collections.emptyList());
		model.addTransition(States.ONE, States.TWO, "hopp", BasicConditions.always(), Collections.emptyList());

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

		model.addTransition(States.INITIAL, States.ONE, "", BasicConditions.always(), Collections.emptyList());
		model.addTransition(States.ONE, States.TWO, "", BasicConditions.always(), Collections.emptyList());
		Action<States, String, Void> entryAction = mock(Action.class);
		model.addEntryAction(States.ONE, entryAction);
		Action<States, String, Void> exitAction = mock(Action.class);
		model.addExitAction(States.ONE, exitAction);

		StateMachine<States, String, Void> machine = model.newStateMachine(States.INITIAL);

		machine.fireEvent("foo");

		verify(entryAction, never()).onTransition(any(States.class), any(States.class), any(String.class), isNull(), eq(machine));
		verify(exitAction, never()).onTransition(any(States.class), any(States.class), any(String.class), isNull(), eq(machine));

		machine.fireEvent("");

		verify(entryAction).onTransition(any(States.class), any(States.class), any(String.class), isNull(), eq(machine));
		verify(exitAction, never()).onTransition(any(States.class), any(States.class), any(String.class), isNull(), eq(machine));

		machine.fireEvent("");

		verify(entryAction).onTransition(any(States.class), any(States.class), any(String.class), isNull(), eq(machine));
		verify(exitAction).onTransition(any(States.class), any(States.class), any(String.class), isNull(), eq(machine));
	}

	@Test
	public void simpleStateTransitionUsingNonEnums() {
		TestState initial = new TestState();
		TestState one = new TestState();
		TestState two = new TestState();

		MutableTransitionModelImpl<TestState, String, Void> model = MutableTransitionModelImpl.create(TestState.class, String.class);

		model.addTransition(initial, one, "", BasicConditions.always(), Collections.emptyList());
		model.addTransition(one, two, "", BasicConditions.always(), Collections.emptyList());
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

		model.addTransition(States.INITIAL, States.ONE, "hej", BasicConditions.always(), Collections.emptyList());
		model.addTransition(States.ONE, States.TWO, "hopp", BasicConditions.always(), Collections.emptyList());
		model.addTransition(States.TWO, States.INITIAL, "hej", BasicConditions.always(), Collections.emptyList());

		Action<States, String, Void> entryAction1 = mock(Action.class);
		model.addEntryAction(States.INITIAL, entryAction1);
		Action<States, String, Void> exitAction1 = mock(Action.class);
		model.addExitAction(States.INITIAL, exitAction1);

		Action<States, String, Void> entryAction2 = mock(Action.class);
		model.addEntryAction(States.TWO, entryAction2);
		Action<States, String, Void> exitAction2 = mock(Action.class);
		model.addExitAction(States.TWO, exitAction2);

		StateMachine<States, String, Void> machine = model.newStateMachine(States.INITIAL);

		boolean switched = machine.forceSetState(States.TWO);
		assertTrue(switched);
		assertEquals(States.TWO, machine.getCurrentState());
		verify(exitAction1).onTransition(any(States.class), any(States.class), nullable(String.class), isNull(), eq(machine));
		verify(entryAction2).onTransition(any(States.class), any(States.class), nullable(String.class), isNull(), eq(machine));

		switched = machine.forceSetState(States.TWO);
		assertFalse(switched);
	}

	@Test
	public void fromAllTransition() {
		MutableTransitionModel<States, String, Void> model = MutableTransitionModelImpl.create(States.class, String.class);

		model.addTransition(States.INITIAL, States.ONE, "hej", BasicConditions.always(), Collections.emptyList());
		model.addTransition(States.ONE, States.TWO, "hopp", BasicConditions.always(), Collections.emptyList());
		model.addFromAllTransition(States.INITIAL, "back", BasicConditions.always(), Collections.emptyList());

		StateMachine<States, String, Void> machine = model.newStateMachine(States.INITIAL);

		machine.fireEvent("hej");
		machine.fireEvent("hopp");
		machine.fireEvent("back");

		assertEquals(States.INITIAL, machine.getCurrentState());
	}

	@Test
	public void singleFiresBeforeFromAllTransition() {
		MutableTransitionModel<States, String, Void> model = MutableTransitionModelImpl.create(States.class, String.class);

		model.addTransition(States.INITIAL, States.ONE, "hej", BasicConditions.always(), Collections.emptyList());
		model.addTransition(States.ONE, States.TWO, "hopp", BasicConditions.always(), Collections.emptyList());
		model.addFromAllTransition(States.INITIAL, "hopp", BasicConditions.always(), Collections.emptyList());

		StateMachine<States, String, Void> machine = model.newStateMachine(States.INITIAL);

		machine.fireEvent("hej");
		machine.fireEvent("hopp");

		assertEquals(States.TWO, machine.getCurrentState());
		DotExporter<States, String, Void> exporter = new DotExporter<>((AbstractTransitionModel<States, String, Void>) model, "test");
		exporter.asDot(System.out, true);
	}

	@Test
	public void addMultipleTransitionsFromOneState() {
		MutableTransitionModel<States, String, Arguments> mutableTransitionModel = MutableTransitionModelImpl.create(States.class, String.class);

		Condition<Arguments> toOne = mock(Condition.class);
		Condition<Arguments> toTwo = mock(Condition.class);
		List<Action<States, String, Arguments>> actions = Collections.emptyList();
		mutableTransitionModel.addTransition(States.INITIAL, States.ONE, "tick", toOne, actions);
		mutableTransitionModel.addTransition(States.INITIAL, States.TWO, "tick", toTwo, actions);

		StateMachine<States, String, Arguments> stateMachine = mutableTransitionModel.newStateMachine(States.INITIAL);

		when(toTwo.isSatisfied(Arguments.NO_ARGS)).thenReturn(true);
		stateMachine.fireEvent("tick", Arguments.NO_ARGS);
		assertEquals(States.TWO, stateMachine.getCurrentState());
	}

	@Test
	public void addMultipleTransitionsFromAllStates() {
		MutableTransitionModel<States, String, Arguments> mutableTransitionModel = MutableTransitionModelImpl.create(States.class, String.class);

		Condition<Arguments> toOne = mock(Condition.class);
		Condition<Arguments> toTwo = mock(Condition.class);
		List<Action<States, String, Arguments>> actions = Collections.emptyList();
		mutableTransitionModel.addFromAllTransition(States.ONE, "tick", toOne, actions);
		mutableTransitionModel.addFromAllTransition(States.TWO, "tick", toTwo, actions);

		StateMachine<States, String, Arguments> stateMachine = mutableTransitionModel.newStateMachine(States.INITIAL);

		when(toTwo.isSatisfied(Arguments.NO_ARGS)).thenReturn(true);
		stateMachine.fireEvent("tick", Arguments.NO_ARGS);
		assertEquals(States.TWO, stateMachine.getCurrentState());
	}


	private static class TestState {
	}
}
