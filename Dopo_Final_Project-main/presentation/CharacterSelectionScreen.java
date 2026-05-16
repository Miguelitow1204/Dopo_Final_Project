package presentation;

import javax.swing.*;
import java.awt.*;

public class CharacterSelectionScreen extends JPanel {

    private PrincipalWindow window;
    private JButton btnRed, btnBlue, btnGreen;
    private String selectedSkin = "RED";
    private static final Color COLOR_SELECTED = new Color(255, 200, 50);

    public CharacterSelectionScreen(PrincipalWindow window) {
        this.window = window;
        setLayout(new GridBagLayout());
        buildUI();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //Fondo oscuro
        g2.setColor(new Color(25, 25, 38));
        g2.fillRect(0, 0, getWidth(), getHeight());

        //Franjas diagonales suaves por personaje
        int w = getWidth() / 3;

        //Franja roja
        g2.setColor(new Color(180, 40, 40, 40));
        g2.fillRect(0, 0, w, getHeight());

        //Franja azul
        g2.setColor(new Color(40, 80, 180, 40));
        g2.fillRect(w, 0, w, getHeight());

        //Franja verde
        g2.setColor(new Color(40, 160, 60, 40));
        g2.fillRect(w * 2, 0, w, getHeight());

        //Cuadrícula encima
        g2.setColor(new Color(50, 50, 70, 80));
        int cellSize = 40;
        for (int x = 0; x < getWidth(); x += cellSize) {
            g2.drawLine(x, 0, x, getHeight());
        }
        for (int y = 0; y < getHeight(); y += cellSize) {
            g2.drawLine(0, y, getWidth(), y);
        }

        //Cuadraditos decorativos de cada personaje
        g2.setColor(new Color(220, 50, 50, 60));
        g2.fillRect(30, 30, 25, 25);

        g2.setColor(new Color(50, 100, 220, 60));
        g2.fillRect(getWidth() / 2 - 12, 30, 25, 25);

        g2.setColor(new Color(50, 180, 70, 60));
        g2.fillRect(getWidth() - 60, 30, 25, 25);
    }

    private void buildUI() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 20, 12, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        JLabel title = new JLabel("SELECT YOUR CHARACTER");
        title.setFont(new Font("Monospaced", Font.BOLD, 22));
        title.setForeground(new Color(255, 200, 50));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 3;
        add(title, gbc);

        // Botones de personaje
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;

        btnRed   = createSkinButton("BLINKY", "Speed: normal\nSize: normal", new Color(220, 50, 50));
        btnBlue  = createSkinButton("INKY",   "Speed: fast\nSize: large",   new Color(50, 100, 220));
        btnGreen = createSkinButton("CLYDE",  "Speed: normal\nAbsorbs 1 hit", new Color(50, 180, 70));

        btnRed.addActionListener(e   -> selectSkin("RED"));
        btnBlue.addActionListener(e  -> selectSkin("BLUE"));
        btnGreen.addActionListener(e -> selectSkin("GREEN"));

        gbc.gridx = 0; gbc.gridy = 1; add(btnRed,   gbc);
        gbc.gridx = 1; gbc.gridy = 1; add(btnBlue,  gbc);
        gbc.gridx = 2; gbc.gridy = 1; add(btnGreen, gbc);

        // Botones de navegación
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;

        JButton btnBack = createNavButton("← BACK", new Color(180, 60, 60));
        btnBack.addActionListener(e -> window.showModeSelection());
        gbc.gridx = 0; gbc.gridy = 2;
        add(btnBack, gbc);

        JButton btnPlay = createNavButton("PLAY ▶", new Color(255, 200, 50));
        btnPlay.setForeground(new Color(30, 30, 40));
        btnPlay.addActionListener(e -> window.startGame(selectedSkin));
        gbc.gridx = 2; gbc.gridy = 2;
        add(btnPlay, gbc);

        selectSkin("RED"); // selección por defecto
    }

    private void selectSkin(String skin) {
        selectedSkin = skin;
        btnRed.setBorder(BorderFactory.createLineBorder(
                skin.equals("RED")   ? COLOR_SELECTED : new Color(220, 50, 50),  skin.equals("RED")   ? 4 : 1));
        btnBlue.setBorder(BorderFactory.createLineBorder(
                skin.equals("BLUE")  ? COLOR_SELECTED : new Color(50, 100, 220), skin.equals("BLUE")  ? 4 : 1));
        btnGreen.setBorder(BorderFactory.createLineBorder(
                skin.equals("GREEN") ? COLOR_SELECTED : new Color(50, 180, 70),  skin.equals("GREEN") ? 4 : 1));
    }

    private JButton createSkinButton(String name, String description, Color color) {
        JButton btn = new JButton("<html><center><b>" + name + "</b><br><br>" +
                description.replace("\n", "<br>") + "</center></html>");
        btn.setPreferredSize(new Dimension(160, 140));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Monospaced", Font.PLAIN, 13));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton createNavButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Monospaced", Font.BOLD, 15));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(140, 44));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
