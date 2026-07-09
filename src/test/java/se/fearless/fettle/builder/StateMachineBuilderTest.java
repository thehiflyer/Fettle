package se.fearless.fettle.builder;

import org.junit.jupiter.api.Test;
import se.fearless.fettle.Action;
import se.fearless.fettle.Arguments;
import se.fearless.fettle.Condition;
import se.fearless.fettle.StateMachine;
import se.fearless.fettle.StateMachineTemplate;
import se.fearless.fettle.States;
import se.fearless.fettle.export.DotExporter;
import se.fearless.fettle.impl.AbstractTransitionModel;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@SuppressWarnings("unchecked")
public class StateMachineBuilderTest {

	@Test
	public void testBuilder() {
		StateMachineBuilder<States, String, Void> builder = StateMachineBuilder.create(States.class, String.class);

		builder.transition().from(States.INITIAL).to(States.ONE).on("hej");
		builder.transition().from(States.ONE).to(States.TWO).on("hopp");

		StateMachineTemplate<States, String, Void> stateMachineTemplate = builder.buildTransitionModel();
		StateMachine<States, String, Void> machine = stateMachineTemplate.newStateMachine(States.INITIAL);

		assertEquals(States.INITIAL, machine.getCurrentState());
		machine.fireEvent("hej");
		assertEquals(States.ONE, machine.getCurrentState());
		machine.fireEvent("hej");
		assertEquals(States.ONE, machine.getCurrentState());
		machine.fireEvent("hopp");
		assertEquals(States.TWO, machine.getCurrentState());
		DotExporter<States, String, Void> exporter = new DotExporter<>((AbstractTransitionModel<States, String, Void>) stateMachineTemplate, "test");
		exporter.asDot(System.out, true);
	}

	@Test
	public void fromAllTransition() {
		StateMachineBuilder<States, String, Void> builder = StateMachineBuilder.create(States.class, String.class);

		builder.transition().from(States.INITIAL).to(States.ONE).on("hej");
		builder.transition().from(States.ONE).to(States.TWO).on("hopp");
		builder.transition().fromAll().to(States.INITIAL).on("back");

		StateMachine<States, String, Void> machine = builder.build(States.INITIAL);

		machine.fireEvent("hej");
		machine.fireEvent("hopp");
		machine.fireEvent("back");

		assertEquals(States.INITIAL, machine.getCurrentState());
	}

	@Test
	public void entryExitActions() {
		StateMachineBuilder<States, String, Void> builder = StateMachineBuilder.create(States.class, String.class);

		builder.transition().from(States.INITIAL).to(States.ONE).on("");
		builder.transition().from(States.ONE).to(States.TWO).on("");
		Action<States, String, Void> entryAction = mock(Action.class);
		builder.onEntry(States.ONE).perform(entryAction);

		Action<States, String, Void> exitAction1 = mock(Action.class);
		Action<States, String, Void> exitAction2 = mock(Action.class);
		List<Action<States, String, Void>> exitActions = new ArrayList<>();
		exitActions.add(exitAction1);
		exitActions.add(exitAction2);
		builder.onExit(States.ONE).perform(exitActions);

		StateMachine<States, String, Void> machine = builder.build(States.INITIAL);

		machine.fireEvent("foo");

		verify(entryAction, never()).onTransition(any(States.class), any(States.class), any(String.class), isNull(), eq(machine));
		verify(exitAction1, never()).onTransition(any(States.class), any(States.class), any(String.class), isNull(), eq(machine));
		verify(exitAction2, never()).onTransition(any(States.class), any(States.class), any(String.class), isNull(), eq(machine));

		machine.fireEvent("");

		verify(entryAction).onTransition(any(States.class), any(States.class), any(String.class), isNull(), eq(machine));
		verify(exitAction1, never()).onTransition(any(States.class), any(States.class), any(String.class), isNull(), eq(machine));
		verify(exitAction2, never()).onTransition(any(States.class), any(States.class), any(String.class), isNull(), eq(machine));

		machine.fireEvent("");

		verify(entryAction).onTransition(any(States.class), any(States.class), any(String.class), isNull(), eq(machine));
		verify(exitAction1).onTransition(any(States.class), any(States.class), any(String.class), isNull(), eq(machine));
		verify(exitAction2).onTransition(any(States.class), any(States.class), any(String.class), isNull(), eq(machine));
	}

	@Test
	public void when() {
		StateMachineBuilder<States, String, Void> builder = StateMachineBuilder.create(States.class, String.class);

		ConditionImpl condition = new ConditionImpl();
		builder.transition().from(States.INITIAL).to(States.ONE).on("").when(condition);

		StateMachine<States, String, Void> machine = builder.build(States.INITIAL);

		machine.fireEvent("");
		assertEquals(States.INITIAL, machine.getCurrentState());

		condition.pass = true;
		machine.fireEvent("");
		assertEquals(States.ONE, machine.getCurrentState());
	}

