package dragpanzoom.statemachines;

/**
 * Interface générique pour un widget dont la mise à l'échelle est toujours homothétique.
 * @author Nicolas Saporito - ENAC
 */
public interface IHomothetic {

    /**
     * Obtenir le facteur de zoom actuel.
     * @return facteur d'échelle
     */
    public abstract double getScale();

    /**
     * Remplacer le facteur de zoom actuel par une nouvelle valeur 
     * (centre de zoom à l'origine du repère local).
     * @param scale nouveau facteur de zoom
     */
    public abstract void setScale(double scale);

    /**
     * Remplacer le facteur de zoom actuel par une nouvelle valeur.
     * @param scale nouveau facteur de zoom
     * @param pivotX abscisse du centre de zoom
     * @param pivotY ordonnée du centre de zoom
     */
    public abstract void setScale(double scale, double pivotX, double pivotY);

    /**
     * Appliquer une mise à l'échelle supplémentaire (qui se multiplie au facteur de zoom actuel)
     * (centre de zoom à l'origine du repère local).
     * @param deltaScale facteur de zoom supplémentaire à appliquer
     */
    public void appendScale(double deltaScale);

    /**
     * Appliquer une mise à l'échelle supplémentaire (multiplier par le facteur de zoom actuel).
     * @param deltaScale facteur de zoom supplémentaire à appliquer
     * @param pivotX abscisse du centre de zoom
     * @param pivotY ordonnée du centre de zoom
     */
    public void appendScale(double deltaScale, double pivotX, double pivotY);
}
