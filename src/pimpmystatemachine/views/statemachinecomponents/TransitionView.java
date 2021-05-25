package pimpmystatemachine.views.statemachinecomponents;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import dragpanzoom.managers.DragManager;
import pimpmystatemachine.model.graphic.TransitionGraphicModel;
import dragpanzoom.views.TranslatableHomotheticPane;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Text;
import javafx.scene.transform.Translate;
import pimpmystatemachine.views.gui.CenterComponent;
import pimpmystatemachine.views.gui.TransitionContextMenu;
import pimpmystatemachine.views.managers.SituationManager;

/**
 * Class for representing a Transition on the screen
 *
 * @author Justin ROSSMANN
 */
public class TransitionView extends TranslatableHomotheticPane {

    //TODO choose to hide certain elements in the HMI
    private final TransitionGraphicModel transition;
    private CustomBezierCurve cbc;
    private CubicCurve l;
    private TransitionContextMenu tcm;
    private Tooltip tooltip_transition_name;

    private BasicStateView sv1;
    private BasicStateView sv2;

    //selected mode
    private Line controlLine1;
    private Line controlLine2;
    private TranslatableHomotheticPane anchor1;
    private TranslatableHomotheticPane anchor2;

    //editing mode
    private final Pane editingPane = new Pane();
    private final Rectangle editBox = new Rectangle();
    private final TextField nameField = new TextField();
    private final Text nameText = new Text("Name:");
    private final TextField eventField = new TextField();
    private final Text eventText = new Text("Event:");
    private final TextField guardField = new TextField();
    private final Text guardText = new Text("Guard:");
    private final TextField actionField = new TextField();
    private final Text actionText = new Text("Action:");
    private final ComboBox colorComboBox = new ComboBox();
    private final Text colorText = new Text("Color:");
    private final ComboBox sizeComboBox = new ComboBox();
    private final Text sizeText = new Text("Size:");

    //description
    private final TranslatableHomotheticPane description = new TranslatableHomotheticPane();
    private final Text e = new Text();
    private final Text g = new Text();
    private final Text a = new Text();
    Line descriptionLine = new Line();

    //used to know if you have to save after a press
    private boolean moved = false;

    /**
     * Constructor for TransitionView
     *
     * @param transition transition
     */
    public TransitionView(TransitionGraphicModel transition) {
        super();

        //make this Pane only big enough for the line
        setPrefSize(0, 0);

        this.transition = transition;
        transition.setTransitionView(this);
        this.tcm = new TransitionContextMenu(this);

        sv1 = transition.getOrigin().getStateView();
        sv2 = transition.getDestination().getStateView();

        initializeTooltip();

        initializeBezierCurve();

        initializeEditingBox();

        initializeDescription();

        //managers and listeners
        setPickOnBounds(false);

        l.setOnContextMenuRequested((ContextMenuEvent event) -> {
            tcm.show(l, event.getScreenX(), event.getScreenY());
            event.consume();
        });

        SituationManager ssm = new SituationManager(this);

        this.addEventFilter(MouseEvent.MOUSE_RELEASED, (MouseEvent event) -> {
            if (moved) {
                ((CenterComponent) getParent()).fireSave();
                moved = false;
            }
        });
    }

    /**
     * Initialize the Custom Bezier Curve
     */
    private void initializeBezierCurve() {
        cbc = new CustomBezierCurve(this);
        this.l = cbc.l;
        this.anchor1 = cbc.anchor1;
        this.anchor2 = cbc.anchor2;
        this.controlLine1 = cbc.controlLine1;
        this.controlLine2 = cbc.controlLine2;
        getChildren().add(cbc);

        if (transition.getC1X() != 0) {
            Bindings.bindBidirectional(anchor1.layoutXProperty(), transition.c1XProperty());
            Bindings.bindBidirectional(anchor1.layoutYProperty(), transition.c1YProperty());
            Bindings.bindBidirectional(anchor2.layoutXProperty(), transition.c2XProperty());
            Bindings.bindBidirectional(anchor2.layoutYProperty(), transition.c2YProperty());
        } else {
            Bindings.bindBidirectional(transition.c1XProperty(), anchor1.layoutXProperty());
            Bindings.bindBidirectional(transition.c1YProperty(), anchor1.layoutYProperty());
            Bindings.bindBidirectional(transition.c2XProperty(), anchor2.layoutXProperty());
            Bindings.bindBidirectional(transition.c2YProperty(), anchor2.layoutYProperty());
        }

        cbc.anchor1.layoutXProperty().addListener(changeListener);
        cbc.anchor1.layoutYProperty().addListener(changeListener);
        cbc.anchor2.layoutXProperty().addListener(changeListener);
        cbc.anchor2.layoutYProperty().addListener(changeListener);
        this.layoutXProperty().addListener(changeListener);
        this.layoutYProperty().addListener(changeListener);
    }

