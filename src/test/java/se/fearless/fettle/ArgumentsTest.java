package se.fearless.fettle;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class ArgumentsTest {

	@Test
	public void getFirst() {
		Arguments args = new Arguments("first");
		assertEquals(1, args.getNumberOfArguments());
		assertEquals("first", args.getFirst());
		assertEquals("first", args.getArgument(0));
	}

	@Test
	public void moreArguments() {
		Arguments args = new Arguments("first", "second", "third");
		assertEquals(3, args.getNumberOfArguments());
		assertEquals("first", args.getFirst());
		assertEquals("first", args.getArgument(0));
		assertEquals("second", args.getArgument(1));
		assertEquals("third", args.getArgument(2));
	}

	@Test
	public void outOfBounds() {
		Arguments args = new Arguments("first", "second", "third");
        assertNull(args.getArgument(-1));
        assertNull(args.getArgument(3));
	}

	@Test
	public void toStringIncludesAllListed() {
		Arguments args = new Arguments("first", "second", "third");
		String toString = args.toString();
		assertTrue(toString.contains("first"));
		assertTrue(toString.contains("second"));
		assertTrue(toString.contains("third"));
	}

	@Test
	public void toStringWithOnlyOneArg() {
		Arguments args = new Arguments("foo");
		String toString = args.toString();
		assertTrue(toString.contains("foo"));
	}

	@Test
	public void toStringNoArgs() {
		Arguments noArgs = Arguments.NO_ARGS;
		String toString = noArgs.toString();
		assertFalse(toString.isEmpty());
	}
}
