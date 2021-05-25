/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pimpmystatemachine.views.statemachinecomponents;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import pimpmystatemachine.views.managers.TextFieldModificationManager;

/**
 * Utility class for some useful methods
 *
 * @author Justin ROSSMANN
 */
public class DiverseUtils {

    /**
     *
     */
    public static String textFieldModificationStyle = "-fx-text-box-border: red; -fx-focus-color: red";

    /**
     *
     */
    public static String textFieldModifiedStyle = "-fx-text-box-border: green; -fx-focus-color: green";

    /**
     *
     */
    public static String textFieldDefaultStyle = "-fx-control-inner-background: white";

    /**
     * Bind layout of a node to layout of another node
     *
     * @param nodeToBind nodeToBind
     * @param n node
     * @param offsetX offsetx
     * @param offsetY offsety
     */
    public static void bindLayout(Node nodeToBind, Node n, double offsetX, double offsetY) {
        nodeToBind.layoutXProperty().bind(n.layoutXProperty().add(offsetX));
        nodeToBind.layoutYProperty().bind(n.layoutYProperty().add(offsetY));
    }

    /**
     * Set layout of a node to a certain offset
     *
     * @param nodeToSet node
     * @param offsetX offsetx
     * @param offsetY offsety
     */
    public static void setLayout(Node nodeToSet, double offsetX, double offsetY) {
        nodeToSet.setLayoutX(offsetX);
        nodeToSet.setLayoutY(offsetY);
    }

    /**
     * Initialize a TextField
     *
     * @param tf tf
     * @param parent parent
     * @param px positionx
     * @param py positiony
     * @param label label
     * @param tx labelx
     * @param ty labely
     * @param defaultText default text
     */
    public static void initializeTextField(TextField tf, Node parent, double px, double py, Text label, double tx, double ty, String defaultText) {
        tf.setText(defaultText);
        bindLayout(tf, parent, px, py);
        tf.setPrefSize(180, 20);
        bindLayout(label, tf, tx, ty);
    }

    /**
     * Initialize a TextField and add a modification manager
     *
     * @param tf tf
     * @param parent parent
     * @param px positionx
     * @param py positiony
     * @param label label
     * @param tx labelx
     * @param ty labely
     * @param defaultText default text
     * @param ssp ssp
     */
    public static void initializeTextField(TextField tf, Node parent, double px, double py, Text label, double tx, double ty, String defaultText, SimpleStringProperty ssp) {
        initializeTextField(tf, parent, px, py, label, tx, ty, defaultText);
        TextFieldModificationManager tfm = new TextFieldModificationManager(tf, ssp);
    }

    /**
     * Initlialize a comboBox
     *
     * @param cb cb
     * @param parent parent
     * @param px posx
     * @param py posy
     * @param label label
     * @param tx labelx
     * @param ty labely
     */
    public static void initializeComboBox(ComboBox cb, Node parent, double px, double py, Text label, double tx, double ty) {
        bindLayout(cb, parent, px, py);
        bindLayout(label, cb, tx, ty);
    }
}
