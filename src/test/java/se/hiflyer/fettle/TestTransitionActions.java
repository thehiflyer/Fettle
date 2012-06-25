package se.hiflyer.fettle;

import com.google.common.collect.Lists;
import com.googlecode.gentyref.TypeToken;
import org.junit.Test;
import se.hiflyer.fettle.builder.StateMachineBuilder;
import se.hiflyer.fettle.impl.MutableTransitionModelImpl;
import se.hiflyer.fettle.impl.Transition;
import se.mockachino.order.OrderingContext;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static se.mockachino.Mockachino.mock;
import static se.mockachino.Mockachino.newOrdering;
import static se.mockachino.Mockachino.verifyNever;
import static se.mockachino.Mockachino.verifyOnce;
import static se.mockachino.matchers.Matchers.any;

public class TestTransitionActions {

	public static final TypeToken<Action<States,String, Void>> ACTION_TYPE_TOKEN = new TypeToken<Action<States, String, Void>>() {
	};


	@Test
	public void testTransitionCreation() {
		States to = States.TWO;
		TypeToken<Condition<Void>> conditionTypeToken = new TypeToken<Condition<Void>>() {
		};
		Condition<Void> condition = mock(conditionTypeToken);
		Transition<States, String, Void> transition = new Transition<States, String, Void>(to, condition, Collections.<Action<States, String, Void>>emptyList());

		assertEquals(to, transition.getTo());
		assertEquals(condition, transition.getCondition());
	}

	@Test
	public void actionsAreRunOnTransitions() {
		MutableTransitionModel<States, String, Void> model = MutableTransitionModelImpl.create(States.class, String.class);

		model.addTransition(States.INITIAL, States.ONE, "", BasicConditions.<Void>always(), Collections.<Action<States, String, Void>>emptyList());
		Action<States, String, Void> transitionAction1 = mock(ACTION_TYPE_TOKEN);
		ArrayList<Action<States,String,Void>> actions1 = Lists.newArrayList();
		actions1.add(transitionAction1);
		model.addTransition(States.ONE, States.TWO, "", BasicConditions.<Void>always(), actions1);
		Action<States, String, Void> transitionAction2 = mock(ACTION_TYPE_TOKEN);
		ArrayList<Action<States, String, Void>> actions2 = Lists.newArrayList();
		actions2.add(transitionAction2);
		model.addTransition(States.TWO, States.ONE, "", BasicConditions.<Void>always(), actions2);

		StateMachine<States, String, Void> machine = model.newStateMachine(States.INITIAL);

		machine.fireEvent("");

		verifyNever().on(transitionAction1).onTransition(any(States.class), any(States.class), "", any(Void.class), machine);
		verifyNever().on(transitionAction2).onTransition(any(States.class), any(States.class), "", any(Void.class), machine);

		machine.fireEvent("");

		verifyOnce().on(transitionAction1).onTransition(States.ONE, States.TWO, "", any(Void.class), machine);
		verifyNever().on(transitionAction2).onTransition(any(States.class), any(States.class), any(String.class), any(Void.class), machine);

		machine.fireEvent("foo");

		verifyOnce().on(transitionAction1).onTransition(States.ONE, States.TWO, "", any(Void.class), machine);
		verifyNever().on(transitionAction2).onTransition(any(States.class), any(States.class), any(String.class), any(Void.class), machine);

		machine.fireEvent("");

		verifyOnce().on(transitionAction1).onTransition(States.ONE, States.TWO, "", any(Void.class), machine);
		verifyOnce().on(transitionAction2).onTransition(States.TWO, States.ONE, "", any(Void.class), machine);

	}

	@Test
	public void testArguments() throws Exception {
		MutableTransitionModel<States, String, Arguments> model = MutableTransitionModelImpl.create(States.class, String.class);

		TypeToken<Action<States, String, Arguments>> argumentTypeToken = new TypeToken<Action<States, String, Arguments>>() {
		};

		Action<States, String, Arguments> transitionAction1 = mock(argumentTypeToken);
		ArrayList<Action<States, String, Arguments>> actions1 = Lists.newArrayList();
		actions1.add(transitionAction1);
		model.addTransition(States.INITIAL, States.ONE, "", BasicConditions.<Arguments>always(), actions1);
		Action<States, String, Arguments> transitionAction2 = mock(argumentTypeToken);
		ArrayList<Action<States, String, Arguments>> actions2 = Lists.newArrayList();
		actions2.add(transitionAction2);
		model.addTransition(States.ONE, States.TWO, "", BasicConditions.<Arguments>always(), actions2);

		StateMachine<States, String, Arguments> machine = model.newStateMachine(States.INITIAL);

		Arguments args = new Arguments("arg");
		machine.fireEvent("", args);
		verifyOnce().on(transitionAction1).onTransition(States.INITIAL, States.ONE, "", args, machine);
	}

	@Test
	public void orderOfEntryTransitionAndExitActions() throws Exception {
		StateMachineBuilder<States,String, Void> builder = Fettle.newBuilder(States.class, String.class);
		Action<States, String, Void> action1 = mock(ACTION_TYPE_TOKEN);
		Action<States, String, Void> action2 = mock(ACTION_TYPE_TOKEN);
		Action<States, String, Void> action3 = mock(ACTION_TYPE_TOKEN);
		builder.transition().from(States.INITIAL).to(States.ONE).on("foo").perform(action2);
		builder.onEntry(States.ONE).perform(action3);
		builder.onExit(States.INITIAL).perform(action1);

		StateMachine<States, String, Void> stateMachine = builder.build(States.INITIAL);
		stateMachine.fireEvent("foo");
		OrderingContext order = newOrdering();
		order.verify().on(action1).onTransition(States.INITIAL, States.ONE, "foo", any(Void.class), stateMachine);
		order.verify().on(action2).onTransition(States.INITIAL, States.ONE, "foo", any(Void.class), stateMachine);
		order.verify().on(action3).onTransition(States.INITIAL, States.ONE, "foo", any(Void.class), stateMachine);
	}
}
