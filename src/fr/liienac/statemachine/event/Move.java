/*
 * Copyright (c) 2016 Stéphane Conversy - ENAC - All rights Reserved
 */
package fr.liienac.statemachine.event;

import fr.liienac.statemachine.geometry.Point;

/**
 *
 * @param <Item> item
 */
public class Move<Item> extends PositionalEvent<Item> {

    /**
     * Constructor for mouse
     *
     * @param p_ point
     * @param s_ item
     */
    public Move(Point p_, Item s_) {
        super(p_, s_);
    }

    /**
     * Constructor for multitouch without orientation
     *
     * @param cursorid_ id
     * @param p_ point
     * @param s_ item
     */
    public Move(int cursorid_, Point p_, Item s_) {
        super(cursorid_, p_, s_);
    }

    /**
     * Constructor for multitouch without orientation
     *
     * @param cursorid_ id
     * @param p_ point
     * @param s_ item
     * @param angRad angle
     */
    public Move(int cursorid_, Point p_, Item s_, float angRad) {
        super(cursorid_, p_, s_, angRad);
    }
}
