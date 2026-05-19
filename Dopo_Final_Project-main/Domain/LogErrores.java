package Domain;

import java.time.format.DateTimeFormatter;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Servicio de registro de errores para los programadores.
 * Escribe un archivo log.txt con fecha y hora de cada error
 * 
 * @author MurilloRubiano
 * @version 1.0
 */
public class LogErrores{
    private static final String RUTA_LOG = "log.txt";
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    //Constructor privado - solo metodos estaticos
    private LogErrores(){}
    
    /**
     * Registra un mensaje de error en el archivo log.txt
     * 
     * @param mensaje descripcion del error
     */
    public static void registrar(String mensaje){
        try(PrintWriter writer = new PrintWriter(new FileWriter(RUTA_LOG, true))){
            writer.println("[" + LocalDateTime.now().format(FORMATO) + "] ERROR: " + mensaje);
        } catch (IOException e){
            System.err.println("No se pudo escribir el log: " + e.getMessage());
        }
    }
    
    /**
     * Registra un error con su excepcion asociada
     * 
     * @param mensaje descripcion del error
     * @param excepcion excepcion original
     */
    public static void registrar(String mensaje, Exception excepcion){
        try(PrintWriter writer = new PrintWriter(new FileWriter(RUTA_LOG, true))){
            writer.println("[" + LocalDateTime.now().format(FORMATO) + "] ERROR: " + mensaje);
            writer.println("    Causa: " + excepcion.getMessage());
        } catch (IOException e){
            System.err.println("No se pudo escribir el log: " + e.getMessage());
        }
    }
}