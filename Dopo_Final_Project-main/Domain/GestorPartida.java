package Domain;

import java.io.*;

/**
 * Servicio para guardar y cargar el estado de una partida.
 * Usa serializacion Java para persistir el objeto Juego completo.
 *
 * @author MurilloRubiano
 * @version 1.0
 */
public class GestorPartida {

    //Constructor privado: solo metodos estaticos
    private GestorPartida() {}

    /**
     * Guarda el estado del juego en la ruta especificada.
     *
     * @param juego estado del juego a guardar.
     * @param ruta  ruta completa del archivo destino.
     * @throws IOException si no se puede escribir el archivo.
     */
    public static void guardar(Juego juego, String ruta) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(ruta))) {
            oos.writeObject(juego);
            LogErrores.registrar("Partida guardada en: " + ruta);
        } catch (IOException e) {
            LogErrores.registrar("Error al guardar la partida.", e);
            throw e;
        }
    }

    /**
     * Carga el estado de una partida desde la ruta especificada.
     *
     * @param ruta ruta completa del archivo a cargar.
     * @return juego restaurado desde el archivo.
     * @throws IOException            si no se puede leer el archivo.
     * @throws ClassNotFoundException si el archivo no es valido.
     */
    public static Juego cargar(String ruta) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(ruta))) {
            Juego juego = (Juego) ois.readObject();
            LogErrores.registrar("Partida cargada desde: " + ruta);
            return juego;
        } catch (IOException | ClassNotFoundException e) {
            LogErrores.registrar("Error al cargar la partida.", e);
            throw e;
        }
    }
}