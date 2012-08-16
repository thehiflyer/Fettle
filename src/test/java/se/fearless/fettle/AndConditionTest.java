package se.fearless.fettle;

import com.google.common.collect.Lists;
import com.googlecode.gentyref.TypeToken;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static se.mockachino.Mockachino.mock;
import static se.mockachino.Mockachino.stubReturn;
import static se.mockachino.matchers.Matchers.any;

public class AndConditionTest {

	public static final TypeToken<Condition<Arguments>> ARGUMENT_CONDITION_TYPE_TOKEN = new TypeToken<Condition<Arguments>>() {
	};

	public static final TypeToken<Condition<Void>> VOID_CONDITION_TYPE_TOKEN = new TypeToken<Condition<Void>>() {
	};

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
		Condition<Arguments> cond1 = mock(ARGUMENT_CONDITION_TYPE_TOKEN);
		Condition<Arguments> cond2 = mock(ARGUMENT_CONDITION_TYPE_TOKEN);
		Condition<Arguments> cond3 = mock(ARGUMENT_CONDITION_TYPE_TOKEN);
		List<Condition<Arguments>> conditions = Lists.newArrayList();
		conditions.add(cond1);
		conditions.add(cond2);
		conditions.add(cond3);
		Condition<Arguments> condition = BasicConditions.and(conditions);

		assertFalse(condition.isSatisfied(Arguments.NO_ARGS));

		stubReturn(true).on(cond1).isSatisfied(Arguments.NO_ARGS);
		stubReturn(true).on(cond2).isSatisfied(Arguments.NO_ARGS);
		stubReturn(true).on(cond3).isSatisfied(Arguments.NO_ARGS);

		assertTrue(condition.isSatisfied(Arguments.NO_ARGS));
	}

	@Test
	public void notSatisfied() {

		Condition<Arguments> cond1 = mock(ARGUMENT_CONDITION_TYPE_TOKEN);
		Condition<Arguments> cond2 = mock(ARGUMENT_CONDITION_TYPE_TOKEN);
		Condition<Arguments> cond3 = mock(ARGUMENT_CONDITION_TYPE_TOKEN);
		List<Condition<Arguments>> conditions = Lists.newArrayList();
		conditions.add(cond1);
		conditions.add(cond2);
		conditions.add(cond3);
		Condition<Arguments> condition = BasicConditions.and(conditions);

		assertFalse(condition.isSatisfied(Arguments.NO_ARGS));

		stubReturn(true).on(cond1).isSatisfied(Arguments.NO_ARGS);
		stubReturn(false).on(cond2).isSatisfied(Arguments.NO_ARGS);
		stubReturn(false).on(cond3).isSatisfied(Arguments.NO_ARGS);
		assertFalse(condition.isSatisfied(Arguments.NO_ARGS));

		stubReturn(false).on(cond1).isSatisfied(Arguments.NO_ARGS);
		stubReturn(true).on(cond2).isSatisfied(Arguments.NO_ARGS);
		stubReturn(false).on(cond3).isSatisfied(Arguments.NO_ARGS);
		assertFalse(condition.isSatisfied(Arguments.NO_ARGS));

		stubReturn(false).on(cond1).isSatisfied(Arguments.NO_ARGS);
		stubReturn(false).on(cond2).isSatisfied(Arguments.NO_ARGS);
		stubReturn(true).on(cond3).isSatisfied(Arguments.NO_ARGS);
		assertFalse(condition.isSatisfied(Arguments.NO_ARGS));

		stubReturn(true).on(cond1).isSatisfied(Arguments.NO_ARGS);
		stubReturn(true).on(cond2).isSatisfied(Arguments.NO_ARGS);
		stubReturn(false).on(cond3).isSatisfied(Arguments.NO_ARGS);
		assertFalse(condition.isSatisfied(Arguments.NO_ARGS));


		stubReturn(true).on(cond1).isSatisfied(Arguments.NO_ARGS);
		stubReturn(false).on(cond2).isSatisfied(Arguments.NO_ARGS);
		stubReturn(true).on(cond3).isSatisfied(Arguments.NO_ARGS);
		assertFalse(condition.isSatisfied(Arguments.NO_ARGS));

		stubReturn(false).on(cond1).isSatisfied(Arguments.NO_ARGS);
		stubReturn(true).on(cond2).isSatisfied(Arguments.NO_ARGS);
		stubReturn(true).on(cond3).isSatisfied(Arguments.NO_ARGS);
		assertFalse(condition.isSatisfied(Arguments.NO_ARGS));

	}

	@Test
	public void testWithArguments() throws Exception {
		Condition<Arguments> cond1 = new Condition<Arguments>() {
			@Override
			public boolean isSatisfied(Arguments args) {
				if (args.getNumberOfArguments() < 1) {
					return false;
				}
				return args.getFirst().equals("foo");
			}
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
