package se.hiflyer.fettle.builder;

import com.google.common.collect.Lists;
import se.hiflyer.fettle.Action;
import se.hiflyer.fettle.ModifiableStateMachine;

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

	private List<Action> actions = Lists.newArrayList();


	public static <S, E> EntryExitActionBuilder<S, E> entry(S to) {
		return new EntryExitActionBuilder<S, E>(Mode.ENTRY, to);
	}

	public static <S, E> EntryExitActionBuilder<S, E> exit(S from) {
		return new EntryExitActionBuilder<S, E>(Mode.EXIT, from);
	}

	public EntryExitActionBuilder<S, E> perform(Action... actions) {
		this.actions.addAll(Lists.newArrayList(actions));
		return this;
	}

	public void addToMachine(ModifiableStateMachine<S, E> machine) {
		for (Action action : actions) {
			add(machine, action);
		}
	}

	private void add(ModifiableStateMachine<S, E> machine, Action action) {
		switch (mode) {
			case ENTRY:
				machine.addEntryAction(state, action);
				break;
			case EXIT:
				machine.addExitAction(state, action);
				break;
		}
	}
}
