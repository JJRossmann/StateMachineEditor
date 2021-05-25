/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pimpmystatemachine.model.graphic;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import pimpmystatemachine.model.semantics.StateMachineModel;
import pimpmystatemachine.model.semantics.StateModel;
import pimpmystatemachine.model.semantics.TransitionModel;
import pimpmystatemachine.views.gui.CenterComponent;
import pimpmystatemachine.views.statemachinecomponents.PhantomStateView;
import pimpmystatemachine.views.statemachinecomponents.RealStateView;
import pimpmystatemachine.views.statemachinecomponents.TransitionView;

/**
 *
 * @author Justin ROSSMANN
 */
public class StateMachineGraphicModel extends StateMachineModel {

    //TODO algorithm for managing transition intersections
    //TODO make it possible to simulate the StateMachine directly in the interface by sending various events
    //constants for State and Transition creation
    private static final String DEFAULT_STATE_NAME = "state";
    private static final double DEFAULT_STATE_SIZE = 50;
    private static final Color DEFAULT_STATE_BORDER_COLOR = Color.STEELBLUE;
    private static final Color DEFAULT_STATE_INSIDE_COLOR = Color.LIGHTBLUE;

    private static final String DEFAULT_TRANSITION_NAME = "transition";
    private static final Color DEFAULT_TRANSITION_COLOR = Color.GREEN;
    private static final double DEFAULT_TRANSITION_THICKNESS = 5;

    private CenterComponent center;

    /**
     * Constructor for StateMachineGraphicModel
     */
    public StateMachineGraphicModel() {
        super();
    }

    /**
     * Constructor for StateMachineGraphicModel
     *
     * @param center center
     */
    public StateMachineGraphicModel(CenterComponent center) {
        this();
        this.center = center;
    }

    /**
     * Add a State to the StateMachineGraphicModel, gets attributed a unique
     * State name based on the one given
     *
     * @param name name
     * @param size size
     * @param border color
     * @param inside color
     * @param position position
     * @return real state view
     */
    public RealStateView addState(String name, double size, Color border, Color inside, Point2D position) {
        String name2 = uniqueStateNameVerification(name);
        StateGraphicModel sg = new StateGraphicModel(name2, size, border, inside, position);
        addState(sg);
        RealStateView sv = new RealStateView(sg);
        center.getChildren().add(sv);

        //add listeners for saving
        sg.nameProperty().addListener(changeListener);
        sg.isFinalProperty().addListener(changeListener);
        sg.isInitialProperty().addListener(changeListener);
        sg.insideColorProperty().addListener(changeListener);
        sg.sizeProperty().addListener(changeListener);
        sg.commentProperty().addListener(changeListener);
        //fire saving event
        center.fireSave();
        return sv;
    }

    private final ChangeListener changeListener = new ChangeListener() {
        @Override
        public void changed(ObservableValue observable, Object oldValue, Object newValue) {
            center.fireSave();
        }

    };

    /**
     * Add a State into the StateMachineGraphicModel
     *
     * @param position position
     * @return realsstateview
     */
    public RealStateView addState(Point2D position) {
        return addState(DEFAULT_STATE_NAME, DEFAULT_STATE_SIZE, DEFAULT_STATE_BORDER_COLOR, DEFAULT_STATE_INSIDE_COLOR, position);
    }

    /**
     * Add a PhantomState into the StateMachine
     *
     * @param s state model
     * @return the created PhantomState
     */
    public PhantomStateView createPhantomState(StateGraphicModel s) {
        StateGraphicModel s2 = new StateGraphicModel(uniqueStateNameVerification("phantomState"), 10, Color.BLACK, Color.BLACK, new Point2D(s.getPositionX(), s.getPositionY()));
        addState(s2);
        PhantomStateView psv = new PhantomStateView(s2);
        center.getChildren().add(psv);
        return psv;
    }

