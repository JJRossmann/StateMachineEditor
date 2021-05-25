/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pimpmystatemachine.io;

import java.io.File;
import java.io.IOException;
import pimpmystatemachine.model.graphic.StateGraphicModel;
import pimpmystatemachine.model.graphic.StateMachineGraphicModel;
import pimpmystatemachine.model.graphic.TransitionGraphicModel;
import pimpmystatemachine.model.semantics.StateModel;
import pimpmystatemachine.model.semantics.TransitionModel;

/**
 * Class for transforming a StateMachineModel into a String in Smala language
 * and putting it into a file
 *
 * @author Naoufel BENYAHIA
 */
public class SmalaWriter implements IWriter {

    //TOREFINE add better handling of events and guards
    private int numInterm = 0;
    private StringBuilder intermStates = new StringBuilder();

    /**
     * Writes the String given by the transformation of the given
     * StateMachineModel into a file
     *
     * @param sm the statemachine to write
     * @param file the file to write to
     * @throws java.io.IOException exception
     */
    @Override
    public void write(StateMachineGraphicModel sm, File file) throws IOException {
        FileReaderWriter.write(file, convertStateMachine(sm));
    }

    /**
     * Convert a TransitionModel into a String
     *
     * @param transM the transition to convert
     * @return a string representing the transition
     */
    @Override
    public String convertTransition(TransitionGraphicModel transM) {
        String origin = transM.getOrigin().getName();
        String destination = transM.getDestination().getName();
        String interm = "guard_to_" + destination + Integer.toString(numInterm);

        if (!transM.getGuard().equals("")) {
            intermStates.append(createIntermediaryState(transM));
            String transition1 = "\t" + origin + "->" + interm + "(" + transM.getEvent() + ")\n";
            String transition2 = "\t" + interm + "->" + destination + "(guard_is_true)\n";
            String transition3 = "\t" + interm + "->" + origin + "(guard_is_false)\n";
            numInterm++;
            return transition1 + transition2 + transition3;
        } else {
            return "\t" + origin + "->" + destination + "(" + transM.getEvent() + ")\n";
        }
    }

    /**
     * Create an intermediary State, used for guards
     *
     * @param transM the transition to convert
     * @return a string giving the intermediary state needed in smala
     */
    private String createIntermediaryState(TransitionGraphicModel transM) {
        StringBuilder str = new StringBuilder("\t\tState guard_to_" + transM.getDestination().getName() + Integer.toString(numInterm) + " {\n");

        str.append("\t\t\tFSM guardFSM {\n");
        str.append("\t\t\t\tState eval_guard {\n");
        str.append("\t\t\t\t\tBool guard (1)\n\t\t\t\t\t(/*" + transM.getGuard() + "*/) =: guard\n");
        str.append("\t\t\t\t\tTimer t1(-1)\n\t\t\t\t\tTimer t2(-1)\n");
        str.append("\t\t\t\t\t(guard==1)?-1:10=:t1.delay\n\t\t\t\t\t(guard==0)?-1:10=:t2.delay\n");
        str.append("\t\t\t\t}\n");
        str.append("\t\t\t\tState idle {}\n");
        str.append("\t\t\t\t\teval_guard->idle(guard_is_true)\n\t\t\t\t\teval_guard->idle(guard_is_false)\n");
        str.append("\t\t\t\t}\n");

        str.append("\t\t\t\tguardFSM.eval_guard.t1.end->guard_is_true\n\t\t\t\tguardFSM.eval_guard.t2.end->guard_is_false\n");

        str.append("\t\t\t}\n");
        return str.toString();
    }

    /**
     * Convert a StateModel into a String
     *
     * @param stateM a state to convert
     * @return a string representing this state
     */
    @Override

    public String convertState(StateGraphicModel stateM) {
        String stateString = "\t\tState " + stateM.getName() + "{}\n";
        return stateString;
    }

    /**
     * Convert a StateMachineModel into a String
     *
     * @param smM a statemachine to convert
     * @return a string representing the statemachine
     */
    @Override
    public String convertStateMachine(StateMachineGraphicModel smM) {
        String useString = "use core\n" + "use base\n" + "use gui\n\n" + "_main_\n";

        String componentString = "Component root {\n" + "\tFrame f ('f',0,0,500,400)\n" + "\tExit ex (0,1)\n" + "\tf.close -> ex\n" + "\n\n";

        String spikeString = "\tSpike guard_is_false\n\tSpike guard_is_true\n";
        String finiteStateMachineString = "\tFSM fsm {\n";
        String endString = "\tfsm.state=>s.state\n}\n";

        String stateListString = "";
        for (StateModel sm : smM.getStateList()) {
            stateListString += convertState((StateGraphicModel) sm);
        }

        String transitionListString = "";
        TransitionGraphicModel tgm;
        for (TransitionModel transM : smM.getTransitionList()) {
            tgm = (TransitionGraphicModel) transM;
            transitionListString += convertTransition(tgm);
        }
        return useString + componentString + spikeString + finiteStateMachineString + stateListString + intermStates.toString() + transitionListString + endString;

    }

}
