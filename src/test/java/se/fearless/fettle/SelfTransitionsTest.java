package se.fearless.fettle;

import org.junit.Before;
import org.junit.Test;
import se.fearless.fettle.builder.StateMachineBuilder;
import se.mockachino.annotations.Mock;

import static se.mockachino.Mockachino.setupMocks;
import static se.mockachino.Mockachino.verifyOnce;
import static se.mockachino.matchers.Matchers.any;

public class SelfTransitionsTest {

	@Mock
	private Action<States, String, Void> transitionAction;

	@Mock
	private Action<States, String, Void> entryAction;

	@Mock
	private Action<States, String, Void> exitAction;

	private StateMachine<States, String, Void> stateMachine;

	@Before
	public void setUp() throws Exception {
		setupMocks(this);
		StateMachineBuilder<States, String, Void> builder = Fettle.newBuilder(States.class, String.class);
		builder.transition().from(States.INITIAL).to(States.INITIAL).on("self").perform(transitionAction);
		builder.onEntry(States.INITIAL).perform(entryAction);
		builder.onExit(States.INITIAL).perform(exitAction);
		stateMachine = builder.build(States.INITIAL);

		stateMachine.fireEvent("self");
	}

	@Test
	public void selfTransitionPerformsTransitionActions() throws Exception {
		verifyActionInvocation(transitionAction);
	}

	@Test
	public void selfTransitionPerformsEntryActions() throws Exception {
		verifyActionInvocation(entryAction);
	}

	@Test
	public void selfTransitionPerformsExitActions() throws Exception {
		verifyActionInvocation(exitAction);
	}

	private void verifyActionInvocation(Action<States, String, Void> action) {
		verifyOnce().on(action).onTransition(States.INITIAL, States.INITIAL, "self", any(Void.class), stateMachine);
	}

}
