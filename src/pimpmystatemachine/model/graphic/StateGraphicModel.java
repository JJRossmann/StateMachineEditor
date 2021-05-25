/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pimpmystatemachine.model.graphic;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import pimpmystatemachine.model.semantics.StateModel;
import pimpmystatemachine.views.statemachinecomponents.BasicStateView;

/**
 * Class for the graphical parameters of a State
 *
 * @author Justin ROSSMANN, Naoufel BENYAHIA, Fatiha OUARDI
 */
public class StateGraphicModel extends StateModel {

    private final ObjectProperty<Color> border = new SimpleObjectProperty<>();
    private final ObjectProperty<Color> inside = new SimpleObjectProperty<>();
    private final SimpleDoubleProperty size = new SimpleDoubleProperty();
    private final SimpleDoubleProperty positionX = new SimpleDoubleProperty();
    private final SimpleDoubleProperty positionY = new SimpleDoubleProperty();

    private final SimpleStringProperty comment = new SimpleStringProperty();
    private final SimpleStringProperty enter = new SimpleStringProperty();
    private final SimpleStringProperty leave = new SimpleStringProperty();

    private BasicStateView parentView;

    /**
     * Constructor for StateGraphicModel
     *
     * @param name name
     * @param size size
     * @param border color
     * @param inside color
     * @param position position
     */
    public StateGraphicModel(String name, double size, Color border, Color inside, Point2D position) {
        super(name);
        this.border.set(border);
        this.inside.set(inside);
        this.size.set(size);
        this.positionX.set(position.getX());
        this.positionY.set(position.getY());
        this.enter.set("");
        this.leave.set("");
        this.comment.set("");
    }

    /**
     * Constructor for StateGraphicModel
     *
     * @param name name
     */
    public StateGraphicModel(String name) {
        super(name);
    }

    /**
     * Bind this StateGraphicModel to the given StateView
     *
     * @param sv stateview to bind to
     */
    public void bindToStateView(BasicStateView sv) {
        this.parentView = sv;
        this.positionX.bind(sv.layoutXProperty());
        this.positionY.bind(sv.layoutYProperty());
    }

    /**
     *
     * @return The ObjectProperty of the State's border color
     */
    public ObjectProperty borderColorProperty() {
        return border;
    }

    /**
     *
     * @return The Color of the State's border
     */
    public Color getBorderColor() {
        return border.get();
    }

    /**
     *
     * @param color color
     */
    public void setBorderColor(Color color) {
        border.set(color);
    }

    /**
     *
     * @return The ObjectProperty of the State's inside color
     */
    public ObjectProperty insideColorProperty() {
        return inside;
    }

    /**
     *
     * @return The Color of the State's inside
     */
    public Color getInsideColor() {
        return inside.get();
    }

    /**
     *
     * @param color color
     */
    public void setInsideColor(Color color) {
        inside.set(color);
    }

    /**
     *
     * @return the sizeProperty
     */
    public SimpleDoubleProperty sizeProperty() {
        return size;
    }

    /**
     *
     * @return size
     */
    public double getSize() {
        return size.get();
    }

    /**
     *
     * @param size value to set
     */
    public void setSize(double size) {
        this.size.set(size);
    }

    /**
     *
     * @return the positionXProperty
     */
    public SimpleDoubleProperty positionXProperty() {
        return positionX;
    }

    /**
     *
     * @return positionX
     */
    public double getPositionX() {
        return positionX.get();
    }

    /**
     *
     * @param x value to set
     */
    public void setPositionX(double x) {
        positionX.set(x);
    }

    /**
     *
     * @return positionYProperty
     */
    public SimpleDoubleProperty positionYProperty() {
        return positionY;
    }

    /**
     *
     * @return positionY
     */
    public double getPositionY() {
        return positionY.get();
    }

    /**
     *
     * @param y value to set
     */
    public void setPositionY(double y) {
        positionY.set(y);
    }

    /**
     *
     * @return the BasicStateView associated with this
     */
    public BasicStateView getStateView() {
        return parentView;
    }

    @Override
    public String toString() {
        String str = super.toString() + " " + size.get() + " " + positionX.get() + " " + positionY.get();
        return str;
    }

    /**
     *
     * @return comment property
     */
    public SimpleStringProperty commentProperty() {
        return comment;
    }

    /**
     *
     * @return comment
     */
    public String getComment() {
        return comment.get();
    }

    /**
     *
     * @return enter
     */
    public String getEnter() {
        return enter.get();
    }

    /**
     *
     * @return enter property
     */
    public SimpleStringProperty enterProperty() {
        return enter;
    }

    /**
     *
     * @return leave
     */
    public String getLeave() {
        return leave.get();
    }

    /**
     *
     * @return leave property
     */
    public SimpleStringProperty leaveProperty() {
        return leave;
    }

    /**
     *
     * @param enter value to set
     */
    public void setEnter(String enter) {
        this.enter.set(enter);
    }

    /**
     *
     * @param leave value to set
     */
    public void setLeave(String leave) {
        this.leave.set(leave);
    }

}
