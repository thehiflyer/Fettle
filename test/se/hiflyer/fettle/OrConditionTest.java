package se.hiflyer.fettle;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static se.mockachino.Mockachino.mock;
import static se.mockachino.Mockachino.stubReturn;

public class OrConditionTest {

	@Test
	public void notSatisfied() {
		Condition cond1 = mock(Condition.class);
		Condition cond2 = mock(Condition.class);
		Condition cond3 = mock(Condition.class);
		AndCondition condition = new AndCondition(cond1, cond2, cond3);

		assertFalse(condition.isSatisfied());
	}

	@Test
	public void satisfied() {
		Condition cond1 = mock(Condition.class);
		Condition cond2 = mock(Condition.class);
		Condition cond3 = mock(Condition.class);
		OrCondition condition = new OrCondition(cond1, cond2, cond3);


		stubReturn(true).on(cond1).isSatisfied();
		stubReturn(false).on(cond2).isSatisfied();
		stubReturn(false).on(cond3).isSatisfied();
		assertTrue(condition.isSatisfied());

		stubReturn(false).on(cond1).isSatisfied();
		stubReturn(true).on(cond2).isSatisfied();
		stubReturn(false).on(cond3).isSatisfied();
		assertTrue(condition.isSatisfied());

		stubReturn(false).on(cond1).isSatisfied();
		stubReturn(false).on(cond2).isSatisfied();
		stubReturn(true).on(cond3).isSatisfied();
		assertTrue(condition.isSatisfied());

		stubReturn(true).on(cond1).isSatisfied();
		stubReturn(true).on(cond2).isSatisfied();
		stubReturn(false).on(cond3).isSatisfied();
		assertTrue(condition.isSatisfied());


		stubReturn(true).on(cond1).isSatisfied();
		stubReturn(false).on(cond2).isSatisfied();
		stubReturn(true).on(cond3).isSatisfied();
		assertTrue(condition.isSatisfied());

		stubReturn(false).on(cond1).isSatisfied();
		stubReturn(true).on(cond2).isSatisfied();
		stubReturn(true).on(cond3).isSatisfied();
		assertTrue(condition.isSatisfied());

		stubReturn(true).on(cond1).isSatisfied();
		stubReturn(true).on(cond2).isSatisfied();
		stubReturn(true).on(cond3).isSatisfied();
		assertTrue(condition.isSatisfied());

	}



}