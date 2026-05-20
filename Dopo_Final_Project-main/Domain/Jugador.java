package Domain;

import java.awt.Rectangle;
import java.io.Serializable;

/**
 * Representa al jugador con movimiento, vidas y recoleccion de monedas.
 * 
 * @author (MurilloRubiano)
 * @version (1.7)
 */
public class Jugador extends EntidadJuego implements Movible, Colisionable, Serializable {
    private static final long serialVersionUID = 1L;

    private int monedasRecogidas;
    private int velocidad;
    private Posicion posicionInicial;
    private String nombre;
    private TipoPersonaje tipoPersonaje;
    private int muertes;
    private boolean escudoActivo;
    private int velocidadOriginal;
    
    private int framesSinDaño = 0;
    private static final int FRAMES_INVULNERABLE = 90; //1.5 sec a 60 FPS

    /**
     * Crea un jugador con nombre, tipo y posicion inicial.
     *
     * @param posicion      posicion inicial del jugador.
     * @param nombre        nombre visible del jugador.
     * @param tipoPersonaje tipo seleccionado por el usuario.
     */
    public Jugador(Posicion posicion, String nombre, TipoPersonaje tipoPersonaje) {
        super(posicion, 20, 20);
        this.nombre = nombre;
        this.tipoPersonaje = tipoPersonaje;
        this.posicionInicial = posicion.clonar();
        this.monedasRecogidas = 0;
        this.muertes = 0;
        configurarSegunTipo();
        this.velocidadOriginal = this.velocidad;
    }

    /**
     * Ajusta atributos base (tamano y velocidad) segun el tipo elegido.
     */
    private void configurarSegunTipo() {
        switch (tipoPersonaje) {
            case ROJO:
                this.velocidad = 3;
                this.ancho = 20;
                this.alto = 20;
                break;
            case AZUL:
                this.velocidad = 5;
                this.ancho = 26;
                this.alto = 26;
                break;
            case VERDE:
                this.velocidad = 3;
                this.ancho = 20;
                this.alto = 20;
                this.escudoActivo = true;
                break;
        }
    }

    /**
     * Convierte el string de skin ("RED","BLUE","GREEN") a TipoPersonaje.
     */
    public static TipoPersonaje skinToTipo(String skin) {
        switch (skin.toUpperCase()) {
            case "BLUE":
                return TipoPersonaje.AZUL;
            case "GREEN":
                return TipoPersonaje.VERDE;
            default:
                return TipoPersonaje.ROJO;
        }
    }

    /**
     * Mueve al jugador aplicando la velocidad configurada.
     *
     * @param dx direccion horizontal.
     * @param dy direccion vertical.
     */
    @Override
    public void mover(int dx, int dy) {
        posicion.setX(posicion.getX() + dx * velocidad);
        posicion.setY(posicion.getY() + dy * velocidad);
    }

    /**
     * Obtiene la velocidad actual del jugador.
     *
     * @return velocidad en pixeles por paso.
     */
    @Override
    public int getVelocidad() {
        return velocidad;
    }

