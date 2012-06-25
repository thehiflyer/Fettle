package se.hiflyer.fettle;

import com.google.common.base.Predicates;
import org.junit.Test;
import se.hiflyer.fettle.util.PredicateCondition;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PredicateConditionTest {

	@Test
	public void predicate() {
		PredicateCondition<String> condition = new PredicateCondition<String>(Predicates.containsPattern("foo"));

		assertFalse(condition.isSatisfied("froo"));
		assertTrue(condition.isSatisfied("foobar"));

	}
}
