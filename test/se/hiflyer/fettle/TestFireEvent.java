package se.hiflyer.fettle;

import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestFireEvent {

	enum Triggers {
		FOO, BAR, BAZ
	}

	@Test
	public void fireEvent() {
		StateMachine<States, Triggers> machine = BasicStateMachine.createStateMachine(States.INITIAL);

		machine.addTransition(States.INITIAL, States.ONE, Triggers.FOO, BasicConditions.ALWAYS, Collections.<Action>emptyList());

		assertFalse(machine.fireEvent(Triggers.BAR));
		assertFalse(machine.fireEvent(Triggers.BAZ));
		assertTrue(machine.fireEvent(Triggers.FOO));
		assertFalse(machine.fireEvent(Triggers.FOO));
	}
}
