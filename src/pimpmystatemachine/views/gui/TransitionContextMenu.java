/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pimpmystatemachine.views.gui;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import pimpmystatemachine.views.event.UnselectEvent;
import pimpmystatemachine.views.statemachinecomponents.TransitionView;

/**
 *
 * @author Naoufel BENYAHIA
 */
public class TransitionContextMenu extends ContextMenu {

    private final MenuItem copyItem = new MenuItem("Copy transition");
    private final MenuItem deleteItem = new MenuItem("Delete transition");
    private final MenuItem renameItem = new MenuItem("Rename transition");

    /**
     *
     * @param tv transition view
     */
    public TransitionContextMenu(TransitionView tv) {
        //create even handlers for each item
        copyItem.setDisable(true);
        copyItem.setOnAction((ActionEvent event) -> {
            throw new UnsupportedOperationException("Not supported yet.");
            //event.consume();
        });

        deleteItem.setOnAction((ActionEvent event) -> {
            Event.fireEvent(tv, new UnselectEvent(UnselectEvent.UNSELECT_EVENT));
            ((CenterComponent) tv.getParent()).getStateMachine().removeTransition(tv.getTransition());
            event.consume();
        });

        renameItem.setDisable(true);
        renameItem.setOnAction((ActionEvent event) -> {
            throw new UnsupportedOperationException("Not supported yet.");
            //event.consume();
        });

        // Add MenuItem to ContextMenu
        getItems().addAll(copyItem, deleteItem, renameItem);
    }

}
