package presentation;

import Domain.*;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * Controlador principal del ciclo de juego, entrada de teclado y estados.
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
                
                //Derrota por tiempo
                if(juego.getTiempoRestante() <= 0) {
                	juego.setEstado(EstadoJuego.GAME_OVER);
                	gameScreen.mostrarDerrota();
                }
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
        gameScreen.ocultarOverlay();
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

            // Limites del mapa coinciden con las paredes del nivel
            if (newX < 0 || newX + jugador.getAncho() > GameScreen.ANCHO ||
                    newY < 155 || newY + jugador.getAlto() > 420) {
                jugador.getPosicion().setX(prevX);
                jugador.getPosicion().setY(prevY);
            }

            // Verificar colision con paredes internas
            gestorColisiones.verificarColisionParedes(
                    juego.getNivelActual(), prevX, prevY);
        }
    }

    /**
     * Gestiona teclas cuando el juego esta en pausa.
     */
    private void procesarTeclasPausa() {
    	if (nivelCompletado) {
            //Retry
            if (controladorTeclado.isTeclaPresionada(KeyEvent.VK_R)) {
                nivelCompletado = false;
                juego.reiniciarNivel();
                gameScreen.setTiempoRestante(juego.getTiempoRestante());
                gameScreen.setMuertes(juego.getNivelActual().getJugador().getMuertes());
                gameScreen.ocultarOverlay();
                juego.setEstado(EstadoJuego.JUGANDO);
                temporizadorSegundo.restart();
            }
            //Next level
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
            //Menu
            if (controladorTeclado.isTeclaPresionada(KeyEvent.VK_M)) {
                detener();
                window.showMenu();
            }
        } else {
            //Pausa normal - reanudar con ENTER o ESC
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
    	//Retry
        if (controladorTeclado.isTeclaPresionada(KeyEvent.VK_R)) {
        	juego.reiniciarNivel();
            gameScreen.setTiempoRestante(juego.getTiempoRestante());
            gameScreen.setMuertes(juego.getNivelActual().getJugador().getMuertes());
            gameScreen.ocultarOverlay();
            juego.setEstado(EstadoJuego.JUGANDO);
            temporizadorSegundo.restart();
        }
        //Menu
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
            gameScreen.mostrarVictoria();
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
