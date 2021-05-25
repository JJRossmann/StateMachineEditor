/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pimpmystatemachine.views.statemachinecomponents;

import dragpanzoom.managers.DragManager;
import dragpanzoom.views.TranslatableHomotheticPane;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import pimpmystatemachine.model.graphic.StateGraphicModel;
import pimpmystatemachine.views.gui.CenterComponent;

/**
 * Class for representing a Basic State View
 *
 * @author Justin ROSSMANN
 */
public class BasicStateView extends TranslatableHomotheticPane {

    /**
     *
     */
    protected final StateGraphicModel state;

    /**
     *
     */
    protected final Circle circle = new Circle();

    //used to know if you have to save after a press
    private boolean moved = false;

    /**
     *
     * @param state state
     */
    public BasicStateView(StateGraphicModel state) {
        super();

        //make this Pane only big enough for the circle
        setPrefSize(150, 150);
        setStyle(null);

        this.state = state;

        //Initialize the position of the stateView
        DiverseUtils.setLayout(this, state.getPositionX(), state.getPositionY());

        //Bind he properties to those of the stateGraphicModel
        circle.radiusProperty().bindBidirectional(state.sizeProperty());
        circle.strokeProperty().bindBidirectional(state.borderColorProperty());
        circle.setStrokeType(StrokeType.INSIDE);

        circle.setFill(state.getInsideColor());

        //bind the stategraphicmodel to the stateview
        state.bindToStateView(this);

        getChildren().add(circle);

        //can only interact with the visible elements of the pane
        setPickOnBounds(false);
        DragManager dm = new DragManager(this);

        this.layoutXProperty().addListener(changeListener);
        this.layoutYProperty().addListener(changeListener);

        this.addEventFilter(MouseEvent.MOUSE_RELEASED, (MouseEvent event) -> {
            if (moved) {
                ((CenterComponent) getParent()).fireSave();
                moved = false;
            }
        });
    }

    private ChangeListener changeListener = new ChangeListener() {
        @Override
        public void changed(ObservableValue observable, Object oldValue, Object newValue) {
            moved = true;
        }
    };

    /**
     *
     * @return The StateGraphicModel associated with this StateView
     */
    public StateGraphicModel getState() {
        return state;
    }
}
