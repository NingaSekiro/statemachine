package com.github.ningasekiro.impl;

import com.github.ningasekiro.*;
import lombok.Data;
import org.springframework.messaging.Message;


/**
 * transition impl
 *
 * @author NingaSekiro
 * @date 2024/06/13
 */
@Data
public class TransitionImpl<S, E> implements Transition<S, E> {

    private State<S, E> source;

    private State<S, E> target;

    private E event;

    private Condition<S, E> condition;

    private Action<S, E> action;

    private Listener<S, E> listener;

    @Override
    public State<S, E> transit(Message<E> ctx) {
        StateContext<S, E> stateContext = new StateContext<>(ctx, this,
                null);
        if (action != null) {
            action.execute(stateContext);
        }
        if (listener != null) {
            try {
                listener.stateChanged(stateContext);
            } catch (Throwable ignored) {
            }
        }
        return target;
    }


    @Override
    public boolean equals(Object anObject) {
        if (anObject instanceof Transition) {
            Transition<?, ?> other = (Transition<?, ?>) anObject;
            return this.event.equals(other.getEvent())
                    && this.source.equals(other.getSource())
                    && this.target.equals(other.getTarget());
        }
        return false;
    }
}
