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

	@Test
	public void whenExample() throws Exception {
		StateMachineBuilder<States,String> builder = Fettle.newBuilder(States.class, String.class);
		Condition firstArgIsOne = new Condition() {
			@Override
			public boolean isSatisfied(Arguments args) {
				return args.getNumberOfArguments() > 0 && args.getFirst().equals(1);
			}
		};

		Condition noArguments = new Condition() {
			@Override
			public boolean isSatisfied(Arguments args) {
				return args.getNumberOfArguments() == 0;
			}
		};

		builder.transition().from(States.INITIAL).to(States.ONE).on("tick").when(BasicConditions.or(firstArgIsOne, noArguments));

		StateMachine<States, String> stateMachine = builder.build(States.INITIAL);
		stateMachine.fireEvent("tick", new Arguments(3));
		assertEquals(States.INITIAL, stateMachine.getCurrentState());

		stateMachine.fireEvent("tick", Arguments.NO_ARGS);
		assertEquals(States.ONE, stateMachine.getCurrentState());
	}

	@Test
	public void actionExample() throws Exception {
		StateMachineBuilder<States,String> builder = Fettle.newBuilder(States.class, String.class);
		Action<States, String> action1 = new Action<States, String>() {
			@Override
			public void onTransition(States from, States to, String causedBy, Arguments args, StateMachine<States, String> statesStringStateMachine) {
				// do whatever is desired
			}
		};
		Action<States, String> action2 = new Action<States, String>() {
			@Override
			public void onTransition(States from, States to, String causedBy, Arguments args, StateMachine<States, String> statesStringStateMachine) {
				// do whatever is desired
			}
		};

		builder.transition().from(States.INITIAL).to(States.ONE).on("foo").perform(action1, action2);
	}

	private class SoutAction implements Action<States, String> {
		private final String text;

		public SoutAction(String text) {
			this.text = text;
		}

		@Override
		public void onTransition(States from, States to, String causedBy, Arguments args, StateMachine<States, String> stateMachine) {
			System.out.println(text);
		}
	}
}
