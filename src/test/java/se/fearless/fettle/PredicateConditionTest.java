package se.fearless.fettle;

import org.junit.Test;
import se.fearless.fettle.util.PredicateCondition;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PredicateConditionTest {

	@Test
	public void predicate() {
		PredicateCondition<String> condition = new PredicateCondition<>(s -> s.contains("foo"));

		assertFalse(condition.isSatisfied("froo"));
		assertTrue(condition.isSatisfied("foobar"));

	}
}
