package se.hiflyer.fettle.builder;

import com.google.common.collect.Lists;
import se.hiflyer.fettle.Action;
import se.hiflyer.fettle.MutableTransitionModel;

import java.util.List;

public class EntryExitActionBuilder<S, E> {
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

	private final List<Action<S, E>> actions = Lists.newArrayList();


	public static <S, E> EntryExitActionBuilder<S, E> entry(S to) {
		return new EntryExitActionBuilder<S, E>(Mode.ENTRY, to);
	}

	public static <S, E> EntryExitActionBuilder<S, E> exit(S from) {
		return new EntryExitActionBuilder<S, E>(Mode.EXIT, from);
	}

	public EntryExitActionBuilder<S, E> perform(Action<S, E>... actions) {
		this.actions.addAll(Lists.newArrayList(actions));
		return this;
	}

	public void addToMachine(MutableTransitionModel<S, E> machineConstructor) {
		for (Action<S, E> action : actions) {
			add(machineConstructor, action);
		}
	}

	private void add(MutableTransitionModel<S, E> machineConstructor, Action<S, E> action) {
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
