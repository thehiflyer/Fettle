package se.hiflyer.fettle;

import org.junit.Test;
import se.hiflyer.fettle.builder.StateMachineBuilder;
import se.hiflyer.fettle.impl.MutableTransitionModelImpl;

import java.util.Collections;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestFireEvent {

	enum Triggers {
		FOO, BAR, BAZ
	}

	@Test
	public void fireEvent() {
		StateMachineBuilder<States, Triggers> builder = StateMachineBuilder.create(States.class, Triggers.class);
		MutableTransitionModelImpl<States,Triggers> model = builder.buildTransitionModel();

		model.addTransition(States.INITIAL, States.ONE, Triggers.FOO, BasicConditions.ALWAYS, Collections.<Action<States, Triggers>>emptyList());
		StateMachine<States, Triggers> machine = model.newStateMachine(States.INITIAL);

		assertFalse(machine.fireEvent(Triggers.BAR));
		assertFalse(machine.fireEvent(Triggers.BAZ));
		assertTrue(machine.fireEvent(Triggers.FOO));
		assertFalse(machine.fireEvent(Triggers.FOO));
	}

	@Test
	public void testFireEventWithParams() throws Exception {
		StateMachineBuilder<States, Triggers> builder = StateMachineBuilder.create(States.class, Triggers.class);
		MutableTransitionModelImpl<States,Triggers> model = builder.buildTransitionModel();

		model.addTransition(States.INITIAL, States.ONE, Triggers.FOO, new Condition() {
			@Override
			public boolean isSatisfied(Arguments args) {
				if (args.getNumberOfArguments() < 1) {
					return false;
				}
				String input = (String) args.getFirst();
				return "hej".equals(input);
			}
		}, Collections.<Action<States, Triggers>>emptyList());

		StateMachine<States, Triggers> machine = model.newStateMachine(States.INITIAL);
		assertFalse(machine.fireEvent(Triggers.FOO));
		assertFalse(machine.fireEvent(Triggers.FOO, new Arguments("foo")));
		assertTrue(machine.fireEvent(Triggers.FOO, new Arguments("hej")));

	}
}
