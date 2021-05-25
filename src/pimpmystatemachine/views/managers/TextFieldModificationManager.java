/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pimpmystatemachine.views.managers;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import pimpmystatemachine.views.statemachinecomponents.DiverseUtils;

/**
 * Class for handling a textfield modification
 *
 * @author Justin ROSSMANN
 */
public class TextFieldModificationManager {

    private TextField tf;
    private SimpleStringProperty ssp;

    /**
     *
     * @param tf textfield for this manager
     * @param ssp string property for this manager
     */
    public TextFieldModificationManager(TextField tf, SimpleStringProperty ssp) {
        this.tf = tf;
        this.ssp = ssp;
        tf.textProperty().addListener(tfChangeListener);
        tf.setOnAction(tfChangeEventHandler);
        tf.focusedProperty().addListener(tfFocusChangeListener);
    }

    private final ChangeListener<String> tfChangeListener = new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue ov, String t, String t1) {
            tf.setStyle(DiverseUtils.textFieldModificationStyle);
        }
    };

    private final EventHandler tfChangeEventHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            ssp.set(tf.textProperty().get());
            tf.setStyle(DiverseUtils.textFieldModifiedStyle);
        }
    };

    private final InvalidationListener tfFocusChangeListener = new InvalidationListener() {
        @Override
        public void invalidated(Observable obs) {
            ssp.set(tf.textProperty().get());
            tf.setStyle(DiverseUtils.textFieldModifiedStyle);
        }
    };

}
