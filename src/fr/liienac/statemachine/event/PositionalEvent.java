/*
 * Copyright (c) 2016 Stéphane Conversy - ENAC - All rights Reserved
 */
package fr.liienac.statemachine.event;

import fr.liienac.statemachine.geometry.Point;

/**
 *
 * @param <Item> item
 */
public class PositionalEvent<Item> extends Event {

    /**
     *
     */
    public int cursorID;

    /**
     *
     */
    public Point p;

    /**
     *
     */
    public Item graphicItem;

    /**
     *
     */
    public float orientation;

    /**
     * Constructor for mouse
     *
     * @param p_ point
     * @param s_ item
     */
    public PositionalEvent(Point p_, Item s_) {
        cursorID = 0;
        p = p_;
        graphicItem = s_;
        orientation = 0;
    }

    /**
     * Constructor for multitouch without orientation
     *
     * @param cursorid_ id
     * @param p_ point
     * @param s_ item
     */
    public PositionalEvent(int cursorid_, Point p_, Item s_) {
        cursorID = cursorid_;
        p = p_;
        graphicItem = s_;
        orientation = 0;
    }

    /**
     * Constructor for multitouch with orientation
     */
    PositionalEvent(int cursorid_, Point p_, Item s_, float o_) {
        cursorID = cursorid_;
        p = p_;
        graphicItem = s_;
        orientation = o_;
    }
}
