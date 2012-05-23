package se.hiflyer.fettle;

import org.junit.Before;
import org.junit.Test;
import se.hiflyer.fettle.builder.StateMachineBuilder;

import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertEquals;

public class ConcurrentEventsTest {

	private StateMachineBuilder<States,String> builder;

	@Before
	public void setUp() throws Exception {
		builder = Fettle.newBuilder(States.class, String.class);
	}

	@Test
	public void endsUpInCorrectState() throws Exception {
		final CountDownLatch inCondition = new CountDownLatch(1);
		final BlockingCondition condition = new BlockingCondition(inCondition);
		builder.transition().from(States.INITIAL).to(States.ONE).on("first").when(condition);
		builder.transition().from(States.ONE).to(States.TWO).on("second");
		builder.transition().from(States.INITIAL).to(States.THREE).on("second");

		final StateMachine<States, String> stateMachine = builder.build(States.INITIAL);
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
					e.printStackTrace();
				}
				stateMachine.fireEvent("second");
				eventsDone.countDown();
			}
		};
		new Thread(fireSecond).start();
		eventsDone.await();
		assertEquals(States.TWO, stateMachine.getCurrentState());
	}

	private class BlockingCondition implements Condition {
		CountDownLatch latch = new CountDownLatch(1);
		private final CountDownLatch inCondition;

		public BlockingCondition(CountDownLatch inCondition) {
			this.inCondition = inCondition;
		}

		@Override
		public boolean isSatisfied(Arguments args) {
			inCondition.countDown();
			try {
				latch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return true;
		}
	}
}
