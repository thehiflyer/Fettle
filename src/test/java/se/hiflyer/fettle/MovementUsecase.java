package se.hiflyer.fettle;

import org.junit.Test;
import se.hiflyer.fettle.impl.MutableTransitionModelImpl;
import se.mockachino.Mockachino;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MovementUsecase {

	private interface State {
	}

	private static enum MovementEvents {
		RELEASED_SPACE, HIT_GROUND, ON_UPDATE, PRESSED_SPACE
	}

	@Test
	public void testMovement() {
		State walking = Mockachino.mock(State.class);
		State jumping = Mockachino.mock(State.class);
		State falling = Mockachino.mock(State.class);
		State crashed = Mockachino.mock(State.class);
		State jetpackthrust = Mockachino.mock(State.class);

		MutableTransitionModelImpl<State, MovementEvents, Void> model = MutableTransitionModelImpl.create(State.class, MovementEvents.class);

		List<Action<State, MovementEvents, Void>> noActions = Collections.<Action<State, MovementEvents, Void>>emptyList();
		Condition<Void> always = BasicConditions.<Void>always();
		model.addTransition(walking, jumping, MovementEvents.PRESSED_SPACE, always, noActions);
		model.addTransition(jumping, falling, MovementEvents.RELEASED_SPACE, always, noActions);
		model.addTransition(falling, jetpackthrust, MovementEvents.PRESSED_SPACE, always, noActions);
		model.addTransition(jetpackthrust, falling, MovementEvents.RELEASED_SPACE, always, noActions);
		model.addTransition(falling, crashed, MovementEvents.HIT_GROUND, always, noActions);
		model.addTransition(crashed, walking, MovementEvents.ON_UPDATE, new Condition<Void>() {
			@Override
			public boolean isSatisfied(Void ignored) {
				// wait until body is upright
				return true;
			}
		}, noActions);

		StateMachine<State, MovementEvents, Void> machine = model.newStateMachine(walking);
		assertEquals(walking, machine.getCurrentState());
		machine.fireEvent(MovementEvents.RELEASED_SPACE);
		assertEquals(walking, machine.getCurrentState());
		machine.fireEvent(MovementEvents.PRESSED_SPACE);
		assertEquals(jumping, machine.getCurrentState());
	}


}
