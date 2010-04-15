package se.hiflyer.fettle;

public class Trigger<T> implements Condition {
	private final StateMachine<T> machine;
	private boolean satisfied;

	public Trigger(StateMachine<T> machine) {
		this.machine = machine;
	}

	@Override
	public boolean isSatisfied() {
		return satisfied;
	}

	public void pull() {
		satisfied = true;
		machine.update();
		satisfied = false;
	}
}
