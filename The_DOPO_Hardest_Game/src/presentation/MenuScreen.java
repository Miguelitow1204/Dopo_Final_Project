package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MenuScreen extends JPanel{
	
	private PrincipalWindow window;
	
	public MenuScreen(PrincipalWindow window) {
		this.window = window;
		setLayout(new GridBagLayout());
		buildUI();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		
		//Fondo Oscuro
		g2.setColor(new Color(30, 30, 40));
		g2.fillRect(0, 0, getWidth(), getHeight());
		
		//Cuadricula
		g2.setColor(new Color(50, 50, 65));
		int cellSize = 40;
		for(int x = 0; x < getWidth(); x += cellSize) {
			g2.drawLine(0, x, getWidth(), x);
		}
		for(int y = 0; y < getHeight(); y += cellSize) {
			g2.drawLine(0, y, getHeight(), y);
		}
		
		//Zona verde izq.
		g2.setColor(new Color(80, 200, 100, 120));
		g2.fillRect(0, 0, 80, getHeight());
		
		//Zona verde der,
		g2.setColor(new Color(80, 200, 100, 120));
		g2.fillRect(getWidth() - 80, 0, 80, getHeight());
	}
	
	private void buildUI() {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 20, 10, 20);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		//Titulo pequeño
		JLabel subTitle = new JLabel("THE DOPO...");
		subTitle.setFont(new Font("Monospaced", Font.BOLD, 20));
		subTitle.setForeground(new Color(100, 200, 255));
		subTitle.setHorizontalAlignment(SwingConstants.CENTER);
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(subTitle, gbc);
		
		//Titulo grande
		JLabel title = new JLabel("HARDEST GAME");
		title.setFont(new Font("Monospaced", Font.BOLD, 42));
		title.setForeground(new Color(255, 60, 60));
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
		gbc.gridy = 1;
		add(title, gbc);
		
		//Espacio
		gbc.gridy = 2;
		add(Box.createVerticalStrut(30), gbc);
		
		//Boton PLAY
		JButton btnPlay = createButton("▶ PLAY", new Color(255, 200, 50));
		btnPlay.setForeground(new Color(30, 30, 40));
		btnPlay.addActionListener(e -> window.showModeSelection());
		gbc.gridy = 3;
		add(btnPlay, gbc);
		
		//Boton LEVELS
		JButton btnLevels = createButton("LEVELS", new Color(70, 180, 70));
		btnLevels.addActionListener(e -> window.showLevelsScreen());
		gbc.gridy = 4;
		add(btnLevels, gbc);
	}
	
	private JButton createButton(String text, Color bg) {
		JButton btn = new JButton(text);
		btn.setFont(new Font("Monospaced", Font.BOLD, 18));
		btn.setBackground(bg);
		btn.setForeground(Color.WHITE);
		btn.setFocusPainted(false);
		btn.setBorderPainted(false);
		btn.setPreferredSize(new Dimension(260, 50));
		btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		return btn;
	}
}
