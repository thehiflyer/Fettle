package se.fearless.fettle;

import com.google.common.base.Predicates;
import org.junit.Test;
import se.fearless.fettle.util.PredicateCondition;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PredicateConditionTest {

	@Test
	public void predicate() {
		PredicateCondition<String> condition = new PredicateCondition<>(Predicates.containsPattern("foo")::apply);

		assertFalse(condition.isSatisfied("froo"));
		assertTrue(condition.isSatisfied("foobar"));

	}
}