    /**
     * Remove a State from the StateMachine
     *
     * @param s statemodel
     */
    public void removeState(StateGraphicModel s) {
        center.getChildren().remove(s.getStateView());

        //if it has transitions create a phantom state for the endings of these transitions
        if (s.hasTransitions()) {
            s.getExitingList().forEach(t -> {
                t.setOrigin(createPhantomState(s).getState());
            });
            s.getEnteringList().forEach(t -> {
                t.setDestination(createPhantomState(s).getState());
            });
        }
        super.getStateList().remove(s);
        //fire saving event
        center.fireSave();
    }

    /**
     * Add a Transition to the StateMachine
     *
     * @param origin origin
     * @param destination destination
     * @param name name
     * @param color color
     * @param thickness thickness
     */
    public void addTransition(StateGraphicModel origin, StateGraphicModel destination, String name, Color color, double thickness) {
        TransitionGraphicModel tg = new TransitionGraphicModel(origin, destination, name, color, thickness);
        getTransitionList().add(tg);
        TransitionView tv = new TransitionView(tg);
        center.getChildren().add(tv);
        tv.toBack();

        //add listeners for saving
        tg.eventProperty().addListener(changeListener);
        tg.actionProperty().addListener(changeListener);
        tg.guardProperty().addListener(changeListener);
        tg.nameProperty().addListener(changeListener);
        tg.colorProperty().addListener(changeListener);
        tg.thicknessProperty().addListener(changeListener);

        //fire saving event
        center.fireSave();
    }

    /**
     * Add a Transition to the StateMachine
     *
     * @param center center
     * @param origin origin
     * @param destination destination
     */
    public void addTransition(CenterComponent center, StateGraphicModel origin, StateGraphicModel destination) {
        addTransition(origin, destination, DEFAULT_TRANSITION_NAME, DEFAULT_TRANSITION_COLOR, DEFAULT_TRANSITION_THICKNESS);
    }

    /**
     * Add a PhantomTransition (Transition with a PhantomState at the end) to
     * the StateMachine
     *
     * @param center center
     * @param origin origin
     * @param p point
     */
    public void addPhantomTransition(CenterComponent center, StateGraphicModel origin, Point2D p) {
        PhantomStateView psv = createPhantomState(new StateGraphicModel("", 10, Color.BLACK, Color.BLACK, p));
        addTransition(center, origin, psv.getState());
    }

    /**
     * Remove a Transition
     *
     * @param t transition
     */
    public void removeTransition(TransitionGraphicModel t) {
        center.getChildren().remove(t.getTransitionView());
        //phantom states have no purpose if they aren't connected to a transition, so they have to be terminated
        StateGraphicModel origin = t.getOrigin();
        StateGraphicModel destination = t.getDestination();
        if (origin.getStateView() instanceof PhantomStateView) {
            center.getChildren().remove(origin.getStateView());
            super.getStateList().remove(origin);
        }
        if (destination.getStateView() instanceof PhantomStateView) {
            center.getChildren().remove(destination.getStateView());
            super.getStateList().remove(destination);
        }
        super.removeTransition(t);
        //fire saving event
        center.fireSave();
    }

    /**
     * Remove all Exiting Transitions of a State
     *
     * @param s state
     */
    public void removeExitingTransitionsFromState(StateModel s) {
        s.getExitingList().forEach(t -> {
            removeTransition((TransitionGraphicModel) t);
        });
    }

    /**
     * Remove all Entering Transitions of a State
     *
     * @param s state
     */
    public void removeEnteringTransitionsFromState(StateModel s) {
        s.getEnteringList().forEach(t -> {
            this.removeTransition((TransitionGraphicModel) t);
        });
    }

    /**
     * Remove all Transitions of a State
     *
     * @param s state
     */
    public void removeAllTransitionsFromState(StateModel s) {
        removeExitingTransitionsFromState(s);
        removeEnteringTransitionsFromState(s);
    }

