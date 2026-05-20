package Domain;

import java.awt.Rectangle;
import java.io.Serializable;

/**
 * Elemento especial que otorga un punto de vida adicional al jugador.
 * Desaparece una vez usada.
 *
 * @author MurilloRubiano
 * @version 1.0
 */
public class FuenteVida extends EntidadJuego implements Colisionable, Serializable {

    private static final long serialVersionUID = 1L;
    private boolean usada;

    /**
     * Crea una fuente de vida en la posicion indicada.
     *
     * @param posicion ubicacion de la fuente.
     */
    public FuenteVida(Posicion posicion) {
        super(posicion, 16, 16);
        this.usada = false;
        this.activa = true;
    }

    /**
     * Activa el efecto de la fuente sobre el jugador.
     * Le da un punto de vida adicional y desaparece.
     *
     * @param jugador jugador que la activa.
     */
    public void activar(Jugador jugador) {
        if (!usada) {
            jugador.ganarVida();
            usada = true;
            activa = false;
        }
    }

    /**
     * Indica si la fuente ya fue usada.
     *
     * @return true si ya fue consumida.
     */
    public boolean isUsada() {
        return usada;
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) posicion.getX(), (int) posicion.getY(), ancho, alto);
    }

    @Override
    public boolean colisionaCon(Colisionable otro) {
        return this.getBounds().intersects(otro.getBounds());
    }

    @Override
    public void actualizar() {
        // Estatica
    }
}