    /**
     * Initialize the Description
     */
    private void initializeDescription() {
        //TOREFINE make it more robust if multiple lines
        e.textProperty().bind(transition.eventProperty());
        g.textProperty().bind(transition.guardProperty());
        a.textProperty().bind(transition.actionProperty());
        descriptionLine.startXProperty().bind(e.layoutXProperty());
        descriptionLine.startYProperty().bind(e.layoutYProperty().add(10));
        descriptionLine.endXProperty().bind(g.layoutXProperty().add(50));
        descriptionLine.endYProperty().bind(g.layoutYProperty().add(10));
        description.getChildren().addAll(e, g, a, descriptionLine);
        description.setPrefSize(0, 0);
        description.setPickOnBounds(false);
        description.setStyle(null);
        DiverseUtils.setLayout(e, -80, 0);
        DiverseUtils.setLayout(g, 80, 0);
        DiverseUtils.setLayout(a, 0, 30);

        Point2D pt = cbc.eval(cbc.l, 0.5);
        DiverseUtils.setLayout(description, pt.getX(), pt.getY());
        getChildren().add(description);
        DragManager dm = new DragManager(description);

        cbc.centerPositionX.addListener(descriptionPositionListener);
        cbc.centerPositionY.addListener(descriptionPositionListener);
        description.layoutXProperty().addListener(layoutXChangeListener);
        description.layoutYProperty().addListener(layoutYChangeListener);
    }

    private final ChangeListener<Number> layoutXChangeListener = new ChangeListener<Number>() {
        @Override
        public void changed(ObservableValue ov, Number d, Number d1) {
            if (description.getLayoutX() != cbc.centerPositionX.get()) {
                description.getTransforms().add(new Translate((double) d1 - (double) d, 0));
                description.setLayoutX(cbc.centerPositionX.get());
            }
        }
    };

    private final ChangeListener<Number> layoutYChangeListener = new ChangeListener<Number>() {
        @Override
        public void changed(ObservableValue ov, Number d, Number d1) {
            if (description.getLayoutY() != cbc.centerPositionY.get()) {
                description.getTransforms().add(new Translate(0, (double) d1 - (double) d));
                description.setLayoutY(cbc.centerPositionY.get());
            }
        }
    };

    private final InvalidationListener descriptionPositionListener = new InvalidationListener() {
        @Override
        public void invalidated(Observable observable) {
            DiverseUtils.setLayout(description, cbc.centerPositionX.get(), cbc.centerPositionY.get());
        }
    };

    private ChangeListener changeListener = new ChangeListener() {
        @Override
        public void changed(ObservableValue observable, Object oldValue, Object newValue) {
            moved = true;
        }
    };

    /**
     * Initialize the editing box
     */
    public void initializeEditingBox() {
        //editing mode elements
        editBox.setWidth(200);
        editBox.setHeight(360);
        editBox.setArcHeight(10);
        editBox.setArcWidth(10);
        editBox.setFill(Color.WHITESMOKE);
        editBox.setStroke(Color.BLACK);
        editBox.setOpacity(0.98);

        DiverseUtils.initializeTextField(nameField, editBox, 10, 30, nameText, 0, -5, transition.getName(), transition.nameProperty());

        DiverseUtils.initializeTextField(eventField, editBox, 10, 90, eventText, 0, -5, transition.getEvent(), transition.eventProperty());

        DiverseUtils.initializeTextField(guardField, editBox, 10, 150, guardText, 0, -5, transition.getGuard(), transition.guardProperty());

        DiverseUtils.initializeTextField(actionField, editBox, 10, 210, actionText, 0, -5, transition.getAction(), transition.actionProperty());

        colorComboBox.getItems().addAll("Blue", "Green", "Red", "Yellow", "Orange", "Brown");
        colorComboBox.setValue("Blue");
        DiverseUtils.initializeComboBox(colorComboBox, editBox, 10, 270, colorText, 0, -5);
        colorComboBox.valueProperty().addListener(colorChangeListener);

        sizeComboBox.getItems().addAll(3, 4, 5, 6, 8, 10, 12);
        sizeComboBox.setValue((int) transition.getThickness());
        DiverseUtils.initializeComboBox(sizeComboBox, editBox, 120, 270, sizeText, 0, -5);
        sizeComboBox.valueProperty().addListener(sizeChangeListener);

        editingPane.getChildren().add(editBox);
        editingPane.getChildren().add(nameField);
        editingPane.getChildren().add(nameText);
        editingPane.getChildren().add(eventField);
        editingPane.getChildren().add(eventText);
        editingPane.getChildren().add(guardField);
        editingPane.getChildren().add(guardText);
        editingPane.getChildren().add(actionField);
        editingPane.getChildren().add(actionText);
        editingPane.getChildren().add(colorComboBox);
        editingPane.getChildren().add(colorText);
        editingPane.getChildren().add(sizeComboBox);
        editingPane.getChildren().add(sizeText);
    }

