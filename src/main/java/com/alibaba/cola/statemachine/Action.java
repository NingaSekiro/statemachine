package com.alibaba.cola.statemachine;

public interface Action<S, E> {
    public void execute(StateContext<S, E> stateContext);
}