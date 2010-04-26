package se.hiflyer.fettle;

public class BasicConditions {

	private BasicConditions() {
	}

	public static final Condition ALWAYS = new Condition() {
		@Override
		public boolean isSatisfied() {
			return true;
		}
	};

	public static Condition and(final Condition first, final Condition second) {
		return new Condition() {
			@Override
			public boolean isSatisfied() {
				return first.isSatisfied() && second.isSatisfied();
			}
		};
	}

	public static Condition and(final Condition... conditions) {
		return new Condition() {
			@Override
			public boolean isSatisfied() {
				for (Condition condition : conditions) {
					if (!condition.isSatisfied()) {
						return false;
					}
				}
				return true;
			}
		};
	}

	public static Condition or(final Condition first, final Condition second) {
		return new Condition() {
			@Override
			public boolean isSatisfied() {
				return first.isSatisfied() || second.isSatisfied();
			}
		};
	}

	public static Condition or(final Condition... conditions) {
		return new Condition() {
			@Override
			public boolean isSatisfied() {
				for (Condition condition : conditions) {
					if (condition.isSatisfied()) {
						return true;
					}
				}
				return false;
			}
		};
	}
}
