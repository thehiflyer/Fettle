package se.hiflyer.fettle;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ArgumentsTest {

	@Test
	public void getFirst() throws Exception {
		Arguments args = new Arguments("first");
		assertEquals(1, args.getNumberOfArguments());
		assertEquals("first", args.getFirst());
		assertEquals("first", args.getArgument(0));

	}

	@Test
	public void moreArguments() throws Exception {
		Arguments args = new Arguments("first", "second", "third");
		assertEquals(3, args.getNumberOfArguments());
		assertEquals("first", args.getFirst());
		assertEquals("first", args.getArgument(0));
		assertEquals("second", args.getArgument(1));
		assertEquals("third", args.getArgument(2));
	}

	@Test
	public void outOfBounds() throws Exception {
		Arguments args = new Arguments("first", "second", "third");
		assertTrue(null == args.getArgument(-1));
		assertTrue(null == args.getArgument(3));
	}

    @Test
    public void toStringIncludesAllListed() throws Exception {
        Arguments args = new Arguments("first", "second", "third");
        String toString = args.toString();
        assertTrue(toString.contains("first"));
        assertTrue(toString.contains("second"));
        assertTrue(toString.contains("third"));
    }

    @Test
    public void toStringWithOnlyOneArg() throws Exception {
        Arguments args = new Arguments("foo");
        String toString = args.toString();
        assertTrue(toString.contains("foo"));
    }

    @Test
    public void toStringNoArgs() throws Exception {
        Arguments noArgs = Arguments.NO_ARGS;
        String toString = noArgs.toString();
        assertFalse(toString.isEmpty());
    }
}
