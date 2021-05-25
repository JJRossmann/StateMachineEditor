/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pimpmystatemachine.model.semantics;

import java.util.ArrayList;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * A class representing a State
 *
 * @author Justin ROSSMANN
 */
public class StateModel {

    private final SimpleStringProperty name = new SimpleStringProperty();
    private final SimpleBooleanProperty isInitial = new SimpleBooleanProperty();
    private final SimpleBooleanProperty isFinal = new SimpleBooleanProperty();

    private final ArrayList<TransitionModel> enteringList = new ArrayList<>();
    private final ArrayList<TransitionModel> exitingList = new ArrayList<>();

    /**
     * Constructor for StateModel
     *
     * @param name state model name
     */
    public StateModel(String name) {
        this.name.set(name);
    }

    //getters and setters
    /**
     *
     * @return the name of the state
     */
    public String getName() {
        return name.get();
    }

    /**
     *
     * @param newName new name of the state
     */
    public void setName(String newName) {
        name.set(newName);
    }

    /**
     *
     * @return the name Property
     */
    public SimpleStringProperty nameProperty() {
        return name;
    }

    /**
     *
     * @return the entering transitions list
     */
    public ArrayList<TransitionModel> getEnteringList() {
        return new ArrayList<>(enteringList);
    }

    /**
     *
     * @return the exiting transitions list
     */
    public ArrayList<TransitionModel> getExitingList() {
        return new ArrayList<>(exitingList);
    }

    /**
     *
     * @param t the transitionModel to add
     */
    public void addEntering(TransitionModel t) {
        enteringList.add(t);
    }

    /**
     *
     * @param t the transitionmodel to remove
     */
    public void removeEntering(TransitionModel t) {
        enteringList.remove(t);
    }

    /**
     *
     * @param t the transition model to add
     */
    public void addExiting(TransitionModel t) {
        exitingList.add(t);
    }

    /**
     *
     * @param t the transitionmodel to remove
     */
    public void removeExiting(TransitionModel t) {
        exitingList.remove(t);
    }

    /**
     *
     * @return initial property
     */
    public SimpleBooleanProperty isInitialProperty() {
        return isInitial;
    }

    /**
     *
     * @return is initial
     */
    public boolean isInitial() {
        return isInitial.get();
    }

    /**
     *
     * @param b value to set
     */
    public void setInitial(boolean b) {
        isInitial.set(b);
    }

    /**
     *
     * @return final property
     */
    public SimpleBooleanProperty isFinalProperty() {
        return isFinal;
    }

    /**
     *
     * @return is final
     */
    public boolean isFinal() {
        return isFinal.get();
    }

    /**
     *
     * @param b value to set
     */
    public void setFinal(boolean b) {
        isFinal.set(b);
    }

    /**
     *
     * @return True if this State has Transitions linked to it
     */
    public boolean hasTransitions() {
        return exitingList.size() + enteringList.size() != 0;
    }

    @Override
    public String toString() {
        String str = name.get() + ": ";
        for (TransitionModel t : exitingList) {
            str += t.toString() + " ";
        }
        return str;
    }
}
