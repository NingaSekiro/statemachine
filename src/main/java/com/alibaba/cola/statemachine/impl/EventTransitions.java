package com.alibaba.cola.statemachine.impl;

import com.alibaba.cola.statemachine.Transition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * EventTransitions
 * <p>
 * 同一个Event可以触发多个Transitions，<a href="https://github.com/alibaba/COLA/pull/158">...</a>
 *
 * @author Frank Zhang
 * @date 2021-05-28 5:17 PM
 */
public class EventTransitions<S, E> {
    private final HashMap<E, List<Transition<S, E>>> eventTransitions;

    public EventTransitions() {
        eventTransitions = new HashMap<>();
    }

    public void put(E event, Transition<S, E> transition) {
        if (eventTransitions.get(event) == null) {
            List<Transition<S, E>> transitions = new ArrayList<>();
            transitions.add(transition);
            eventTransitions.put(event, transitions);
        } else {
            List<Transition<S, E>> existingTransitions = eventTransitions.get(event);
            verify(existingTransitions, transition);
            existingTransitions.add(transition);
        }
    }

    /**
     * Per one source and target state, there is only one transition is allowed
     *
     */
    private void verify(List<Transition<S, E>> existingTransitions, Transition<S, E> newTransition) {
        for (Transition<S, E> transition : existingTransitions) {
            if (transition.equals(newTransition)) {
                throw new StateMachineException(transition + " already Exist, you can not add another one");
            }
        }
    }

    public List<Transition<S, E>> get(E event) {
        return eventTransitions.get(event);
    }

    public List<Transition<S, E>> allTransitions() {
        List<Transition<S, E>> allTransitions = new ArrayList<>();
        for (List<Transition<S, E>> transitions : eventTransitions.values()) {
            allTransitions.addAll(transitions);
        }
        return allTransitions;
    }
}