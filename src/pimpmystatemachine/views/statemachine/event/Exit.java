package pimpmystatemachine.views.statemachine.event;

import fr.liienac.statemachine.event.PositionalEvent;
import fr.liienac.statemachine.geometry.Point;

/**
 * Custom Event for the StateMachine library usage
 *
 * @author Justin ROSSMANN
 * @param <Item> item
 */
public class Exit<Item> extends PositionalEvent<Item> {

    /**
     * Constructor
     *
     * @param p_ point
     * @param s_ item
     */
    public Exit(Point p_, Item s_) {
        super(p_, s_);
    }
}
