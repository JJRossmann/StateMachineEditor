/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pimpmystatemachine.views.managers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import pimpmystatemachine.io.JsonReader;
import pimpmystatemachine.io.JsonWriter;
import pimpmystatemachine.views.event.SaveEvent;
import pimpmystatemachine.views.gui.CenterComponent;

/**
 *
 * @author Justin ROSSMANN
 */
public class SaveManager {

    private CenterComponent center;
    private JsonWriter jw = new JsonWriter();
    private JsonReader jr = new JsonReader();
    private int fileCount = 0;
    private int actualFile = 0;

    /**
     * Constructor for SaveManager
     *
     * @param center center component for this save manager
     */
    public SaveManager(CenterComponent center) {
        this.center = center;
        try {
            jw.write(center.getStateMachine(), new File("temporarySave/undoSave0.json"));
        } catch (IOException ex) {
            Logger.getLogger(SaveManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        center.addEventHandler(SaveEvent.SAVE_EVENT, saveHandler);
    }

    private final EventHandler<SaveEvent> saveHandler = new EventHandler<SaveEvent>() {
        @Override
        public void handle(SaveEvent event) {
            try {
                if (fileCount == actualFile) {
                    fileCount++;
                    actualFile++;
                } else {
                    fileCount = actualFile + 1;
                    actualFile++;
                }
                if (fileCount == 0) {
                    fileCount++;
                    actualFile++;
                }
                jw.write(center.getStateMachine(), new File("temporarySave/undoSave" + Integer.toString(fileCount) + ".json"));
            } catch (IOException ex) {
                Logger.getLogger(SaveManager.class.getName()).log(Level.SEVERE, null, ex);
            }

            event.consume();
        }
    };

    /**
     * Load previous save in memory
     */
    public void loadPreviousSave() {
        if (actualFile > 0) {
            actualFile--;
        }
        try {
            center.setStateMachine(jr.read(new File("temporarySave/undoSave" + Integer.toString(actualFile) + ".json")));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SaveManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Load next save in memory
     */
    public void loadNextSave() {
        if (actualFile < fileCount) {
            actualFile++;
        }
        try {
            center.setStateMachine(jr.read(new File("temporarySave/undoSave" + Integer.toString(actualFile) + ".json")));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SaveManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
