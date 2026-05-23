package Domain;


/**
 * Estrategia de maquina que se mueve hacia las monedas mas cercanas
 * y luego hacia la zona meta para ganar
 * 
 * @author MurilloRubiano
 * @version 1.0
 */
public class MaquinaExperta implements EstrategiaMaquina{
    
    /**
     * Calcula el movimiento optimo hacia la moneda mas cercana
     * o hacia la zona meta si ya recogio todas las monedas
     * 
     * @param maquina   jugador controlado por la mquina
     * @param nivel   nivel actual del juego
     * @return array con dx y dy del movimiento
     */
    @Override
    public int[] calcularMovimiento(Jugador maquina, Nivel nivel){
        //Si ya recogio todas las monedas, ir a la zona de meta
        if(maquina.haRecogidoTodas(nivel.getMonedas().size())){
            return moverHacia(maquina, nivel.getZonaInicio().getPosicion());
        }
        
        //buscar la moneda mas cercana
        Moneda monedaCercana = null;
        double distanciaMin = Double.MAX_VALUE;
        
        for(Moneda moneda : nivel.getMonedas()){
            if(moneda.isActiva()){
                double distancia = maquina.getPosicion().distanciaA(moneda.getPosicion());
                if(distancia < distanciaMin){
                    distanciaMin = distancia;
                    monedaCercana = moneda;
                }
            }
        }
        
        if(monedaCercana != null){
            return moverHacia(maquina, monedaCercana.getPosicion());
        }
        
        return new int[]{0, 0};
    }
    
    /**
     * Calcula el dx y dy para moverse hacia una posicion objetivo
     * 
     * @param Jugador 
     * @param objetivo  posicion destino
     * @return array con dx y dy
     */
    private int[] moverHacia(Jugador maquina, Posicion objetivo){
        int dx = 0;
        int dy = 0;
        
        double diffX = objetivo.getX() - maquina.getPosicion().getX();
        double diffY = objetivo.getY() - maquina.getPosicion().getY();
        
        if(Math.abs(diffX) > Math.abs(diffY)){
            dx = diffX > 0 ? 1 : -1;
        } else {
            dy = diffY > 0 ? 1 : -1; 
        }
        
        return new int[]{dx, dy};
    }
}