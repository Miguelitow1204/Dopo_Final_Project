
package Domain;

/**
 * Excepcion lanzada cuando un nivel no puede ser cargado
 * desde su archivo de configuracion.
 * 
 * @author MurilloRubiano
 * @version 1.0
 */
public class NivelNoEncontradoException extends RuntimeException {

    /**
     * Crea la excepcion con un mensaje descriptivo.
     *
     * @param mensaje descripcion del error.
     */
    public NivelNoEncontradoException(String mensaje) {
        super(mensaje);
    }

    /**
     * Crea la excepcion con mensaje y causa original.
     *
     * @param mensaje descripcion del error.
     * @param causa   excepcion original que genero el problema.
     */
    public NivelNoEncontradoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}