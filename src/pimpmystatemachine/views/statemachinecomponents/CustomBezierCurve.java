/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pimpmystatemachine.views.statemachinecomponents;

import dragpanzoom.managers.DragManager;
import dragpanzoom.views.TranslatableHomotheticPane;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.transform.Rotate;
import pimpmystatemachine.model.graphic.TransitionGraphicModel;
import pimpmystatemachine.views.gui.CenterComponent;

/**
 * Class for representing a Bezier Curve with added control points, arrow and
 * handlers
 *
 * @author Justin ROSSMANN, Fatiha OUARDI
 */
public class CustomBezierCurve extends TranslatableHomotheticPane {

    private final TransitionView tv;
    TransitionGraphicModel transition;
    private BasicStateView sv1;
    private BasicStateView sv2;
    final CubicCurve l = new CubicCurve();
    private final Polygon arrow = new Polygon(0, 0, 15, 30, -15, 30);
    private final Rotate rz = new Rotate();
    Line controlLine1;
    Line controlLine2;
    TranslatableHomotheticPane anchor1;
    TranslatableHomotheticPane anchor2;
    private final Circle handler1 = new Circle();
    private final Circle handler2 = new Circle();

    /**
     *
     */
    public final SimpleDoubleProperty centerPositionX = new SimpleDoubleProperty();

    /**
     *
     */
    public final SimpleDoubleProperty centerPositionY = new SimpleDoubleProperty();

    class BoundLine extends Line {

        BoundLine(DoubleProperty startX, DoubleProperty startY, DoubleProperty endX, DoubleProperty endY) {
            startXProperty().bind(startX);
            startYProperty().bind(startY);
            endXProperty().bind(endX);
            endYProperty().bind(endY);
            setStrokeWidth(2);
            setStroke(Color.GRAY.deriveColor(0, 1, 1, 0.5));
            setStrokeLineCap(StrokeLineCap.BUTT);
            getStrokeDashArray().setAll(10.0, 5.0);
        }
    }

    /**
     *
     * @param tv transition view
     */
    public CustomBezierCurve(TransitionView tv) {
        setPrefSize(0, 0);
        setPickOnBounds(false);
        this.tv = tv;
        transition = tv.getTransition();
        controlLine1 = new BoundLine(l.controlX1Property(), l.controlY1Property(), l.startXProperty(), l.startYProperty());
        controlLine2 = new BoundLine(l.controlX2Property(), l.controlY2Property(), l.endXProperty(), l.endYProperty());
        anchor1 = new TranslatableHomotheticPane();
        anchor2 = new TranslatableHomotheticPane();
        updateBinding();
        initializeBezierCurve();
        initializeHandling();
        Point2D pt = eval(l, 0.5);
        centerPositionX.set(pt.getX());
        centerPositionY.set(pt.getY());
    }

    /**
     * Initiliaze the handlers on the control points
     */
    private void initializeHandling() {
        double calc1 = getIntersection(l, sv1.getLayoutX(), sv1.getLayoutY(), sv1.getState().getSize() + 2, 0, 50);
        double calc2 = getIntersection(l, sv2.getLayoutX(), sv2.getLayoutY(), sv2.getState().getSize() + 2, 50, 100);
        Point2D pt1 = eval(l, calc1);
        Point2D pt2 = eval(l, calc2);
        handler1.setRadius(10);
        handler2.setRadius(10);
        handler1.setFill(Color.VIOLET);
        handler2.setFill(Color.VIOLET);
        DiverseUtils.setLayout(handler1, pt1.getX(), pt1.getY());
        DiverseUtils.setLayout(handler2, pt2.getX(), pt2.getY());
        handler1.toFront();
        handler2.toFront();

        handler1.setOnMouseClicked(handler1ClickHandler);
        handler2.setOnMouseClicked(handler2ClickHandler);

        transition.getOrigin().sizeProperty().addListener(stateSizeChangeListener);
        transition.getDestination().sizeProperty().addListener(stateSizeChangeListener);
    }

