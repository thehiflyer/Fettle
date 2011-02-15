package se.hiflyer.fettle;

import org.junit.Test;
import se.hiflyer.fettle.builder.StateMachineBuilder;
import se.hiflyer.fettle.impl.MutableTransitionModelImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FettleTest {
	@Test
	public void useOfUtil() {
		MutableTransitionModelImpl<States, String> transitionModel = Fettle.newTransitionModel(States.class, String.class);
		assertNotNull(transitionModel);

		StateMachineBuilder<States, String> builder = Fettle.newBuilder(States.class, String.class);
		assertNotNull(builder);
		StateMachine<States, String> machine = builder.build(States.TWO);
		assertEquals(States.TWO, machine.getCurrentState());
	}
}
