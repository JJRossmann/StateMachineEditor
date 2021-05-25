/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pimpmystatemachine.views.gui;

import dragpanzoom.managers.PanManager;
import dragpanzoom.managers.ZoomManager;
import dragpanzoom.views.TranslatableHomotheticPane;
import java.util.ArrayList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.ContextMenuEvent;
import pimpmystatemachine.model.graphic.StateMachineGraphicModel;
import pimpmystatemachine.views.event.SaveEvent;
import pimpmystatemachine.views.managers.SaveManager;
import pimpmystatemachine.views.managers.SelectionDeletionMovingManager;
import pimpmystatemachine.views.managers.StateCreationManager;
import pimpmystatemachine.views.managers.TransitionCreationChangeManager;

/**
 *
 * @author Justin ROSSMANN
 */
public class CenterComponent extends TranslatableHomotheticPane {

    private StateMachineGraphicModel smg;
    private final ArrayList<Node> selectedNodes = new ArrayList<>();

    private CenterContextMenu ccm;
    private final SaveManager saveManager;

    /**
     * Constructor for CenterComponent
     *
     * @param width width
     * @param height height
     */
    public CenterComponent(double width, double height) {
        super();

        this.ccm = new CenterContextMenu(this);

        //focus needs to be on this or else it won't catch key events
        setFocusTraversable(true);
        requestFocus();

        //TOREFINE make size of the pane more adaptable and update the bounds when an object goes outside
        setPrefSize(width, height);

        this.setOnContextMenuRequested(contextMenuHandler);

        //managers
        smg = new StateMachineGraphicModel(this);
        PanManager pm = new PanManager(this);
        ZoomManager zm = new ZoomManager(this);
        StateCreationManager scm = new StateCreationManager(this);
        TransitionCreationChangeManager tcm = new TransitionCreationChangeManager(this);
        SelectionDeletionMovingManager sm = new SelectionDeletionMovingManager(this);
        saveManager = new SaveManager(this);
    }

    //needed for contextMenuHandler, "this" alone does not work
    private CenterComponent getThis() {
        return this;
    }

    private final EventHandler contextMenuHandler = new EventHandler<ContextMenuEvent>() {
        @Override
        public void handle(ContextMenuEvent event) {
            ccm.show(getThis(), event.getScreenX(), event.getScreenY());
            event.consume();
        }
    };

    /**
     *
     * @return the StateMachine associated with this
     */
    public StateMachineGraphicModel getStateMachine() {
        return smg;
    }

    /**
     * Set the StateMachine represented graphically
     *
     * @param smg statemachine
     */
    public void setStateMachine(StateMachineGraphicModel smg) {
        getChildren().clear();
        selectedNodes.clear();
        this.smg = smg;
        this.smg.setCenter(this);
        this.smg.loadStateMachine();
    }

    /**
     * Delete everything represented
     */
    void deleteAll() {
        getChildren().clear();
        selectedNodes.clear();
        this.smg = new StateMachineGraphicModel();
        this.smg.setCenter(this);
    }

    /**
     *
     * @return selectedNodes
     */
    public ArrayList<Node> getSelectedNodes() {
        return selectedNodes;
    }

    /**
     *
     * @return saveManager
     */
    public SaveManager getSaveManager() {
        return saveManager;
    }

    /**
     *
     * @return ccm
     */
    public CenterContextMenu getCenterContextMenu() {
        return ccm;
    }

    /**
     *
     */
    public void fireSave() {
        Event.fireEvent(this, new SaveEvent(SaveEvent.SAVE_EVENT));
    }
}
