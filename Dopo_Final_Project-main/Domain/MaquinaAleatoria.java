package Domain;

import java.util.Random;

/**
 * Estrategia de maquina que elige direcciones aleatorias de movimeinto.
 * 
 * @author MurilloRubiano
 * @version 1.0
 */
public class MaquinaAleatoria implements EstrategiaMaquina{
    private Random random;
    private int framesCambio;
    private int[] movimientoActual;
    private static final int FRAMES_POR_CAMBIO = 30; //Cambia de direccion cada 30 frame
    
    /**
     * Crea una maquina aleatoria con movimiento inicial nulo
     */
    public MaquinaAleatoria(){
        this.random = new Random();
        this.framesCambio = 0;
        this.movimientoActual = new int[]{0, 0};
    }
    
    @Override
    public int[] calcularMovimiento(Jugador Maquina, Nivel nivel){
        framesCambio ++;
        if(framesCambio >= FRAMES_POR_CAMBIO){
            framesCambio = 0;
            int direccion = random.nextInt(5); //0-3 direcciones + 4 quieto
            switch(direccion){
                case 0: movimientoActual = new int[]{0, -1}; break;
                case 1: movimientoActual = new int[]{0, 1}; break;
                case 2: movimientoActual = new int[]{-1, 0}; break;
                case 3: movimientoActual = new int[]{1, 0}; break;
                default: movimientoActual = new int[]{0, 0}; break; //quieto
            }
        }
        return movimientoActual;
    }
}
