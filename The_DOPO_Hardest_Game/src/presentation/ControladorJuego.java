package presentation;

import Domain.*;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * Controlador principal del ciclo de juego, entrada de teclado y estados.
 * Autores: MurilloRubiano con apoyo de Claude Opus 4.6.
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
            }
        });
    }

    /**
     * Inicia una nueva partida y arranca el ciclo de actualizacion.
     */
    public void iniciar() {
        juego.iniciar();
        Nivel nivel = juego.getNivelActual();
        gameScreen.setNivel(nivel);
        gameScreen.setTiempoRestante(juego.getTiempoRestante());
        gameScreen.setMuertes(nivel.getJugador().getMuertes());
        gameScreen.ocultarMensaje();
        temporizadorJuego.start();
        temporizadorSegundo.start();

        // Asegurar que el panel tenga el foco para recibir teclas
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
        if (juego.getEstado() == EstadoJuego.GAME_OVER) {
            procesarTeclasGameOver();
            gameScreen.repaint();
            return;
        }

        if (juego.getEstado() == EstadoJuego.PAUSA) {
            procesarTeclasPausa();
            gameScreen.repaint();
            return;
        }

        if (juego.getEstado() == EstadoJuego.JUGANDO) {
            procesarEntrada();
            juego.getNivelActual().actualizar();
            gestorColisiones.verificarColisionesNivel(juego.getNivelActual());
            verificarVictoria();
            actualizarVista();
        }
    }

    /**
     * Lee la entrada de teclado y aplica movimiento o pausa.
     */
    private void procesarEntrada() {
        Jugador jugador = juego.getNivelActual().getJugador();
        int dx = 0;
        int dy = 0;

        // Solo una direccion a la vez (sin diagonal)
        if (controladorTeclado.isTeclaPresionada(KeyEvent.VK_W) ||
                controladorTeclado.isTeclaPresionada(KeyEvent.VK_UP)) {
            dy = -1;
        } else if (controladorTeclado.isTeclaPresionada(KeyEvent.VK_S) ||
                controladorTeclado.isTeclaPresionada(KeyEvent.VK_DOWN)) {
            dy = 1;
        } else if (controladorTeclado.isTeclaPresionada(KeyEvent.VK_A) ||
                controladorTeclado.isTeclaPresionada(KeyEvent.VK_LEFT)) {
            dx = -1;
        } else if (controladorTeclado.isTeclaPresionada(KeyEvent.VK_D) ||
                controladorTeclado.isTeclaPresionada(KeyEvent.VK_RIGHT)) {
            dx = 1;
        }

        // Pausa con ESC
        if (controladorTeclado.isTeclaPresionada(KeyEvent.VK_ESCAPE)) {
            juego.pausar();
            gameScreen.setMensaje("PAUSED");
            return;
        }

        if (dx != 0 || dy != 0) {
            double prevX = jugador.getPosicion().getX();
            double prevY = jugador.getPosicion().getY();

            jugador.mover(dx, dy);

            // Verificar limites del area de juego (800x600, HUD=45)
            double newX = jugador.getPosicion().getX();
            double newY = jugador.getPosicion().getY();

            if (newX < 0 || newX + jugador.getAncho() > GameScreen.ANCHO ||
                    newY < 45 || newY + jugador.getAlto() > GameScreen.ALTO) {
                jugador.getPosicion().setX(prevX);
                jugador.getPosicion().setY(prevY);
            }
        }
    }

    /**
     * Gestiona teclas cuando el juego esta en pausa.
     */
    private void procesarTeclasPausa() {
        if (controladorTeclado.isTeclaPresionada(KeyEvent.VK_ENTER)) {
            if (nivelCompletado) {
                nivelCompletado = false;
                boolean hayMas = juego.siguienteNivel();
                if (hayMas) {
                    gameScreen.ocultarMensaje();
                    gameScreen.setNivel(juego.getNivelActual());
                    gameScreen.setTiempoRestante(juego.getTiempoRestante());
                    gameScreen.setMuertes(juego.getNivelActual().getJugador().getMuertes());
                } else {
                    // Juego completado - ya se mostro VICTORIA
                }
            } else {
                // Reanudar pausa normal
                juego.pausar();
                gameScreen.ocultarMensaje();
            }
        }
        if (controladorTeclado.isTeclaPresionada(KeyEvent.VK_ESCAPE)) {
            // Volver al menu desde pausa
            detener();
            window.showMenu();
        }
    }

    /**
     * Gestiona teclas cuando el juego esta en estado game over.
     */
    private void procesarTeclasGameOver() {
        if (controladorTeclado.isTeclaPresionada(KeyEvent.VK_ENTER) ||
                controladorTeclado.isTeclaPresionada(KeyEvent.VK_ESCAPE)) {
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

            // Verificar si era el ultimo nivel
            if (juego.getIndiceNivel() >= juego.getNiveles().size() - 1) {
                gameScreen.setMensaje("VICTORY! GAME COMPLETED!");
            } else {
                gameScreen.setMensaje("LEVEL " + nivel.getNumero() + " COMPLETED!");
            }
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
