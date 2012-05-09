package se.hiflyer.fettle;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static se.mockachino.Mockachino.mock;
import static se.mockachino.Mockachino.stubReturn;

public class XorConditionTest {

	@Test
	public void twoWayXor() throws Exception {
		Condition cond1 = mock(Condition.class);
		Condition cond2 = mock(Condition.class);

		Condition condition = BasicConditions.xor(cond1, cond2);

		assertFalse(condition.isSatisfied(Arguments.NO_ARGS));

		stubReturn(true).on(cond1).isSatisfied(Arguments.NO_ARGS);
		stubReturn(false).on(cond2).isSatisfied(Arguments.NO_ARGS);
		assertTrue(condition.isSatisfied(Arguments.NO_ARGS));

		stubReturn(false).on(cond1).isSatisfied(Arguments.NO_ARGS);
		stubReturn(true).on(cond2).isSatisfied(Arguments.NO_ARGS);
		assertTrue(condition.isSatisfied(Arguments.NO_ARGS));

		stubReturn(true).on(cond1).isSatisfied(Arguments.NO_ARGS);
		stubReturn(true).on(cond2).isSatisfied(Arguments.NO_ARGS);
		assertFalse(condition.isSatisfied(Arguments.NO_ARGS));
	}
}
