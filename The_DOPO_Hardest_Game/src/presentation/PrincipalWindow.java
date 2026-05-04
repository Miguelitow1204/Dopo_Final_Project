package presentation;

import Domain.*;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana principal Swing que organiza pantallas y arranca partidas.
 * Autores: MurilloRubiano con apoyo de Claude Opus 4.6.
 */
public class PrincipalWindow extends JFrame {

    private static final String MENU_SCREEN = "MENU";
    private static final String MODE_SCREEN = "PLAYING MODE";
    private static final String LEVELS_SCREEN = "SELECT LEVEL";
    private static final String CHARACTER_SCREEN = "SELECT CHARACTER";
    private static final String GAME_SCREEN = "GAME";

    private CardLayout cardLayout;
    private JPanel container;
    private ControladorJuego controladorJuego;

    /**
     * Construye la ventana principal y registra las pantallas base.
     */
    public PrincipalWindow() {
        setTitle("The DOPO Hardest Game");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        cardLayout = new CardLayout();
        container = new JPanel(cardLayout);

        container.add(new MenuScreen(this), MENU_SCREEN);
        container.add(new SelectionModeScreen(this), MODE_SCREEN);
        container.add(new LevelsScreen(this), LEVELS_SCREEN);
        container.add(new CharacterSelectionScreen(this), CHARACTER_SCREEN);

        add(container);
        showMenu();
    }

    /**
     * Muestra el menu principal y detiene partida activa si existe.
     */
    public void showMenu() {
        // Detener el juego si estaba corriendo
        if (controladorJuego != null) {
            controladorJuego.detener();
            controladorJuego = null;
        }
        cardLayout.show(container, MENU_SCREEN);
    }

    /**
     * Muestra la pantalla de seleccion de modo.
     */
    public void showModeSelection() {
        cardLayout.show(container, MODE_SCREEN);
    }

    /**
     * Muestra la pantalla de seleccion de nivel.
     */
    public void showLevelsScreen() {
        cardLayout.show(container, LEVELS_SCREEN);
    }

    /**
     * Muestra la pantalla de seleccion de personaje.
     */
    public void showCharacterSelection() {
        cardLayout.show(container, CHARACTER_SCREEN);
    }

    /**
     * Inicia el juego con el skin seleccionado.
     * Crea los niveles, el modelo, la pantalla de juego y el controlador.
     */
    public void startGame(String skin) {
        // Convertir skin string a TipoPersonaje
        TipoPersonaje tipo = Jugador.skinToTipo(skin);

        // Crear el modelo del juego
        Juego juego = new Juego();

        // Crear los 3 niveles
        juego.agregarNivel(crearNivel1(tipo));
        juego.agregarNivel(crearNivel2(tipo));
        juego.agregarNivel(crearNivel3(tipo));

        // Crear la pantalla de juego y agregarla al CardLayout
        GameScreen gameScreen = new GameScreen();
        container.add(gameScreen, GAME_SCREEN);
        cardLayout.show(container, GAME_SCREEN);

        // Crear y arrancar el controlador del juego
        controladorJuego = new ControladorJuego(juego, gameScreen, this);
        controladorJuego.iniciar();
    }

    // =============================================
    // Creacion de niveles
    // =============================================

    /**
     * Crea la configuracion del nivel 1.
     *
     * @param tipo tipo de personaje seleccionado.
     * @return nivel listo para agregarse al juego.
     */
    private Nivel crearNivel1(TipoPersonaje tipo) {
        Nivel nivel = new Nivel(1, 60);

        Jugador jugador = new Jugador(new Posicion(45, 270), "Player", tipo);
        nivel.setJugador(jugador);

        nivel.setZonaInicio(new ZonaSegura(
                new Posicion(0, 210), 100, 150, TipoZona.INICIO));
        nivel.setZonaMeta(new ZonaSegura(
                new Posicion(700, 210), 100, 150, TipoZona.META));

        // Monedas
        nivel.getMonedas().add(new Moneda(new Posicion(280, 230)));
        nivel.getMonedas().add(new Moneda(new Posicion(400, 280)));
        nivel.getMonedas().add(new Moneda(new Posicion(520, 230)));
        nivel.getMonedas().add(new Moneda(new Posicion(400, 180)));
        nivel.getMonedas().add(new Moneda(new Posicion(340, 340)));

        // Enemigos horizontales
        nivel.getEnemigos().add(new EnemigoLineal(
                new Posicion(250, 190), 14, 14, 2,
                new Posicion(180, 190), new Posicion(620, 190), true));
        nivel.getEnemigos().add(new EnemigoLineal(
                new Posicion(450, 280), 14, 14, 2.5,
                new Posicion(180, 280), new Posicion(620, 280), true));
        nivel.getEnemigos().add(new EnemigoLineal(
                new Posicion(350, 370), 14, 14, 1.8,
                new Posicion(180, 370), new Posicion(620, 370), true));

        // Enemigos verticales
        nivel.getEnemigos().add(new EnemigoLineal(
                new Posicion(220, 180), 14, 14, 2,
                new Posicion(220, 100), new Posicion(220, 480), false));
        nivel.getEnemigos().add(new EnemigoLineal(
                new Posicion(580, 250), 14, 14, 2.2,
                new Posicion(580, 100), new Posicion(580, 480), false));

        return nivel;
    }

