package dragpanzoom.statemachines;

/**
 * Interface pour un widget (indépendamment de l'API graphique) que l'on peut
 * translater
 *
 * @author saporito
 */
public interface ITranslatable {

    /**
     * Translater
     *
     * @param dx composante de la translation selon l'axe des abscisses
     * @param dy composante de la translation selon l'axe des ordonnées
     */
    public void translate(double dx, double dy);

}
