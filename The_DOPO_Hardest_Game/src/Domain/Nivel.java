package Domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Agregado de entidades de un nivel y su condicion de finalizacion.
 * @author (MurilloRubiano)
 * @version (2.1)
 */
public class Nivel {

    private int numero;
    private Jugador jugador;
    private List<Enemigo> enemigos;
    private List<Moneda> monedas;
    private ZonaSegura zonaInicio;
    private ZonaSegura zonaMeta;
    private int tiempoLimite;
    private List<Wall> paredes;

    /**
     * Construye un nivel con numero identificador y tiempo limite.
     *
     * @param numero       numero del nivel.
     * @param tiempoLimite tiempo maximo en segundos.
     */
    public Nivel(int numero, int tiempoLimite) {
        this.numero = numero;
        this.tiempoLimite = tiempoLimite;
        this.enemigos = new ArrayList<>();
        this.monedas = new ArrayList<>();
        this.paredes = new ArrayList<>();
    }

    /**
     * Inicializa estado del jugador y monedas al comenzar el nivel.
     */
    public void inicializar() {
        jugador.setMonedasRecogidas(0);
        jugador.reiniciarPosicion();
        for (Moneda m : monedas) {
            m.reiniciar();
        }
    }

    /**
     * Actualiza los enemigos del nivel para el frame actual.
     */
    public void actualizar() {
        for (Enemigo e : enemigos) {
            e.actualizar();
        }
    }

    /**
     * Evalua si el nivel se considera completado.
     *
     * @return true si se recogieron todas las monedas y el jugador llego a meta.
     */
    public boolean estaCompleto() {
        return jugador.haRecogidoTodas(monedas.size())
                && zonaMeta.contiene(jugador);
    }

    /**
     * Reinicia el nivel reutilizando su inicializacion.
     */
    public void reiniciar() {
        inicializar();
    }

    // Getters y setters

    /**
     * Obtiene el numero del nivel.
     *
     * @return numero identificador.
     */
    public int getNumero() {
        return numero;
    }
    
    /**
     * Obtiene las paredes
     * @return paredes la lista de las paredes
     */
    public List<Wall> getParedes(){
    	return paredes;
    }

    /**
     * Obtiene el jugador asociado al nivel.
     *
     * @return jugador del nivel.
     */
    public Jugador getJugador() {
        return jugador;
    }

    /**
     * Define el jugador asociado al nivel.
     *
     * @param jugador jugador del nivel.
     */
    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    /**
     * Obtiene la lista de enemigos del nivel.
     *
     * @return enemigos configurados.
     */
    public List<Enemigo> getEnemigos() {
        return enemigos;
    }

    /**
     * Reemplaza la lista de enemigos del nivel.
     *
     * @param enemigos nueva lista de enemigos.
     */
    public void setEnemigos(List<Enemigo> enemigos) {
        this.enemigos = enemigos;
    }

    /**
     * Obtiene la lista de monedas del nivel.
     *
     * @return monedas configuradas.
     */
    public List<Moneda> getMonedas() {
        return monedas;
    }

    /**
     * Reemplaza la lista de monedas del nivel.
     *
     * @param monedas nueva lista de monedas.
     */
    public void setMonedas(List<Moneda> monedas) {
        this.monedas = monedas;
    }

    /**
     * Obtiene la zona de inicio del nivel.
     *
     * @return zona de inicio.
     */
    public ZonaSegura getZonaInicio() {
        return zonaInicio;
    }

    /**
     * Define la zona de inicio del nivel.
     *
     * @param zonaInicio zona de inicio.
     */
    public void setZonaInicio(ZonaSegura zonaInicio) {
        this.zonaInicio = zonaInicio;
    }

    /**
     * Obtiene la zona meta del nivel.
     *
     * @return zona de llegada.
     */
    public ZonaSegura getZonaMeta() {
        return zonaMeta;
    }

    /**
     * Define la zona meta del nivel.
     *
     * @param zonaMeta zona objetivo.
     */
    public void setZonaMeta(ZonaSegura zonaMeta) {
        this.zonaMeta = zonaMeta;
    }

    /**
     * Obtiene el tiempo limite de este nivel.
     *
     * @return tiempo maximo en segundos.
     */
    public int getTiempoLimite() {
        return tiempoLimite;
    }
}
