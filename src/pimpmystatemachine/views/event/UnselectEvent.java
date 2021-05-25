/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pimpmystatemachine.views.event;

import javafx.event.Event;
import static javafx.event.Event.ANY;
import javafx.event.EventType;

/**
 * Custom event used for unselecting nodes
 *
 * @author Justin ROSSMANN
 */
public class UnselectEvent extends Event {

    /**
     *
     */
    public static final EventType<UnselectEvent> UNSELECT_EVENT = new EventType<>(ANY, "UNSELECT_EVENT");

    /**
     *
     * @param eventType type of event
     */
    public UnselectEvent(EventType<? extends UnselectEvent> eventType) {
        super(eventType);
    }
}
