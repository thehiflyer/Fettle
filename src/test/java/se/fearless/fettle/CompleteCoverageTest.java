package se.fearless.fettle;

import org.junit.Test;
import se.fearless.fettle.util.GuavaReplacement;

import java.lang.reflect.Constructor;

/**
 * The tests in here are not really tests and are not useful for anything but getting the coverage to 100%.
 * I don't care about that metric hitting 100 as much as it makes any place I've missed to cover really stand out
 */
public class CompleteCoverageTest {
	@Test
	public void createGuavaReplacement() throws Exception {
		Constructor<?>[] constructors = GuavaReplacement.class.getDeclaredConstructors();
		Constructor<?> privateConstructor = constructors[0];
		privateConstructor.setAccessible(true);
		GuavaReplacement g = (GuavaReplacement) privateConstructor.newInstance();
	}

	@Test
	public void createFettle() throws Exception {
		Constructor<?>[] constructors = Fettle.class.getDeclaredConstructors();
		Constructor<?> privateConstructor = constructors[0];
		privateConstructor.setAccessible(true);
		Fettle f = (Fettle) privateConstructor.newInstance();
	}

	@Test
	public void createBasicConditions() throws Exception {
		Constructor<?>[] constructors = BasicConditions.class.getDeclaredConstructors();
		Constructor<?> privateConstructor = constructors[0];
		privateConstructor.setAccessible(true);
		BasicConditions b = (BasicConditions) privateConstructor.newInstance();
	}
}
