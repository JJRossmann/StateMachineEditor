/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pimpmystatemachine.model.graphic;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;
import pimpmystatemachine.model.semantics.StateModel;
import pimpmystatemachine.model.semantics.TransitionModel;
import pimpmystatemachine.views.statemachinecomponents.BasicStateView;
import pimpmystatemachine.views.statemachinecomponents.RealStateView;
import pimpmystatemachine.views.statemachinecomponents.TransitionView;

/**
 * Class for the graphical parameters of a Transition
 *
 * @author Justin ROSSMANN
 */
public class TransitionGraphicModel extends TransitionModel {

    private final ObjectProperty<Color> color = new SimpleObjectProperty<>();
    private final SimpleDoubleProperty thickness = new SimpleDoubleProperty();

    //control points of the bezier curve
    private final SimpleDoubleProperty c1X = new SimpleDoubleProperty();
    private final SimpleDoubleProperty c1Y = new SimpleDoubleProperty();
    private final SimpleDoubleProperty c2X = new SimpleDoubleProperty();
    private final SimpleDoubleProperty c2Y = new SimpleDoubleProperty();

    private TransitionView parentView;

    /**
     * Constructor for TransitionGraphicModel
     *
     * @param origin origin
     * @param name name
     * @param color color
     * @param destination destination
     * @param thickness thickness
     */
    public TransitionGraphicModel(StateModel origin, StateModel destination, String name, Color color, double thickness) {
        super(origin, destination, name);
        this.color.set(color);
        this.thickness.set(thickness);
    }

    /**
     * Constructor for TransitionGraphicModel
     */
    public TransitionGraphicModel() {
    }

    /**
     *
     * @return state
     */
    @Override
    public StateGraphicModel getOrigin() {
        return (StateGraphicModel) super.getOrigin();
    }

    /**
     *
     * @param state value to set
     */
    @Override
    public void setOrigin(StateModel state) {
        super.setOrigin(state);
        StateGraphicModel s = (StateGraphicModel) state;
        BasicStateView sv = s.getStateView();
        parentView.changeOrigin(sv);
    }

    /**
     *
     * @return state
     */
    @Override
    public StateGraphicModel getDestination() {
        return (StateGraphicModel) super.getDestination();
    }

    /**
     *
     * @param state value to set
     */
    @Override
    public void setDestination(StateModel state) {
        super.setDestination(state);
        StateGraphicModel s = (StateGraphicModel) state;
        BasicStateView sv = s.getStateView();
        parentView.changeDestination(sv);
    }

    /**
     *
     * @return color property
     */
    public ObjectProperty<Color> colorProperty() {
        return color;
    }

    /**
     *
     * @return color
     */
    public Color getColor() {
        return color.get();
    }

    /**
     *
     * @param color value to set
     */
    public void setColor(Color color) {
        this.color.set(color);
    }

    /**
     *
     * @return thickness property
     */
    public SimpleDoubleProperty thicknessProperty() {
        return thickness;
    }

    /**
     *
     * @return thickness
     */
    public double getThickness() {
        return thickness.get();
    }

    /**
     *
     * @param thickness value to set
     */
    public void setThickness(double thickness) {
        this.thickness.set(thickness);
    }

    /**
     *
     * @return parentview
     */
    public TransitionView getTransitionView() {
        return parentView;
    }

    /**
     *
     * @param tv value to set
     */
    public void setTransitionView(TransitionView tv) {
        parentView = tv;
    }

    /**
     *
     * @return boolean
     */
    public boolean checkOrigin() {
        return (getOrigin().getStateView() instanceof RealStateView);
    }

    /**
     *
     * @return boolean
     */
    public boolean checkDestination() {
        return (getDestination().getStateView() instanceof RealStateView);
    }

    /**
     *
     * @return c1XProperty
     */
    public SimpleDoubleProperty c1XProperty() {
        return c1X;
    }

    /**
     *
     * @return c1YProperty
     */
    public SimpleDoubleProperty c1YProperty() {
        return c1Y;
    }

    /**
     *
     * @return c2XProperty
     */
    public SimpleDoubleProperty c2XProperty() {
        return c2X;
    }

    /**
     *
     * @return c2YProperty
     */
    public SimpleDoubleProperty c2YProperty() {
        return c2Y;
    }

    /**
     *
     * @return c1X
     */
    public double getC1X() {
        return c1X.get();
    }

    /**
     *
     * @return c1Y
     */
    public double getC1Y() {
        return c1Y.get();
    }

    /**
     *
     * @return c2X
     */
    public double getC2X() {
        return c2X.get();
    }

    /**
     *
     * @return c2Y
     */
    public double getC2Y() {
        return c2Y.get();
    }

    /**
     *
     * @param d value to set
     */
    public void setC1X(double d) {
        c1X.set(d);
    }

    /**
     *
     * @param d value to set
     */
    public void setC1Y(double d) {
        c1Y.set(d);
    }

    /**
     *
     * @param d value to set
     */
    public void setC2X(double d) {
        c2X.set(d);
    }

    /**
     *
     * @param d value to set
     */
    public void setC2Y(double d) {
        c2Y.set(d);
    }

}
