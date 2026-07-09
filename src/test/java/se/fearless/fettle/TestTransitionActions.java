package se.fearless.fettle;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import se.fearless.fettle.builder.StateMachineBuilder;
import se.fearless.fettle.impl.BasicTransition;
import se.fearless.fettle.impl.MutableTransitionModelImpl;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@SuppressWarnings("unchecked")
public class TestTransitionActions {

	@Test
	public void testTransitionCreation() {
		States to = States.TWO;
		Condition<Void> condition = mock(Condition.class);
		BasicTransition<States, String, Void> transition = new BasicTransition<>(to, condition, Collections.emptyList());

		assertEquals(to, transition.getTo());
		assertEquals(condition, transition.getCondition());
	}

	@Test
	public void actionsAreRunOnTransitions() {
		MutableTransitionModel<States, String, Void> model = MutableTransitionModelImpl.create(States.class, String.class);

		model.addTransition(States.INITIAL, States.ONE, "", BasicConditions.always(), Collections.emptyList());
		Action<States, String, Void> transitionAction1 = mock(Action.class);
		ArrayList<Action<States, String, Void>> actions1 = new ArrayList<>();
		actions1.add(transitionAction1);
		model.addTransition(States.ONE, States.TWO, "", BasicConditions.always(), actions1);
		Action<States, String, Void> transitionAction2 = mock(Action.class);
		ArrayList<Action<States, String, Void>> actions2 = new ArrayList<>();
		actions2.add(transitionAction2);
		model.addTransition(States.TWO, States.ONE, "", BasicConditions.always(), actions2);

		StateMachine<States, String, Void> machine = model.newStateMachine(States.INITIAL);

		machine.fireEvent("");

		verify(transitionAction1, never()).onTransition(any(States.class), any(States.class), eq(""), isNull(), eq(machine));
		verify(transitionAction2, never()).onTransition(any(States.class), any(States.class), eq(""), isNull(), eq(machine));

		machine.fireEvent("");

		verify(transitionAction1).onTransition(eq(States.ONE), eq(States.TWO), eq(""), isNull(), eq(machine));
		verify(transitionAction2, never()).onTransition(any(States.class), any(States.class), any(String.class), isNull(), eq(machine));

		machine.fireEvent("foo");

		verify(transitionAction1).onTransition(eq(States.ONE), eq(States.TWO), eq(""), isNull(), eq(machine));
		verify(transitionAction2, never()).onTransition(any(States.class), any(States.class), any(String.class), isNull(), eq(machine));

		machine.fireEvent("");

		verify(transitionAction1).onTransition(eq(States.ONE), eq(States.TWO), eq(""), isNull(), eq(machine));
		verify(transitionAction2).onTransition(eq(States.TWO), eq(States.ONE), eq(""), isNull(), eq(machine));
	}

	@Test
	public void testArguments() {
		MutableTransitionModel<States, String, Arguments> model = MutableTransitionModelImpl.create(States.class, String.class);

		Action<States, String, Arguments> transitionAction1 = mock(Action.class);
		ArrayList<Action<States, String, Arguments>> actions1 = new ArrayList<>();
		actions1.add(transitionAction1);
		model.addTransition(States.INITIAL, States.ONE, "", BasicConditions.always(), actions1);
		Action<States, String, Arguments> transitionAction2 = mock(Action.class);
		ArrayList<Action<States, String, Arguments>> actions2 = new ArrayList<>();
		actions2.add(transitionAction2);
		model.addTransition(States.ONE, States.TWO, "", BasicConditions.always(), actions2);

		StateMachine<States, String, Arguments> machine = model.newStateMachine(States.INITIAL);

		Arguments args = new Arguments("arg");
		machine.fireEvent("", args);
		verify(transitionAction1).onTransition(eq(States.INITIAL), eq(States.ONE), eq(""), eq(args), eq(machine));
	}

	@Test
	public void orderOfEntryTransitionAndExitActions() {
		StateMachineBuilder<States, String, Void> builder = Fettle.newBuilder(States.class, String.class);
		Action<States, String, Void> action1 = mock(Action.class);
		Action<States, String, Void> action2 = mock(Action.class);
		Action<States, String, Void> action3 = mock(Action.class);
		builder.transition().from(States.INITIAL).to(States.ONE).on("foo").perform(action2);
		builder.onEntry(States.ONE).perform(action3);
		builder.onExit(States.INITIAL).perform(action1);

		StateMachine<States, String, Void> stateMachine = builder.build(States.INITIAL);
		stateMachine.fireEvent("foo");

		InOrder order = inOrder(action1, action2, action3);
		order.verify(action1).onTransition(eq(States.INITIAL), eq(States.ONE), eq("foo"), isNull(), eq(stateMachine));
		order.verify(action2).onTransition(eq(States.INITIAL), eq(States.ONE), eq("foo"), isNull(), eq(stateMachine));
		order.verify(action3).onTransition(eq(States.INITIAL), eq(States.ONE), eq("foo"), isNull(), eq(stateMachine));
	}
}