	@Test
	public void missingOn() {
		StateMachineBuilder<States, String, Void> builder = StateMachineBuilder.create(States.class, String.class);
		builder.transition().from(States.INITIAL).to(States.ONE);
		IllegalStateException ex = assertThrows(IllegalStateException.class, () -> builder.build(States.INITIAL));
		assertTrue(ex.getMessage().contains(States.INITIAL.toString()));
		assertTrue(ex.getMessage().contains(States.ONE.toString()));
	}

	@Test
	public void missingOnFromAll() {
		StateMachineBuilder<States, String, Void> builder = StateMachineBuilder.create(States.class, String.class);
		builder.transition().fromAll().to(States.ONE);
		IllegalStateException ex = assertThrows(IllegalStateException.class, () -> builder.build(States.INITIAL));
		assertTrue(ex.getMessage().contains(States.ONE.toString()));
	}

	private static class CheckWasCalledAction implements Action<States, String, Arguments> {
		private int callCount;

		@Override
		public void onTransition(States from, States to, String causedBy, Arguments context, StateMachine<States, String, Arguments> stateMachine) {
			callCount++;
		}

		public int getCallCount() {
			return callCount;
		}
	}

	private static class ConditionImpl implements Condition<Void> {
		boolean pass = false;

		@Override
		public boolean isSatisfied(Void ignored) {
			return pass;
		}
	}

	@Test
	public void perform() {
		StateMachineBuilder<States, String, Void> builder = StateMachineBuilder.create(States.class, String.class);
		Action<States, String, Void> action1 = mock(Action.class);
		Action<States, String, Void> action2 = mock(Action.class);

		builder.transition().from(States.INITIAL).to(States.ONE).on("a").perform(Actions.actions(action1, action2));

		StateMachine<States, String, Void> machine = builder.build(States.INITIAL);

		verify(action1, never()).onTransition(any(States.class), any(States.class), any(String.class), isNull(), eq(machine));
		verify(action2, never()).onTransition(any(States.class), any(States.class), any(String.class), isNull(), eq(machine));
		machine.fireEvent("");

		verify(action1, never()).onTransition(any(States.class), any(States.class), any(String.class), isNull(), eq(machine));
		verify(action2, never()).onTransition(any(States.class), any(States.class), any(String.class), isNull(), eq(machine));

		machine.fireEvent("a");

		verify(action1).onTransition(eq(States.INITIAL), eq(States.ONE), eq("a"), isNull(), eq(machine));
		verify(action2).onTransition(eq(States.INITIAL), eq(States.ONE), eq("a"), isNull(), eq(machine));
	}

	@Test
	public void multiplePerformsInARowForEntryActions() {
		StateMachineBuilder<States, String, Void> builder = StateMachineBuilder.create(States.class, String.class);
		Action<States, String, Void> action1 = mock(Action.class);
		Action<States, String, Void> action2 = mock(Action.class);

		builder.onEntry(States.ONE).perform(action1).perform(action2);
		builder.transition().from(States.INITIAL).to(States.ONE).on("a");

		StateMachine<States, String, Void> machine = builder.build(States.INITIAL);

		machine.fireEvent("a");

		verify(action1).onTransition(eq(States.INITIAL), eq(States.ONE), eq("a"), isNull(), eq(machine));
		verify(action2).onTransition(eq(States.INITIAL), eq(States.ONE), eq("a"), isNull(), eq(machine));
	}

	@Test
	public void nullInitialState() {
		StateMachineBuilder<States, String, Void> builder = StateMachineBuilder.create(States.class, String.class);
		assertThrows(IllegalArgumentException.class, () -> builder.build(null));
	}

	@Test
	public void specifyDefaultContext() {
		StateMachineBuilder<States, String, Arguments> builder = StateMachineBuilder.create(States.class, String.class);
		builder.defaultContext(Arguments.NO_ARGS);
		CheckWasCalledAction wasCalledAction = new CheckWasCalledAction();
		Action<States, String, Arguments> action = (from, to, causedBy, context, stateMachine) -> assertEquals(Arguments.NO_ARGS, context);
		List<Action<States, String, Arguments>> actions = new ArrayList<>();
		actions.add(action);
		actions.add(wasCalledAction);
		builder.transition().from(States.INITIAL).to(States.ONE).on("foo").when(context -> {
			assertEquals(Arguments.NO_ARGS, context);
			return true;
		}).perform(actions);
		StateMachine<States, String, Arguments> stateMachine = builder.build(States.INITIAL);
		stateMachine.fireEvent("foo");

		assertEquals(1, wasCalledAction.getCallCount());
	}
}
