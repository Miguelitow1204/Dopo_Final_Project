package Domain;
import java.io.Serializable;

/**
 * Area rectangular segura usada como inicio o meta del nivel.
 * @author (MurilloRubiano)
 * @version (1.5)
 */
public class ZonaSegura extends EntidadJuego implements Serializable {
    private static final long serialVersionUID = 1L;

    private TipoZona tipo;

    /**
     * Crea una zona segura con tipo y dimensiones.
     *
     * @param posicion esquina superior izquierda.
     * @param ancho    ancho de la zona.
     * @param alto     alto de la zona.
     * @param tipo     tipo de zona (inicio o meta).
     */
    public ZonaSegura(Posicion posicion, int ancho, int alto, TipoZona tipo) {
        super(posicion, ancho, alto);
        this.tipo = tipo;
    }

    /**
     * Verifica si una entidad queda completamente dentro de la zona.
     *
     * @param entidad entidad a evaluar.
     * @return true si la entidad esta contenida totalmente.
     */
    public boolean contiene(EntidadJuego entidad) {
        double ex = entidad.getPosicion().getX();
        double ey = entidad.getPosicion().getY();
        double zx = posicion.getX();
        double zy = posicion.getY();

        return ex >= zx && ex + entidad.getAncho() <= zx + ancho
                && ey >= zy && ey + entidad.getAlto() <= zy + alto;
    }

    /**
     * No aplica logica por frame porque la zona es estatica.
     */
    @Override
    public void actualizar() {
        // Las zonas son estaticas
    }

    /**
     * Obtiene el tipo de zona segura.
     *
     * @return tipo de zona.
     */
    public TipoZona getTipo() {
        return tipo;
    }
}

