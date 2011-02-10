package se.hiflyer.fettle;

import org.junit.Test;

public class FettleTest {
	@Test
	public void useOfUtil() {
		MutableTransitionModel<States, String> transitionModel = Fettle.newTransitionModel(States.class);
	}
}