    /**
     * Load the graphical information of a StateMachine to display it
     */
    public void loadStateMachine() {
        //x and y used if there are no position information in the states (if the SM was imported from a format other than JSON)
        double x = 100;
        double y = 100;
        double incrementX = 100;
        double incrementY = 100;

        StateGraphicModel sgm;
        for (StateModel sm : getStateList()) {
            sgm = (StateGraphicModel) sm;
            //set default position if it wasn't saved
            if (sgm.positionXProperty().get() == 0) {
                sgm.setPositionX(x);
                sgm.setPositionY(y);
                incrementX += 100;
                x += incrementX;
                y += incrementY;
            }
            //set default size if it wasn't saved
            if (sgm.sizeProperty().get() == 0) {
                sgm.setSize(DEFAULT_STATE_SIZE);
            }

            //set default color if not saved
            if (sgm.borderColorProperty().get() == null) {
                sgm.setBorderColor(DEFAULT_STATE_BORDER_COLOR);
            }

            if (sgm.insideColorProperty().get() == null) {
                sgm.setInsideColor(DEFAULT_STATE_INSIDE_COLOR);
            }

            //difference in treatment for phantomStates and realStates
            if (sgm.getName().startsWith("phantomState")) {
                PhantomStateView psv = new PhantomStateView(sgm);
                center.getChildren().add(psv);
            } else {
                RealStateView rsv = new RealStateView(sgm);
                center.getChildren().add(rsv);
                sgm.nameProperty().addListener(changeListener);
                sgm.isFinalProperty().addListener(changeListener);
                sgm.isInitialProperty().addListener(changeListener);
                sgm.insideColorProperty().addListener(changeListener);
                sgm.sizeProperty().addListener(changeListener);
                sgm.commentProperty().addListener(changeListener);
            }
        }
        //Load Transitions
        TransitionGraphicModel tm;
        for (TransitionModel t : getTransitionList()) {
            tm = (TransitionGraphicModel) t;

            //set default thickness if it wasn't saved
            if (tm.getThickness() == 0) {
                tm.setThickness(DEFAULT_TRANSITION_THICKNESS);
            }

            //set default color
            if (tm.getColor() == null) {
                tm.setColor(DEFAULT_TRANSITION_COLOR);
            }

            TransitionView tv = new TransitionView(tm);
            center.getChildren().add(tv);
            tv.toBack();

            tm.eventProperty().addListener(changeListener);
            tm.actionProperty().addListener(changeListener);
            tm.guardProperty().addListener(changeListener);
            tm.nameProperty().addListener(changeListener);
            tm.colorProperty().addListener(changeListener);
            tm.thicknessProperty().addListener(changeListener);
        }
    }

    /**
     *
     * @param center center
     */
    public void setCenter(CenterComponent center) {
        this.center = center;
    }

    /**
     * Check if all Transitions have RealStates at their Start and End
     *
     * @return boolean
     */
    public boolean checkAllTransitions() {
        boolean allHaveOrigin = true;
        boolean allHaveDestination = true;

        TransitionGraphicModel tm;
        for (TransitionModel t : getTransitionList()) {
            tm = (TransitionGraphicModel) t;
            boolean originIsValid = tm.checkOrigin();
            boolean destinationIsValid = tm.checkDestination();
            if (originIsValid == false) {
                allHaveOrigin = false;
            }
            if (destinationIsValid == false) {
                allHaveDestination = false;
            }
        }
        return (allHaveOrigin && allHaveDestination);
    }

    /**
     * Check if all States have Transitions
     *
     * @return boolean
     */
    public boolean checkAllStates() {
        boolean allHaveTransitions = true;

        StateGraphicModel sm;
        for (StateModel s : getStateList()) {
            sm = (StateGraphicModel) s;
            if (sm.getStateView() instanceof RealStateView) {
                allHaveTransitions = (sm.getEnteringList().isEmpty()) && (sm.getExitingList().isEmpty());
            }
        }
        return allHaveTransitions;
    }

}
