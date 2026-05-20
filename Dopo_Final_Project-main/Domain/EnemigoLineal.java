package Domain;
import java.io.Serializable;

/**
 * Enemigo que recorre una ruta lineal horizontal o vertical.
 * @author (MurilloRubiano)
 * @version (1.5)
 */
public class EnemigoLineal extends Enemigo implements Serializable {
    private static final long serialVersionUID = 1L;

    private int direccionActual;
    private boolean movimientoHorizontal;

    /**
     * Crea un enemigo con recorrido lineal entre dos limites.
     *
     * @param posicion             posicion inicial.
     * @param ancho                ancho del enemigo.
     * @param alto                 alto del enemigo.
     * @param velocidad            velocidad de movimiento.
     * @param puntoA               limite inicial del recorrido.
     * @param puntoB               limite final del recorrido.
     * @param movimientoHorizontal true para mover en X, false para mover en Y.
     */
    public EnemigoLineal(Posicion posicion, int ancho, int alto, double velocidad,
            Posicion puntoA, Posicion puntoB, boolean movimientoHorizontal) {
        super(posicion, ancho, alto, velocidad, puntoA, puntoB);
        this.direccionActual = 1;
        this.movimientoHorizontal = movimientoHorizontal;
    }

    /**
     * Avanza al enemigo y cambia direccion al alcanzar los extremos.
     */
    @Override
    public void calcularTrayectoria() {
        if (movimientoHorizontal) {
            double nuevaX = posicion.getX() + velocidad * direccionActual;
            if (nuevaX >= puntoB.getX() || nuevaX <= puntoA.getX()) {
                direccionActual *= -1;
            }
            posicion.setX(posicion.getX() + velocidad * direccionActual);
        } else {
            double nuevaY = posicion.getY() + velocidad * direccionActual;
            if (nuevaY >= puntoB.getY() || nuevaY <= puntoA.getY()) {
                direccionActual *= -1;
            }
            posicion.setY(posicion.getY() + velocidad * direccionActual);
        }
    }

    /**
     * Actualiza el enemigo aplicando su trayectoria lineal.
     */
    @Override
    public void actualizar() {
        calcularTrayectoria();
    }

    /**
     * Obtiene la direccion actual de avance.
     *
     * @return 1 para avance y -1 para retroceso.
     */
    public int getDireccionActual() {
        return direccionActual;
    }

    /**
     * Define la direccion de avance del enemigo.
     *
     * @param direccionActual valor de direccion (1 o -1).
     */
    public void setDireccionActual(int direccionActual) {
        this.direccionActual = direccionActual;
    }
}