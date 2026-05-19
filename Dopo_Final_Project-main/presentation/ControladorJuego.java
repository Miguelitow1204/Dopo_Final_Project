package presentation;

import Domain.*;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * Controlador principal del ciclo de juego, entrada de teclado y estados.
 * 
 * @author Murillo-Rubiano
 * @version 2.0
 */
public class ControladorJuego implements ActionListener {

    private Juego juego;
    private GameScreen gameScreen;
    private PrincipalWindow window;
    private ControladorTeclado controladorTeclado;
    private GestorColisiones gestorColisiones;
    private Timer temporizadorJuego;
    private Timer temporizadorSegundo;
    private boolean nivelCompletado;

    private static final int FPS = 60;
    private static final int INTERVALO_MS = 1000 / FPS;
    
    private boolean modoPvP; //Indica si el juego esta en modo PvP
    private TipoPersonaje tipoP2; //Tipo de personaje de P2 

    /**
     * Construye el controlador del juego y configura timers y entrada.
     *
     * @param juego      modelo principal del juego.
     * @param gameScreen vista de render del juego.
     * @param window     ventana principal para cambiar de pantalla.
     */
    public ControladorJuego(Juego juego, GameScreen gameScreen, PrincipalWindow window) {
        this.juego = juego;
        this.gameScreen = gameScreen;
        this.window = window;
        this.controladorTeclado = new ControladorTeclado();
        this.gestorColisiones = new GestorColisiones();
        this.nivelCompletado = false;

        // Agregar controlador de teclado al panel del juego
        gameScreen.addKeyListener(controladorTeclado);
        gameScreen.setFocusable(true);

        // Timer principal del game loop (60 FPS)
        temporizadorJuego = new Timer(INTERVALO_MS, this);

        // Timer para decrementar el tiempo cada segundo
        temporizadorSegundo = new Timer(1000, e -> {
            if (juego.getEstado() == EstadoJuego.JUGANDO) {
                juego.decrementarTiempo();
                gameScreen.setTiempoRestante(juego.getTiempoRestante());

                // Derrota por tiempo
                if (juego.getTiempoRestante() <= 0) {
                    juego.setEstado(EstadoJuego.GAME_OVER);
                    gameScreen.mostrarDerrota();
                }
            }
        });
    }
    
    /**
     * Construye el controlador en modo PvP con dos jugadores.
     *
     * @param juego      modelo principal del juego.
     * @param gameScreen vista de render del juego.
     * @param window     ventana principal para cambiar de pantalla.
     * @param tipoP2     tipo de personaje del jugador 2.
     */
    public ControladorJuego(Juego juego, GameScreen gameScreen, PrincipalWindow window, TipoPersonaje tipoP2) {
        this(juego, gameScreen, window);
        this.modoPvP = true;
        this.tipoP2 = tipoP2;
    }

    /**
     * Inicia una nueva partida y arranca el ciclo de actualizacion.
     */
    public void iniciar() {
        juego.iniciar();
        Nivel nivel = juego.getNivelActual();

        //Configurar jugador 2 en modo PvP
        if (modoPvP) {
            Jugador jugador2 = new Jugador(new Posicion(nivel.getZonaMeta().getPosicion().getX() + 30,
                nivel.getZonaMeta().getPosicion().getY() + nivel.getZonaMeta().getAlto() / 2), "Player2", tipoP2);
            nivel.setJugador2(jugador2);
            jugador2.setPosicionInicial(new Posicion(
                    nivel.getZonaMeta().getPosicion().getX() + 30,
                    nivel.getZonaMeta().getPosicion().getY() + 
                    nivel.getZonaMeta().getAlto() / 2));
            nivel.setModoPvP(true);
            gameScreen.setModoPvP(true);
        }

        gameScreen.setNivel(nivel);
        gameScreen.setTiempoRestante(juego.getTiempoRestante());
        gameScreen.setMuertes(nivel.getJugador().getMuertes());
        gameScreen.ocultarOverlay();
        temporizadorJuego.start();
        temporizadorSegundo.start();
        gameScreen.requestFocusInWindow();
    }

    /**
     * Detiene los temporizadores activos del juego.
     */
    public void detener() {
        temporizadorJuego.stop();
        temporizadorSegundo.stop();
    }

