/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pimpmystatemachine.io;

import java.io.File;
import java.io.FileNotFoundException;
import pimpmystatemachine.model.graphic.StateGraphicModel;
import pimpmystatemachine.model.graphic.StateMachineGraphicModel;
import pimpmystatemachine.model.graphic.TransitionGraphicModel;

/**
 * Interface for readers
 *
 * @author Justin ROSSMANN
 */
public interface IReader {

    /**
     * Convert a String to a StateMachineGraphicModel
     *
     * @param str string to read
     * @return A StateMachineGraphicModel corresponding to the String
     */
    public StateMachineGraphicModel readStateMachineGraphicModel(String str);

    /**
     * Convert a String to a StateGraphicModel
     *
     * @param sm the statemachine
     * @param str the string to read from
     * @return A StateGraphicModel corresponding to the String
     */
    public StateGraphicModel readStateGraphicModel(StateMachineGraphicModel sm, String str);

    /**
     * Convert a String to a TransitionGraphicModel
     *
     * @param sm the statemachine
     * @param str the string to read from
     * @return A TransitionGraphicModel corresponding to the String
     */
    public TransitionGraphicModel readTransitionGraphicModel(StateMachineGraphicModel sm, String str);

    /**
     * Convert the contents of a File to a StateMachineGraphicModel
     *
     * @param file the file to read from
     * @return a StateMachineModel
     * @throws java.io.FileNotFoundException exception
     */
    public StateMachineGraphicModel read(File file) throws FileNotFoundException;
}
