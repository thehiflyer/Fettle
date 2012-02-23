package se.hiflyer.fettle;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static se.mockachino.Mockachino.mock;
import static se.mockachino.Mockachino.stubReturn;
import static se.mockachino.matchers.Matchers.any;

public class AndConditionTest {

	@Test
	public void twoWayAnd() {
		Condition cond1 = mock(Condition.class);
		Condition cond2 = mock(Condition.class);

		Condition condition = BasicConditions.and(cond1, cond2);

		assertFalse(condition.isSatisfied(Arguments.NO_ARGS));

		stubReturn(true).on(cond1).isSatisfied(Arguments.NO_ARGS);
		stubReturn(false).on(cond2).isSatisfied(Arguments.NO_ARGS);
		assertFalse(condition.isSatisfied(Arguments.NO_ARGS));

		stubReturn(false).on(cond1).isSatisfied(Arguments.NO_ARGS);
		stubReturn(true).on(cond2).isSatisfied(Arguments.NO_ARGS);
		assertFalse(condition.isSatisfied(Arguments.NO_ARGS));

		stubReturn(true).on(cond1).isSatisfied(Arguments.NO_ARGS);
		stubReturn(true).on(cond2).isSatisfied(Arguments.NO_ARGS);
		assertTrue(condition.isSatisfied(Arguments.NO_ARGS));
	}


	@Test
	public void satisfied() {
		Condition cond1 = mock(Condition.class);
		Condition cond2 = mock(Condition.class);
		Condition cond3 = mock(Condition.class);
		Condition condition = BasicConditions.and(cond1, cond2, cond3);

		assertFalse(condition.isSatisfied(Arguments.NO_ARGS));

		stubReturn(true).on(cond1).isSatisfied(Arguments.NO_ARGS);
		stubReturn(true).on(cond2).isSatisfied(Arguments.NO_ARGS);
		stubReturn(true).on(cond3).isSatisfied(Arguments.NO_ARGS);

		assertTrue(condition.isSatisfied(Arguments.NO_ARGS));
	}

	@Test
	public void notSatisfied() {
		Condition cond1 = mock(Condition.class);
		Condition cond2 = mock(Condition.class);
		Condition cond3 = mock(Condition.class);
		Condition condition = BasicConditions.and(cond1, cond2, cond3);

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
		Condition cond1 = new Condition() {
			@Override
			public boolean isSatisfied(Arguments args) {
				if (args.getNumberOfArguments() < 1) {
					return false;
				}
				return args.getFirst().equals("foo");
			}
		};

		Condition cond2 = mock(Condition.class);

		Condition condition = BasicConditions.and(cond1, cond2);

		assertFalse(condition.isSatisfied(Arguments.NO_ARGS));
		stubReturn(true).on(cond2).isSatisfied(any(Arguments.class));

		assertFalse(condition.isSatisfied(Arguments.NO_ARGS));
		assertFalse(condition.isSatisfied(new Arguments("bar")));
		assertTrue(condition.isSatisfied(new Arguments("foo")));

	}
}
