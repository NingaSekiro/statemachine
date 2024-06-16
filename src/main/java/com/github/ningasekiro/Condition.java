package com.github.ningasekiro;


public interface Condition<S, E> {

    /**
     * @param context context object
     * @return whether the context satisfied current condition
     */
    boolean isSatisfied(StateContext<S, E> context);

    default String name() {
        return this.getClass().getSimpleName();
    }
}