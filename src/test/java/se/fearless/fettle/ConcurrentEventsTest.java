package se.fearless.fettle;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.fearless.fettle.builder.StateMachineBuilder;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@SuppressWarnings("unchecked")
public class ConcurrentEventsTest {

	private StateMachineBuilder<States, String, Void> builder;

	@BeforeEach
	public void setUp() {
		builder = Fettle.newBuilder(States.class, String.class);
	}

	@Test
	public void endsUpInCorrectStateConcurrentFireEvent() throws Exception {
		final CountDownLatch inCondition = new CountDownLatch(1);
		final BlockingCondition condition = new BlockingCondition(inCondition);
		builder.transition().from(States.INITIAL).to(States.ONE).on("first").when(condition);
		builder.transition().from(States.ONE).to(States.TWO).on("second");
		builder.transition().from(States.INITIAL).to(States.THREE).on("second");

		final StateMachine<States, String, Void> stateMachine = builder.build(States.INITIAL);

		try (ExecutorService executor = Executors.newCachedThreadPool()) {
			var f1 = executor.submit((Callable<Void>) () -> {
				stateMachine.fireEvent("first");
				return null;
			});
			var f2 = executor.submit((Callable<Void>) () -> {
				inCondition.await();
				condition.latch.countDown();
				stateMachine.fireEvent("second");
				return null;
			});
			f1.get();
			f2.get();
		}
		assertEquals(States.TWO, stateMachine.getCurrentState());
	}

	@Test
	public void endsUpInCorrectStateConcurrentRawSet() throws Exception {
		final CountDownLatch inCondition = new CountDownLatch(1);
		final BlockingCondition condition = new BlockingCondition(inCondition);
		builder.transition().from(States.INITIAL).to(States.ONE).on("first").when(condition);
		builder.transition().from(States.ONE).to(States.TWO).on("second");
		builder.transition().from(States.INITIAL).to(States.THREE).on("second");

		final StateMachine<States, String, Void> stateMachine = builder.build(States.INITIAL);
		final CountDownLatch rawSetDone = new CountDownLatch(1);

		try (ExecutorService executor = Executors.newCachedThreadPool()) {
			var f1 = executor.submit((Callable<Void>) () -> {
				stateMachine.fireEvent("first");
				return null;
			});
			var f2 = executor.submit((Callable<Void>) () -> {
				inCondition.await();
				condition.latch.countDown();
				stateMachine.rawSetState(States.INITIAL);
				rawSetDone.countDown();
				return null;
			});
			var f3 = executor.submit((Callable<Void>) () -> {
				rawSetDone.await();
				stateMachine.fireEvent("second");
				return null;
			});
			f1.get();
			f2.get();
			f3.get();
		}
		assertEquals(States.THREE, stateMachine.getCurrentState());
	}

	@Test
	public void endsUpInCorrectStateConcurrentForceSet() throws Exception {
		final CountDownLatch inCondition = new CountDownLatch(1);
		final BlockingCondition condition = new BlockingCondition(inCondition);
		builder.transition().from(States.INITIAL).to(States.ONE).on("first").when(condition);
		builder.transition().from(States.ONE).to(States.TWO).on("second");
		builder.transition().from(States.INITIAL).to(States.THREE).on("second");

		final StateMachine<States, String, Void> stateMachine = builder.build(States.INITIAL);
		final CountDownLatch forceSetDone = new CountDownLatch(1);

		try (ExecutorService executor = Executors.newCachedThreadPool()) {
			var f1 = executor.submit((Callable<Void>) () -> {
				stateMachine.fireEvent("first");
				return null;
			});
			var f2 = executor.submit((Callable<Void>) () -> {
				inCondition.await();
				condition.latch.countDown();
				stateMachine.forceSetState(States.INITIAL);
				forceSetDone.countDown();
				return null;
			});
			var f3 = executor.submit((Callable<Void>) () -> {
				forceSetDone.await();
				stateMachine.fireEvent("second");
				return null;
			});
			f1.get();
			f2.get();
			f3.get();
		}
		assertEquals(States.THREE, stateMachine.getCurrentState());
	}

	@Test
	public void actionTriggersStateChange() throws Exception {
		Action<States, String, Void> changeStateAction = (from, to, causedBy, context, stateMachine) ->
				stateMachine.fireEvent("second");
		Action<States, String, Void> otherAction = mock(Action.class);
		builder.transition().from(States.INITIAL).to(States.ONE).on("first").perform(List.of(changeStateAction, otherAction));
		builder.transition().from(States.ONE).to(States.TWO).on("second");

		StateMachine<States, String, Void> stateMachine = builder.build(States.INITIAL);
		stateMachine.fireEvent("first");

		assertEquals(States.TWO, stateMachine.getCurrentState());
		verify(otherAction).onTransition(eq(States.INITIAL), eq(States.ONE), eq("first"), isNull(), eq(stateMachine));
	}

	private static class BlockingCondition implements Condition<Void> {
		CountDownLatch latch = new CountDownLatch(1);
		private final CountDownLatch inCondition;

		BlockingCondition(CountDownLatch inCondition) {
			this.inCondition = inCondition;
		}

		@Override
		public boolean isSatisfied(Void ignored) {
			inCondition.countDown();
			try {
				latch.await();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				throw new RuntimeException("Interrupted while waiting for latch", e);
			}
			return true;
		}
	}
}
