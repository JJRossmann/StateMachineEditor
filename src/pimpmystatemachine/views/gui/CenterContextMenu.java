/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pimpmystatemachine.views.gui;

import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import pimpmystatemachine.views.statemachinecomponents.DiverseUtils;
import pimpmystatemachine.views.statemachinecomponents.RealStateView;

/**
 *
 * @author Justin ROSSMANN
 */
public class CenterContextMenu extends ContextMenu {

    private final MenuItem deleteAllItems = new MenuItem("Delete Selected Items");
    private final MenuItem alignHorizontal = new MenuItem("Align Selected States Horizontally");
    private final MenuItem alignVertical = new MenuItem("Align Selected States Vertically");

    /**
     * Constructor for StateContextMenu
     *
     * @param center center
     */
    public CenterContextMenu(CenterComponent center) {
        //create even handlers for each item

        deleteAllItems.setOnAction((ActionEvent event) -> {
            Event.fireEvent(center, new KeyEvent(center, center, KeyEvent.KEY_PRESSED, "", "", KeyCode.DELETE, false, false, false, false));
            event.consume();
        });

        alignHorizontal.setOnAction((ActionEvent event) -> {
            Point2D position = null;
            int cp = 1;
            ArrayList<Node> nodes = new ArrayList(center.getSelectedNodes());
            for (Node n : nodes) {
                if (n instanceof RealStateView) {
                    RealStateView rsv = (RealStateView) n;
                    if (position == null) {
                        position = new Point2D(rsv.getState().getPositionX(), rsv.getState().getPositionY());
                    } else {
                        DiverseUtils.setLayout(rsv, position.getX() + (cp * 200), position.getY());
                        cp++;
                    }
                }
            }
            event.consume();
        });

        alignVertical.setOnAction((ActionEvent event) -> {
            Point2D position = null;
            int cp = 1;
            ArrayList<Node> nodes = new ArrayList(center.getSelectedNodes());
            for (Node n : nodes) {
                if (n instanceof RealStateView) {
                    RealStateView rsv = (RealStateView) n;
                    if (position == null) {
                        position = new Point2D(rsv.getState().getPositionX(), rsv.getState().getPositionY());
                    } else {
                        DiverseUtils.setLayout(rsv, position.getX(), position.getY() + (cp * 200));
                        cp++;
                    }
                }
            }
            event.consume();
        });

        // Add MenuItem to ContextMenu
        this.getItems().addAll(deleteAllItems, alignHorizontal, alignVertical);
    }
}
