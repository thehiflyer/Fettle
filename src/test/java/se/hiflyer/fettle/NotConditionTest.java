package se.hiflyer.fettle;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static se.mockachino.Mockachino.mock;
import static se.mockachino.Mockachino.stubReturn;

public class NotConditionTest {

	@Test
	public void simpleNot() throws Exception {
		Condition cond = mock(Condition.class);

		Condition condition = BasicConditions.not(cond);
		assertTrue(condition.isSatisfied(Arguments.NO_ARGS));

		stubReturn(true).on(cond).isSatisfied(Arguments.NO_ARGS);

		assertFalse(condition.isSatisfied(Arguments.NO_ARGS));

	}
}
