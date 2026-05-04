package Domain;

import java.awt.Rectangle;

/**
 * Entidad coleccionable que incrementa el progreso del nivel.
 * @author (MurilloRubiano)
 * @version (1.0)
 */
public class Moneda extends EntidadJuego implements Colisionable {

    private boolean recogida;

    /**
     * Crea una moneda en la posicion indicada.
     *
     * @param posicion ubicacion inicial de la moneda.
     */
    public Moneda(Posicion posicion) {
        super(posicion, 10, 10);
        this.recogida = false;
    }

    /**
     * Marca la moneda como recogida y la desactiva.
     */
    public void recoger() {
        this.recogida = true;
        this.activa = false;
    }

    /**
     * Indica si la moneda ya fue recogida.
     *
     * @return true cuando ya no esta disponible en el nivel.
     */
    public boolean isRecogida() {
        return recogida;
    }

    /**
     * Restablece la moneda a su estado inicial.
     */
    public void reiniciar() {
        this.recogida = false;
        this.activa = true;
    }

    /**
     * Obtiene el area de colision de la moneda.
     *
     * @return rectangulo de colision.
     */
    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) posicion.getX(), (int) posicion.getY(), ancho, alto);
    }

    /**
     * Evalua colision con otra entidad colisionable.
     *
     * @param otro entidad a comparar.
     * @return true si los limites se intersectan.
     */
    @Override
    public boolean colisionaCon(Colisionable otro) {
        return this.getBounds().intersects(otro.getBounds());
    }

    /**
     * No aplica logica por frame porque la moneda es estatica.
     */
    @Override
    public void actualizar() {
        // Las monedas son estaticas
    }
}
