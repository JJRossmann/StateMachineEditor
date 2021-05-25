package dragpanzoom.statemachines;

import fr.liienac.statemachine.StateMachine;
import fr.liienac.statemachine.event.Move;
import fr.liienac.statemachine.event.Press;
import fr.liienac.statemachine.event.Release;
import fr.liienac.statemachine.geometry.Point;

/**
 * Machine à états de gestion du drag
 * @author saporito
 */
public class DragStateMachine extends StateMachine {

    private Point p0;

    /**
     *
     */
    public State idle = new State() {
        Transition press = new Transition<Press>() {
            @Override
            public void action() {
                p0 = evt.p;
            }

            @Override
            public State goTo() {
                return dragging;
            }
        };
    };

    /**
     *
     */
    public State dragging = new State() {
        Transition move = new Transition<Move>() {
            @Override
            public void action() {
                ((ITranslatable) evt.graphicItem).translate(evt.p.x - p0.x, evt.p.y - p0.y);
                // Il n'y a pas ici de stockage de evt.p dans p0
                // car on gère les coordonnées dans le repère du widget lui-même
                // (il le faudrait si on les gérait dans le repère de son parent)
            }
        };

        Transition release = new Transition<Release>() {
            @Override
            public State goTo() {
                return idle;
            }
        };
    };

}
