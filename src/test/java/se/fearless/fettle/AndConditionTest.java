package se.fearless.fettle;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class AndConditionTest {

	private Condition<Arguments> argumentsCondition1;
	private Condition<Arguments> argumentsCondition2;
	private Condition<Arguments> argumentsCondition3;
	private Condition<Arguments> argumentsAndCondition;

	@BeforeEach
	public void setUp() {
		argumentsCondition1 = mock(Condition.class);
		argumentsCondition2 = mock(Condition.class);
		argumentsCondition3 = mock(Condition.class);

		List<Condition<Arguments>> conditions = new ArrayList<>();
		conditions.add(argumentsCondition1);
		conditions.add(argumentsCondition2);
		conditions.add(argumentsCondition3);
		argumentsAndCondition = BasicConditions.and(conditions);
	}

	@Test
	public void twoWayAnd() {
		Condition<Void> cond1 = mock(Condition.class);
		Condition<Void> cond2 = mock(Condition.class);

		Condition<Void> condition = BasicConditions.and(cond1, cond2);

		assertFalse(condition.isSatisfied(null));

		when(cond1.isSatisfied(null)).thenReturn(true);
		when(cond2.isSatisfied(null)).thenReturn(false);
		assertFalse(condition.isSatisfied(null));

		when(cond1.isSatisfied(null)).thenReturn(false);
		when(cond2.isSatisfied(null)).thenReturn(true);
		assertFalse(condition.isSatisfied(null));

		when(cond1.isSatisfied(null)).thenReturn(true);
		when(cond2.isSatisfied(null)).thenReturn(true);
		assertTrue(condition.isSatisfied(null));
	}

	@Test
	public void satisfied() {
		assertFalse(argumentsAndCondition.isSatisfied(Arguments.NO_ARGS));

		when(argumentsCondition1.isSatisfied(Arguments.NO_ARGS)).thenReturn(true);
		when(argumentsCondition2.isSatisfied(Arguments.NO_ARGS)).thenReturn(true);
		when(argumentsCondition3.isSatisfied(Arguments.NO_ARGS)).thenReturn(true);

		assertTrue(argumentsAndCondition.isSatisfied(Arguments.NO_ARGS));
	}

	@Test
	public void notSatisfied() {
		assertFalse(argumentsAndCondition.isSatisfied(Arguments.NO_ARGS));

		when(argumentsCondition1.isSatisfied(Arguments.NO_ARGS)).thenReturn(true);
		when(argumentsCondition2.isSatisfied(Arguments.NO_ARGS)).thenReturn(false);
		when(argumentsCondition3.isSatisfied(Arguments.NO_ARGS)).thenReturn(false);
		assertFalse(argumentsAndCondition.isSatisfied(Arguments.NO_ARGS));

		when(argumentsCondition1.isSatisfied(Arguments.NO_ARGS)).thenReturn(false);
		when(argumentsCondition2.isSatisfied(Arguments.NO_ARGS)).thenReturn(true);
		when(argumentsCondition3.isSatisfied(Arguments.NO_ARGS)).thenReturn(false);
		assertFalse(argumentsAndCondition.isSatisfied(Arguments.NO_ARGS));

		when(argumentsCondition1.isSatisfied(Arguments.NO_ARGS)).thenReturn(false);
		when(argumentsCondition2.isSatisfied(Arguments.NO_ARGS)).thenReturn(false);
		when(argumentsCondition3.isSatisfied(Arguments.NO_ARGS)).thenReturn(true);
		assertFalse(argumentsAndCondition.isSatisfied(Arguments.NO_ARGS));

		when(argumentsCondition1.isSatisfied(Arguments.NO_ARGS)).thenReturn(true);
		when(argumentsCondition2.isSatisfied(Arguments.NO_ARGS)).thenReturn(true);
		when(argumentsCondition3.isSatisfied(Arguments.NO_ARGS)).thenReturn(false);
		assertFalse(argumentsAndCondition.isSatisfied(Arguments.NO_ARGS));

		when(argumentsCondition1.isSatisfied(Arguments.NO_ARGS)).thenReturn(true);
		when(argumentsCondition2.isSatisfied(Arguments.NO_ARGS)).thenReturn(false);
		when(argumentsCondition3.isSatisfied(Arguments.NO_ARGS)).thenReturn(true);
		assertFalse(argumentsAndCondition.isSatisfied(Arguments.NO_ARGS));

		when(argumentsCondition1.isSatisfied(Arguments.NO_ARGS)).thenReturn(false);
		when(argumentsCondition2.isSatisfied(Arguments.NO_ARGS)).thenReturn(true);
		when(argumentsCondition3.isSatisfied(Arguments.NO_ARGS)).thenReturn(true);
		assertFalse(argumentsAndCondition.isSatisfied(Arguments.NO_ARGS));
	}

	@Test
	public void testWithArguments() {
		Condition<Arguments> cond1 = args -> {
			if (args.getNumberOfArguments() < 1) {
				return false;
			}
			return args.getFirst().equals("foo");
		};

		Condition<Arguments> cond2 = mock(Condition.class);

		Condition<Arguments> condition = BasicConditions.and(cond1, cond2);

		assertFalse(condition.isSatisfied(Arguments.NO_ARGS));
		when(cond2.isSatisfied(any(Arguments.class))).thenReturn(true);

		assertFalse(condition.isSatisfied(Arguments.NO_ARGS));
		assertFalse(condition.isSatisfied(new Arguments("bar")));
		assertTrue(condition.isSatisfied(new Arguments("foo")));
	}
}
