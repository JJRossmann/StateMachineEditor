/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pimpmystatemachine.io;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Naoufel BENYAHIA
 */
public class StateMachineAdditionnals {

    //TODO put it together with the reading of a file
    private final ArrayList<String> smAdditionnalStrings = new ArrayList(); //This arraylist contains the additionnals atttributes and methods a StateMachine may have in a read .java file
    private final HashMap<String, ArrayList<String>> statesAdditionnalStrings = new HashMap<>(); //This hashmap links a state name with the additionnal things this state may possess
    private final HashMap<String, HashMap<String, ArrayList<String>>> stateTransitionsAdditionnalStrings = new HashMap<>(); //This hashmap links a state name with another hashmap linking the transition names with their additionnal elements

    /**
     *
     * @return arraylist of strings
     */
    public ArrayList<String> getSMAdditionnalStrings() {
        return smAdditionnalStrings;
    }

    /**
     *
     * @return HashMapString, ArrayListString
     */
    public HashMap<String, ArrayList<String>> getStatesAdditionnalStrings() {
        return statesAdditionnalStrings;
    }

    /**
     *
     * @return HashMapString, HashMapString, ArrayListString
     */
    public HashMap<String, HashMap<String, ArrayList<String>>> getStatesTransitionsAdditionnalStrings() {
        return stateTransitionsAdditionnalStrings;
    }

    /**
     *
     * @param str string
     */
    public void storeAdditionnalSMString(String str) {
        this.smAdditionnalStrings.add(str);
    }

    /**
     *
     * @param stateName name of state
     * @param str extra
     */
    public void storeAdditionnalStateString(String stateName, String str) {
        if (!statesAdditionnalStrings.containsKey(stateName)) {
            statesAdditionnalStrings.put(stateName, new ArrayList<>());
        }
        statesAdditionnalStrings.get(stateName).add(str);
    }

    /**
     *
     * @param stateName name of state
     * @param transitionName name of transition
     * @param str extra
     */
    public void storeAdditionnalStateTransitionString(String stateName, String transitionName, String str) {
        if (!stateTransitionsAdditionnalStrings.containsKey(stateName)) {
            stateTransitionsAdditionnalStrings.put(stateName, new HashMap<>());
        }
        //Checks if the hashmap encountered the given state name; if not, adds it as a key with a new hashmap of <transitionname,arraylist of additionnal strings>
        HashMap<String, ArrayList<String>> transitionsAdditionnalStrings = stateTransitionsAdditionnalStrings.get(stateName);
        if (!transitionsAdditionnalStrings.containsKey(transitionName)) {
            transitionsAdditionnalStrings.put(transitionName, new ArrayList<>());
        }
        //Same as above but with the transition name
        transitionsAdditionnalStrings.get(transitionName).add(str);
        stateTransitionsAdditionnalStrings.replace(stateName, transitionsAdditionnalStrings);
    }

}
