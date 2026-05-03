package presentation;

import javax.swing.*;
import java.awt.*;

public class LevelsScreen extends JPanel {

    private PrincipalWindow window;

    public LevelsScreen(PrincipalWindow window) {
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
        g2.setColor(new Color(20, 20, 30));
        g2.fillRect(0, 0, getWidth(), getHeight());

        //Cuadrícula
        g2.setColor(new Color(40, 40, 55));
        int cellSize = 40;
        for (int x = 0; x < getWidth(); x += cellSize) {
            g2.drawLine(x, 0, x, getHeight());
        }
        for (int y = 0; y < getHeight(); y += cellSize) {
            g2.drawLine(0, y, getWidth(), y);
        }

        //Monedas decorativas dispersas
        int[][] coins = {
            {50, 60}, {150, 120}, {700, 80}, {680, 300},
            {60, 350}, {720, 480}, {200, 500}, {600, 150},
            {350, 50}, {400, 520}
        };
        for (int[] pos : coins) {
            //Círculo exterior
            g2.setColor(new Color(200, 160, 20, 60));
            g2.fillOval(pos[0] - 14, pos[1] - 14, 28, 28);
            //Círculo interior
            g2.setColor(new Color(255, 200, 50, 80));
            g2.fillOval(pos[0] - 8, pos[1] - 8, 16, 16);
        }
    }

    private void buildUI() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Título
        JLabel title = new JLabel("LEVELS");
        title.setFont(new Font("Monospaced", Font.BOLD, 30));
        title.setForeground(new Color(255, 165, 0));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 3;
        add(title, gbc);

        // Niveles — por ahora solo el 1 desbloqueado
        gbc.gridwidth = 1;
        String[] labels = {"1", "2", "3", "4", "5"};
        boolean[] unlocked = {true, false, false, false, false};

        int col = 0, row = 1;
        for (int i = 0; i < labels.length; i++) {
            JButton btn = createLevelButton(labels[i], unlocked[i]);
            gbc.gridx = col; gbc.gridy = row;
            add(btn, gbc);
            col++;
            if (col > 2) { col = 0; row++; }
        }

        // Botón volver
        JButton btnBack = new JButton("← BACK");
        btnBack.setFont(new Font("Monospaced", Font.BOLD, 14));
        btnBack.setBackground(new Color(180, 60, 60));
        btnBack.setForeground(Color.WHITE);
        btnBack.setFocusPainted(false);
        btnBack.setBorderPainted(false);
        btnBack.addActionListener(e -> window.showMenu());
        gbc.gridx = 0; gbc.gridy = row + 1;
        gbc.gridwidth = 3;
        add(btnBack, gbc);
    }

    private JButton createLevelButton(String number, boolean unlocked) {
        JButton btn = new JButton(number);
        btn.setFont(new Font("Monospaced", Font.BOLD, 20));
        btn.setPreferredSize(new Dimension(80, 80));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        if (unlocked) {
            btn.setBackground(new Color(70, 180, 70));
            btn.setForeground(Color.WHITE);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        } else {
            btn.setBackground(new Color(180, 100, 80));
            btn.setForeground(new Color(120, 60, 50));
            btn.setEnabled(false);
        }
        return btn;
    }
}
