package se.hiflyer.fettle;

import com.google.common.collect.Lists;
import com.googlecode.gentyref.TypeToken;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static se.mockachino.Mockachino.mock;
import static se.mockachino.Mockachino.stubReturn;

public class OrConditionTest {

	public static final TypeToken<Condition<Arguments>> ARGUMENT_CONDITION_TYPE_TOKEN = new TypeToken<Condition<Arguments>>() {
	};

	public static final TypeToken<Condition<Void>> VOID_CONDITION_TYPE_TOKEN = new TypeToken<Condition<Void>>() {
	};


	@Test
	public void twoWayOr() {
		Condition<Void> cond1 = mock(VOID_CONDITION_TYPE_TOKEN);
		Condition<Void> cond2 = mock(VOID_CONDITION_TYPE_TOKEN);

		Condition<Void> condition = BasicConditions.or(cond1, cond2);

		assertFalse(condition.isSatisfied(null));

		stubReturn(true).on(cond1).isSatisfied(null);
		stubReturn(false).on(cond2).isSatisfied(null);
		assertTrue(condition.isSatisfied(null));

		stubReturn(false).on(cond1).isSatisfied(null);
		stubReturn(true).on(cond2).isSatisfied(null);
		assertTrue(condition.isSatisfied(null));

		stubReturn(true).on(cond1).isSatisfied(null);
		stubReturn(true).on(cond2).isSatisfied(null);
		assertTrue(condition.isSatisfied(null));
	}


	@Test
	public void notSatisfied() {
		Condition<Void> cond1 = mock(VOID_CONDITION_TYPE_TOKEN);
		Condition<Void> cond2 = mock(VOID_CONDITION_TYPE_TOKEN);
		Condition<Void> cond3 = mock(VOID_CONDITION_TYPE_TOKEN);
		List<Condition<Void>> conditions = Lists.newArrayList();
		conditions.add(cond1);
		conditions.add(cond2);
		conditions.add(cond3);

		Condition<Void> condition = BasicConditions.or(conditions);

		assertFalse(condition.isSatisfied(null));
	}

	@Test
	public void satisfied() {
		Condition<Void> cond1 = mock(VOID_CONDITION_TYPE_TOKEN);
		Condition<Void> cond2 = mock(VOID_CONDITION_TYPE_TOKEN);
		Condition<Void> cond3 = mock(VOID_CONDITION_TYPE_TOKEN);
		List<Condition<Void>> conditions = Lists.newArrayList();
		conditions.add(cond1);
		conditions.add(cond2);
		conditions.add(cond3);

		Condition<Void> condition = BasicConditions.or(conditions);


		stubReturn(true).on(cond1).isSatisfied(null);
		stubReturn(false).on(cond2).isSatisfied(null);
		stubReturn(false).on(cond3).isSatisfied(null);
		assertTrue(condition.isSatisfied(null));

		stubReturn(false).on(cond1).isSatisfied(null);
		stubReturn(true).on(cond2).isSatisfied(null);
		stubReturn(false).on(cond3).isSatisfied(null);
		assertTrue(condition.isSatisfied(null));

		stubReturn(false).on(cond1).isSatisfied(null);
		stubReturn(false).on(cond2).isSatisfied(null);
		stubReturn(true).on(cond3).isSatisfied(null);
		assertTrue(condition.isSatisfied(null));

		stubReturn(true).on(cond1).isSatisfied(null);
		stubReturn(true).on(cond2).isSatisfied(null);
		stubReturn(false).on(cond3).isSatisfied(null);
		assertTrue(condition.isSatisfied(null));


		stubReturn(true).on(cond1).isSatisfied(null);
		stubReturn(false).on(cond2).isSatisfied(null);
		stubReturn(true).on(cond3).isSatisfied(null);
		assertTrue(condition.isSatisfied(null));

		stubReturn(false).on(cond1).isSatisfied(null);
		stubReturn(true).on(cond2).isSatisfied(null);
		stubReturn(true).on(cond3).isSatisfied(null);
		assertTrue(condition.isSatisfied(null));

		stubReturn(true).on(cond1).isSatisfied(null);
		stubReturn(true).on(cond2).isSatisfied(null);
		stubReturn(true).on(cond3).isSatisfied(null);
		assertTrue(condition.isSatisfied(null));
	}
}