package se.hiflyer.fettle;

import org.junit.Test;
import se.hiflyer.fettle.builder.StateMachineBuilder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FettleTest {
	@Test
	public void useOfUtil() {
		MutableTransitionModel<States, String, Void> transitionModel = Fettle.newTransitionModel(States.class, String.class);
		assertNotNull(transitionModel);

		StateMachineBuilder<States, String, Void> builder = Fettle.newBuilder(States.class, String.class);
		assertNotNull(builder);
		StateMachine<States, String, Void> machine = builder.build(States.TWO);
		assertEquals(States.TWO, machine.getCurrentState());
	}
}
