package statemachines;

import fr.liienac.statemachine.StateMachine;
import fr.liienac.statemachine.event.Event;

public class Test extends StateMachine {

    public State state = new State() {
        Transition transition = new Transition<Event>() {
            @Override
            public boolean guard() {
//x = 5
                return false;
            }

            @Override
            public void action() {
//afficher rectangle
            }

            @Override
            public State goTo() {
                return state2;
            }
        };
        Transition transition2 = new Transition<Event>() {
            @Override
            public State goTo() {
                return state3;
            }
        };
    };
    public State state2 = new State() {
        Transition transition = new Transition<Event>() {
            @Override
            public State goTo() {
                return state4;
            }
        };
    };
    public State state3 = new State() {
    };
    public State state4 = new State() {
    };
}
