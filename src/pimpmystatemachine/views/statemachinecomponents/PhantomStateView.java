/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pimpmystatemachine.views.statemachinecomponents;

import pimpmystatemachine.model.graphic.StateGraphicModel;
import pimpmystatemachine.model.graphic.TransitionGraphicModel;

/**
 * Class for representing a Phantom State View
 *
 * @author Justin ROSSMANN
 */
public class PhantomStateView extends BasicStateView {

    /**
     * Constructor for PhantomStateView
     *
     * @param state state
     */
    public PhantomStateView(StateGraphicModel state) {
        super(state);
    }

    /**
     *
     * @return true if the phantom state is the destination of the transition
     */
    public boolean isDestination() {
        //if exiting list is empty that means the transition is in the enterig list
        return state.getExitingList().isEmpty();
    }

    /**
     * Return the only Transition of a Phantom State
     *
     * @return TransitionGraphicModel
     */
    public TransitionGraphicModel getTransition() {
        //a phantom state only has 1 transition
        if (isDestination()) {
            return (TransitionGraphicModel) state.getEnteringList().get(0);
        } else {
            return (TransitionGraphicModel) state.getExitingList().get(0);
        }
    }
}
