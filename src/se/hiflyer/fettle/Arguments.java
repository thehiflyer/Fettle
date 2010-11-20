package se.hiflyer.fettle;

public class Arguments {
	private final Object first;
	private final Object[] rest;

	public static final Arguments NO_ARGS = new Arguments(null);

	public Arguments(Object first, Object... rest) {
		this.first = first;
		this.rest = rest;
	}

	public Object getFirst() {
		return first;
	}

	public Object getArgument(int number) {
		if (number > rest.length || number < 0) {
			return null;
		}
		if (number == 0) {
			return first;
		}
		return rest[number - 1];
	}

	public int getNumberOfArguments() {
		if (first == null) {
			return 0;
		}
		return rest.length + 1;
	}
}
