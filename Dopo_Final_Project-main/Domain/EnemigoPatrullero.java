package Domain;

/**
 * Enemigo que patrulla una zona circular con radio y velocidad angular
 * configurables.
 * La posicion se calcula trigonometricamente en cada frame a partir del centro,
 * el radio y el angulo acumulado.
 * 
 * @author MurilloRubiano
 * @version 1.0
 */
public class EnemigoPatrullero extends Enemigo {

    private Posicion centro;
    private double radio;
    private double anguloActual;
    private double velocidadAngular;

    /**
     * Crea un enemigo patrullero circular.
     *
     * @param centro           centro de la trayectoria circular.
     * @param radio            radio del circulo en pixeles.
     * @param velocidadAngular radianes por frame; negativo invierte el sentido.
     */
    public EnemigoPatrullero(Posicion centro, double radio, double velocidadAngular) {
        super(
                new Posicion(centro.getX() + radio, centro.getY()),
                14, 14,
                Math.abs(velocidadAngular),
                centro.clonar(),
                centro.clonar());
        this.centro = centro;
        this.radio = radio;
        this.anguloActual = 0;
        this.velocidadAngular = velocidadAngular;
    }

    /**
     * Calcula la siguiente posicion en la trayectoria circular.
     * Resta la mitad del ancho y alto para que el centro del enemigo
     * siga exactamente el circulo definido.
     */
    @Override
    public void calcularTrayectoria() {
        anguloActual += velocidadAngular;
        posicion.setX(centro.getX() + radio * Math.cos(anguloActual) - ancho / 2.0);
        posicion.setY(centro.getY() + radio * Math.sin(anguloActual) - alto / 2.0);
    }

    /**
     * Actualiza el enemigo aplicando su trayectoria circular.
     */
    @Override
    public void actualizar() {
        calcularTrayectoria();
    }

    /**
     * Obtiene el centro de la trayectoria circular.
     *
     * @return posicion central del circulo.
     */
    public Posicion getCentro() {
        return centro;
    }

    /**
     * Obtiene el radio de la trayectoria circular.
     *
     * @return radio en pixeles.
     */
    public double getRadio() {
        return radio;
    }

    /**
     * Obtiene la velocidad angular del enemigo.
     *
     * @return radianes por frame.
     */
    public double getVelocidadAngular() {
        return velocidadAngular;
    }
}