    /**
     * Ejecuta un tick del game loop segun el estado actual.
     *
     * @param e evento del temporizador.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (juego.getEstado()) {
            case GAME_OVER:
                procesarTeclasGameOver();
                break;
            case PAUSA:
                procesarTeclasPausa();
                break;
            case JUGANDO:
                juego.getNivelActual().getJugador().actualizarInvulnerabilidad();
                if(modoPvP && juego.getNivelActual().getJugador2() != null){
                    juego.getNivelActual().getJugador2().actualizarInvulnerabilidad();
                }
                procesarEntrada();
                if (modoPvP) {
                    procesarEntradaP2();
                }
                juego.getNivelActual().actualizar();
                if (modoPvP) {
                    gestorColisiones.verificarColisionesNivelPvP(juego.getNivelActual());
                    verificarVictoriaPvP();
                } else {
                    gestorColisiones.verificarColisionesNivel(juego.getNivelActual());
                    verificarVictoria();
                }
                actualizarVista();
                break;
        }
        gameScreen.repaint();
    }

    /**
     * Lee la entrada de teclado y aplica movimiento o pausa.
     */
    private void procesarEntrada() {
        Jugador jugador = juego.getNivelActual().getJugador();
        int dx = 0;
        int dy = 0;

        if (controladorTeclado.isTeclaPresionada(KeyEvent.VK_W)) {
            dy = -1;
        } else if (controladorTeclado.isTeclaPresionada(KeyEvent.VK_S)) {
            dy = 1;
        } else if (controladorTeclado.isTeclaPresionada(KeyEvent.VK_A)) {
            dx = -1;
        } else if (controladorTeclado.isTeclaPresionada(KeyEvent.VK_D)) {
            dx = 1;
        }

        if (controladorTeclado.isTeclaPresionada(KeyEvent.VK_ESCAPE)) {
            juego.pausar();
            gameScreen.mostrarPausa();
            return;
        }

        if (dx != 0 || dy != 0) {
            double prevX = jugador.getPosicion().getX();
            double prevY = jugador.getPosicion().getY();

            jugador.mover(dx, dy);

            double newX = jugador.getPosicion().getX();
            double newY = jugador.getPosicion().getY();

            // Limites del mapa: consultados desde el nivel, no desde la vista
            java.awt.Rectangle limites = juego.getNivelActual().getLimitesJugables();
            if (newX < limites.getMinX() || newX + jugador.getAncho() > limites.getMaxX() ||
                    newY < limites.getMinY() || newY + jugador.getAlto() > limites.getMaxY()) {
                jugador.getPosicion().setX(prevX);
                jugador.getPosicion().setY(prevY);
            }

            // Verificar colision con paredes internas
            gestorColisiones.verificarColisionParedes(
                    juego.getNivelActual(), prevX, prevY, jugador);
        }
    }
    
