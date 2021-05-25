/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pimpmystatemachine.views.managers;

import fr.liienac.statemachine.event.Move;
import fr.liienac.statemachine.event.Press;
import fr.liienac.statemachine.event.Release;
import fr.liienac.statemachine.geometry.Point;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import pimpmystatemachine.views.gui.CenterComponent;
import pimpmystatemachine.views.statemachinecomponents.PhantomStateView;
import pimpmystatemachine.views.statemachinecomponents.RealStateView;
import pimpmystatemachine.views.statemachines.TransitionChangeStateMachine;
import pimpmystatemachine.views.statemachines.TransitionCreationStateMachine;

/**
 * Class for handling Transition Creation and start/end change
 *
 * @author Justin ROSSMANN
 */
public class TransitionCreationChangeManager {

    CenterComponent center;
    TransitionCreationStateMachine tsm = new TransitionCreationStateMachine();
    TransitionChangeStateMachine tcm = new TransitionChangeStateMachine();

    /**
     * Constructor for TransitionCreationChangeManager
     *
     * @param center center component
     */
    public TransitionCreationChangeManager(CenterComponent center) {
        this.center = center;
        center.setOnDragDetected(onDragDetectedEventHandler);
        center.setOnMouseDragged(onMouseDraggedEventHandler);
        center.setOnMouseDragReleased(onMouseDragReleasedEventHandler);

    }

    private final EventHandler<MouseEvent> onDragDetectedEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            center.startFullDrag();
            if ((!event.isShiftDown())) {
                if (event.getTarget() instanceof Circle) {
                    Circle c = (Circle) event.getTarget();
                    if (c.getParent() instanceof RealStateView) {
                        RealStateView sv = (RealStateView) c.getParent();
                        tsm.handleEvent(new Press(new Point(event.getX(), event.getY()), sv));
                    }
                }
            } else if (event.isShiftDown()) {
                if (event.getTarget() instanceof Circle) {
                    Circle c = (Circle) event.getTarget();
                    if (c.getParent() instanceof PhantomStateView) {
                        PhantomStateView psv = (PhantomStateView) c.getParent();
                        tcm.handleEvent(new Press(new Point(event.getX(), event.getY()), psv));
                    }
                }
            }
            event.consume();
        }
    };

    private final EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            if ((!event.isShiftDown())) {
                tsm.handleEvent(new Move(new Point(event.getX(), event.getY()), event.getSource()));
            }
            event.consume();
        }
    };

    private final EventHandler<MouseEvent> onMouseDragReleasedEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            if ((!event.isShiftDown())) {
                if (event.getTarget() instanceof Circle) {
                    Circle c = (Circle) event.getTarget();
                    if (c.getParent() instanceof RealStateView) {
                        RealStateView sv = (RealStateView) c.getParent();
                        tsm.handleEvent(new Release(new Point(event.getX(), event.getY()), sv));
                    } else {
                        tsm.handleEvent(new Release(new Point(event.getX(), event.getY()), center));
                    }
                } else {
                    tsm.handleEvent(new Release(new Point(event.getX(), event.getY()), center));
                }
            } else if (event.isShiftDown()) {
                if (event.getTarget() instanceof Circle) {
                    Circle c = (Circle) event.getTarget();
                    if (c.getParent() instanceof RealStateView) {
                        RealStateView sv = (RealStateView) c.getParent();
                        tcm.handleEvent(new Release(new Point(event.getX(), event.getY()), sv));
                    } else {
                        tcm.handleEvent(new Release(new Point(event.getX(), event.getY()), center));
                    }
                } else {
                    tcm.handleEvent(new Release(new Point(event.getX(), event.getY()), center));
                }
            }
            event.consume();
        }
    };
}
