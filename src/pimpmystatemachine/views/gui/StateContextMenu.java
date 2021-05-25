/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pimpmystatemachine.views.gui;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.geometry.Point2D;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import pimpmystatemachine.model.graphic.StateGraphicModel;
import pimpmystatemachine.views.event.UnselectEvent;
import pimpmystatemachine.views.statemachinecomponents.RealStateView;

/**
 *
 * @author Naoufel BENYHIA, Justin ROSSMANN
 */
public class StateContextMenu extends ContextMenu {

    private final MenuItem duplicateItem = new MenuItem("Duplicate state");
    private final MenuItem deleteItem = new MenuItem("Delete state");
    private final MenuItem renameItem = new MenuItem("Rename state");
    private final MenuItem finalItem = new MenuItem("Make state final");
    private final MenuItem initialItem = new MenuItem("Make state initial");
    private final MenuItem normalItem = new MenuItem("Make state normal");
    private final MenuItem deleteAllTransItem = new MenuItem("Delete all transitions");
    private final MenuItem deleteEnterTransItem = new MenuItem("Delete entering transitions");
    private final MenuItem deleteExitTransItem = new MenuItem("Delete exiting transitions");

    /**
     * Constructor for StateContextMenu
     *
     * @param sv real state view
     */
    public StateContextMenu(RealStateView sv) {
        //create even handlers for each item

        duplicateItem.setOnAction((ActionEvent event) -> {
            StateGraphicModel sg = sv.getState();
            ((CenterComponent) sv.getParent()).getStateMachine().addState(sg.getName() + "_duplicate", sg.getSize(), sg.getBorderColor(), sg.getInsideColor(), new Point2D(sg.getPositionX() + 100, sg.getPositionY() + 100));
            event.consume();
        });

        deleteItem.setOnAction((ActionEvent event) -> {
            Event.fireEvent(sv, new UnselectEvent(UnselectEvent.UNSELECT_EVENT));
            ((CenterComponent) sv.getParent()).getStateMachine().removeState(sv.getState());
            event.consume();
        });

        renameItem.setDisable(true);
        renameItem.setOnAction((ActionEvent event) -> {
            throw new UnsupportedOperationException("Not supported yet.");
            //event.consume();
        });

        finalItem.setOnAction((ActionEvent event) -> {
            sv.setFinal(true);
            event.consume();
        });

        initialItem.setOnAction((ActionEvent event) -> {
            sv.setInitial(true);
            event.consume();
        });

        normalItem.setOnAction((ActionEvent event) -> {
            sv.setFinal(false);
            sv.setInitial(false);
            event.consume();
        });

        deleteAllTransItem.setOnAction((ActionEvent event) -> {
            ((CenterComponent) sv.getParent()).getStateMachine().removeAllTransitionsFromState(sv.getState());
        });

        deleteEnterTransItem.setOnAction((ActionEvent event) -> {
            ((CenterComponent) sv.getParent()).getStateMachine().removeEnteringTransitionsFromState(sv.getState());
        });

        deleteExitTransItem.setOnAction((ActionEvent event) -> {
            ((CenterComponent) sv.getParent()).getStateMachine().removeExitingTransitionsFromState(sv.getState());
        });

        // Add MenuItem to ContextMenu
        this.getItems().addAll(duplicateItem, deleteItem, renameItem, finalItem, initialItem, normalItem, deleteAllTransItem, deleteEnterTransItem, deleteExitTransItem);
    }

}
