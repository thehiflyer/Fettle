package se.hiflyer.fettle.builder;

import se.hiflyer.fettle.Action;
import se.hiflyer.fettle.Condition;

import java.util.List;

public interface On<S, E> extends When<S, E> {

	When<S, E> when(Condition condition);
}
