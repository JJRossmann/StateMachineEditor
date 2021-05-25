/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pimpmystatemachine.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import pimpmystatemachine.model.graphic.StateGraphicModel;
import pimpmystatemachine.model.graphic.StateMachineGraphicModel;
import pimpmystatemachine.model.graphic.TransitionGraphicModel;
import pimpmystatemachine.model.semantics.StateModel;
import pimpmystatemachine.model.semantics.TransitionModel;

/**
 * Class for transforming a StateMachineModel into a String in Java language and
 * putting it into a file
 *
 * @author Naoufel BENYAHIA
 */
public class JavaWriter implements IWriter {

    private String className;

    /**
     * Writes the String given by the transformation of the given
     * StateMachineModel into a file
     *
     * @param sm the statemachine to convert
     * @param file the file to write to
     * @throws java.io.IOException exception
     */
    @Override
    public void write(StateMachineGraphicModel sm, File file) throws IOException {
        //Make sure it has a Java-esque name
        String className1 = file.getName().split("\\.")[0];
        String firstLetter = className1.substring(0, 1).toUpperCase();
        className = firstLetter + className1.substring(1);
        FileReaderWriter.write(file, convertStateMachine(sm));
    }

    /**
     * Convert a TransitionModel into a String
     *
     * @param transM the transition to convert
     * @return a string
     */
    @Override
    public String convertTransition(TransitionGraphicModel transM) {
        //guard action gotTo
        String transitionString = "\nTransition " + transM.getName() + " = new Transition<" + transM.getEvent() + ">(){ ";
        if (!transM.getGuard().equals("")) {
            transitionString += "\n@Override \npublic boolean guard(){" + "\n//" + transM.getGuard() + "\n" + "return false;} ";
        }
        if (!transM.getAction().equals("")) {
            transitionString += "\n@Override \npublic void action(){" + "\n//" + transM.getAction() + "\n" + "} ";
        }
        String goToString = "\n@Override \npublic State goTo(){\nreturn " + transM.getDestination().getName() + ";} ";
        String endString = "};";
        return transitionString + goToString + endString;
    }

    /**
     * Convert a StateModel into a String
     *
     * @param stateM the state to convert
     * @return a string representing the state
     */
    @Override
    public String convertState(StateGraphicModel stateM) {
        String stateString = "public State " + stateM.getName() + " = new State(){ ";
        String endString = "};";

        String enterString = "";
        if (!stateM.getEnter().equals("")) {
            enterString = "protected void enter() \\{\n //" + stateM.getEnter() + "\n\\}";
        }
        String leaveString = "";
        if (!stateM.getLeave().equals("")) {
            leaveString = "protected void enter() \\{\n //" + stateM.getLeave() + "\n\\}";
        }

        //for Java, make sure all transitions in a State have different names
        ArrayList<String> transNames = new ArrayList<>();
        for (TransitionModel transM : stateM.getExitingList()) {
            if (transNames.contains(transM.getName())) {
                int i = 2;
                while (transNames.contains(transM.getName() + Integer.toString(i))) {
                    i++;
                }
                transM.setName(transM.getName() + Integer.toString(i));
            }
            transNames.add(transM.getName());
        }

        String transitionListString = "";
        TransitionGraphicModel tgm;
        for (TransitionModel transM : stateM.getExitingList()) {
            tgm = (TransitionGraphicModel) transM;
            transitionListString += convertTransition(tgm);
        }

        return stateString + enterString + leaveString + transitionListString + endString;

    }

    /**
     * Convert a StateMachineModel into a String
     *
     * @param smM the statemachine to onvert
     * @return a string representing the statemachine
     */
    @Override
    public String convertStateMachine(StateMachineGraphicModel smM) {
        String packageString = "package statemachines; ";
        String importsString = "import fr.liienac.statemachine.StateMachine; import fr.liienac.statemachine.event.Event; ";

        String stateMachineString = "public class " + className + " extends StateMachine {";
        String endString = "}";

        String stateListString = "";
        for (StateModel sm : smM.getStateList()) {
            stateListString += convertState((StateGraphicModel) sm);
        }

        return packageString + importsString + stateMachineString + stateListString + endString;

    }

}
