/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pimpmystatemachine.model.semantics;

import javafx.beans.property.SimpleStringProperty;

/**
 * A class representing a transition
 *
 * @author Justin ROSSMANN
 */
public class TransitionModel {

    private StateModel origin;
    private StateModel destination;

    private final SimpleStringProperty name = new SimpleStringProperty();
    private String originName;
    private String destinationName;

    private final SimpleStringProperty event = new SimpleStringProperty();
    private final SimpleStringProperty guard = new SimpleStringProperty();
    private final SimpleStringProperty action = new SimpleStringProperty();

    /**
     * Constructor for TransitionModel
     *
     * @param origin origin
     * @param destination destination
     * @param name name
     */
    public TransitionModel(StateModel origin, StateModel destination, String name) {
        this.origin = origin;
        originName = origin.getName();
        origin.addExiting(this);
        this.destination = destination;
        destinationName = destination.getName();
        destination.addEntering(this);
        this.name.set(name);
        this.event.set("Event");
        this.guard.set("");
        this.action.set("");
    }

    /**
     * Constructor for TransitionModel
     */
    public TransitionModel() {
    }

    /**
     * Update the start and end states of this Transition
     *
     * @param smm the statemachine in which the transition is
     */
    public void updateTransition(StateMachineModel smm) {
        origin = smm.getState(originName);
        origin.addExiting(this);
        destination = smm.getState(destinationName);
        destination.addEntering(this);
    }

    /**
     * Remove this TransitionModel from the entering and exiting lists of his
     * origin and destination
     */
    public void delete() {
        origin.removeExiting(this);
        destination.removeEntering(this);
    }

    //getters and setters
    /**
     *
     * @return name property
     */
    public SimpleStringProperty nameProperty() {
        return name;
    }

    /**
     *
     * @return name of the transition
     */
    public String getName() {
        return name.get();
    }

    /**
     *
     * @param name name to set
     */
    public void setName(String name) {
        this.name.set(name);
    }

    /**
     *
     * @return origin state
     */
    public StateModel getOrigin() {
        return origin;
    }

    /**
     *
     * @param state value to set
     */
    public void setOrigin(StateModel state) {
        if (origin != null) {
            origin.removeExiting(this);
        }
        origin = state;
        originName = state.getName();
        origin.addExiting(this);
    }

    /**
     *
     * @return destination
     */
    public StateModel getDestination() {
        return destination;
    }

    /**
     *
     * @param state value to set
     */
    public void setDestination(StateModel state) {
        if (destination != null) {
            destination.removeEntering(this);
        }
        destination = state;
        destinationName = state.getName();
        destination.addEntering(this);
    }

    /**
     *
     * @param originName name to set
     */
    public void setOriginName(String originName) {
        this.originName = originName;
    }

    /**
     *
     * @param destinationName name to set
     */
    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    /**
     *
     * @return event property
     */
    public SimpleStringProperty eventProperty() {
        return event;
    }

    /**
     *
     * @return event
     */
    public String getEvent() {
        return event.get();
    }

    /**
     *
     * @param event value to set
     */
    public void setEvent(String event) {
        this.event.set(event);
    }

    /**
     *
     * @return guard property
     */
    public SimpleStringProperty guardProperty() {
        return guard;
    }

    /**
     *
     * @return guard
     */
    public String getGuard() {
        return guard.get();
    }

    /**
     *
     * @param guard value to set
     */
    public void setGuard(String guard) {
        this.guard.set(guard);
    }

    /**
     *
     * @return action property
     */
    public SimpleStringProperty actionProperty() {
        return action;
    }

    /**
     *
     * @return action
     */
    public String getAction() {
        return action.get();
    }

    /**
     *
     * @param action value to set
     */
    public void setAction(String action) {
        this.action.set(action);
    }

    @Override
    public String toString() {
        String str = name.get() + ":" + originName + "->" + destinationName;
        return str;
    }
}
