package se.hiflyer.fettle;

import com.google.common.base.Predicates;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PredicateConditionTest {

	@Test
	public void predicate() {
		PredicateCondition<String> condition = new PredicateCondition<String>(Predicates.containsPattern("foo"));

		condition.setInput("froo");
		assertFalse(condition.isSatisfied());
		condition.setInput("foobar");
		assertTrue(condition.isSatisfied());

	}
}
