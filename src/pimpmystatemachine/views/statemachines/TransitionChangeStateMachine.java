/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pimpmystatemachine.views.statemachines;

import fr.liienac.statemachine.StateMachine;
import fr.liienac.statemachine.event.Move;
import fr.liienac.statemachine.event.Press;
import fr.liienac.statemachine.event.Release;
import pimpmystatemachine.model.graphic.TransitionGraphicModel;
import pimpmystatemachine.views.gui.CenterComponent;
import pimpmystatemachine.views.statemachinecomponents.PhantomStateView;
import pimpmystatemachine.views.statemachinecomponents.RealStateView;

/**
 * Class representing a StateMachine used for Transition changes
 *
 * @author Justin ROSSMANN
 */
public class TransitionChangeStateMachine extends StateMachine {

    private PhantomStateView psv;

    /**
     *
     */
    public State idle = new State() {
        Transition press = new Transition<Press>() {
            @Override
            public void action() {
                psv = (PhantomStateView) evt.graphicItem;
                psv.toFront();
                psv.setMouseTransparent(true);
            }

            @Override
            public State goTo() {
                return dragging;
            }
        };
    };

    /**
     *
     */
    public State dragging = new State() {
        Transition drag = new Transition<Move>() {
            @Override
            public void action() {
                psv.translate(evt.p.x, evt.p.y);
            }

            @Override
            public State goTo() {
                return dragging;
            }
        };
        Transition release = new Transition<Release>() {
            @Override
            public void action() {
                CenterComponent center = (CenterComponent) psv.getParent();
                if (evt.graphicItem instanceof RealStateView) {
                    RealStateView rsv = (RealStateView) evt.graphicItem;
                    TransitionGraphicModel tgm = psv.getTransition();
                    if (psv.isDestination()) {
                        tgm.setDestination(rsv.getState());
                    } else {
                        tgm.setOrigin(rsv.getState());
                    }
                    center.getStateMachine().removeState(psv.getState());
                } else if (evt.graphicItem instanceof CenterComponent) {
                    psv.setMouseTransparent(false);
                }
            }

            @Override
            public State goTo() {
                return idle;
            }
        };
    };
}
