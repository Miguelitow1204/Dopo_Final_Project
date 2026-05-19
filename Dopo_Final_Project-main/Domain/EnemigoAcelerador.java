package Domain;

/**
 * Enemigo Acelerador Tipo A que se desplaza al doble de velocidad
 * de los enemigos normales en linea recta. Rebota en paredes
 * 
 * @author MurilloRubiano
 * @versio 1.0
 */
public class EnemigoAcelerador extends EnemigoLineal {
    private static final double MULTIPLICADOR_VELOCIDAD = 2.0;
    
    public EnemigoAcelerador(Posicion posicion, int ancho, int alto, double velocidad, Posicion puntoA,
                             Posicion puntoB, boolean movimientoHorizontal){
        super(posicion, ancho, alto, velocidad * MULTIPLICADOR_VELOCIDAD, puntoA, puntoB, movimientoHorizontal);                             
    }
}