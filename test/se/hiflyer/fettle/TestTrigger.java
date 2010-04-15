package se.hiflyer.fettle;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestTrigger {

	private enum States {
		INITIAL,
		ONE,
		TWO
	}

	@Test
	public void trigger() {
		StateMachine<States> machine = StateMachine.createStateMachineOfEnum(States.class, States.INITIAL);

		Trigger trigger = new Trigger<States>(machine);

		Transition<States> initToOne = new BasicTransition<States>(States.INITIAL, States.ONE, trigger);
		machine.addTransition(initToOne);

		assertEquals(States.INITIAL, machine.getCurrentState());
		trigger.pull();
		
		assertEquals(States.ONE, machine.getCurrentState());

	}
}
