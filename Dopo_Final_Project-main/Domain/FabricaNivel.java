package Domain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Fabrica centralizada para la carga de niveles desde archivos de
 * configuracion.
 * Encapsula el detalle de donde y como se leen los niveles, de modo que
 * el resto del sistema solo necesita pedir "dame el nivel N" sin saber nada
 * sobre archivos, rutas ni excepciones de I/O.
 *
 * @author MurilloRubiano
 * @version 1.0
 */
public class FabricaNivel {

    private static final String RUTA_BASE = "configuraciones/";

    // Constructor privado: esta clase solo ofrece metodos estaticos
    private FabricaNivel() {
    }

    /**
     * Carga un nivel especifico desde su archivo de configuracion.
     * El archivo esperado es: configuraciones/nivelN.txt
     *
     * @param numero numero del nivel a cargar (1, 2, 3...).
     * @param tipo   tipo de personaje seleccionado por el jugador.
     * @return nivel configurado y listo para agregarse al juego.
     * @throws NivelNoEncontradoException si el archivo no existe o tiene errores de
     *                                    formato.
     */
    public static Nivel cargar(int numero, TipoPersonaje tipo) {
        String ruta = RUTA_BASE + "nivel" + numero + ".txt";
        try {
            return LectorNivel.cargar(ruta, tipo);
        } catch (IOException e) {
            throw new NivelNoEncontradoException(
                    "No se pudo cargar el nivel " + numero + " desde: " + ruta, e);
        }
    }

    /**
     * Carga todos los niveles disponibles de forma secuencial.
     * Si alguno falla, lanza NivelNoEncontradoException y detiene la carga.
     *
     * @param tipo     tipo de personaje seleccionado por el jugador.
     * @param cantidad numero total de niveles a cargar.
     * @return lista ordenada de niveles listos para jugar.
     * @throws NivelNoEncontradoException si cualquier nivel no puede cargarse.
     */
    public static List<Nivel> cargarTodos(TipoPersonaje tipo, int cantidad) {
        List<Nivel> niveles = new ArrayList<>();
        for (int i = 1; i <= cantidad; i++) {
            niveles.add(cargar(i, tipo));
        }
        return niveles;
    }
}