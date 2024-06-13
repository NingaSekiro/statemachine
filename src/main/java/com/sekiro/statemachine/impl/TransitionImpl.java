package com.sekiro.statemachine.impl;

import com.sekiro.statemachine.*;
import org.springframework.messaging.Message;


/**
 * transition impl
 *
 * @author NingaSekiro
 * @date 2024/06/13
 */
public class TransitionImpl<S, E> implements Transition<S, E> {

    private State<S, E> source;

    private State<S, E> target;

    private E event;

    private Condition<S, E> condition;

    private Action<S, E> action;

    private Listener<S, E> listener;

    private TransitionType type = TransitionType.EXTERNAL;

    @Override
    public State<S, E> getSource() {
        return source;
    }

    @Override
    public void setSource(State<S, E> state) {
        this.source = state;
    }

    @Override
    public E getEvent() {
        return this.event;
    }

    @Override
    public void setEvent(E event) {
        this.event = event;
    }

    @Override
    public void setType(TransitionType type) {
        this.type = type;
    }

    @Override
    public State<S, E> getTarget() {
        return this.target;
    }

    @Override
    public void setTarget(State<S, E> target) {
        this.target = target;
    }

    @Override
    public Condition<S, E> getCondition() {
        return this.condition;
    }

    @Override
    public void setCondition(Condition<S, E> condition) {
        this.condition = condition;
    }

    @Override
    public Action<S, E> getAction() {
        return this.action;
    }

    @Override
    public void setAction(Action<S, E> action) {
        this.action = action;
    }

    @Override
    public Listener<S, E> getListener() {
        return this.listener;
    }

    @Override
    public void setListener(Listener<S, E> listener) {
        this.listener = listener;
    }


    @Override
    public State<S, E> transit(Message<E> ctx) {
        this.verify();
        StateContextImpl<S, E> stateContext = new StateContextImpl<>(ctx, this, source, target,
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
    public final String toString() {
        return source + "-[" + event.toString() + ", " + type + "]->" + target;
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

    @Override
    public void verify() {
        if (type == TransitionType.INTERNAL && source != target) {
            throw new StateMachineException(String.format("Internal transition source state '%s' " +
                    "and target state '%s' must be same.", source, target));
        }
    }
}