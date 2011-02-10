package se.hiflyer.fettle;

public interface MutableStateMachine<S, E> extends StateMachine<S, E>, TransitionModel<S, E> {
}
