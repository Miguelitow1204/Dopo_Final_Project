package Domain;

/**
 * Servicio de reglas de colision entre jugador, enemigos y monedas.
 * 
 * @author (MurilloRubiano)
 * @version (1.7)
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

        //Verificar colision con enemigos
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

        //Verificar colision con monedas
        for (Moneda moneda : nivel.getMonedas()) {
            if (moneda.isActiva() && jugadorTocaMoneda(jugador, moneda)) {
                jugador.recogerMoneda(moneda);
            }
        }
        
        //Verificar zona intermedia
        ZonaSegura intermedia = nivel.getZonaIntermedia();
        if(intermedia != null && intermedia.contiene(nivel.getJugador())){
            nivel.getJugador().setPosicionInicial(new Posicion(intermedia.getPosicion().getX() + 10,
                                                               intermedia.getPosicion().getY() + intermedia.getAlto()/2));
        }
    }

    /**
     * Verifica si un jugador colisiona con alguna pared y revierte su posicion.
     *
     * @param nivel   nivel a procesar.
     * @param prevX   posicion X previa del jugador.
     * @param prevY   posicion Y previa del jugador.
     * @param jugador jugador a verificar.
     */
    public void verificarColisionParedes(Nivel nivel, double prevX, double prevY, Jugador jugador) {
        for (Wall pared : nivel.getParedes()) {
            if (jugador.colisionaCon(pared)) {
                jugador.getPosicion().setX(prevX);
                jugador.getPosicion().setY(prevY);
                return;
            }
        }
    }
    
    /**
     * Verifica si los dos jugadores colisionan entre si.
     * En ese caso ambos pierden una vida y regresan a su punto de inicio.
     *
     * @param jugador1 primer jugador.
     * @param jugador2 segundo jugador.
     * @return true si colisionaron.
     */
    public boolean jugadoresColisionan(Jugador jugador1, Jugador jugador2) {
        return detectarColision(jugador1, jugador2);
    }

    /**
     * Aplica reglas de colision del nivel en modo PvP.
     *
     * @param nivel nivel a procesar.
     */
    public void verificarColisionesNivelPvP(Nivel nivel) {
        Jugador j1 = nivel.getJugador();
        Jugador j2 = nivel.getJugador2();
        
        //Colision P1 con enemigos
        for(Enemigo enemigo : nivel.getEnemigos()){
            if(jugadorTocaEnemigo(j1, enemigo)){
                boolean murio = j1.perderVida();
                if(murio){
                    j1.setMonedasRecogidas(0);
                    j2.setMonedasRecogidas(0);
                    for(Moneda m : nivel.getMonedas()){
                        m.reiniciar();
                    }
                }
                return;
            }
        }
        
        //Colision P2 con enemigos
        for(Enemigo enemigo : nivel.getEnemigos()){
            if(jugadorTocaEnemigo(j2, enemigo)){
                boolean murio = j2.perderVida();
                if(murio){
                    j1.setMonedasRecogidas(0);
                    j2.setMonedasRecogidas(0);
                    for(Moneda m : nivel.getMonedas()){
                        m.reiniciar();
                    }
                }
                return;
            }
        }
        
        //Colision entre jugadores
        if(jugadoresColisionan(j1, j2)){
            boolean murioJ1 = j1.perderVida();
            boolean murioJ2 = j2.perderVida();
            if(murioJ1 || murioJ2){
                j1.setMonedasRecogidas(0);
                j2.setMonedasRecogidas(0);
                for(Moneda m : nivel.getMonedas()){
                    m.reiniciar();
                }
            }
            return;
        }
        
        //P1 recoge monedas
        for(Moneda moneda : nivel.getMonedas()){
            if(moneda.isActiva() && jugadorTocaMoneda(j1, moneda)){
                j1.recogerMoneda(moneda);
            }
        }
        
        //P2 recoge monedas
        for(Moneda moneda : nivel.getMonedas()){
            if(moneda.isActiva() && jugadorTocaMoneda(j2, moneda)){
                j2.recogerMoneda(moneda);
            }
        }
    }
}