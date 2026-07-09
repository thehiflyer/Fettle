package se.fearless.fettle;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.fearless.fettle.builder.StateMachineBuilder;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@SuppressWarnings("unchecked")
public class ConcurrentEventsTest {

	private static final Logger log = LoggerFactory.getLogger(ConcurrentEventsTest.class);

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
		final CountDownLatch eventsDone = new CountDownLatch(2);
		Runnable fireFirst = new Runnable() {
			@Override
			public void run() {
				stateMachine.fireEvent("first");
				eventsDone.countDown();
			}
		};
		new Thread(fireFirst).start();

		Runnable fireSecond = new Runnable() {
			@Override
			public void run() {
				try {
					inCondition.await();
					condition.latch.countDown();
				} catch (InterruptedException e) {
					log.error("Thread interrupted while waiting for condition", e);
					Thread.currentThread().interrupt();
				}
				stateMachine.fireEvent("second");
				eventsDone.countDown();
			}
		};
		new Thread(fireSecond).start();
		eventsDone.await();
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
		final CountDownLatch eventsDone = new CountDownLatch(3);
		Runnable fireFirst = new Runnable() {
			@Override
			public void run() {
				stateMachine.fireEvent("first");
				eventsDone.countDown();
			}
		};
		new Thread(fireFirst).start();

		final CountDownLatch rawSetDone = new CountDownLatch(1);
		Runnable rawSetInitial = new Runnable() {
			@Override
			public void run() {
				try {
					inCondition.await();
					condition.latch.countDown();
				} catch (InterruptedException e) {
					log.error("Thread interrupted while waiting for condition", e);
					Thread.currentThread().interrupt();
				}
				stateMachine.rawSetState(States.INITIAL);
				rawSetDone.countDown();
				eventsDone.countDown();
			}
		};
		new Thread(rawSetInitial).start();

		Runnable fireSecond = new Runnable() {
			@Override
			public void run() {
				try {
					rawSetDone.await();
				} catch (InterruptedException e) {
					log.error("Thread interrupted while waiting for rawSet", e);
					Thread.currentThread().interrupt();
				}
				stateMachine.fireEvent("second");
				eventsDone.countDown();
			}
		};
		new Thread(fireSecond).start();

		eventsDone.await();
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
		final CountDownLatch eventsDone = new CountDownLatch(3);
		Runnable fireFirst = new Runnable() {
			@Override
			public void run() {
				stateMachine.fireEvent("first");
				eventsDone.countDown();
			}
		};
		new Thread(fireFirst).start();

		final CountDownLatch rawSetDone = new CountDownLatch(1);
		Runnable rawSetInitial = new Runnable() {
			@Override
			public void run() {
				try {
					inCondition.await();
					condition.latch.countDown();
				} catch (InterruptedException e) {
					log.error("Thread interrupted while waiting for condition", e);
					Thread.currentThread().interrupt();
				}
				stateMachine.forceSetState(States.INITIAL);
				rawSetDone.countDown();
				eventsDone.countDown();
			}
		};
		new Thread(rawSetInitial).start();

		Runnable fireSecond = new Runnable() {
			@Override
			public void run() {
				try {
					rawSetDone.await();
				} catch (InterruptedException e) {
					log.error("Thread interrupted while waiting for rawSet", e);
					Thread.currentThread().interrupt();
				}
				stateMachine.fireEvent("second");
				eventsDone.countDown();
			}
		};
		new Thread(fireSecond).start();

		eventsDone.await();
		assertEquals(States.THREE, stateMachine.getCurrentState());
	}

	@Test
	public void actionTriggersStateChange() throws Exception {
		Action<States, String, Void> changeStateAction = new Action<States, String, Void>() {
			@Override
			public void onTransition(States from, States to, String causedBy, Void context, StateMachine<States, String, Void> stateMachine) {
				stateMachine.fireEvent("second");
			}
		};
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
				log.error("Thread interrupted while waiting for latch", e);
				Thread.currentThread().interrupt();
			}
			return true;
		}
	}
}
