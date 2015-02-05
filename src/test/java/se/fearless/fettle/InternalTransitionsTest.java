package se.fearless.fettle;

import org.junit.Before;
import org.junit.Test;
import se.fearless.fettle.builder.StateMachineBuilder;
import se.mockachino.annotations.Mock;

import static se.mockachino.Mockachino.*;
import static se.mockachino.matchers.Matchers.any;

public class InternalTransitionsTest {

	public static final String INTERNAL_TRANSITION_TRIGGER = "internal";
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
		builder.transition().internal(States.INITIAL).on(INTERNAL_TRANSITION_TRIGGER).perform(transitionAction);
		builder.onEntry(States.INITIAL).perform(entryAction);
		builder.onExit(States.INITIAL).perform(exitAction);
		stateMachine = builder.build(States.INITIAL);

		stateMachine.fireEvent(INTERNAL_TRANSITION_TRIGGER);
	}

	@Test
	public void internalTransitionPerformsTransitionActions() throws Exception {
		verifyActionInvocation(transitionAction);
	}

	@Test
	public void internalTransitionDoesNotPerformEntryActions() throws Exception {
		verifyNever().on(entryAction).onTransition(States.INITIAL, States.INITIAL, INTERNAL_TRANSITION_TRIGGER, any(Void.class), stateMachine);
	}

	@Test
	public void internalTransitionDoesNotPerformExitActions() throws Exception {
		verifyNever().on(exitAction).onTransition(States.INITIAL, States.INITIAL, INTERNAL_TRANSITION_TRIGGER, any(Void.class), stateMachine);
	}

	private void verifyActionInvocation(Action<States, String, Void> action) {
		verifyOnce().on(action).onTransition(States.INITIAL, States.INITIAL, INTERNAL_TRANSITION_TRIGGER, any(Void.class), stateMachine);
	}

}
