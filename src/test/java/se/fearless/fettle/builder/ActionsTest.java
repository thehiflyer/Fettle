package se.fearless.fettle.builder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.fearless.fettle.Action;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

@SuppressWarnings("unchecked")
public class ActionsTest {

	private Action<String, String, Void> action1;
	private Action<String, String, Void> action2;
	private Action<String, String, Void> action3;
	private Action<String, String, Void> action4;
	private Action<String, String, Void> action5;

	@BeforeEach
	public void setUp() {
		action1 = mock(Action.class);
		action2 = mock(Action.class);
		action3 = mock(Action.class);
		action4 = mock(Action.class);
		action5 = mock(Action.class);
	}

	@Test
	public void singleActionIsIncludedInList() {
		assertTrue(Actions.actions(action1).contains(action1));
	}

	@Test
	public void twoActionsAreIncludedInList() {
		List<?> result = Actions.actions(action1, action2);
		assertTrue(result.contains(action1));
		assertTrue(result.contains(action2));
	}

	@Test
	public void threeActionsAreIncludedInList() {
		List<?> result = Actions.actions(action1, action2, action3);
		assertTrue(result.contains(action1));
		assertTrue(result.contains(action2));
		assertTrue(result.contains(action3));
	}

	@Test
	public void fourActionsAreIncludedInList() {
		List<?> result = Actions.actions(action1, action2, action3, action4);
		assertTrue(result.contains(action1));
		assertTrue(result.contains(action2));
		assertTrue(result.contains(action3));
		assertTrue(result.contains(action4));
	}

	@Test
	public void fiveActionsAreIncludedInList() {
		List<?> result = Actions.actions(action1, action2, action3, action4, action5);
		assertTrue(result.contains(action1));
		assertTrue(result.contains(action2));
		assertTrue(result.contains(action3));
		assertTrue(result.contains(action4));
		assertTrue(result.contains(action5));
	}
}