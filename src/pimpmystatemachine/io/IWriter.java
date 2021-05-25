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

/**
 * Interfaces for writers
 *
 * @author Justin ROSSMANN
 */
public interface IWriter {

    /**
     * Convert a StateMachineGraphicModel into a String
     *
     * @param sm the statemachine
     * @return the string representing the statemachine
     */
    public String convertStateMachine(StateMachineGraphicModel sm);

    /**
     * Convert a StateGraphicModel into a String
     *
     * @param s the state
     * @return a string representing the state
     */
    public String convertState(StateGraphicModel s);

    /**
     * Convert a TransitionGraphicModel into a String
     *
     * @param t a transition
     * @return a string representing the transition
     */
    public String convertTransition(TransitionGraphicModel t);

    /**
     * Writes the String given by the transformation of the given
     * StateMachineGraphicModel into a file
     *
     * @param sm the statemachine
     * @param file file to write to
     * @throws java.io.IOException exception
     */
    public void write(StateMachineGraphicModel sm, File file) throws IOException;
}
