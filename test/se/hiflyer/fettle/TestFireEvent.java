package se.hiflyer.fettle;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestFireEvent {

	enum Triggers {
		FOO, BAR, BAZ
	}

	@Test
	public void fireEvent() {
		StateMachine<States, Triggers> machine = BasicStateMachine.createStateMachineOfEnum(States.class, States.INITIAL);

		machine.addTransition(States.INITIAL, States.ONE, Triggers.FOO);

		assertFalse(machine.fireEvent(Triggers.BAR));
		assertFalse(machine.fireEvent(Triggers.BAZ));
		assertTrue(machine.fireEvent(Triggers.FOO));
		assertFalse(machine.fireEvent(Triggers.FOO));
	}
}
