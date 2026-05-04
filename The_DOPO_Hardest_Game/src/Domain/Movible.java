package Domain;

/**
 * Contrato para entidades con desplazamiento y velocidad.
 * @author (MurilloRubiano)
 * @version (1.0)
 */
public interface Movible {
    /**
     * Desplaza la entidad en los ejes X y Y.
     *
     * @param dx desplazamiento en X.
     * @param dy desplazamiento en Y.
     */
    void mover(int dx, int dy);

    /**
     * Obtiene la velocidad base de la entidad.
     *
     * @return velocidad configurada.
     */
    int getVelocidad();
}
