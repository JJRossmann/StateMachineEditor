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
 * Custom event used for firing saves
 *
 * @author Justin ROSSMANN
 */
public class SaveEvent extends Event {

    /**
     *
     */
    public static final EventType<SaveEvent> SAVE_EVENT = new EventType<>(ANY, "SAVE_EVENT");

    /**
     *
     * @param eventType type of event
     */
    public SaveEvent(EventType<? extends SaveEvent> eventType) {
        super(eventType);
    }
}
