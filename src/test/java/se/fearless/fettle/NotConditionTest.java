package se.fearless.fettle;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class NotConditionTest {

	@Test
	public void simpleNot() {
		Condition cond = mock(Condition.class);

		Condition condition = BasicConditions.not(cond);
		assertTrue(condition.isSatisfied(Arguments.NO_ARGS));

		when(cond.isSatisfied(Arguments.NO_ARGS)).thenReturn(true);

		assertFalse(condition.isSatisfied(Arguments.NO_ARGS));
	}
}
