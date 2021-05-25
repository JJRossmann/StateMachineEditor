/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pimpmystatemachine.views.managers;

import dragpanzoom.views.TranslatableHomotheticPane;
import fr.liienac.statemachine.event.Click;
import pimpmystatemachine.views.statemachine.event.DoubleClick;
import pimpmystatemachine.views.statemachine.event.Enter;
import pimpmystatemachine.views.statemachine.event.Exit;
import fr.liienac.statemachine.event.Release;
import fr.liienac.statemachine.geometry.Point;
import java.util.ArrayList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import pimpmystatemachine.views.event.UnselectEvent;
import pimpmystatemachine.views.gui.CenterComponent;
import pimpmystatemachine.views.statemachines.SituationStateMachine;

/**
 * Class for handling the situation of a component
 *
 * @author Justin ROSSMANN
 */
public class SituationManager {

    TranslatableHomotheticPane thp;
    private final SituationStateMachine sm = new SituationStateMachine();

    /**
     *
     * @param thp pane for this manager
     */
    public SituationManager(TranslatableHomotheticPane thp) {
        this.thp = thp;
        thp.addEventHandler(MouseEvent.MOUSE_ENTERED, onMouseEnteredEventHandler);
        thp.addEventHandler(MouseEvent.MOUSE_EXITED, onMouseExitedEventHandler);
        thp.addEventHandler(MouseEvent.MOUSE_CLICKED, onMouseClickedEventHandler);
        thp.addEventHandler(UnselectEvent.UNSELECT_EVENT, onUnselectEventHandler);
    }

    private final EventHandler<MouseEvent> onMouseEnteredEventHandler = (MouseEvent event) -> {
        sm.handleEvent(new Enter(new Point(event.getX(), event.getY()), event.getSource()));
        event.consume();
    };

    private final EventHandler<MouseEvent> onMouseExitedEventHandler = (MouseEvent event) -> {
        sm.handleEvent(new Exit(new Point(event.getX(), event.getY()), event.getSource()));
        event.consume();
    };

    private final EventHandler<MouseEvent> onMouseClickedEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            if (event.getClickCount() == 1) {
                CenterComponent c = ((CenterComponent) thp.getParent());
                //if shortcut is not down, unselect all other nodes
                if (!(event.isShortcutDown())) {
                    //make a copy of the arraylist to avoid concurrent modification exception
                    ArrayList<Node> nodes = new ArrayList<>(c.getSelectedNodes());
                    nodes.remove(thp);
                    nodes.forEach(n -> {
                        Event.fireEvent(n, new UnselectEvent(UnselectEvent.UNSELECT_EVENT));
                    });
                }
                if (!c.getSelectedNodes().contains(thp)) {
                    c.getSelectedNodes().add(thp);
                }
                sm.handleEvent(new Click(new Point(event.getX(), event.getY()), event.getSource()));

            } else if (event.getClickCount() == 2) {
                sm.handleEvent(new DoubleClick(new Point(event.getX(), event.getY()), event.getSource()));
            }
            event.consume();
        }
    };

    private final EventHandler<UnselectEvent> onUnselectEventHandler = (UnselectEvent event) -> {
        sm.handleEvent(new Release(new Point(0, 0), event.getSource()));
        CenterComponent c = ((CenterComponent) thp.getParent());
        c.getSelectedNodes().remove(thp);
        event.consume();
    };
}
