package se.fearless.fettle;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class OrConditionTest {

	@Test
	public void twoWayOr() {
		Condition<Void> cond1 = mock(Condition.class);
		Condition<Void> cond2 = mock(Condition.class);

		Condition<Void> condition = BasicConditions.or(cond1, cond2);

		assertFalse(condition.isSatisfied(null));

		when(cond1.isSatisfied(null)).thenReturn(true);
		when(cond2.isSatisfied(null)).thenReturn(false);
		assertTrue(condition.isSatisfied(null));

		when(cond1.isSatisfied(null)).thenReturn(false);
		when(cond2.isSatisfied(null)).thenReturn(true);
		assertTrue(condition.isSatisfied(null));

		when(cond1.isSatisfied(null)).thenReturn(true);
		when(cond2.isSatisfied(null)).thenReturn(true);
		assertTrue(condition.isSatisfied(null));
	}


	@Test
	public void notSatisfied() {
		Condition<Void> cond1 = mock(Condition.class);
		Condition<Void> cond2 = mock(Condition.class);
		Condition<Void> cond3 = mock(Condition.class);
		List<Condition<Void>> conditions = List.of(cond1, cond2, cond3);

		Condition<Void> condition = BasicConditions.or(conditions);

		assertFalse(condition.isSatisfied(null));
	}

	@Test
	public void satisfied() {
		Condition<Void> cond1 = mock(Condition.class);
		Condition<Void> cond2 = mock(Condition.class);
		Condition<Void> cond3 = mock(Condition.class);
		List<Condition<Void>> conditions = List.of(cond1, cond2, cond3);

		Condition<Void> condition = BasicConditions.or(conditions);

		when(cond1.isSatisfied(null)).thenReturn(true);
		when(cond2.isSatisfied(null)).thenReturn(false);
		when(cond3.isSatisfied(null)).thenReturn(false);
		assertTrue(condition.isSatisfied(null));

		when(cond1.isSatisfied(null)).thenReturn(false);
		when(cond2.isSatisfied(null)).thenReturn(true);
		when(cond3.isSatisfied(null)).thenReturn(false);
		assertTrue(condition.isSatisfied(null));

		when(cond1.isSatisfied(null)).thenReturn(false);
		when(cond2.isSatisfied(null)).thenReturn(false);
		when(cond3.isSatisfied(null)).thenReturn(true);
		assertTrue(condition.isSatisfied(null));

		when(cond1.isSatisfied(null)).thenReturn(true);
		when(cond2.isSatisfied(null)).thenReturn(true);
		when(cond3.isSatisfied(null)).thenReturn(false);
		assertTrue(condition.isSatisfied(null));

		when(cond1.isSatisfied(null)).thenReturn(true);
		when(cond2.isSatisfied(null)).thenReturn(false);
		when(cond3.isSatisfied(null)).thenReturn(true);
		assertTrue(condition.isSatisfied(null));

		when(cond1.isSatisfied(null)).thenReturn(false);
		when(cond2.isSatisfied(null)).thenReturn(true);
		when(cond3.isSatisfied(null)).thenReturn(true);
		assertTrue(condition.isSatisfied(null));

		when(cond1.isSatisfied(null)).thenReturn(true);
		when(cond2.isSatisfied(null)).thenReturn(true);
		when(cond3.isSatisfied(null)).thenReturn(true);
		assertTrue(condition.isSatisfied(null));
	}
}