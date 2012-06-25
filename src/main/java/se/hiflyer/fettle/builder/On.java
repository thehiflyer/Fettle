package se.hiflyer.fettle.builder;

import se.hiflyer.fettle.Condition;

public interface On<S, E, C> extends When<S, E, C> {

	When<S, E, C> when(Condition<C> condition);
}
