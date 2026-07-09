package se.fearless.fettle;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class XorConditionTest {

	@Test
	public void twoWayXor() {
		Condition cond1 = mock(Condition.class);
		Condition cond2 = mock(Condition.class);

		Condition condition = BasicConditions.xor(cond1, cond2);

		assertFalse(condition.isSatisfied(Arguments.NO_ARGS));

		when(cond1.isSatisfied(Arguments.NO_ARGS)).thenReturn(true);
		when(cond2.isSatisfied(Arguments.NO_ARGS)).thenReturn(false);
		assertTrue(condition.isSatisfied(Arguments.NO_ARGS));

		when(cond1.isSatisfied(Arguments.NO_ARGS)).thenReturn(false);
		when(cond2.isSatisfied(Arguments.NO_ARGS)).thenReturn(true);
		assertTrue(condition.isSatisfied(Arguments.NO_ARGS));

		when(cond1.isSatisfied(Arguments.NO_ARGS)).thenReturn(true);
		when(cond2.isSatisfied(Arguments.NO_ARGS)).thenReturn(true);
		assertFalse(condition.isSatisfied(Arguments.NO_ARGS));
	}
}
