/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pimpmystatemachine.application;

import java.io.File;
import java.io.IOException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pimpmystatemachine.io.JsonWriter;
import pimpmystatemachine.views.gui.MainComponent;

/**
 * The launcher for the application
 *
 * @author Justin ROSSMANN -- Naoufel BENYAHIA -- Fatiha OUARDI
 */
public class StateMachineEditorApplication extends Application {

    private MainComponent root;

    /**
     * width of the window
     */
    public double WIDTH = 1024;

    /**
     * height of the window
     */
    public double HEIGHT = 768;

    /**
     *
     * @param args args
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        root = new MainComponent(primaryStage, WIDTH, HEIGHT);

        Scene scene = new Scene(root, WIDTH, HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.setTitle("SMEditor3000 (X-trem Edition 3 Deluxe)");
        primaryStage.setResizable(true);

        primaryStage.show();
    }

    @Override
    public void stop() throws IOException {
        //when program stops, delete all temporary save files for undo/redo
        for (File file : new java.io.File("temporarySave/").listFiles()) {
            file.delete();
        }
        //TODO create a save manager that lets you choose where to save and make it homogenous with the saving in MenuBarComponent
        //make temporary json save if the program is abruptly quitted
        JsonWriter jw = new JsonWriter();
        jw.write(root.getCenterPane().getStateMachine(), new File("temporarySave/temp.json"));
    }
}
