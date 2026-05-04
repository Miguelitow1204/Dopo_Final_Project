package Domain;

/**
 * Eventos del dominio usados para notificacion a observadores.
 * @author (MurilloRubiano)
 * @version (1.0)
 */
public enum EventoJuego {
    JUGADOR_MURIO,
    MONEDA_RECOGIDA,
    JUEGO_PAUSADO,
    NIVEL_COMPLETADO,
    GAME_OVER,
    VICTORIA,
    TIEMPO_AGOTADO
}