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
import javafx.geometry.Point2D;
import javafx.scene.shape.Line;
import pimpmystatemachine.views.gui.CenterComponent;
import pimpmystatemachine.views.statemachinecomponents.RealStateView;

/**
 * Class representing a StateMachine used for Transition Creation
 *
 * @author Justin ROSSMANN
 */
public class TransitionCreationStateMachine extends StateMachine {

    private RealStateView origin;
    private RealStateView destination;
    private Line templine = new Line();

    /**
     *
     */
    public State idle = new State() {
        Transition press = new Transition<Press>() {
            @Override
            public void action() {
                origin = (RealStateView) evt.graphicItem;
                templine.setMouseTransparent(true);
                templine.setStartX(origin.getLayoutX());
                templine.setStartY(origin.getLayoutY());
                templine.setEndX(origin.getLayoutX());
                templine.setEndY(origin.getLayoutY());
                ((CenterComponent) origin.getParent()).getChildren().add(templine);
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
                templine.setEndX(evt.p.x);
                templine.setEndY(evt.p.y);
            }

            @Override
            public State goTo() {
                return dragging;
            }
        };
        Transition release = new Transition<Release>() {
            @Override
            public void action() {
                CenterComponent center = (CenterComponent) origin.getParent();
                if (evt.graphicItem instanceof RealStateView) {
                    destination = (RealStateView) evt.graphicItem;
                    center.getStateMachine().addTransition(center, origin.getState(), destination.getState());
                } else if (evt.graphicItem instanceof CenterComponent) {
                    center.getStateMachine().addPhantomTransition(center, origin.getState(), new Point2D(evt.p.x, evt.p.y));
                }

                ((CenterComponent) origin.getParent()).getChildren().remove(templine);
                origin = null;
                destination = null;
            }

            @Override
            public State goTo() {
                return idle;
            }
        };
    };
}