    /**
     * Crea la configuracion del nivel 2.
     *
     * @param tipo tipo de personaje seleccionado.
     * @return nivel listo para agregarse al juego.
     */
    private Nivel crearNivel2(TipoPersonaje tipo) {
        Nivel nivel = new Nivel(2, 90);

        Jugador jugador = new Jugador(new Posicion(45, 270), "Player", tipo);
        nivel.setJugador(jugador);

        nivel.setZonaInicio(new ZonaSegura(
                new Posicion(0, 210), 100, 150, TipoZona.INICIO));
        nivel.setZonaMeta(new ZonaSegura(
                new Posicion(700, 210), 100, 150, TipoZona.META));

        // Mas monedas
        nivel.getMonedas().add(new Moneda(new Posicion(200, 140)));
        nivel.getMonedas().add(new Moneda(new Posicion(350, 200)));
        nivel.getMonedas().add(new Moneda(new Posicion(500, 300)));
        nivel.getMonedas().add(new Moneda(new Posicion(600, 180)));
        nivel.getMonedas().add(new Moneda(new Posicion(400, 400)));
        nivel.getMonedas().add(new Moneda(new Posicion(250, 380)));
        nivel.getMonedas().add(new Moneda(new Posicion(550, 440)));

        // Mas enemigos - filas horizontales
        nivel.getEnemigos().add(new EnemigoLineal(
                new Posicion(250, 130), 14, 14, 2.5,
                new Posicion(140, 130), new Posicion(650, 130), true));
        nivel.getEnemigos().add(new EnemigoLineal(
                new Posicion(450, 220), 14, 14, 3,
                new Posicion(140, 220), new Posicion(650, 220), true));
        nivel.getEnemigos().add(new EnemigoLineal(
                new Posicion(300, 310), 14, 14, 2,
                new Posicion(140, 310), new Posicion(650, 310), true));
        nivel.getEnemigos().add(new EnemigoLineal(
                new Posicion(500, 400), 14, 14, 2.8,
                new Posicion(140, 400), new Posicion(650, 400), true));

        // Columnas verticales
        nivel.getEnemigos().add(new EnemigoLineal(
                new Posicion(250, 160), 14, 14, 2.2,
                new Posicion(250, 70), new Posicion(250, 520), false));
        nivel.getEnemigos().add(new EnemigoLineal(
                new Posicion(420, 280), 14, 14, 2.5,
                new Posicion(420, 70), new Posicion(420, 520), false));
        nivel.getEnemigos().add(new EnemigoLineal(
                new Posicion(590, 200), 14, 14, 2,
                new Posicion(590, 70), new Posicion(590, 520), false));

        return nivel;
    }

    /**
     * Crea la configuracion del nivel 3.
     *
     * @param tipo tipo de personaje seleccionado.
     * @return nivel listo para agregarse al juego.
     */
    private Nivel crearNivel3(TipoPersonaje tipo) {
        Nivel nivel = new Nivel(3, 120);

        Jugador jugador = new Jugador(new Posicion(45, 270), "Player", tipo);
        nivel.setJugador(jugador);

        nivel.setZonaInicio(new ZonaSegura(
                new Posicion(0, 210), 100, 150, TipoZona.INICIO));
        nivel.setZonaMeta(new ZonaSegura(
                new Posicion(700, 210), 100, 150, TipoZona.META));

        // Monedas dispersas por todo el mapa
        nivel.getMonedas().add(new Moneda(new Posicion(200, 110)));
        nivel.getMonedas().add(new Moneda(new Posicion(400, 110)));
        nivel.getMonedas().add(new Moneda(new Posicion(600, 110)));
        nivel.getMonedas().add(new Moneda(new Posicion(200, 290)));
        nivel.getMonedas().add(new Moneda(new Posicion(400, 290)));
        nivel.getMonedas().add(new Moneda(new Posicion(600, 290)));
        nivel.getMonedas().add(new Moneda(new Posicion(200, 470)));
        nivel.getMonedas().add(new Moneda(new Posicion(400, 470)));
        nivel.getMonedas().add(new Moneda(new Posicion(600, 470)));

        // Patron cruzado de enemigos
        for (int i = 0; i < 5; i++) {
            nivel.getEnemigos().add(new EnemigoLineal(
                    new Posicion(160 + i * 100, 90 + i * 40), 14, 14, 2 + i * 0.3,
                    new Posicion(120, 90 + i * 40), new Posicion(680, 90 + i * 40), true));
        }
        for (int i = 0; i < 4; i++) {
            nivel.getEnemigos().add(new EnemigoLineal(
                    new Posicion(200 + i * 120, 120), 14, 14, 2 + i * 0.2,
                    new Posicion(200 + i * 120, 60), new Posicion(200 + i * 120, 540), false));
        }

        return nivel;
    }

    /**
     * Punto de entrada de la aplicacion de escritorio.
     *
     * @param args argumentos de linea de comandos.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PrincipalWindow().setVisible(true);
        });
    }
}
