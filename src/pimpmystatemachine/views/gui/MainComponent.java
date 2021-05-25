/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pimpmystatemachine.views.gui;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author Justin ROSSMANN
 */
public class MainComponent extends BorderPane {

    private final CenterComponent center;

    /**
     * Constructor for MainComponent
     *
     * @param primaryStage stage
     * @param width width
     * @param height height
     */
    public MainComponent(Stage primaryStage, double width, double height) {
        center = new CenterComponent(width, height);

        Pane p = new Pane();
        p.getChildren().add(center);

        setCenter(p);
        setTop(new MenuBarComponent(primaryStage, center));
    }

    /**
     *
     * @return center
     */
    public CenterComponent getCenterPane() {
        return center;
    }
}
