/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pimpmystatemachine.views.statemachines;

import dragpanzoom.views.TranslatableHomotheticPane;
import fr.liienac.statemachine.StateMachine;
import fr.liienac.statemachine.event.Click;
import pimpmystatemachine.views.statemachine.event.DoubleClick;
import pimpmystatemachine.views.statemachine.event.Enter;
import pimpmystatemachine.views.statemachine.event.Exit;
import fr.liienac.statemachine.event.Release;
import pimpmystatemachine.views.statemachinecomponents.RealStateView;
import pimpmystatemachine.views.statemachinecomponents.TransitionView;

/**
 * Class representing a StateMachine used for the Situation of a component
 *
 * @author Justin ROSSMANN
 */
public class SituationStateMachine extends StateMachine {

    TranslatableHomotheticPane thp;
    boolean state;

    /**
     *
     */
    public State idle = new State() {
        Transition enter = new Transition<Enter>() {
            @Override
            public void action() {
                thp = (TranslatableHomotheticPane) evt.graphicItem;
                state = thp instanceof RealStateView;
            }

            @Override
            public State goTo() {
                return hovered;
            }
        };
    };

    /**
     *
     */
    public State hovered = new State() {
        protected void enter() {
            if (state) {
                ((RealStateView) thp).setHovered(true);
            } else {
                ((TransitionView) thp).setHovered(true);
            }
        }

        protected void leave() {
            if (state) {
                ((RealStateView) thp).setHovered(false);
            } else {
                ((TransitionView) thp).setHovered(false);
            }
        }

        Transition click = new Transition<Click>() {
            @Override
            public State goTo() {
                return selected;
            }
        };
        Transition exit = new Transition<Exit>() {
            @Override
            public State goTo() {
                return idle;
            }
        };
    };

    /**
     *
     */
    public State selected = new State() {
        protected void enter() {
            if (state) {
                ((RealStateView) thp).setSelected(true);
            } else {
                ((TransitionView) thp).setSelected(true);
            }
        }
        Transition doubleclick = new Transition<DoubleClick>() {
            @Override
            public State goTo() {
                return editing;
            }
        };
        Transition unhover = new Transition<Exit>() {
            @Override
            public void action() {
                if (state) {
                    ((RealStateView) thp).setHovered(false);
                } else {
                    ((TransitionView) thp).setHovered(false);
                }
            }

            @Override
            public State goTo() {
                return selected;
            }
        };
        Transition hover = new Transition<Enter>() {
            @Override
            public void action() {
                if (state) {
                    ((RealStateView) thp).setHovered(true);
                } else {
                    ((TransitionView) thp).setHovered(true);
                }
            }

            @Override
            public State goTo() {
                return selected;
            }
        };
        Transition unselect = new Transition<Release>() {
            @Override
            public void action() {
                if (state) {
                    ((RealStateView) thp).setSelected(false);
                    ((RealStateView) thp).setHovered(false);
                } else {
                    ((TransitionView) thp).setSelected(false);
                    ((TransitionView) thp).setHovered(false);
                }
            }

            @Override
            public State goTo() {
                return idle;
            }
        };
    };

    /**
     *
     */
    public State editing = new State() {
        protected void enter() {
            if (state) {
                ((RealStateView) thp).setEditing(true);
            } else {
                ((TransitionView) thp).setEditing(true);
            }
        }

        protected void leave() {
            if (state) {
                ((RealStateView) thp).setEditing(false);
            } else {
                ((TransitionView) thp).setEditing(false);
            }
        }

        State.Transition back = new State.Transition<DoubleClick>() {
            @Override
            public State goTo() {
                return selected;
            }
        };
        Transition unselect = new Transition<Release>() {
            @Override
            public void action() {
                if (state) {
                    ((RealStateView) thp).setSelected(false);
                    ((RealStateView) thp).setHovered(false);
                } else {
                    ((TransitionView) thp).setSelected(false);
                    ((TransitionView) thp).setHovered(false);
                }
            }

            @Override
            public State goTo() {
                return idle;
            }
        };
    };

}
