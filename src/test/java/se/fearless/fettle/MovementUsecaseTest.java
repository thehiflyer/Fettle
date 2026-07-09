package se.fearless.fettle;

import org.junit.jupiter.api.Test;
import se.fearless.fettle.impl.MutableTransitionModelImpl;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

@SuppressWarnings("unchecked")
public class MovementUsecaseTest {

	private interface State {
	}

	private enum MovementEvents {
		RELEASED_SPACE, HIT_GROUND, ON_UPDATE, PRESSED_SPACE
	}

	@Test
	public void testMovement() {
		State walking = mock(State.class);
		State jumping = mock(State.class);
		State falling = mock(State.class);
		State crashed = mock(State.class);
		State jetpackthrust = mock(State.class);

		MutableTransitionModelImpl<State, MovementEvents, Void> model = MutableTransitionModelImpl.create(State.class, MovementEvents.class);

		List<Action<State, MovementEvents, Void>> noActions = Collections.emptyList();
		Condition<Void> always = BasicConditions.always();
		model.addTransition(walking, jumping, MovementEvents.PRESSED_SPACE, always, noActions);
		model.addTransition(jumping, falling, MovementEvents.RELEASED_SPACE, always, noActions);
		model.addTransition(falling, jetpackthrust, MovementEvents.PRESSED_SPACE, always, noActions);
		model.addTransition(jetpackthrust, falling, MovementEvents.RELEASED_SPACE, always, noActions);
		model.addTransition(falling, crashed, MovementEvents.HIT_GROUND, always, noActions);
		model.addTransition(crashed, walking, MovementEvents.ON_UPDATE, ignored -> {
            // wait until body is upright
            return true;
        }, noActions);

		StateMachine<State, MovementEvents, Void> machine = model.newStateMachine(walking);
		assertEquals(walking, machine.getCurrentState());
		machine.fireEvent(MovementEvents.RELEASED_SPACE);
		assertEquals(walking, machine.getCurrentState());
		machine.fireEvent(MovementEvents.PRESSED_SPACE);
		assertEquals(jumping, machine.getCurrentState());
	}
}
