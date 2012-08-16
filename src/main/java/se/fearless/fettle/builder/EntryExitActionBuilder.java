package se.fearless.fettle.builder;

import se.fearless.fettle.Action;
import se.fearless.fettle.MutableTransitionModel;
import se.fearless.fettle.util.GuavaReplacement;

import java.util.List;

public class EntryExitActionBuilder<S, E, C> implements EntryExit<S, E, C> {
	private final Mode mode;
	private final S state;

	private EntryExitActionBuilder(Mode mode, S state) {
		this.mode = mode;
		this.state = state;
	}

	private enum Mode {
		ENTRY,
		EXIT
	}

	private final List<Action<S, E, C>> actions = GuavaReplacement.newArrayList();


	public static <S, E, C> EntryExitActionBuilder<S, E, C> entry(S to) {
		return new EntryExitActionBuilder<S, E, C>(Mode.ENTRY, to);
	}

	public static <S, E, C> EntryExitActionBuilder<S, E, C> exit(S from) {
		return new EntryExitActionBuilder<S, E, C>(Mode.EXIT, from);
	}

	@Override
	public EntryExit<S, E, C> perform(Action<S, E, C> action) {
		this.actions.add(action);
		return this;
	}

	@Override
	public EntryExit<S, E, C> perform(List<Action<S, E, C>> actions) {
		this.actions.addAll(actions);
		return this;
	}

	public void addToMachine(MutableTransitionModel<S, E, C> machineConstructor) {
		for (Action<S, E, C> action : actions) {
			add(machineConstructor, action);
		}
	}

	private void add(MutableTransitionModel<S, E, C> machineConstructor, Action<S, E, C> action) {
		switch (mode) {
			case ENTRY:
				machineConstructor.addEntryAction(state, action);
				break;
			case EXIT:
				machineConstructor.addExitAction(state, action);
				break;
		}
	}
}