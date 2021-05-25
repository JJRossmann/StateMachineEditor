/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pimpmystatemachine.model.semantics;

import java.util.ArrayList;

/**
 * A class for representing a StateMachine
 *
 * @author Justin ROSSMANN
 */
public class StateMachineModel {

    //TODO make these arraylists observable to listen to changes more easily
    private final ArrayList<StateModel> stateList;
    private final ArrayList<TransitionModel> transList;

    /**
     * Instanciate a StateMachine
     *
     */
    public StateMachineModel() {
        stateList = new ArrayList<>();
        transList = new ArrayList<>();
    }

    /**
     * Add a StateModel
     *
     * @param s StateModel to add
     */
    public void addState(StateModel s) {
        stateList.add(s);
    }

    /**
     * Add a State to the machine by specifying its name and make sure it is
     * unique
     *
     * @param name name of the state
     */
    public void addState(String name) {
        String name2 = uniqueStateNameVerification(name);
        StateModel s = new StateModel(name2);
        addState(s);
    }

    /**
     * Returns if the given name already exists for a StateModel in the
     * StateMachine
     *
     * @param name
     * @return true if already exists else false
     */
    private boolean stateNameExists(String name) {
        for (StateModel s : stateList) {
            if (s.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a unique name based on the given name
     *
     * @param name name of the state
     * @return a unique name not already existing in the StateMachineModel
     */
    protected String uniqueStateNameVerification(String name) {
        String name2 = name;
        if (stateNameExists(name2)) {
            int num = 2;
            name2 = name + Integer.toString(num);
            while (stateNameExists(name2)) {
                num++;
                name2 = name + Integer.toString(num);
            }
        }
        return name2;
    }

    /**
     * Renames a StateModel
     *
     * @param state StateModel to rename
     * @param newName new name for the StateModel
     */
    public void renameState(StateModel state, String newName) {
        if (!(state.getName().equals(newName))) {
            state.setName(uniqueStateNameVerification(newName));
        }
    }

    /**
     *
     * @param t transitionmodel
     */
    public void addTransition(TransitionModel t) {
        transList.add(t);
    }

    /**
     * Remove a TransitionModel
     *
     * @param t TransitionModel to remove
     */
    public void removeTransition(TransitionModel t) {
        t.delete();
        transList.remove(t);
    }

    /**
     * get the StateModel with a given name
     *
     * @param name name of the stateModel
     * @return the state with the given parameter as name
     */
    public StateModel getState(String name) {
        StateModel state = null;
        for (StateModel s : stateList) {
            if (s.getName().equals(name)) {
                return s;
            }
        }
        return state;
    }

    /**
     * Get the ArrayList of all StateModels
     *
     * @return the ArrayList of all StateModels
     */
    public ArrayList<StateModel> getStateList() {
        return stateList;
    }

    /**
     * Get the ArrayList of all TransitionModels
     *
     * @return the ArrayList of all TransitionModels
     */
    public ArrayList<TransitionModel> getTransitionList() {
        return transList;
    }

    @Override
    public String toString() {
        String str = "Current StateMachine: \n";
        for (StateModel s : stateList) {
            str += s.toString() + "\n";
        }
        return str;
    }

}
