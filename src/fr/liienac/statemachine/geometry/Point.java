/*
 * Copyright (c) 2016 Stéphane Conversy - ENAC - All rights Reserved
 * Modified by Nicolas Saporito - ENAC (28/04/2017) -> fixed comparison with doubles
 */
package fr.liienac.statemachine.geometry;

/**
 *
 */
public class Point {

    /**
     *
     */
    public double x,
            /**
             *
             */
            y;

    /**
     *
     * @param x_ x
     * @param y_ y
     */
    public Point(double x_, double y_) {
        x = x_;
        y = y_;
    }

    /**
     *
     * @param p point
     * @return equals
     */
    public boolean equals(Point p) {
        return Double.compare(x, p.x) == 0
                && Double.compare(y, p.y) == 0;
    }

    /**
     *
     * @param p1 point1
     * @param p2 point2
     * @return equals
     */
    static public Vector minus(Point p1, Point p2) {
        double dx = (p1.x - p2.x);
        double dy = (p1.y - p2.y);
        return new Vector(dx, dy);
    }

    /**
     *
     * @param p1 point1
     * @param p2 point2
     * @return middle
     */
    static public Point middle(Point p1, Point p2) {
        double x = (p1.x + p2.x) / 2;
        double y = (p1.y + p2.y) / 2;
        return new Point(x, y);
    }

    /**
     *
     * @param p1 point1
     * @param p2 point2
     * @return distanceSq
     */
    static public double distanceSq(Point p1, Point p2) {
        return new Vector(p1, p2).normSq();
    }

    /**
     *
     * @param p1 point1
     * @param p2 point2
     * @return distance
     */
    static public double distance(Point p1, Point p2) {
        return Math.sqrt(distanceSq(p1, p2));
    }

}
