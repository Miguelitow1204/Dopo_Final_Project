package Domain;

import java.io.Serializable;

/**
 * Valor mutable de coordenadas 2D para entidades del juego.
 * @author (MurilloRubiano)
 * @version (1.0)
 */
public class Posicion implements Serializable {

    private double x;
    private double y;

    /**
     * Construye una posicion 2D.
     *
     * @param x coordenada horizontal.
     * @param y coordenada vertical.
     */
    public Posicion(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Obtiene la coordenada X.
     *
     * @return valor actual de X.
     */
    public double getX() {
        return x;
    }

    /**
     * Define la coordenada X.
     *
     * @param x nuevo valor de X.
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Obtiene la coordenada Y.
     *
     * @return valor actual de Y.
     */
    public double getY() {
        return y;
    }

    /**
     * Define la coordenada Y.
     *
     * @param y nuevo valor de Y.
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Calcula la distancia euclidiana a otra posicion.
     *
     * @param otra posicion objetivo.
     * @return distancia entre ambos puntos.
     */
    public double distanciaA(Posicion otra) {
        double dx = this.x - otra.x;
        double dy = this.y - otra.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Crea una copia de la posicion actual.
     *
     * @return nueva instancia con los mismos valores de X e Y.
     */
    public Posicion clonar() {
        return new Posicion(this.x, this.y);
    }
}