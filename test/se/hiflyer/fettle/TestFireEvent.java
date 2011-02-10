package se.hiflyer.fettle;

import org.junit.Test;
import se.hiflyer.fettle.builder.StateMachineBuilder;

import java.util.Collections;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestFireEvent {

	enum Triggers {
		FOO, BAR, BAZ
	}

	@Test
	public void fireEvent() {
		StateMachineBuilder<States, Triggers> builder = StateMachineBuilder.create();
		ModifiableStateMachine<States, Triggers> machine = builder.buildModifiable(States.INITIAL);

		machine.addTransition(States.INITIAL, States.ONE, Triggers.FOO, BasicConditions.ALWAYS, Collections.<Action<States, Triggers>>emptyList());

		assertFalse(machine.fireEvent(Triggers.BAR));
		assertFalse(machine.fireEvent(Triggers.BAZ));
		assertTrue(machine.fireEvent(Triggers.FOO));
		assertFalse(machine.fireEvent(Triggers.FOO));
	}

	@Test
	public void testFireEventWithParams() throws Exception {
		StateMachineBuilder<States, Triggers> builder = StateMachineBuilder.create();
		ModifiableStateMachine<States, Triggers> machine = builder.buildModifiable(States.INITIAL);

		machine.addTransition(States.INITIAL, States.ONE, Triggers.FOO, new Condition() {
			@Override
			public boolean isSatisfied(Arguments args) {
				if (args.getNumberOfArguments() < 1) {
					return false;
				}
				String input = (String) args.getFirst();
				return "hej".equals(input);
			}
		}, Collections.<Action<States, Triggers>>emptyList());

		assertFalse(machine.fireEvent(Triggers.FOO));
		assertFalse(machine.fireEvent(Triggers.FOO, new Arguments("foo")));
		assertTrue(machine.fireEvent(Triggers.FOO, new Arguments("hej")));

	}
}
