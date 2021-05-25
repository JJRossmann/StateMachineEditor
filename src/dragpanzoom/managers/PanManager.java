package dragpanzoom.managers;

import dragpanzoom.statemachines.DragStateMachine;
import dragpanzoom.views.TranslatableHomotheticPane;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import fr.liienac.statemachine.event.Move;
import fr.liienac.statemachine.event.Press;
import fr.liienac.statemachine.event.Release;
import fr.liienac.statemachine.geometry.Point;

/**
 * Gestionnaire de Pan des TranslatableHomotheticPane. Responsable de l'écoute
 * des événements javafx sur le TranslatableHomotheticPane dont elle a la
 * charge, cette classe rend ces événements abstraits et les redirige vers une
 * machine à états indépendante de la techno utilisée.
 *
 * @author saporito
 */
public class PanManager {

    private final DragStateMachine sm = new DragStateMachine();

    /**
     * Constructeur
     *
     * @param paneToPan TranslatableHomotheticPane à gérer
     */
    public PanManager(TranslatableHomotheticPane paneToPan) {
        // Solution 1 :
        // - écoute du pane en bubbling phase
        // - écoute du node en capturing phase (dans NodeInteractionManager) et arrêt de la propagation
        // Les écouteurs du pane ne seront donc jamais appelés si la cible de l'événement est un node.
        paneToPan.addEventHandler(MouseEvent.MOUSE_PRESSED, onMousePressed);
        paneToPan.addEventHandler(MouseEvent.MOUSE_DRAGGED, onMouseDragged);
        paneToPan.addEventHandler(MouseEvent.MOUSE_RELEASED, onMouseReleased);

        // Solution 2 :
        // - écoutes sur n'importe quelle phase
        // - les écouteurs du pane testent la cible et ne réagissent pas si ce n'est pas le pane
        // Tous les écouteurs seront donc appelés mais certains "choisiront" de ne pas réagir.
    }

    private final EventHandler<MouseEvent> onMousePressed = (MouseEvent ev) -> {
        // Solution 2 :
        // Si la cible n'est pas la source (le widget abonné à ce handler, ici le panAndZoomPane), ne pas gérer le cas.
        // Il sera géré par un handler spécifique si il y a un autre abonnement (ici chaque Node sur lequel on met en place du d&d dans NodeGestures)
        if (ev.getTarget() != ev.getSource()) { // on aurait pu tester directement event.getTarget() != panAndZoomPane
            return;
        }

        // Solution 1 : rien à faire ici si ce n'est commenter la solution 2 (événement potentiellement déjà intercepté par le node en capturing phase)
        // Dans tous les cas :
        sm.handleEvent(new Press(new Point(ev.getX(), ev.getY()), ev.getSource()));
        //System.out.println("Mouse pressed on pane");
    };

    private final EventHandler<MouseEvent> onMouseDragged = (MouseEvent ev) -> {
        // Solution 2 :
        // Si la cible n'est pas la source (le widget abonné à ce handler, ici le panAndZoomPane), ne pas gérer le cas.
        // Il sera géré par un handler spécifique si il y a un autre abonnement (ici chaque Node sur lequel on met en place du d&d dans NodeGestures)
        if (ev.getTarget() != ev.getSource()) { // on aurait pu tester directement event.getTarget() != panAndZoomPane
            return;
        }

        // Solution 1 : rien à faire ici si ce n'est commenter la solution 2 (événement potentiellement déjà intercepté par le node en capturing phase)
        // Dans tous les cas :
        sm.handleEvent(new Move(new Point(ev.getX(), ev.getY()), ev.getSource()));
        //System.out.println("Mouse dragged on pane");
    };

    private final EventHandler<MouseEvent> onMouseReleased = (MouseEvent ev) -> {
        // Solution 2 :
        // Si la cible n'est pas la source (le widget abonné à ce handler, ici le panAndZoomPane), ne pas gérer le cas.
        // Il sera géré par un handler spécifique si il y a un autre abonnement (ici chaque Node sur lequel on met en place du d&d dans NodeGestures)
        if (ev.getTarget() != ev.getSource()) { // on aurait pu tester directement event.getTarget() != panAndZoomPane
            return;
        }

        // Solution 1 : rien à faire ici si ce n'est commenter la solution 2 (événement potentiellement déjà intercepté par le node en capturing phase)
        // Dans tous les cas :
        sm.handleEvent(new Release(new Point(ev.getX(), ev.getY()), ev.getSource()));
        //System.out.println("Mouse released on pane");
    };
}
