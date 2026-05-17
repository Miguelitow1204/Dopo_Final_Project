package Domain;

/**
 * Servicio de reglas de colision entre jugador, enemigos y monedas.
 * 
 * @author (MurilloRubiano)
 * @version (1.5)
 */
public class GestorColisiones {

    /**
     * Detecta interseccion entre dos objetos colisionables.
     *
     * @param a primer objeto.
     * @param b segundo objeto.
     * @return true si los limites se intersectan.
     */
    public boolean detectarColision(Colisionable a, Colisionable b) {
        return a.getBounds().intersects(b.getBounds());
    }

    /**
     * Verifica colision entre jugador y enemigo.
     *
     * @param jugador jugador activo.
     * @param enemigo enemigo evaluado.
     * @return true si el jugador toca al enemigo.
     */
    public boolean jugadorTocaEnemigo(Jugador jugador, Enemigo enemigo) {
        return detectarColision(jugador, enemigo);
    }

    /**
     * Verifica colision entre jugador y moneda.
     *
     * @param jugador jugador activo.
     * @param moneda  moneda evaluada.
     * @return true si el jugador toca la moneda.
     */
    public boolean jugadorTocaMoneda(Jugador jugador, Moneda moneda) {
        return detectarColision(jugador, moneda);
    }

    /**
     * Indica si el jugador esta dentro de una zona segura.
     *
     * @param jugador jugador activo.
     * @param zona    zona segura objetivo.
     * @return true si el jugador esta totalmente contenido.
     */
    public boolean jugadorEnZona(Jugador jugador, ZonaSegura zona) {
        return zona.contiene(jugador);
    }

    /**
     * Aplica reglas de colision del nivel en el frame actual.
     *
     * @param nivel nivel a procesar.
     */
    public void verificarColisionesNivel(Nivel nivel) {
        Jugador jugador = nivel.getJugador();

        // Verificar colision con enemigos
        for (Enemigo enemigo : nivel.getEnemigos()) {
            if (jugadorTocaEnemigo(jugador, enemigo)) {
                boolean murio = jugador.perderVida();
                if (murio) {
                    // Solo reiniciar monedas si el jugador murio realmente
                    for (Moneda moneda : nivel.getMonedas()) {
                        moneda.reiniciar();
                    }
                }
                return;
            }
        }

        // Verificar colision con monedas
        for (Moneda moneda : nivel.getMonedas()) {
            if (moneda.isActiva() && jugadorTocaMoneda(jugador, moneda)) {
                jugador.recogerMoneda(moneda);
            }
        }
    }

    /**
     * Verifica si el jugador colisiona con alguna pared y revierte su posicion
     * 
     * @param nivel
     * @param prevX
     * @param prevY
     */
    public void verificarColisionParedes(Nivel nivel, double prevX, double prevY) {
        Jugador jugador = nivel.getJugador();
        for (Wall pared : nivel.getParedes()) {
            if (detectarColision(jugador, pared)) {
                jugador.getPosicion().setX(prevX);
                jugador.getPosicion().setY(prevY);
                return;
            }
        }
    }
}