package Domain;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

/**
 * Modelo central del juego: niveles, tiempo, estado y observadores.
 * 
 * @author (MurilloRubiano)
 * @version (3.0)
 */
public class Juego implements Serializable{
    private static final long serialVersionUID = 1L;

    private Nivel nivelActual;
    private List<Nivel> niveles;
    private int indiceNivel;
    private EstadoJuego estado;
    private int tiempoRestante;

    /**
     * Inicializa estructuras y estado base del juego.
     */
    public Juego() {
        this.niveles = new ArrayList<>();
        this.estado = EstadoJuego.MENU;
        this.indiceNivel = 0;
    }

    /**
     * Inicia la partida desde el nivel indicado por indiceNivel.
     * Si no se llama a irALNivel() previamente, inicia desde el nivel 1.
     */
    public void iniciar() {
        if (!niveles.isEmpty()) {
            nivelActual = niveles.get(indiceNivel);
            tiempoRestante = nivelActual.getTiempoLimite();
            nivelActual.inicializar();
            estado = EstadoJuego.JUGANDO;
        }
    }

    /**
     * Alterna el estado entre jugando y pausa.
     */
    public void pausar() {
        if (estado == EstadoJuego.JUGANDO) {
            estado = EstadoJuego.PAUSA;
        } else if (estado == EstadoJuego.PAUSA) {
            estado = EstadoJuego.JUGANDO;
        }
    }

    /**
     * Avanza al siguiente nivel si existe.
     *
     * @return true si se cargo otro nivel, false si se terminaron todos.
     */
    public boolean siguienteNivel() {
        indiceNivel++;
        if (indiceNivel < niveles.size()) {
            nivelActual = niveles.get(indiceNivel);
            tiempoRestante = nivelActual.getTiempoLimite();
            nivelActual.inicializar();
            return true;
        } else {
            estado = EstadoJuego.GAME_OVER;
            return false;
        }
    }

    /**
     * Reinicia el nivel actual a su estado inicial.
     */
    public void reiniciarNivel() {
        if (nivelActual != null) {
            tiempoRestante = nivelActual.getTiempoLimite();
            nivelActual.reiniciar();
        }
    }

    /**
     * Reduce el tiempo restante en un segundo.
     * El estado GAME_OVER por tiempo lo gestiona el controlador.
     */
    public void decrementarTiempo() {
        if (tiempoRestante > 0) {
            tiempoRestante--;
        }
    }

    /**
     * Agrega un nivel al juego.
     *
     * @param nivel nivel a incorporar.
     */
    public void agregarNivel(Nivel nivel) {
        niveles.add(nivel);
    }

    // Getters y setters

    /**
     * Obtiene el nivel actualmente cargado.
     *
     * @return nivel actual.
     */
    public Nivel getNivelActual() {
        return nivelActual;
    }

    /**
     * Obtiene el estado global del juego.
     *
     * @return estado vigente.
     */
    public EstadoJuego getEstado() {
        return estado;
    }

    /**
     * Define el estado global del juego.
     *
     * @param estado nuevo estado.
     */
    public void setEstado(EstadoJuego estado) {
        this.estado = estado;
    }

    /**
     * Obtiene el tiempo restante del nivel.
     *
     * @return tiempo en segundos.
     */
    public int getTiempoRestante() {
        return tiempoRestante;
    }

    /**
     * Obtiene la lista de niveles cargados.
     *
     * @return coleccion de niveles.
     */
    public List<Nivel> getNiveles() {
        return niveles;
    }

    /**
     * Obtiene el indice del nivel actual.
     *
     * @return indice basado en cero.
     */
    public int getIndiceNivel() {
        return indiceNivel;
    }
    
    /**
     * Salta directamente a un nivel especifico dado su indice
     * Se usa cuando el jugador selecciona un nivel desbloqueado desde Level Screen
     * 
     * @param indice indice basado en cero del nivel a cargar
     */
    public void irAlNivel(int indice){
        if(indice >= 0 && indice < niveles.size()){
            indiceNivel = indice;
        }
    }
}
