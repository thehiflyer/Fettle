package se.fearless.fettle;

import java.util.Arrays;
import java.util.stream.Stream;

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
		if (number < 0 || number > rest.length) {
			return null;
		}
		return number == 0 ? first : rest[number - 1];
	}

	public int getNumberOfArguments() {
		return first == null ? 0 : rest.length + 1;
	}

	@Override
	public String toString() {
		if (first == null) {
			return "Arguments()";
		}
		String allArgs = Stream.concat(
			Stream.of(first),
			Arrays.stream(rest)
		).map(String::valueOf).reduce((a, b) -> a + ", " + b).orElse("");
		return "Arguments(" + allArgs + ")";
	}
}

