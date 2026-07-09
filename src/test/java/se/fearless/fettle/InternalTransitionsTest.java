package se.fearless.fettle;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.fearless.fettle.builder.StateMachineBuilder;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@SuppressWarnings("unchecked")
@ExtendWith(MockitoExtension.class)
public class InternalTransitionsTest {

	private static final String INTERNAL_TRANSITION_TRIGGER = "internal";

	@Mock
	private Action<States, String, Void> transitionAction;

	@Mock
	private Action<States, String, Void> entryAction;

	@Mock
	private Action<States, String, Void> exitAction;

	private StateMachine<States, String, Void> stateMachine;

	@BeforeEach
	public void setUp() {
		StateMachineBuilder<States, String, Void> builder = Fettle.newBuilder(States.class, String.class);
		builder.transition().internal(States.INITIAL).on(INTERNAL_TRANSITION_TRIGGER).perform(transitionAction);
		builder.onEntry(States.INITIAL).perform(entryAction);
		builder.onExit(States.INITIAL).perform(exitAction);
		stateMachine = builder.build(States.INITIAL);

		stateMachine.fireEvent(INTERNAL_TRANSITION_TRIGGER);
	}

	@Test
	public void internalTransitionPerformsTransitionActions() {
		verifyActionInvocation(transitionAction);
	}

	@Test
	public void internalTransitionDoesNotPerformEntryActions() {
		verify(entryAction, never()).onTransition(eq(States.INITIAL), eq(States.INITIAL), eq(INTERNAL_TRANSITION_TRIGGER), isNull(), eq(stateMachine));
	}

	@Test
	public void internalTransitionDoesNotPerformExitActions() {
		verify(exitAction, never()).onTransition(eq(States.INITIAL), eq(States.INITIAL), eq(INTERNAL_TRANSITION_TRIGGER), isNull(), eq(stateMachine));
	}

	private void verifyActionInvocation(Action<States, String, Void> action) {
		verify(action).onTransition(eq(States.INITIAL), eq(States.INITIAL), eq(INTERNAL_TRANSITION_TRIGGER), isNull(), eq(stateMachine));
	}
}
