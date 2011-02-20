package se.hiflyer.fettle;

import com.google.common.collect.Lists;
import org.junit.Test;
import se.hiflyer.fettle.builder.StateMachineBuilder;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class Example {
	@Test
	public void usingBuilder() {
		StateMachineBuilder<States,String> builder = Fettle.newBuilder(States.class, String.class);


		builder.transition().from(States.INITIAL).to(States.ONE).on("foo").perform(new SoutAction("Performing fooTransition"));
		builder.onEntry(States.ONE).perform(new SoutAction("Entering state ONE"));

		TransitionModel<States, String> transitionModel = builder.buildTransitionModel();
		StateMachine<States, String> stateMachine = transitionModel.newStateMachine(States.INITIAL);

		stateMachine.fireEvent("foo");
		assertEquals(States.ONE, stateMachine.getCurrentState());
	}

	@Test
	public void usingTransitionModelDirectly() {
		MutableTransitionModel<States,String> model = Fettle.newTransitionModel(States.class, String.class);


		List<Action<States,String>> actions = Lists.<Action<States, String>>newArrayList(new SoutAction("Performing fooTransition"));
		model.addTransition(States.INITIAL, States.ONE, "foo", BasicConditions.ALWAYS, actions);
		model.addEntryAction(States.ONE, new SoutAction("Entering state ONE"));

		StateMachine<States, String> stateMachine = model.newStateMachine(States.INITIAL);

		stateMachine.fireEvent("foo");
		assertEquals(States.ONE, stateMachine.getCurrentState());
	}


	private class SoutAction implements Action<States, String> {
		private final String text;

		public SoutAction(String text) {
			this.text = text;
		}

		@Override
		public void onTransition(States from, States to, String causedBy, Arguments args) {
			System.out.println(text);
		}
	}
}
