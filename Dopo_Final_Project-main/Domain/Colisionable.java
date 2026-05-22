package Domain;

import java.awt.Rectangle;

/**
 * Contrato para entidades que participan en deteccion de colisiones.
 * 
 * @author (MurilloRubiano)
 * @version (1.0)
 */
public interface Colisionable {
    /**
     * Obtiene el rectangulo de colision de la entidad.
     *
     * @return area rectangular usada para detectar intersecciones.
     */
    Rectangle getBounds();

    /**
     * Evalua si esta entidad colisiona con otra entidad colisionable.
     *
     * @param otro entidad contra la que se compara la colision.
     * @return true si los limites se intersectan.
     */
    boolean colisionaCon(Colisionable otro);
}
