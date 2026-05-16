package Domain;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Agregado de entidades de un nivel y su condicion de finalizacion.
 * 
 * @author MurilloRubiano
 * @version 2.2
 */
public class Nivel {

    // Limites del area jugable en pixeles.
    // Definen hasta donde puede moverse el jugador antes de chocar con las paredes
    // del mapa.
    // Centralizados aqui para que el controlador no dependa de constantes de la
    // vista.
    public static final int LIMITE_X_MIN = 0;
    public static final int LIMITE_X_MAX = 800;
    public static final int LIMITE_Y_MIN = 155;
    public static final int LIMITE_Y_MAX = 420;

    private int numero;
    private Jugador jugador;
    private List<Enemigo> enemigos;
    private List<Moneda> monedas;
    private ZonaSegura zonaInicio;
    private ZonaSegura zonaMeta;
    private int tiempoLimite;
    private List<Wall> paredes;

    public Nivel(int numero, int tiempoLimite) {
        this.numero = numero;
        this.tiempoLimite = tiempoLimite;
        this.enemigos = new ArrayList<>();
        this.monedas = new ArrayList<>();
        this.paredes = new ArrayList<>();
    }

    public void inicializar() {
        jugador.setMonedasRecogidas(0);
        jugador.reiniciarPosicion();
        for (Moneda m : monedas) {
            m.reiniciar();
        }
    }

    public void actualizar() {
        for (Enemigo e : enemigos) {
            e.actualizar();
        }
    }

    public boolean estaCompleto() {
        return jugador.haRecogidoTodas(monedas.size())
                && zonaMeta.contiene(jugador);
    }

    public void reiniciar() {
        inicializar();
    }

    public int getNumero() {
        return numero;
    }

    public Rectangle getLimitesJugables() {
        return new Rectangle(LIMITE_X_MIN, LIMITE_Y_MIN,
                LIMITE_X_MAX - LIMITE_X_MIN,
                LIMITE_Y_MAX - LIMITE_Y_MIN);
    }

    public List<Wall> getParedes() {
        return Collections.unmodifiableList(paredes);
    }

    public void agregarPared(Wall pared) {
        paredes.add(pared);
    }

    public Jugador getJugador() {
        return jugador;
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    public List<Enemigo> getEnemigos() {
        return Collections.unmodifiableList(enemigos);
    }

    public void agregarEnemigo(Enemigo enemigo) {
        enemigos.add(enemigo);
    }

    public void setEnemigos(List<Enemigo> enemigos) {
        this.enemigos = new ArrayList<>(enemigos);
    }

    public List<Moneda> getMonedas() {
        return Collections.unmodifiableList(monedas);
    }

    public void agregarMoneda(Moneda moneda) {
        monedas.add(moneda);
    }

    public void setMonedas(List<Moneda> monedas) {
        this.monedas = new ArrayList<>(monedas);
    }

    public ZonaSegura getZonaInicio() {
        return zonaInicio;
    }

    public void setZonaInicio(ZonaSegura zonaInicio) {
        this.zonaInicio = zonaInicio;
    }

    public ZonaSegura getZonaMeta() {
        return zonaMeta;
    }

    public void setZonaMeta(ZonaSegura zonaMeta) {
        this.zonaMeta = zonaMeta;
    }

    public int getTiempoLimite() {
        return tiempoLimite;
    }
}