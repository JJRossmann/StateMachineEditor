/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pimpmystatemachine.views.managers;

import java.util.ArrayList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import pimpmystatemachine.views.event.UnselectEvent;
import pimpmystatemachine.views.gui.CenterComponent;
import pimpmystatemachine.views.statemachinecomponents.RealStateView;
import pimpmystatemachine.views.statemachinecomponents.TransitionView;

/**
 * Class to handle clicks on the CenterComponent and Deletion and Moving of
 * selected Nodes
 *
 * @author Justin ROSSMANN
 */
public class SelectionDeletionMovingManager {

    private CenterComponent center;

    /**
     *
     * @param center center component for this manager
     */
    public SelectionDeletionMovingManager(CenterComponent center) {
        this.center = center;
        center.addEventHandler(MouseEvent.MOUSE_CLICKED, selectHandler);
        center.addEventFilter(KeyEvent.KEY_PRESSED, deletionHandler);
        center.addEventFilter(KeyEvent.KEY_PRESSED, movingHandler);
    }

    private final EventHandler<MouseEvent> selectHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            if (event.getButton() == MouseButton.PRIMARY) {
                center.getCenterContextMenu().hide();
                //make a copy of the arraylist to avoid concurrent modification exception
                ArrayList<Node> nodes = new ArrayList<>(center.getChildren());
                for (Node n : nodes) {
                    if (n instanceof RealStateView || n instanceof TransitionView) {
                        Event.fireEvent(n, new UnselectEvent(UnselectEvent.UNSELECT_EVENT));
                    }
                }
            }
            event.consume();
        }
    };

    private final EventHandler<KeyEvent> deletionHandler = new EventHandler<KeyEvent>() {
        public void handle(KeyEvent ke) {
            if (ke.getCode() == KeyCode.DELETE) {
                ArrayList<Node> nodes = new ArrayList(center.getSelectedNodes());
                for (Node n : nodes) {
                    Event.fireEvent(n, new UnselectEvent(UnselectEvent.UNSELECT_EVENT));
                    if (n instanceof RealStateView) {
                        center.getStateMachine().removeState(((RealStateView) n).getState());
                    } else if (n instanceof TransitionView) {
                        center.getStateMachine().removeTransition(((TransitionView) n).getTransition());
                    }
                }
                ke.consume();
            }
        }
    };

    private final EventHandler<KeyEvent> movingHandler = new EventHandler<KeyEvent>() {
        public void handle(KeyEvent ke) {
            KeyCode k = ke.getCode();
            if (k == KeyCode.UP || k == KeyCode.DOWN || k == KeyCode.LEFT || k == KeyCode.RIGHT) {

                double dx = 0, dy = 0;

                switch (ke.getCode()) {
                    case UP:
                        dx = 0;
                        dy = -1;
                        break;
                    case DOWN:
                        dx = 0;
                        dy = 1;
                        break;
                    case LEFT:
                        dx = -1;
                        dy = 0;
                        break;
                    case RIGHT:
                        dx = 1;
                        dy = 0;
                        break;
                    default:
                        break;
                }

                ArrayList<Node> nodes = new ArrayList(center.getSelectedNodes());
                for (Node n : nodes) {
                    if (n instanceof RealStateView) {
                        ((RealStateView) n).translate(dx, dy);
                    }
                }
                ke.consume();
            }
        }
    };

}
