package dragpanzoom.managers;

import dragpanzoom.statemachines.DragHystStateMachine;
import dragpanzoom.views.TranslatableHomotheticPane;
import fr.liienac.statemachine.event.Move;
import fr.liienac.statemachine.event.Press;
import fr.liienac.statemachine.event.Release;
import fr.liienac.statemachine.geometry.Point;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

/**
 * Gestionnaire de Drag des TranslatableHomotheticPane. Responsable de l'écoute
 * des événements javafx sur le TranslatableHomotheticPane dont elle a la
 * charge, cette classe rend ces événements abstraits et les redirige vers une
 * machine à états indépendante de la techno utilisée.
 *
 * @author saporito
 */
public class DragManager {

    private final DragHystStateMachine sm = new DragHystStateMachine();

    /**
     * Constructeur
     *
     * @param paneToDrag TranslatableHomotheticPane à gérer
     */
    public DragManager(TranslatableHomotheticPane paneToDrag) {
        paneToDrag.addEventFilter(MouseEvent.MOUSE_PRESSED, onMousePressed);
        paneToDrag.addEventFilter(MouseEvent.MOUSE_DRAGGED, onMouseDragged);
        paneToDrag.addEventFilter(MouseEvent.MOUSE_RELEASED, onMouseReleased);
    }

    private final EventHandler<MouseEvent> onMousePressed = (MouseEvent ev) -> {
        if (ev.isShiftDown()) {
            //System.out.println("Mouse pressed on rectangle");
            sm.handleEvent(new Press(new Point(ev.getX(), ev.getY()), ev.getSource()));
            // Solution 1 :
            // - écoute du pane en bubbling phase (dans HomotheticPaneInteractionManager)
            // - écoute du node en capturing phase et arrêt de la propagation
            // Les écouteurs du pane ne seront donc jamais appelés si la cible de l'événement est un node.
            ev.consume();

            // Solution 2 :
            // - écoutes sur n'importe quelle phase
            // - les écouteurs du pane (dans HomotheticPaneInteractionManager) testent la cible et ne réagissent pas si ce n'est pas le pane
            // Donc rien à faire ici si ce n'est commenter la solution 1
        }
    };

    private final EventHandler<MouseEvent> onMouseDragged = (MouseEvent ev) -> {
        if (ev.isShiftDown()) {
            //System.out.println("Mouse dragged on rectangle");
            sm.handleEvent(new Move(new Point(ev.getX(), ev.getY()), ev.getSource()));

            // Solution 1 :
            // - écoute du pane en bubbling phase (dans HomotheticPaneInteractionManager)
            // - écoute du node en capturing phase et arrêt de la propagation
            // Les écouteurs du pane ne seront donc jamais appelés si la cible de l'événement est un node.
            ev.consume();

            // Solution 2 :
            // - écoutes sur n'importe quelle phase
            // - les écouteurs du pane (dans HomotheticPaneInteractionManager) testent la cible et ne réagissent pas si ce n'est pas le pane
            // Donc rien à faire ici si ce n'est commenter la solution 1
        }
    };

    private final EventHandler<MouseEvent> onMouseReleased = (MouseEvent ev) -> {
        if (ev.isShiftDown()) {
            //System.out.println("Mouse released on rectangle");
            sm.handleEvent(new Release(new Point(ev.getX(), ev.getY()), ev.getSource()));

            // Solution 1 :
            // - écoute du pane en bubbling phase (dans HomotheticPaneInteractionManager)
            // - écoute du node en capturing phase et arrêt de la propagation
            // Les écouteurs du pane ne seront donc jamais appelés si la cible de l'événement est un node.
            ev.consume();

            // Solution 2 :
            // - écoutes sur n'importe quelle phase
            // - les écouteurs du pane (dans HomotheticPaneInteractionManager) testent la cible et ne réagissent pas si ce n'est pas le pane
            // Donc rien à faire ici si ce n'est commenter la solution 1
        }
    };
}
