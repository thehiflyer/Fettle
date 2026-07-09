package se.fearless.fettle;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.fearless.fettle.builder.StateMachineBuilder;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;

@SuppressWarnings("unchecked")
@ExtendWith(MockitoExtension.class)
public class SelfTransitionsTest {

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
		builder.transition().from(States.INITIAL).to(States.INITIAL).on("self").perform(transitionAction);
		builder.onEntry(States.INITIAL).perform(entryAction);
		builder.onExit(States.INITIAL).perform(exitAction);
		stateMachine = builder.build(States.INITIAL);

		stateMachine.fireEvent("self");
	}

	@Test
	public void selfTransitionPerformsTransitionActions() {
		verifyActionInvocation(transitionAction);
	}

	@Test
	public void selfTransitionPerformsEntryActions() {
		verifyActionInvocation(entryAction);
	}

	@Test
	public void selfTransitionPerformsExitActions() {
		verifyActionInvocation(exitAction);
	}

	private void verifyActionInvocation(Action<States, String, Void> action) {
		verify(action).onTransition(eq(States.INITIAL), eq(States.INITIAL), eq("self"), isNull(), eq(stateMachine));
	}
}
