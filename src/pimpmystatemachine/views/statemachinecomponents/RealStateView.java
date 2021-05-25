/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pimpmystatemachine.views.statemachinecomponents;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.ComboBox;
import pimpmystatemachine.model.graphic.StateGraphicModel;
import javafx.scene.shape.Circle;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.shape.StrokeType;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import pimpmystatemachine.views.gui.CenterComponent;
import pimpmystatemachine.views.gui.StateContextMenu;
import pimpmystatemachine.views.managers.SituationManager;

/**
 * Class for representing a Real State View
 *
 * @author Fatiha OUARDI, Justin ROSSMANN
 */
public class RealStateView extends BasicStateView {

    private final Circle selection = new Circle();
    private final Circle finalStateIndicator = new Circle();
    private final Polygon initialStateIndicator = new Polygon(0, 0, -30, 15, -30, -15);
    private final TextField normalNameText = new TextField();

    //editing
    private final Pane editingPane = new Pane();
    private final Rectangle editBox = new Rectangle();
    private final TextField nameField = new TextField();
    private final Text nameText = new Text("Name:");
    private final TextField commentField = new TextField();
    private final Text commentText = new Text("Comment:");
    private final TextField enterField = new TextField();
    private final Text enterText = new Text("Enter:");
    private final TextField leaveField = new TextField();
    private final Text leaveText = new Text("Leave:");
    private final ComboBox typeComboBox = new ComboBox();
    private final Text typeText = new Text("Type:");
    private final ComboBox colorComboBox = new ComboBox();
    private final Text colorText = new Text("Color:");
    private final ComboBox sizeComboBox = new ComboBox();
    private final Text sizeText = new Text("Size:");

    private final Tooltip tooltip_state_comment;

    private StateContextMenu scm;

    /**
     * Constructor of Real State View
     *
     * @param state state
     */
    public RealStateView(StateGraphicModel state) {
        super(state);
        this.scm = new StateContextMenu(this);

        circle.setOnContextMenuRequested(contextMenuHandler);

        //if it's a final state, it has a second circle
        if (state.isFinal()) {
            Circle circleAround = new Circle(5 + state.getSize());
            circleAround.strokeProperty().bind(state.borderColorProperty());
            circleAround.setStrokeType(StrokeType.INSIDE);
            circleAround.fillProperty().bind(state.insideColorProperty());
            getChildren().add(circleAround);
        }

        initializeEditingBox();

        initialStateIndicator.setFill(Color.BLACK);
        initialStateIndicator.translateXProperty().bind(state.sizeProperty().multiply(-1));

        normalNameText.textProperty().bind(state.nameProperty());
        DiverseUtils.bindLayout(normalNameText, circle, -92, -13);
        normalNameText.setAlignment(Pos.CENTER);
        normalNameText.setBackground(Background.EMPTY);
        normalNameText.setEditable(false);
        normalNameText.setMouseTransparent(true);
        normalNameText.setFocusTraversable(false);
        getChildren().add(normalNameText);

        selection.radiusProperty().bind(circle.radiusProperty().add(3));
        selection.setStroke(Color.BLUE);

        //if it's a final state, it has a second circle
        finalStateIndicator.radiusProperty().bind(circle.radiusProperty().add(5));
        finalStateIndicator.strokeProperty().bind(state.borderColorProperty());
        finalStateIndicator.setStrokeType(StrokeType.INSIDE);
        finalStateIndicator.fillProperty().bind(state.insideColorProperty());

        if (state.isFinal()) {
            setFinal(true);
        }
        if (state.isInitial()) {
            setInitial(true);
        }
        tooltip_state_comment = new Tooltip(commentField.getText());
        Tooltip.install(circle, tooltip_state_comment);
        tooltip_state_comment.textProperty().bind(commentField.textProperty());

        SituationManager ssm = new SituationManager(this);
    }

