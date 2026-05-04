package Domain;

/**
 * Contrato Observer para recibir cambios de estado y nivel.
 * @author (MurilloRubiano)
 * @version (1.1)
 */
public interface ObservadorJuego {
    /**
     * Recibe la notificacion de un cambio de estado del juego.
     *
     * @param evento evento ocurrido en el juego.
     */
    void alCambiarEstado(EventoJuego evento);

    /**
     * Recibe la notificacion al cambiar de nivel.
     *
     * @param nivel numero del nuevo nivel activo.
     */
    void alCambiarNivel(int nivel);
}
