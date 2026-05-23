package presentation;

import javax.swing.*;
import java.awt.*;

/**
 * Pantalla de selección de modo de juego.
 *
 * <p>
 * Proporciona la interfaz para elegir el modo de juego: jugador único,
 * jugador contra jugador (PvP) o jugador contra máquina (PvM). La pantalla
 * es principalmente una vista que delega la navegación y los cambios de
 * estado en la `PrincipalWindow`.
 * </p>
 *
 * <p>
 * Incluye render de fondo decorativo y botones para cada modo y para
 * regresar al menú principal.
 * 
 * @author MurilloRubiano
 * @version 4.0
 */
public class SelectionModeScreen extends JPanel {

    private PrincipalWindow window;

    /**
     * Construye la pantalla de selección de modo y crea la UI inicial.
     *
     * @param window ventana principal usada para la navegación entre pantallas
     */
    public SelectionModeScreen(PrincipalWindow window) {
        this.window = window;
        setLayout(new GridBagLayout());
        buildUI();
    }

    /**
     * Dibuja el fondo decorativo de la pantalla: fondo oscuro, cuadrícula
     * y motivos circulares que representan enemigos decorativos.
     *
     * @param g contexto gráfico proporcionado por Swing
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Fondo oscuro
        g2.setColor(new Color(20, 20, 35));
        g2.fillRect(0, 0, getWidth(), getHeight());

        // Cuadrícula
        g2.setColor(new Color(40, 40, 60));
        int cellSize = 40;
        for (int x = 0; x < getWidth(); x += cellSize) {
            g2.drawLine(x, 0, x, getHeight());
        }
        for (int y = 0; y < getHeight(); y += cellSize) {
            g2.drawLine(0, y, getWidth(), y);
        }

        // Enemigos azules decorativos
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int[][] enemies = {
                { 60, 80 }, { 120, 200 }, { 680, 100 }, { 720, 350 },
                { 50, 400 }, { 700, 480 }, { 150, 500 }, { 630, 220 }
        };
        for (int[] pos : enemies) {
            g2.setColor(new Color(50, 120, 220, 80));
            g2.fillOval(pos[0] - 15, pos[1] - 15, 30, 30);
            g2.setColor(new Color(80, 160, 255, 120));
            g2.fillOval(pos[0] - 8, pos[1] - 8, 16, 16);
        }
    }

    private void buildUI() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 20, 12, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Titulo
        JLabel title = new JLabel("SELECT GAME MODE");
        title.setFont(new Font("Monospaced", Font.BOLD, 24));
        title.setForeground(new Color(255, 200, 50));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(title, gbc);

        // Boton player
        JButton btnPlayer = createButton("PLAYER", new Color(60, 160, 60));
        btnPlayer.addActionListener(e -> window.showCharacterSelection());
        gbc.gridy = 1;
        add(btnPlayer, gbc);

        // Boton PvP
        JButton btnPvP = createButton("PLAYER vs PLAYER", new Color(60, 60, 180));
        btnPvP.addActionListener(e -> {
            window.setModoJuego("PVP");
            window.showLevelsScreen();
        });
        gbc.gridy = 2;
        add(btnPvP, gbc);

        // Boton PvM
        JButton btnPvM = createButton("PLAYER VS MACHINE", new Color(150, 60, 180));
        btnPvM.addActionListener(e -> {
            window.setModoJuego("PVM");
            window.showLevelsScreen();
        });
        gbc.gridy = 3;
        add(btnPvM, gbc);

        // Boton Back
        JButton btnBack = createButton("← BACK", new Color(180, 60, 60));
        btnBack.addActionListener(e -> window.showMenu());
        gbc.gridy = 4;
        add(btnBack, gbc);
    }

    /**
     * Crea un botón estilizado para esta pantalla con las propiedades
     * visuales usadas en la interfaz (fuente, colores, tamaño y cursor).
     *
     * @param text texto mostrado en el botón
     * @param bg   color de fondo del botón
     * @return JButton configurado
     */
    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Monospaced", Font.BOLD, 16));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(280, 50));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

}
