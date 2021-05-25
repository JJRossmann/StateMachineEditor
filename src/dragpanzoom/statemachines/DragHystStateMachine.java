package dragpanzoom.statemachines;

import fr.liienac.statemachine.StateMachine;
import fr.liienac.statemachine.event.Move;
import fr.liienac.statemachine.event.Press;
import fr.liienac.statemachine.event.Release;
import fr.liienac.statemachine.geometry.Point;

/**
 * Machine à états de gestion du drag avec une hystérèse
 * @author saporito
 */
public class DragHystStateMachine extends StateMachine {

    private Point p0;
    private static final double THRESHOLD = 100; // seuil de l'hystérèse (module au carré)

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
                return hyst;
            }
        };
    };
    
    /**
     *
     */
    public State hyst = new State() {
        Transition move = new Transition<Move>() {
            @Override
            public boolean guard() {
                return overshootThreshold(evt.p);
            }
            
            @Override
            public void action() {
                // optionnel :
                // pour démarrer le drag sans faire un saut égal au
                // seuil, il faut réinitialiser la position de départ
                p0 = evt.p;
            }
            
            @Override
            public State goTo() {
                return dragging;
            }
        };

        Transition release = new Transition<Release>() {
            @Override
            public State goTo() {
                return idle;
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
    
    /**
     * Vérifier si le déplacement de la souris depuis le press a déplacé le seuil. 
     * Défaut : 
     * Dans le système de coordonnées de l'objet que l'on déplace cette interaction est dépendente 
     * du niveau de zoom. Ça ne gène donc pas le drag du rectangle (qui est visuellement invariant 
     * au zoom) mais modifie considérablement le seuil pour la grille si elle est beaucoup zoomée.
     * Solution :
     * Pour avoir un seuil constant en toutes circonstances il faudrait mesurer la distance
     * à l'écran et donc faire appel à une méthode dédiée dans le Pan/DragManager 
     * sinon on perd le bénéfice de l'abstraction dans cette machine à états).
     * @param p
     * @return 
     */
    private boolean overshootThreshold(Point p) {
        double sqd = Math.pow((p.x - p0.x), 2) + Math.pow((p.y - p0.y), 2);
        return sqd >= THRESHOLD;
    }

}