    /**
     * Lee entrada del teclado para el jugador 2 (flechas).
     */
    private void procesarEntradaP2() {
        Jugador jugador2 = juego.getNivelActual().getJugador2();
        if (jugador2 == null) return;

        int dx = 0, dy = 0;

        if (controladorTeclado.isTeclaPresionada(KeyEvent.VK_UP))    dy = -1;
        else if (controladorTeclado.isTeclaPresionada(KeyEvent.VK_DOWN))  dy = 1;
        else if (controladorTeclado.isTeclaPresionada(KeyEvent.VK_LEFT))  dx = -1;
        else if (controladorTeclado.isTeclaPresionada(KeyEvent.VK_RIGHT)) dx = 1;

        if (dx != 0 || dy != 0) {
            double prevX = jugador2.getPosicion().getX();
            double prevY = jugador2.getPosicion().getY();

            jugador2.mover(dx, dy);

            double newX = jugador2.getPosicion().getX();
            double newY = jugador2.getPosicion().getY();

            java.awt.Rectangle limites = juego.getNivelActual().getLimitesJugables();
            if (newX < limites.getMinX() || newX + jugador2.getAncho() > limites.getMaxX() ||
                    newY < limites.getMinY() || newY + jugador2.getAlto() > limites.getMaxY()) {
                jugador2.getPosicion().setX(prevX);
                jugador2.getPosicion().setY(prevY);
            }
            gestorColisiones.verificarColisionParedes(
                    juego.getNivelActual(), prevX, prevY, jugador2);
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

    private void procesarTeclasPausa() {
        if (nivelCompletado) {
            if (juego.getNivelActual().isModoPvP()) {
                if (controladorTeclado.isTeclaPresionada(KeyEvent.VK_R)) {
                    nivelCompletado = false;
                    juego.reiniciarNivel();
                    gameScreen.ocultarOverlay();
                    juego.setEstado(EstadoJuego.JUGANDO);
                    temporizadorSegundo.restart();
                }
                
                if (controladorTeclado.isTeclaPresionada(KeyEvent.VK_L)) {
                    detener();
                    window.showLevelsScreen();
                }
                
                if (controladorTeclado.isTeclaPresionada(KeyEvent.VK_M)) {
                    detener();
                    window.showMenu();
                }
                return;
            }
            // Retry modo normal
            if (controladorTeclado.isTeclaPresionada(KeyEvent.VK_R)) {
                nivelCompletado = false;
                juego.reiniciarNivel();
                gameScreen.setTiempoRestante(juego.getTiempoRestante());
                gameScreen.setMuertes(juego.getNivelActual().getJugador().getMuertes());
                gameScreen.ocultarOverlay();
                juego.setEstado(EstadoJuego.JUGANDO);
                temporizadorSegundo.restart();
            }
            // Next level
            if (controladorTeclado.isTeclaPresionada(KeyEvent.VK_N)) {
                nivelCompletado = false;
                boolean hayMas = juego.siguienteNivel();
                if (hayMas) {
                    gameScreen.ocultarOverlay();
                    gameScreen.setNivel(juego.getNivelActual());
                    gameScreen.setTiempoRestante(juego.getTiempoRestante());
                    gameScreen.setMuertes(juego.getNivelActual().getJugador().getMuertes());
                    juego.setEstado(EstadoJuego.JUGANDO);
                } else {
                    detener();
                    window.showMenu();
                }
            }
            // Menu
            if (controladorTeclado.isTeclaPresionada(KeyEvent.VK_M)) {
                detener();
                window.showMenu();
            }
        } else {
            if (controladorTeclado.isTeclaPresionada(KeyEvent.VK_ENTER) ||
                    controladorTeclado.isTeclaPresionada(KeyEvent.VK_ESCAPE)) {
                juego.pausar();
                gameScreen.ocultarOverlay();
            }
        }
    }

    /**
     * Gestiona teclas cuando el juego esta en estado game over.
     */
    private void procesarTeclasGameOver() {
        // Retry
        if (controladorTeclado.isTeclaPresionada(KeyEvent.VK_R)) {
            juego.reiniciarNivel();
            gameScreen.setTiempoRestante(juego.getTiempoRestante());
            gameScreen.setMuertes(juego.getNivelActual().getJugador().getMuertes());
            gameScreen.ocultarOverlay();
            juego.setEstado(EstadoJuego.JUGANDO);
            temporizadorSegundo.restart();
        }
        // Menu
        if (controladorTeclado.isTeclaPresionada(KeyEvent.VK_M)) {
            detener();
            window.showMenu();
        }
    }

    /**
     * Verifica si el nivel fue completado y prepara transicion.
     */
    private void verificarVictoria() {
        Nivel nivel = juego.getNivelActual();
        if (nivel.estaCompleto()) {
            nivelCompletado = true;
            juego.setEstado(EstadoJuego.PAUSA);
            window.desbloquearSiguienteNivel();
            gameScreen.mostrarVictoria();
        }
    }
    
    /**
     * Verifica si alguno de los jugadores gano en modo PvP.
     */
    private void verificarVictoriaPvP() {
        Nivel nivel = juego.getNivelActual();
        int ganador = nivel.ganadorPvP();
        if (ganador > 0) {
            nivelCompletado = true;
            juego.setEstado(EstadoJuego.PAUSA);
            gameScreen.mostrarVictoriaPvP(ganador);
        }
    }

    /**
     * Actualiza indicadores de la vista y solicita repintado.
     */
    private void actualizarVista() {
        Nivel nivel = juego.getNivelActual();
        gameScreen.setMuertes(nivel.getJugador().getMuertes());
        gameScreen.repaint();
    }
}