    /**
     * Initialize the editing box
     */
    public void initializeEditingBox() {
        //editing mode elements
        editBox.setWidth(200);
        editBox.setHeight(380);
        editBox.setArcHeight(10);
        editBox.setArcWidth(10);
        editBox.setFill(Color.WHITESMOKE);
        editBox.setStroke(Color.BLACK);
        editBox.setOpacity(0.98);

        DiverseUtils.initializeTextField(nameField, editBox, 10, 30, nameText, 0, -5, state.getName());
        nameField.textProperty().addListener(nameChangeListener);
        nameField.setOnAction(nameChangeEventHandler);
        nameField.focusedProperty().addListener(nameFocusChangeListener);

        DiverseUtils.initializeTextField(commentField, editBox, 10, 90, commentText, 0, -5, state.getComment(), state.commentProperty());

        DiverseUtils.initializeTextField(enterField, editBox, 10, 150, enterText, 0, -5, state.getEnter(), state.enterProperty());

        DiverseUtils.initializeTextField(leaveField, editBox, 10, 210, leaveText, 0, -5, state.getLeave(), state.leaveProperty());

        //TODO use the mousewheel for changing elements in comboboxes
        typeComboBox.getItems().addAll("Normal", "Final", "Initial");
        typeComboBox.setValue("Normal");
        DiverseUtils.initializeComboBox(typeComboBox, editBox, 50, 330, typeText, 0, -5);
        typeComboBox.valueProperty().addListener(typeChangeListener);

        colorComboBox.getItems().addAll("Blue", "Green", "Red", "Yellow", "Orange", "Brown");
        colorComboBox.setValue("Blue");
        DiverseUtils.initializeComboBox(colorComboBox, editBox, 10, 270, colorText, 0, -5);
        colorComboBox.valueProperty().addListener(colorChangeListener);

        sizeComboBox.getItems().addAll(40, 50, 60, 75, 90, 120);
        sizeComboBox.setValue((int) state.getSize());
        DiverseUtils.initializeComboBox(sizeComboBox, editBox, 120, 270, sizeText, 0, -5);
        sizeComboBox.valueProperty().addListener(sizeChangeListener);

        editingPane.getChildren().add(editBox);
        editingPane.getChildren().add(nameField);
        editingPane.getChildren().add(nameText);
        editingPane.getChildren().add(commentField);
        editingPane.getChildren().add(commentText);
        editingPane.getChildren().add(enterField);
        editingPane.getChildren().add(enterText);
        editingPane.getChildren().add(leaveField);
        editingPane.getChildren().add(leaveText);
        editingPane.getChildren().add(colorComboBox);
        editingPane.getChildren().add(colorText);
        editingPane.getChildren().add(sizeComboBox);
        editingPane.getChildren().add(sizeText);
        editingPane.getChildren().add(typeComboBox);
        editingPane.getChildren().add(typeText);
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
            if (getChildren().contains(finalStateIndicator)) {
                finalStateIndicator.setEffect(ds);
            } else {
                circle.setEffect(ds);
            }
            getScene().setCursor(Cursor.HAND);
        } else {
            circle.setEffect(null);
            finalStateIndicator.setEffect(null);
            getScene().setCursor(Cursor.DEFAULT);
        }
    }

    /**
     *
     * @param b boolean
     */
    public void setSelected(boolean b) {
        if (b) {
            if (!(getChildren().contains(selection))) {
                getChildren().add(selection);
                selection.toBack();
            }
        } else {
            getChildren().remove(selection);
        }
    }

    /**
     *
     * @param b boolean
     */
    public void setEditing(boolean b) {
        if (b) {
            this.toFront();
            getChildren().add(editingPane);
        } else {
            getChildren().remove(editingPane);
            getParent().requestFocus();
        }
    }

    /**
     *
     * @param b boolean
     */
    public void setFinal(boolean b) {
        if (b) {
            if (!(getChildren().contains(finalStateIndicator))) {
                state.setFinal(true);
                //now selection has to be around the bigger circle
                selection.radiusProperty().unbind();
                selection.radiusProperty().bind(finalStateIndicator.radiusProperty().add(3));
                getChildren().add(finalStateIndicator);
                finalStateIndicator.toBack();
            }
        } else {
            if (getChildren().contains(finalStateIndicator)) {
                state.setFinal(false);
                selection.radiusProperty().unbind();
                selection.radiusProperty().bind(circle.radiusProperty().add(3));
                getChildren().remove(finalStateIndicator);
            }
        }
    }

    /**
     *
     * @param b boolean
     */
    public void setInitial(boolean b) {
        if (b) {
            state.setInitial(true);
            if (!getChildren().contains(initialStateIndicator)) {
                getChildren().add(initialStateIndicator);
                initialStateIndicator.toFront();
            }
        } else {
            getChildren().remove(initialStateIndicator);
            state.setInitial(false);
        }
    }

    private final ChangeListener<String> typeChangeListener = new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue ov, String t, String t1) {
            if (t1.equals("Normal")) {
                setFinal(false);
            } else if (t1.equals("Final")) {
                setFinal(true);
            } else if (t1.equals("Initial")) {
                setInitial(true);
            }
        }
    };

    private final ChangeListener<String> colorChangeListener = new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue ov, String t, String t1) {
            switch (t1) {
                case "Green":
                    circle.setStroke(Color.GREEN);
                    circle.setFill(Color.LIGHTGREEN);
                    break;
                case "Red":
                    circle.setStroke(Color.RED);
                    circle.setFill(Color.PINK);
                    break;
                case "Yellow":
                    circle.setStroke(Color.DARKGOLDENROD);
                    circle.setFill(Color.YELLOW);
                    break;
                case "Orange":
                    circle.setStroke(Color.ORANGE);
                    circle.setFill(Color.LIGHTSALMON);
                    break;
                case "Brown":
                    circle.setStroke(Color.BROWN);
                    circle.setFill(Color.BURLYWOOD);
                    break;
                default:
                    circle.setStroke(Color.STEELBLUE);
                    circle.setFill(Color.LIGHTBLUE);
            }
        }
    };

    private final ChangeListener<Integer> sizeChangeListener = new ChangeListener<Integer>() {
        @Override
        public void changed(ObservableValue ov, Integer i, Integer i1) {
            getState().setSize(i1);
        }
    };

    private final EventHandler contextMenuHandler = new EventHandler<ContextMenuEvent>() {
        @Override
        public void handle(ContextMenuEvent event) {
            scm.show(circle, event.getScreenX(), event.getScreenY());
            event.consume();
        }
    };

    private final ChangeListener<String> nameChangeListener = new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue ov, String t, String t1) {
            nameField.setStyle(DiverseUtils.textFieldModificationStyle);
        }
    };

    private final EventHandler nameChangeEventHandler = (EventHandler<ActionEvent>) (ActionEvent event) -> {
        CenterComponent center = (CenterComponent) getParent();
        center.getStateMachine().renameState(state, nameField.textProperty().get());
        nameField.textProperty().set(state.getName());
        nameField.setStyle(DiverseUtils.textFieldModifiedStyle);
    };

    private final InvalidationListener nameFocusChangeListener = (Observable obs) -> {
        CenterComponent center = (CenterComponent) getParent();
        center.getStateMachine().renameState(state, nameField.textProperty().get());
        nameField.textProperty().set(state.getName());
        nameField.setStyle(DiverseUtils.textFieldModifiedStyle);
    };

}
