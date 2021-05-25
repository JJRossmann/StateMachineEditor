/*
 * Copyright (c) 2016 Stéphane Conversy - ENAC - All rights Reserved
 * Modified by Nicolas Saporito - ENAC (28/04/2017) -> fixed comparison with doubles
 */
package fr.liienac.statemachine.geometry;

public class Vector {

    /**
     *
     */
    public double dx,
            /**
             *
             */
            dy;

    /**
     *
     * @param dx_ dx
     * @param dy_ dy
     */
    public Vector(double dx_, double dy_) {
        dx = dx_;
        dy = dy_;
    }

    /**
     *
     * @param p1 point1
     * @param p2 point2
     */
    public Vector(Point p1, Point p2) {
        dx = p2.x - p1.x;
        dy = p2.y - p1.y;
    }

    /**
     *
     * @param p point
     * @return equals
     */
    public boolean equals(Vector p) {
        return Double.compare(dx, p.dx) == 0
                && Double.compare(dy, p.dy) == 0;
    }

    /**
     *
     * @param p1 vector1
     * @param p2 vector2
     * @return Vector
     */
    static public Vector plus(Vector p1, Vector p2) {
        double dx = (p1.dx + p2.dx);
        double dy = (p1.dy + p2.dy);
        return new Vector(dx, dy);
    }

    /**
     *
     * @param p1 vector1
     * @param p2 vector2
     * @return vector
     */
    static public Vector minus(Vector p1, Vector p2) {
        double dx = (p1.dx - p2.dx);
        double dy = (p1.dy - p2.dy);
        return new Vector(dx, dy);
    }

    /**
     *
     * @param p1 vector1
     * @param d double
     * @return div
     */
    static public Vector div(Vector p1, float d) {
        double dx = p1.dx / d;
        double dy = p1.dy / d;
        return new Vector(dx, dy);
    }

    /**
     *
     * @return norm
     */
    public double normSq() {
        return dx * dx + dy * dy;
    }

    /**
     *
     * @return norm
     */
    public double norm() {
        return Math.sqrt(normSq());
    }

    /**
     *
     * @param u vector1
     * @param v vector2
     * @return scalarProduct
     */
    static public double scalarProduct(Vector u, Vector v) {
        return u.dx * v.dx + u.dy * v.dy;
    }

    /**
     *
     * @param u vector1
     * @param v vector2
     * @return crossProduct
     */
    static public double crossProduct(Vector u, Vector v) {
        return u.dx * v.dy - u.dy * v.dx;
    }
}
