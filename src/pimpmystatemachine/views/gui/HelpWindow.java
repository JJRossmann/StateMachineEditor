/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pimpmystatemachine.views.gui;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author fatiha
 */
public class HelpWindow extends Stage {

    private final StackPane pane = new StackPane();
    private final VBox vbox = new VBox();
    private final HBox hbox1 = new HBox();
    private final HBox hbox2 = new HBox();
    private final Scene scene;

    private final Label aboutTitle;
    private final TextArea aboutText;
    private final Label shortcutsTitle;
    private final TextArea shortcutsText;
    private final Label manipTitle;
    private final TextArea manipText;
    private final String headingsStyles = "-fx-font-weight: bold;";
    private final String textsStyles = "-fx-background-color: #F9F9F9; -fx-border-color: #F2F2F2";

    /**
     *
     */
    public HelpWindow() {
        super();
        aboutTitle = new Label("About US");
        aboutTitle.setStyle(headingsStyles);
        aboutText = new TextArea("This application is made by\nJustin ROSSMANN\nNaoufel BENYAHIA\nFatiha OUARDI\nAs part of a Java project at ENAC\nin 2021\n");
        aboutText.setEditable(false);
        aboutText.setStyle(textsStyles);
        aboutText.setMinHeight(450);
        shortcutsTitle = new Label("Shortcuts");
        shortcutsTitle.setStyle(headingsStyles);
        shortcutsText = new TextArea("Undo : Ctrl+Z\nRedo : Ctrl+Y\nRemove all : Ctrl+D\nNew File : Ctrl+N\nSave File : Ctrl+S\nOpen File : Ctrl+O\nExit Ctrl+Q\nHelp : Ctrl+H\n\n For Mac use Command instead of Ctrl\n");
        shortcutsText.setStyle(textsStyles);
        shortcutsText.setEditable(false);
        manipTitle = new Label("Manipulation");
        manipTitle.setStyle(headingsStyles);
        manipText = new TextArea("- Create a state :\ndouble click on the editor \n- Create a transition :\ndrag from the center of the origin state \nto the destination state \n- Edit the state informations:\ndouble click on the state\n- Edit the transition informations :\ndouble click on the transition \n- Show the menu related to a node : \nright click on the node \nMove selected states with the arrow keys\n\nTO DRAG SOMETHING USE SHIFT\n\nAlignment of states is done in the\norder of selection\n");
        manipText.setStyle(textsStyles);
        manipText.setEditable(false);

        hbox1.setSpacing(270);
        hbox2.setSpacing(10);

        hbox1.getChildren().addAll(aboutTitle, shortcutsTitle, manipTitle);
        hbox2.getChildren().addAll(aboutText, shortcutsText, manipText);
        vbox.getChildren().add(hbox1);
        vbox.getChildren().add(hbox2);
        pane.getChildren().add(vbox);

        scene = new Scene(pane, 900, 500);

        this.setTitle("Help Window");
        this.setScene(scene);
        this.setResizable(false);
        this.show();
    }

}
