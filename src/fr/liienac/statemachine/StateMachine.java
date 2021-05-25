/*
 * Copyright (c) 2016-2018 Stéphane Conversy - ENAC - All rights Reserved
 * Modified by Nicolas Saporito - ENAC (06/04/2017):
 *     added generics reflection
 *     added getCurrentState()
 *     added initialize() to fix leaking this in constructor
 *     modified goTo() to leave() and enter() only on actual state changes
 * Modified by Stéphane Conversy & Nicolas Saporito (07/10/2020):
 *     added debug mode
 *     modified initialize() to make first state pass by it's enter() method.
 */
package fr.liienac.statemachine;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class StateMachine {

    /**
     *
     */
    protected State current = null;

    /**
     *
     */
    protected State first = null;

    /**
     *
     */
    protected boolean debug = false;

    /**
     *
     * @return current
     */
    public State getCurrentState() {
        return current;
    }

    /**
     *
     * @param d value to set
     */
    public void setDebug(boolean d) {
        debug = d;
    }

    /**
     *
     * @param <Event> event
     * @param evt event
     */
    public <Event> void handleEvent(Event evt) {
        current.handleEvent(evt);
    }

    private void goTo(State s) {
        if (current != s) {
            if (current != null) {
                current.leave();
            }
            if (debug) {
                State fst = first;
                current = null; // current...
                first = null; //... and first belong to the set of fields!
                debug(s, this, "Now in state");
                first = fst;
            }
            current = s;
            current.enter();
        }
    }

    /**
     *
     */
    public class State {

        Map<Object, ArrayList<Transition>> transitionsPerType = new HashMap<>(); // with static type checking

        /**
         *
         */
        public State() {
            initialize();
        }

        private void initialize() {
            // first state is the initial state
            if (first == null) {
                first = this;
                goTo(first);
            }
        }

        /**
         *
         */
        protected void enter() {
        }

        /**
         *
         */
        protected void leave() {
        }

        // Hack for generics reflection: Type arguments to generic classes
        // are not available for reflection at runtime, unless...
        // they come from a generic superclass, so here it is
        private class MotherOfAllTransitions<EventT> {
        }

        /**
         *
         * @param <EventT> event
         */
        public class Transition<EventT> extends MotherOfAllTransitions<EventT> {

            /**
             *
             */
            public Transition() {
                // generics reflection to get the specific event type from the generic type specified in this class
                ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
                Class clazz = (Class) parameterizedType.getActualTypeArguments()[0];

                // register transition in state
                Transition t = this;
                ArrayList<Transition> ts = transitionsPerType.get(clazz);
                if (ts == null) {
                    ts = new ArrayList<>();
                    ts.add(t);
                    transitionsPerType.put(clazz, ts);
                } else {
                    ts.add(t);
                }
            }

            /**
             *
             */
            protected EventT evt;

            /**
             *
             * @return true
             */
            protected boolean guard() {
                return true;
            }

            /**
             *
             */
            protected void action() {
            }

            /**
             *
             * @return current
             */
            protected State goTo() {
                return current;
            }
        }

        /**
         *
         * @param <EventT> event
         * @param evt event
         */
        protected <EventT> void handleEvent(EventT evt) {
            ArrayList<Transition> ts = transitionsPerType.get(evt.getClass());
            if (ts == null) {
                return;
            }
            for (Transition t : ts) {
                t.evt = evt;
                if (t.guard()) {
                    if (debug) {
                        debug(t, this, "  Transition");
                    }
                    t.action();
                    StateMachine.this.goTo(t.goTo());
                    break;
                }
            }
        }
    }

    private void debug(Object toDebug, Object toCheckAgainst, String header) {
        for (Field f : getAllFields(toCheckAgainst.getClass())) {
            f.setAccessible(true);
            try {
                if (toDebug == f.get(toCheckAgainst)) {
                    System.out.println(header + ": " + f.getName());
                    break;
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
            }
        }
    }

    private ArrayList<Field> getAllFields(Class clazz) {
        // Get all fields from a class and its super classes:
        // Uses getDeclaredFields() instead of getFields() in order to get
        // not only the public fields but also the protected and private ones.
        // But getDeclaredFields() doesn't return the inherited fields, so this
        // method must recursively go up the inheritance chain.
        ArrayList<Field> fields = new ArrayList<Field>();
        fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        Class superClazz = clazz.getSuperclass();
        if (superClazz != null) {
            fields.addAll(getAllFields(superClazz));
        }
        return fields;
    }
}
