/*
 * Copyright (c) 2016 Stéphane Conversy, Nicolas Saporito - ENAC - All rights Reserved
 */
package fr.liienac.statemachine.graphic;

import fr.liienac.statemachine.geometry.Point;
import fr.liienac.statemachine.geometry.Vector;

public interface GraphicItem {

    /**
     *
     * @param r_ red
     * @param g_ green
     * @param b_ blue
     */
    void setStyle(int r_, int g_, int b_);

    /**
     *
     * @param v vector
     */
    void translateBy(Vector v);

    /**
     *
     * @param dangle angle
     * @param center center
     */
    void rotateBy(float dangle, Point center);

    // Rotate about center with an angle specified by two vectors.
    // Avoids the use of acos
    /**
     *
     * @param u vector1
     * @param v vector2
     * @param center center
     */
    void rotateBy(Vector u, Vector v, Point center);

    /**
     *
     * @param ds ds
     * @param center center
     */
    void scaleBy(float ds, Point center);
}
