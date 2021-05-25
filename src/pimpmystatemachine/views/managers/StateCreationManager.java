/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pimpmystatemachine.views.managers;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import pimpmystatemachine.views.gui.CenterComponent;
import pimpmystatemachine.views.statemachinecomponents.RealStateView;

/**
 * Class for handling the creation of a State
 *
 * @author Justin ROSSMANN
 */
public class StateCreationManager {

    RealStateView originState = null;
    RealStateView destinationState = null;
    CenterComponent center;

    /**
     * Constructor for StateCreationManager
     *
     * @param center center component for this manager
     */
    public StateCreationManager(CenterComponent center) {
        this.center = center;
        center.addEventHandler(MouseEvent.MOUSE_CLICKED, onMouseClickedEventHandler);
    }

    private final EventHandler<MouseEvent> onMouseClickedEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            //use double click
            if (event.getClickCount() == 2) {
                center.getStateMachine().addState(new Point2D(event.getX(), event.getY()));
                event.consume();
            }
        }
    };
}
