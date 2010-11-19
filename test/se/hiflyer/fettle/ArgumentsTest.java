package se.hiflyer.fettle;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ArgumentsTest {

	@Test
	public void testGetFirst() throws Exception {
		Arguments args = new Arguments("first");
		assertEquals(1, args.getNumberOfArguments());
		assertEquals("first", args.getFirst());
		assertEquals("first", args.getArgument(0));

	}

	@Test
	public void testMoreArguments() throws Exception {
		Arguments args = new Arguments("first", "second", "third");
		assertEquals(3, args.getNumberOfArguments());
		assertEquals("first", args.getFirst());
		assertEquals("first", args.getArgument(0));
		assertEquals("second", args.getArgument(1));
		assertEquals("third", args.getArgument(2));
	}

	@Test
	public void testOutOfBounds() throws Exception {
		Arguments args = new Arguments("first", "second", "third");
		assertTrue(null == args.getArgument(-1));
		assertTrue(null == args.getArgument(3));
	}
}