    /**
     *
     * @param sv stateview
     */
    public void changeOrigin(BasicStateView sv) {
        l.startXProperty().bind(sv.layoutXProperty());
        l.startYProperty().bind(sv.layoutYProperty());
        cbc.updateBinding();
    }

    /**
     *
     * @param sv stateview
     */
    public void changeDestination(BasicStateView sv) {
        l.endXProperty().bind(sv.layoutXProperty());
        l.endYProperty().bind(sv.layoutYProperty());
        cbc.updateBinding();
    }

    /**
     *
     * @return The TransitionGraphicModel associated with this TransitionView
     */
    public TransitionGraphicModel getTransition() {
        return transition;
    }

    /**
     *
     * @param b boolean
     */
    public void setHovered(boolean b) {
        if (b) {
            DropShadow ds = new DropShadow();
            ds.setRadius(20.0);
            ds.setColor(Color.color(0.4, 0.5, 0.5));
            l.setEffect(ds);
            e.setEffect(ds);
            g.setEffect(ds);
            a.setEffect(ds);
            descriptionLine.setEffect(ds);
            getScene().setCursor(Cursor.HAND);
        } else {
            l.setEffect(null);
            e.setEffect(null);
            g.setEffect(null);
            a.setEffect(null);
            descriptionLine.setEffect(null);
            getScene().setCursor(Cursor.DEFAULT);
        }
    }

    /**
     *
     * @param b boolean
     */
    public void setSelected(boolean b) {
        if (b) {
            l.strokeWidthProperty().unbind();
            l.strokeWidthProperty().bind(transition.thicknessProperty().add(4));
            l.strokeProperty().unbind();
            l.setStroke(Color.GREY);
            getChildren().add(controlLine1);
            getChildren().add(controlLine2);
            getChildren().add(anchor1);
            getChildren().add(anchor2);
        } else {
            l.strokeWidthProperty().unbind();
            l.strokeProperty().bind(transition.colorProperty());
            l.strokeWidthProperty().bind(transition.thicknessProperty());
            getChildren().remove(controlLine1);
            getChildren().remove(controlLine2);
            getChildren().remove(anchor1);
            getChildren().remove(anchor2);
        }
    }

    /**
     *
     * @param b boolean
     */
    public void setEditing(boolean b) {
        if (b) {
            editingPane.toFront();
            //make this go to front or else there could be other transitions or states above it
            this.toFront();
            editingPane.setLayoutX(((sv1.getLayoutX() + sv2.getLayoutX()) / 2) - 100);
            editingPane.setLayoutY(((sv1.getLayoutY() + sv2.getLayoutY()) / 2) - 150);
            getChildren().add(editingPane);
        } else {
            this.toBack();
            getChildren().remove(editingPane);
            getParent().requestFocus();
        }
    }

    private final ChangeListener<String> colorChangeListener = new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue ov, String t, String t1) {
            switch (t1) {
                case "Green":
                    l.setStroke(Color.DARKGREEN);
                    transition.setColor(Color.DARKGREEN);

                    break;
                case "Red":
                    l.setStroke(Color.DARKRED);
                    transition.setColor(Color.DARKRED);

                    break;
                case "Yellow":
                    l.setStroke(Color.DARKGOLDENROD);
                    transition.setColor(Color.DARKGOLDENROD);

                    break;
                case "Orange":
                    l.setStroke(Color.DARKORANGE);
                    transition.setColor(Color.DARKORANGE);

                    break;
                case "Brown":
                    l.setStroke(Color.BROWN);
                    transition.setColor(Color.BROWN);

                    break;
                default:
                    l.setStroke(Color.STEELBLUE);
                    transition.setColor(Color.STEELBLUE);

            }
        }
    };

    private final ChangeListener<Integer> sizeChangeListener = new ChangeListener<Integer>() {
        @Override
        public void changed(ObservableValue ov, Integer i, Integer i1) {
            getTransition().setThickness(i1);
        }
    };

    /**
     *
     * @return anchor1
     */
    public TranslatableHomotheticPane getAnchor1() {
        return anchor1;
    }

    /**
     *
     * @return anchor2
     */
    public TranslatableHomotheticPane getAnchor2() {
        return anchor2;
    }

    /**
     * Initilize the transitions Tooltip
     */
    private void initializeTooltip() {
        tooltip_transition_name = new Tooltip(transition.getName());
        Tooltip.install(this, tooltip_transition_name);
        tooltip_transition_name.textProperty().bind(nameField.textProperty());
    }

}
