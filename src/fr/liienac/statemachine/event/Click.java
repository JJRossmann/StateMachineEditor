/*
 * Copyright (c) 2016 Stéphane Conversy - ENAC - All rights Reserved
 */
package fr.liienac.statemachine.event;

import fr.liienac.statemachine.geometry.Point;

/**
 *
 * @param <Item> item
 */
public class Click<Item> extends PositionalEvent<Item> {

    /**
     *
     */
    public int num;

    /**
     * Constructor for mouse
     *
     * @param p_ point
     * @param s_ item
     */
    public Click(Point p_, Item s_) {
        super(p_, s_);
        num = 0;
    }

    /**
     * Constructor for multitouch without orientation
     *
     * @param cursorid_ id
     * @param p_ point
     * @param s_ item
     * @param num_ num
     */
    public Click(int cursorid_, Point p_, Item s_, int num_) {
        super(cursorid_, p_, s_);
        num = num_;
    }

    /**
     * Constructor for multitouch with orientation
     *
     * @param cursorid_ id
     * @param p_ point
     * @param s_ item
     * @param angRad angle
     * @param num_ num
     */
    public Click(int cursorid_, Point p_, Item s_, float angRad, int num_) {
        super(cursorid_, p_, s_, angRad);
        num = num_;
    }
}
