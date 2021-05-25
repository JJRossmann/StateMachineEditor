/*
 * Copyright (c) 2016 Stéphane Conversy - ENAC - All rights Reserved
 */
package fr.liienac.statemachine.event;

import fr.liienac.statemachine.geometry.Point;

/**
 *
 *
 * @param <Item> item
 */
public class Release<Item> extends PositionalEvent {

    /**
     * Constructor for mouse
     *
     * @param p_ point
     * @param s_ item
     */
    public Release(Point p_, Item s_) {
        super(p_, s_);
    }

    /**
     * Constructor for multitouch without orientation
     *
     * @param cursorid_ id
     * @param p_ point
     * @param s_ item
     */
    public Release(int cursorid_, Point p_, Item s_) {
        super(cursorid_, p_, s_);
    }

    /**
     * Constructor for multitouch with orientation
     *
     * @param cursorid_ id
     * @param p_ point
     * @param s_ item
     * @param angRad angle
     */
    public Release(int cursorid_, Point p_, Item s_, float angRad) {
        super(cursorid_, p_, s_, angRad);
    }
}
