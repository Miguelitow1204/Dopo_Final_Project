package Domain;

/**
 * Moneda especial que cambia temporalmente el skin del jugador.
 * El efecto dura hasta que el jugador recoge otra moneda o muere.
 */
public class MonedaSkin extends Moneda {
    private TipoPersonaje tipoAsociado;

    /**
     * Crea una MonedaSkin con su tipo y posicion.
     *
     * @param posicion     posicion en el tablero.
     * @param tipoAsociado tipo de personaje que otorga al recogerla.
     */
    public MonedaSkin(Posicion posicion, TipoPersonaje tipoAsociado) {
        super(posicion);
        this.tipoAsociado = tipoAsociado;
    }

    /**
     * Obtiene el tipo de personaje asociado a esta moneda.
     *
     * @return tipo de personaje.
     */

    public TipoPersonaje getTipoAsociado() {
        return tipoAsociado;
    }
}