    private final EventHandler<MouseEvent> handler1ClickHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            PhantomStateView psv = ((CenterComponent) tv.getParent()).getStateMachine().createPhantomState(transition.getOrigin());
            DiverseUtils.setLayout(psv, handler1.getLayoutX(), handler1.getLayoutY());
            transition.setOrigin(psv.getState());
            getChildren().remove(handler1);
            event.consume();
        }
    };

    private final EventHandler<MouseEvent> handler2ClickHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            PhantomStateView psv = ((CenterComponent) tv.getParent()).getStateMachine().createPhantomState(transition.getDestination());
            DiverseUtils.setLayout(psv, handler2.getLayoutX(), handler2.getLayoutY());
            transition.setDestination(psv.getState());
            getChildren().remove(handler2);
            event.consume();
        }
    };

    /**
     * Update the binding to different start or end states
     */
    public void updateBinding() {
        this.sv1 = transition.getOrigin().getStateView();
        this.sv2 = transition.getDestination().getStateView();
        l.startXProperty().unbind();
        l.startYProperty().unbind();
        l.endXProperty().unbind();
        l.endYProperty().unbind();
        l.startXProperty().bind(sv1.layoutXProperty());
        l.startYProperty().bind(sv1.layoutYProperty());
        l.endXProperty().bind(sv2.layoutXProperty());
        l.endYProperty().bind(sv2.layoutYProperty());
        if (sv1 instanceof RealStateView) {
            if (!getChildren().contains(handler1)) {
                getChildren().add(handler1);
            }
        } else if (sv1 instanceof PhantomStateView) {
            getChildren().remove(handler1);
        }
        if (sv2 instanceof RealStateView) {
            if (!getChildren().contains(handler2)) {
                getChildren().add(handler2);
            }
        } else if (sv2 instanceof PhantomStateView) {
            getChildren().remove(handler2);
        }
        updateHandlers();
    }

    /**
     * Initialize the CubicCurve / Bezier Curve
     */
    private void initializeBezierCurve() {
        l.setStroke(Color.FORESTGREEN);
        l.setStrokeWidth(4);
        l.setStrokeLineCap(StrokeLineCap.ROUND);
        l.setFill(null);
        //Bind the properties to those of the transitionGraphicModel

        l.strokeProperty().bind(transition.colorProperty());
        l.strokeWidthProperty().bind(transition.thicknessProperty());
        getChildren().add(l);

        //Create the anchors
        anchor1.getChildren().add(new Circle(20, Color.GREEN));
        anchor1.setPrefSize(0, 0);
        anchor1.setPickOnBounds(false);
        DragManager dmAnchor1 = new DragManager(anchor1);
        anchor2.getChildren().add(new Circle(20, Color.YELLOW));
        anchor2.setPrefSize(0, 0);
        anchor2.setPickOnBounds(false);
        DragManager dmAnchor2 = new DragManager(anchor2);

        //Put different positions for the control points depending on the ralative placement of the start and end of the curve
        if (anchor1.getLayoutX() == 0 && anchor1.getLayoutX() == 0) {
            double c1x, c1y, c2x, c2y;
            if (sv1 != sv2) {
                double deltax = sv1.getLayoutX() - sv2.getLayoutX();
                double deltay = sv1.getLayoutY() - sv2.getLayoutY();
                deltay = deltay / Math.abs(deltay);
                deltax = deltax / Math.abs(deltax);

                if (deltax <= 0) {
                    if (deltay <= 0) {
                        c1x = sv1.getLayoutX() + 2 * sv1.getState().getSize();
                        c1y = sv1.getLayoutY();
                        c2x = sv2.getLayoutX();
                        c2y = sv2.getLayoutY() - 2 * sv2.getState().getSize();
                    } else {
                        c1x = sv1.getLayoutX();
                        c1y = sv1.getLayoutY() - 2 * sv1.getState().getSize();
                        c2x = sv2.getLayoutX() - 2 * sv2.getState().getSize();
                        c2y = sv2.getLayoutY();
                    }
                } else {
                    if (deltay <= 0) {
                        c1x = sv1.getLayoutX();
                        c1y = sv1.getLayoutY() + 2 * sv1.getState().getSize();
                        c2x = sv2.getLayoutX() + 2 * sv2.getState().getSize();
                        c2y = sv2.getLayoutY();
                    } else {
                        c1x = sv1.getLayoutX() - 2 * sv1.getState().getSize();
                        c1y = sv1.getLayoutY();
                        c2x = sv2.getLayoutX();
                        c2y = sv2.getLayoutY() + 2 * sv2.getState().getSize();
                    }
                }
            } else {
                c1x = sv1.getLayoutX() + 4 * sv1.getState().getSize();
                c1y = sv1.getLayoutY();
                c2x = sv2.getLayoutX();
                c2y = sv2.getLayoutY() - 4 * sv2.getState().getSize();

            }
            DiverseUtils.setLayout(anchor1, c1x, c1y);
            DiverseUtils.setLayout(anchor2, c2x, c2y);
//            anchor1.translate(c1x, c1y);
//            anchor2.translate(c2x, c2y);
        }

        l.controlX1Property().bind(anchor1.layoutXProperty());
        l.controlY1Property().bind(anchor1.layoutYProperty());
        l.controlX2Property().bind(anchor2.layoutXProperty());
        l.controlY2Property().bind(anchor2.layoutYProperty());

        //Control line for the anchors
        controlLine1 = new BoundLine(l.controlX1Property(), l.controlY1Property(), l.startXProperty(), l.startYProperty());
        controlLine2 = new BoundLine(l.controlX2Property(), l.controlY2Property(), l.endXProperty(), l.endYProperty());

        //initilize the arrow
        arrow.fillProperty().bind(transition.colorProperty());
        rz.setAxis(Rotate.Z_AXIS);
        arrow.getTransforms().add(rz);
        getChildren().add(arrow);
        updateArrow();

        //add invalidationlisteners to update the arrow
        l.startXProperty().addListener(updateListener);
        l.startYProperty().addListener(updateListener);
        l.endXProperty().addListener(updateListener);
        l.endYProperty().addListener(updateListener);
        l.controlX1Property().addListener(updateListener);
        l.controlY1Property().addListener(updateListener);
        l.controlX2Property().addListener(updateListener);
        l.controlY2Property().addListener(updateListener);

        //add listeners to translate the control points when you the endpoints are translated
        l.startXProperty().addListener(StartXChangeListener);
        l.startYProperty().addListener(StartYChangeListener);
        l.endXProperty().addListener(EndXChangeListener);
        l.endYProperty().addListener(EndYChangeListener);
    }

    /**
     * Evaluate the cubic curve at a parameter t
     *
     * @param c curve
     * @param t parameter on the curve
     * @return a point on the curve
     */
    public Point2D eval(CubicCurve c, double t) {
        Point2D p = new Point2D(Math.pow(1 - t, 3) * c.getStartX()
                + 3 * t * Math.pow(1 - t, 2) * c.getControlX1()
                + 3 * (1 - t) * t * t * c.getControlX2()
                + Math.pow(t, 3) * c.getEndX(),
                Math.pow(1 - t, 3) * c.getStartY()
                + 3 * t * Math.pow(1 - t, 2) * c.getControlY1()
                + 3 * (1 - t) * t * t * c.getControlY2()
                + Math.pow(t, 3) * c.getEndY());
        return p;
    }

    //TOREFINE sensible to zoom
    private double getIntersection(CubicCurve curve, double cx, double cy, double r, int mint, int maxt) {
        Point2D p;
        double minDist = 1000000;
        double minT = 1;
        for (int i = mint; i <= maxt; i++) {
            p = eval(curve, (double) i / 100);
            double temp = Math.abs(p.distance(new Point2D(cx, cy)) - r);
            if (temp < minDist) {
                minDist = temp;
                minT = (double) i / 100;
            }
        }
        return minT;
    }

    /**
     * Evaluate the tangent of the cubic curve at a parameter t
     */
    private Point2D evalDt(CubicCurve c, double t) {
        Point2D p = new Point2D(-3 * Math.pow(1 - t, 2) * c.getStartX()
                + 3 * (Math.pow(1 - t, 2) - 2 * t * (1 - t)) * c.getControlX1()
                + 3 * ((1 - t) * 2 * t - t * t) * c.getControlX2()
                + 3 * Math.pow(t, 2) * c.getEndX(),
                -3 * Math.pow(1 - t, 2) * c.getStartY()
                + 3 * (Math.pow(1 - t, 2) - 2 * t * (1 - t)) * c.getControlY1()
                + 3 * ((1 - t) * 2 * t - t * t) * c.getControlY2()
                + 3 * Math.pow(t, 2) * c.getEndY());
        return p;
    }

    /**
     * Update the arrows position
     */
    private void updateArrow() {
        updateBinding();
        double calc = getIntersection(l, sv2.getLayoutX(), sv2.getLayoutY(), sv2.getState().getSize() + 10, 50, 100);
        Point2D position = eval(l, calc);
        double size = Math.max(l.getBoundsInLocal().getWidth(), l.getBoundsInLocal().getHeight());
        double scale = size / 4d;
        Point2D tangent = evalDt(l, calc).normalize().multiply(scale);
        double angle = Math.atan2(tangent.getY(), tangent.getX());
        angle = Math.toDegrees(angle);
        rz.setAngle(angle + 90);
        arrow.setLayoutX(position.getX());
        arrow.setLayoutY(position.getY());
    }

    /**
     * update the handlers positions
     */
    private void updateHandlers() {
        double calc1 = getIntersection(l, sv1.getLayoutX(), sv1.getLayoutY(), sv1.getState().getSize() + 2, 0, 50);
        Point2D pt1 = eval(l, calc1);
        DiverseUtils.setLayout(handler1, pt1.getX(), pt1.getY());
        double calc2 = getIntersection(l, sv2.getLayoutX(), sv2.getLayoutY(), sv2.getState().getSize() + 2, 50, 100);
        Point2D pt2 = eval(l, calc2);
        DiverseUtils.setLayout(handler2, pt2.getX(), pt2.getY());
    }

    private final InvalidationListener updateListener = new InvalidationListener() {
        @Override
        public void invalidated(Observable observable) {
            updateArrow();
            Point2D pt = eval(l, 0.5);
            centerPositionX.set(pt.getX());
            centerPositionY.set(pt.getY());
        }
    };

    private final InvalidationListener stateSizeChangeListener = new InvalidationListener() {
        @Override
        public void invalidated(Observable observable) {
            updateHandlers();
            updateArrow();
        }
    };

    private final ChangeListener<Number> StartXChangeListener = new ChangeListener<Number>() {
        @Override
        public void changed(ObservableValue ov, Number d, Number d1) {
            anchor1.translate((double) d1 - (double) d, 0);
        }
    };

    private final ChangeListener<Number> StartYChangeListener = new ChangeListener<Number>() {
        @Override
        public void changed(ObservableValue ov, Number d, Number d1) {
            anchor1.translate(0, (double) d1 - (double) d);
        }
    };

    private final ChangeListener<Number> EndXChangeListener = new ChangeListener<Number>() {
        @Override
        public void changed(ObservableValue ov, Number d, Number d1) {
            anchor2.translate((double) d1 - (double) d, 0);
        }
    };

    private final ChangeListener<Number> EndYChangeListener = new ChangeListener<Number>() {
        @Override
        public void changed(ObservableValue ov, Number d, Number d1) {
            anchor2.translate(0, (double) d1 - (double) d);
        }
    };
}
