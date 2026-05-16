package Domain;

import java.io.Serializable;

/**
 * Entidad base serializable para todos los objetos del juego.
 * @author (MurilloRubiano)
 * @version (2.1)
 */
public abstract class EntidadJuego implements Serializable {

    protected Posicion posicion;
    protected int ancho;
    protected int alto;
    protected boolean activa;

    /**
     * Crea una entidad base con posicion y dimensiones.
     *
     * @param posicion posicion inicial.
     * @param ancho    ancho de la entidad.
     * @param alto     alto de la entidad.
     */
    public EntidadJuego(Posicion posicion, int ancho, int alto) {
        this.posicion = posicion;
        this.ancho = ancho;
        this.alto = alto;
        this.activa = true;
    }

    /**
     * Obtiene la posicion actual.
     *
     * @return posicion de la entidad.
     */
    public Posicion getPosicion() {
        return posicion;
    }

    /**
     * Actualiza la posicion de la entidad.
     *
     * @param posicion nueva posicion.
     */
    public void setPosicion(Posicion posicion) {
        this.posicion = posicion;
    }

    /**
     * Obtiene el ancho de la entidad.
     *
     * @return ancho en pixeles.
     */
    public int getAncho() {
        return ancho;
    }

    /**
     * Obtiene el alto de la entidad.
     *
     * @return alto en pixeles.
     */
    public int getAlto() {
        return alto;
    }

    /**
     * Indica si la entidad esta activa en juego.
     *
     * @return true cuando participa en la logica actual.
     */
    public boolean isActiva() {
        return activa;
    }

    /**
     * Cambia el estado de actividad de la entidad.
     *
     * @param activa nuevo estado activo/inactivo.
     */
    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    /**
     * Ejecuta la logica de actualizacion por frame.
     */
    public abstract void actualizar();
}