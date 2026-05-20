package Domain;

import java.awt.Rectangle;
import java.io.Serializable;

/**
 * Base abstracta para enemigos con trayectorias y colisiones.
 * @author (MurilloRubiano)
 * @version (2.0)
 */
public abstract class Enemigo extends EntidadJuego implements Movible, Colisionable, Serializable {
    private static final long serialVersionUID = 1L;

    protected double velocidad;
    protected Posicion puntoA;
    protected Posicion puntoB;

    /**
     * Inicializa los datos base de un enemigo.
     *
     * @param posicion  posicion inicial.
     * @param ancho     ancho del enemigo.
     * @param alto      alto del enemigo.
     * @param velocidad velocidad de desplazamiento.
     * @param puntoA    limite inferior o izquierdo del recorrido.
     * @param puntoB    limite superior o derecho del recorrido.
     */
    public Enemigo(Posicion posicion, int ancho, int alto, double velocidad,
            Posicion puntoA, Posicion puntoB) {
        super(posicion, ancho, alto);
        this.velocidad = velocidad;
        this.puntoA = puntoA;
        this.puntoB = puntoB;
    }

    /**
     * Calcula y aplica el siguiente paso de la trayectoria.
     */
    public abstract void calcularTrayectoria();

    /**
     * Obtiene la velocidad del enemigo.
     *
     * @return velocidad truncada a entero.
     */
    @Override
    public int getVelocidad() {
        return (int) velocidad;
    }

    /**
     * Mueve el enemigo en los ejes solicitados.
     *
     * @param dx desplazamiento en X.
     * @param dy desplazamiento en Y.
     */
    @Override
    public void mover(int dx, int dy) {
        posicion.setX(posicion.getX() + dx);
        posicion.setY(posicion.getY() + dy);
    }

    /**
     * Obtiene el rectangulo de colision del enemigo.
     *
     * @return limites de colision.
     */
    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) posicion.getX(), (int) posicion.getY(), ancho, alto);
    }

    /**
     * Comprueba si el enemigo colisiona con otra entidad.
     *
     * @param otro entidad a comparar.
     * @return true si hay interseccion.
     */
    @Override
    public boolean colisionaCon(Colisionable otro) {
        return this.getBounds().intersects(otro.getBounds());
    }

    /**
     * Obtiene el primer punto de control de la trayectoria.
     *
     * @return punto A.
     */
    public Posicion getPuntoA() {
        return puntoA;
    }

    /**
     * Obtiene el segundo punto de control de la trayectoria.
     *
     * @return punto B.
     */
    public Posicion getPuntoB() {
        return puntoB;
    }
}
