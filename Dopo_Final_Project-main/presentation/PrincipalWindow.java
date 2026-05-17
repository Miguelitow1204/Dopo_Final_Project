package presentation;

import Domain.*;

import javax.swing.*;
import java.awt.*;

import java.io.IOException;

/**
 * Ventana principal Swing que organiza pantallas y arranca partidas.
 * 
 * @author (MurilloRubiano)
 * @version (2.0)
 */
public class PrincipalWindow extends JFrame {

    private static final String MENU_SCREEN = "MENU";
    private static final String MODE_SCREEN = "PLAYING MODE";
    private static final String LEVELS_SCREEN = "SELECT LEVEL";
    private static final String CHARACTER_SCREEN = "SELECT CHARACTER";
    private static final String GAME_SCREEN = "GAME";

    private static final int TOTAL_NIVELES = 4; // Total de niveles disponibles

    private CardLayout cardLayout;
    private JPanel container;
    private ControladorJuego controladorJuego;
    
    private int nivelesDesbloqueados = 1; //Numero de niveles desbloqueados en la sesion actual
    private int nivelSeleccionado = 1; // Nivel seleccionado desde la pantalla de levels
    
    private LevelsScreen levelsScreen;
    
    private String modoJuego = "PLAYER"; //Modo de juego seleccionado: PLAYER o PvP

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
        levelsScreen = new LevelsScreen(this);
        container.add(levelsScreen, LEVELS_SCREEN);
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
        levelsScreen.actualizar();
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
     * Delega la creacion de niveles a FabricaNivel: cada nivel
     * se lee desde su archivo configuraciones/nivelN.txt.
     *
     * @param skin identificador de skin ("RED", "BLUE", "GREEN").
     */
    public void startGame(String skin) {
        TipoPersonaje tipo = Jugador.skinToTipo(skin);

        Juego juego = new Juego();
        try {
            FabricaNivel.cargarTodos(tipo, TOTAL_NIVELES)
                    .forEach(juego::agregarNivel);
        } catch (NivelNoEncontradoException e) {
            System.err.println("[ERROR] " + e.getMessage());
            JOptionPane.showMessageDialog(this,
                    "No se pudieron cargar los niveles.\nVerifica la carpeta 'configuraciones/'.",
                    "Error al cargar", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        //Saltar al nivel Seleccionado
        juego.irAlNivel(nivelSeleccionado - 1);

        GameScreen gameScreen = new GameScreen();
        container.add(gameScreen, GAME_SCREEN);
        cardLayout.show(container, GAME_SCREEN);

        controladorJuego = new ControladorJuego(juego, gameScreen, this);
        controladorJuego.iniciar();
    }
    
    /**
     * Inicia el juego en modo Player vs Player.
     *
     * @param skinP1 skin del jugador 1.
     * @param skinP2 skin del jugador 2.
     */
    public void startGamePvP(String skinP1, String skinP2) {
        TipoPersonaje tipoP1 = Jugador.skinToTipo(skinP1);
        TipoPersonaje tipoP2 = Jugador.skinToTipo(skinP2);

        Juego juego = new Juego();
        try {
            FabricaNivel.cargarTodos(tipoP1, TOTAL_NIVELES)
                    .forEach(juego::agregarNivel);
        } catch (NivelNoEncontradoException e) {
            System.err.println("[ERROR] " + e.getMessage());
            JOptionPane.showMessageDialog(this,
                    "No se pudieron cargar los niveles.\nVerifica la carpeta 'configuraciones/'.",
                    "Error al cargar", JOptionPane.ERROR_MESSAGE);
            return;
        }

        juego.irAlNivel(nivelSeleccionado - 1);

        GameScreen gameScreen = new GameScreen();
        container.add(gameScreen, GAME_SCREEN);
        cardLayout.show(container, GAME_SCREEN);

        controladorJuego = new ControladorJuego(juego, gameScreen, this, tipoP2);
        controladorJuego.iniciar();
    }
    
    /**
     * Desbloquea el siguiente nivel si no se ha alcanzado el maximo.
     * Se llama cuando el jugador completa el nivel actual.
     */
    public void desbloquearSiguienteNivel() {
        if (nivelesDesbloqueados < TOTAL_NIVELES) {
            nivelesDesbloqueados++;
        }
    }

    /**
     * Obtiene el numero de niveles desbloqueados en la sesion actual.
     *
     * @return cantidad de niveles disponibles.
     */
    public int getNivelesDesbloqueados() {
        return nivelesDesbloqueados;
    }

    /**
     * Define el nivel desde el que arrancara la partida.
     * Se usa cuando el jugador selecciona un nivel desde LevelsScreen.
     *
     * @param nivel numero del nivel seleccionado.
     */
    public void setNivelSeleccionado(int nivel) {
        this.nivelSeleccionado = nivel;
    }

    /**
     * Obtiene el nivel seleccionado para iniciar la partida.
     *
     * @return numero del nivel a cargar.
     */
    public int getNivelSeleccionado() {
        return nivelSeleccionado;
    }
    
    /**
     * Define el modo de juego seleccionado
     * 
     * @param modo modo de juego
     */
    public void setModoJuego(String modo){
        this.modoJuego = modo;
    }
    
    /**
     * Obtiene el modo de juego seleccionado
     * 
     * @return modo de juego actual
     */
    public String getModoJuego(){
        return modoJuego;
    }

    /**
     *
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
