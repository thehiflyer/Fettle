package se.fearless.fettle;

import com.google.common.collect.Lists;
import com.googlecode.gentyref.TypeToken;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static se.mockachino.Mockachino.mock;
import static se.mockachino.Mockachino.stubReturn;
import static se.mockachino.matchers.Matchers.any;

public class AndConditionTest {

	private static final TypeToken<Condition<Arguments>> ARGUMENT_CONDITION_TYPE_TOKEN = new TypeToken<Condition<Arguments>>() {
	};

	private static final TypeToken<Condition<Void>> VOID_CONDITION_TYPE_TOKEN = new TypeToken<Condition<Void>>() {
	};
	private Condition<Arguments> argumentsCondition1;
	private Condition<Arguments> argumentsCondition2;
	private Condition<Arguments> argumentsCondition3;
	private Condition<Arguments> argumentsAndCondition;

	@Before
	public void setUp() throws Exception {
		argumentsCondition1 = mock(ARGUMENT_CONDITION_TYPE_TOKEN);
		argumentsCondition2 = mock(ARGUMENT_CONDITION_TYPE_TOKEN);
		argumentsCondition3 = mock(ARGUMENT_CONDITION_TYPE_TOKEN);

		List<Condition<Arguments>> conditions = Lists.newArrayList();
		conditions.add(argumentsCondition1);
		conditions.add(argumentsCondition2);
		conditions.add(argumentsCondition3);
		argumentsAndCondition = BasicConditions.and(conditions);
	}

	@Test
	public void twoWayAnd() {
		Condition<Void> cond1 = mock(VOID_CONDITION_TYPE_TOKEN);
		Condition<Void> cond2 = mock(VOID_CONDITION_TYPE_TOKEN);

		Condition<Void> condition = BasicConditions.and(cond1, cond2);

		assertFalse(condition.isSatisfied(null));

		stubReturn(true).on(cond1).isSatisfied(null);
		stubReturn(false).on(cond2).isSatisfied(null);
		assertFalse(condition.isSatisfied(null));

		stubReturn(false).on(cond1).isSatisfied(null);
		stubReturn(true).on(cond2).isSatisfied(null);
		assertFalse(condition.isSatisfied(null));

		stubReturn(true).on(cond1).isSatisfied(null);
		stubReturn(true).on(cond2).isSatisfied(null);
		assertTrue(condition.isSatisfied(null));
	}


	@Test
	public void satisfied() {


		assertFalse(argumentsAndCondition.isSatisfied(Arguments.NO_ARGS));

		stubReturn(true).on(argumentsCondition1).isSatisfied(Arguments.NO_ARGS);
		stubReturn(true).on(argumentsCondition2).isSatisfied(Arguments.NO_ARGS);
		stubReturn(true).on(argumentsCondition3).isSatisfied(Arguments.NO_ARGS);

		assertTrue(argumentsAndCondition.isSatisfied(Arguments.NO_ARGS));
	}

	@Test
	public void notSatisfied() {
		assertFalse(argumentsAndCondition.isSatisfied(Arguments.NO_ARGS));

		stubReturn(true).on(argumentsCondition1).isSatisfied(Arguments.NO_ARGS);
		stubReturn(false).on(argumentsCondition2).isSatisfied(Arguments.NO_ARGS);
		stubReturn(false).on(argumentsCondition3).isSatisfied(Arguments.NO_ARGS);
		assertFalse(argumentsAndCondition.isSatisfied(Arguments.NO_ARGS));

		stubReturn(false).on(argumentsCondition1).isSatisfied(Arguments.NO_ARGS);
		stubReturn(true).on(argumentsCondition2).isSatisfied(Arguments.NO_ARGS);
		stubReturn(false).on(argumentsCondition3).isSatisfied(Arguments.NO_ARGS);
		assertFalse(argumentsAndCondition.isSatisfied(Arguments.NO_ARGS));

		stubReturn(false).on(argumentsCondition1).isSatisfied(Arguments.NO_ARGS);
		stubReturn(false).on(argumentsCondition2).isSatisfied(Arguments.NO_ARGS);
		stubReturn(true).on(argumentsCondition3).isSatisfied(Arguments.NO_ARGS);
		assertFalse(argumentsAndCondition.isSatisfied(Arguments.NO_ARGS));

		stubReturn(true).on(argumentsCondition1).isSatisfied(Arguments.NO_ARGS);
		stubReturn(true).on(argumentsCondition2).isSatisfied(Arguments.NO_ARGS);
		stubReturn(false).on(argumentsCondition3).isSatisfied(Arguments.NO_ARGS);
		assertFalse(argumentsAndCondition.isSatisfied(Arguments.NO_ARGS));


		stubReturn(true).on(argumentsCondition1).isSatisfied(Arguments.NO_ARGS);
		stubReturn(false).on(argumentsCondition2).isSatisfied(Arguments.NO_ARGS);
		stubReturn(true).on(argumentsCondition3).isSatisfied(Arguments.NO_ARGS);
		assertFalse(argumentsAndCondition.isSatisfied(Arguments.NO_ARGS));

		stubReturn(false).on(argumentsCondition1).isSatisfied(Arguments.NO_ARGS);
		stubReturn(true).on(argumentsCondition2).isSatisfied(Arguments.NO_ARGS);
		stubReturn(true).on(argumentsCondition3).isSatisfied(Arguments.NO_ARGS);
		assertFalse(argumentsAndCondition.isSatisfied(Arguments.NO_ARGS));

	}

	@Test
	public void testWithArguments() throws Exception {
		Condition<Arguments> cond1 = args -> {
			if (args.getNumberOfArguments() < 1) {
				return false;
			}
			return args.getFirst().equals("foo");
		};

		Condition<Arguments> cond2 = mock(ARGUMENT_CONDITION_TYPE_TOKEN);

		Condition<Arguments> condition = BasicConditions.and(cond1, cond2);

		assertFalse(condition.isSatisfied(Arguments.NO_ARGS));
		stubReturn(true).on(cond2).isSatisfied(any(Arguments.class));

		assertFalse(condition.isSatisfied(Arguments.NO_ARGS));
		assertFalse(condition.isSatisfied(new Arguments("bar")));
		assertTrue(condition.isSatisfied(new Arguments("foo")));

	}
}
