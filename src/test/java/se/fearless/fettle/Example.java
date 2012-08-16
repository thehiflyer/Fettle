package se.fearless.fettle;

import com.google.common.collect.Lists;
import org.junit.Test;
import se.fearless.fettle.builder.StateMachineBuilder;
import se.fearless.fettle.util.GuavaReplacement;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class Example {
	@Test
	public void usingBuilder() {
		StateMachineBuilder<States, String, Void> builder = Fettle.newBuilder(States.class, String.class);

		builder.transition().from(States.INITIAL).to(States.ONE).on("foo").perform(new SoutAction("Performing fooTransition"));
		builder.onEntry(States.ONE).perform(new SoutAction("Entering state ONE"));

		StateMachineTemplate<States, String, Void> stateMachineTemplate = builder.buildTransitionModel();
		StateMachine<States, String, Void> stateMachine1 = stateMachineTemplate.newStateMachine(States.INITIAL);
		StateMachine<States, String, Void> stateMachine2 = stateMachineTemplate.newStateMachine(States.INITIAL);

		stateMachine1.fireEvent("foo");
		assertEquals(States.ONE, stateMachine1.getCurrentState());
		assertEquals(States.INITIAL, stateMachine2.getCurrentState());
	}

	@Test
	public void usingTransitionModelDirectly() {
		MutableTransitionModel<States, String, Void> model = Fettle.newTransitionModel(States.class, String.class);


		List<Action<States, String, Void>> actions = Lists.<Action<States, String, Void>>newArrayList(new SoutAction("Performing fooTransition"));
		model.addTransition(States.INITIAL, States.ONE, "foo", BasicConditions.<Void>always(), actions);
		model.addEntryAction(States.ONE, new SoutAction("Entering state ONE"));

		StateMachine<States, String, Void> stateMachine = model.newStateMachine(States.INITIAL);

		stateMachine.fireEvent("foo");
		assertEquals(States.ONE, stateMachine.getCurrentState());
	}

	@Test
	public void whenExample() throws Exception {
		StateMachineBuilder<States, String, Arguments> builder = Fettle.newBuilder(States.class, String.class);
		Condition<Arguments> firstArgIsOne = new Condition<Arguments>() {
			@Override
			public boolean isSatisfied(Arguments args) {
				return args.getNumberOfArguments() > 0 && args.getFirst().equals(1);
			}
		};

		Condition<Arguments> noArguments = new Condition<Arguments>() {
			@Override
			public boolean isSatisfied(Arguments args) {
				return args.getNumberOfArguments() == 0;
			}
		};

		builder.transition().from(States.INITIAL).to(States.ONE).on("tick").when(BasicConditions.or(firstArgIsOne, noArguments));

		StateMachine<States, String, Arguments> stateMachine = builder.build(States.INITIAL);
		stateMachine.fireEvent("tick", new Arguments(3));
		assertEquals(States.INITIAL, stateMachine.getCurrentState());

		stateMachine.fireEvent("tick", Arguments.NO_ARGS);
		assertEquals(States.ONE, stateMachine.getCurrentState());
	}

	@Test
	public void transitionActionExample() throws Exception {
		StateMachineBuilder<States, String, Void> builder = Fettle.newBuilder(States.class, String.class);
		Action<States, String, Void> action1 = new Action<States, String, Void>() {
			@Override
			public void onTransition(States from, States to, String causedBy, Void ignored, StateMachine<States, String, Void> statesStringStateMachine) {
				// do whatever is desired
			}
		};
		Action<States, String, Void> action2 = new Action<States, String, Void>() {
			@Override
			public void onTransition(States from, States to, String causedBy, Void ignored, StateMachine<States, String, Void> statesStringStateMachine) {
				// do whatever is desired
			}
		};

		List<Action<States, String, Void>> actions = GuavaReplacement.newArrayList();
		actions.add(action1);
		actions.add(action2);
		builder.transition().from(States.INITIAL).to(States.ONE).on("foo").perform(actions);
	}

	@Test
	public void entryExitActionExample() throws Exception {
		StateMachineBuilder<States, String, Void> builder = Fettle.newBuilder(States.class, String.class);
		Action<States, String, Void> action1 = new Action<States, String, Void>() {
			@Override
			public void onTransition(States from, States to, String causedBy, Void ignored, StateMachine<States, String, Void> statesStringStateMachine) {
				// do whatever is desired
			}
		};
		Action<States, String, Void> action2 = new Action<States, String, Void>() {
			@Override
			public void onTransition(States from, States to, String causedBy, Void ignored, StateMachine<States, String, Void> statesStringStateMachine) {
				// do whatever is desired
			}
		};


		builder.onExit(States.INITIAL).perform(action1);
		builder.onEntry(States.ONE).perform(action2);
	}

	@Test
	public void fireEventExample() throws Exception {
		StateMachineBuilder<States, String, Arguments> builder = Fettle.newBuilder(States.class, String.class);
		// Setup transitions

		// This changes the default context (passed when fireEvent is called without context) from null to Arguments.NO_ARGS
		builder.defaultContext(Arguments.NO_ARGS);
		StateMachine<States, String, Arguments> stateMachine = builder.build(States.INITIAL);

		stateMachine.fireEvent("foo");
		stateMachine.fireEvent("foo", new Arguments("bar"));
		stateMachine.fireEvent("foo", new Arguments("bar", 1, 2));
	}

	private class SoutAction implements Action<States, String, Void> {
		private final String text;

		public SoutAction(String text) {
			this.text = text;
		}

		@Override
		public void onTransition(States from, States to, String causedBy, Void ignored, StateMachine<States, String, Void> stateMachine) {
			System.out.println(text);
		}
	}
}
