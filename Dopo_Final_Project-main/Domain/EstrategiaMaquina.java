package Domain;


/**
 * Contrato para las estrategias de movimiento de la máquina para el modo machine
 * 
 * @author MurilloRubiano
 * @version 1.0
 */
public interface EstrategiaMaquina{
    
    /**
     * Calcula el siguiente movimiento de la máquina
     * 
     * @param maquina  jugador controlado por la maquina
     * @param nivel  nivel actual del juego
     * @return array con dx y dy del movimiento
     */
    int[] calcularMovimiento(Jugador maquina, Nivel nivel);
}