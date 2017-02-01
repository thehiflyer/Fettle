package se.fearless.fettle.builder;

import com.googlecode.gentyref.TypeToken;
import org.junit.Before;
import org.junit.Test;
import se.fearless.fettle.Action;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertThat;
import static se.fearless.fettle.builder.Actions.actions;
import static se.mockachino.Mockachino.mock;

public class ActionsTest {

	private static TypeToken<Action<String, String, Void>> ACTION_TYPE_TOKEN = new TypeToken<Action<String, String, Void>>() {
	};

	private Action<String, String, Void> action1;
	private Action<String, String, Void> action2;
	private Action<String, String, Void> action3;
	private Action<String, String, Void> action4;
	private Action<String, String, Void> action5;

	@Before
	public void setUp() throws Exception {
		action1 = mock(ACTION_TYPE_TOKEN);
		action2 = mock(ACTION_TYPE_TOKEN);
		action3 = mock(ACTION_TYPE_TOKEN);
		action4 = mock(ACTION_TYPE_TOKEN);
		action5 = mock(ACTION_TYPE_TOKEN);
	}

	@Test
	public void singleActionIsIncludedInList() throws Exception {
		assertThat(actions(action1), hasItem(action1));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void twoActionsAreIncludedInList() throws Exception {
		assertThat(actions(action1, action2), hasItems(action1, action2));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void threeActionsAreIncludedInList() throws Exception {
		assertThat(actions(action1, action2, action3), hasItems(action1, action2, action3));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void fourActionsAreIncludedInList() throws Exception {
		assertThat(actions(action1, action2, action3, action4), hasItems(action1, action2, action3, action4));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void fiveActionsAreIncludedInList() throws Exception {
		assertThat(actions(action1, action2, action3, action4, action5), hasItems(action1, action2, action3, action4, action5));
	}
}