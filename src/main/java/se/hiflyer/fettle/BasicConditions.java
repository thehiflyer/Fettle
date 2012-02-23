package se.hiflyer.fettle;

public class BasicConditions {

	private BasicConditions() {
	}

	public static final Condition ALWAYS = new Condition() {
		@Override
		public boolean isSatisfied(Arguments args) {
			return true;
		}
	};

	public static Condition and(final Condition first, final Condition second) {
		return new Condition() {
			@Override
			public boolean isSatisfied(Arguments args) {
				return first.isSatisfied(args) && second.isSatisfied(args);
			}
		};
	}

	public static Condition and(final Condition... conditions) {
		return new Condition() {
			@Override
			public boolean isSatisfied(Arguments args) {
				for (Condition condition : conditions) {
					if (!condition.isSatisfied(args)) {
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
			public boolean isSatisfied(Arguments args) {
				return first.isSatisfied(args) || second.isSatisfied(args);
			}
		};
	}

	public static Condition or(final Condition... conditions) {
		return new Condition() {
			@Override
			public boolean isSatisfied(Arguments args) {
				for (Condition condition : conditions) {
					if (condition.isSatisfied(args)) {
						return true;
					}
				}
				return false;
			}
		};
	}
}