    /**
     * Obtiene el rectangulo de colision del jugador.
     *
     * @return limites de colision.
     */
    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) posicion.getX(), (int) posicion.getY(), ancho, alto);
    }

    /**
     * Comprueba colision con otra entidad colisionable.
     *
     * @param otro entidad a comparar.
     * @return true si hay interseccion.
     */
    @Override
    public boolean colisionaCon(Colisionable otro) {
        return this.getBounds().intersects(otro.getBounds());
    }

    /**
     * No procesa movimiento autonomo: lo controla el controlador de juego.
     */
    @Override
    public void actualizar() {
        // Movimiento procesado por el controlador
    }

    /**
     * Registra la recoleccion de una moneda.
     *
     * @param moneda moneda recolectada.
     */
    public void recogerMoneda(Moneda moneda) {
        moneda.recoger();
        monedasRecogidas++;
    }

    /**
     * Gestiona el impacto de un enemigo sobre el jugador.
     * Para el verde con escudo activo: absorbe el golpe, guarda la posicion
     * actual como checkpoint y reduce la velocidad.
     * Para el verde sin escudo: muere y reaparece en el checkpoint guardado.
     *
     * @return true si el jugador murio, false si el escudo absorb el golpe.
     */
    public boolean perderVida() {
        //Si esta en periodo de invulnerabilidad, ignorar el golpe
        if(framesSinDaño > 0){
            return false;
        }
        framesSinDaño = FRAMES_INVULNERABLE;
        
        if (tipoPersonaje == TipoPersonaje.VERDE && escudoActivo) {
            //Primer golpe: pierde escudo y velocidad, no muere, monedas intactas
            escudoActivo = false;
            velocidad = (int) (velocidadOriginal * 0.7);
            return false;
        }
        //Segundo hit (verde sin escudo) o cualquier hit(rojo/azul): muerte real
        muertes++;
        monedasRecogidas = 0;
        reiniciarPosicion();
        if (tipoPersonaje == TipoPersonaje.VERDE) {
            //Restaurar escudo y velocidad al morir
            escudoActivo = true;
            velocidad = velocidadOriginal;
        }   
        return true;
    }
    
    /**
     * Reduce el contador de invulnerabilidad cada frame
     * Debe llamarse en cada tick del game loop
     */
    public void actualizarInvulnerabilidad(){
        if(framesSinDaño > 0){
            framesSinDaño--;
        }
    }

    /**
     * Reubica al jugador en su posicion de inicio.
     */
    public void reiniciarPosicion() {
        posicion.setX(posicionInicial.getX());
        posicion.setY(posicionInicial.getY());
    }

    /**
     * Verifica si ya se reunio el total de monedas requerido.
     *
     * @param total total de monedas del nivel.
     * @return true si se alcanzaron o superaron.
     */
    public boolean haRecogidoTodas(int total) {
        return monedasRecogidas >= total;
    }

    // Getters y setters

    /**
     * Indica si el escudo del jugador verde esta activo.
     *
     * @return true si el proximo golpe sera absorbido.
     */
    public boolean isEscudoActivo() {
        return escudoActivo;
    }

    /**
     * Obtiene la cantidad de monedas recogidas en el intento actual.
     *
     * @return contador de monedas.
     */
    public int getMonedasRecogidas() {
        return monedasRecogidas;
    }

    /**
     * Define manualmente el contador de monedas.
     *
     * @param monedasRecogidas nuevo contador.
     */
    public void setMonedasRecogidas(int monedasRecogidas) {
        this.monedasRecogidas = monedasRecogidas;
    }

    /**
     * Define la velocidad del jugador.
     *
     * @param velocidad nueva velocidad.
     */
    public void setVelocidad(int velocidad) {
        this.velocidad = velocidad;
    }

    /**
     * Obtiene la posicion inicial del jugador.
     *
     * @return posicion de respawn.
     */
    public Posicion getPosicionInicial() {
        return posicionInicial;
    }

    /**
     * Define la posicion inicial del jugador.
     *
     * @param posicionInicial nueva posicion de respawn.
     */
    public void setPosicionInicial(Posicion posicionInicial) {
        this.posicionInicial = posicionInicial;
    }

    /**
     * Obtiene el nombre del jugador.
     *
     * @return nombre asignado.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene la cantidad acumulada de muertes.
     *
     * @return numero de muertes.
     */
    public int getMuertes() {
        return muertes;
    }

    /**
     * Obtiene el tipo de personaje seleccionado.
     *
     * @return tipo de personaje.
     */
    public TipoPersonaje getTipoPersonaje() {
        return tipoPersonaje;
    }
    
    /**
     * Otorga un punto de vida adicional al jugador.
     * Para el verde restaura el escudo si lo habia perdido.
     */
    public void ganarVida() {
        if (tipoPersonaje == TipoPersonaje.VERDE && !escudoActivo) {
            escudoActivo = true;
            velocidad = velocidadOriginal;
        }
        //Para rojo y azul simplemente registramos la vida extra
        //como invulnerabilidad temporal
        framesSinDaño = FRAMES_INVULNERABLE * 2;
    }
}