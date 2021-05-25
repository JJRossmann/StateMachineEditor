package pimpmystatemachine.views.statemachine.event;

import fr.liienac.statemachine.event.PositionalEvent;
import fr.liienac.statemachine.geometry.Point;

/**
 * Custom Event for the StateMachine library usage
 *
 * @author Justin ROSSMANN
 * @param <Item> item
 */
public class DoubleClick<Item> extends PositionalEvent<Item> {

    /**
     *
     */
    public int num;

    /**
     * Constructor
     *
     * @param p_ point
     * @param s_ item
     */
    public DoubleClick(Point p_, Item s_) {
        super(p_, s_);
        num = 0;
    }
}
