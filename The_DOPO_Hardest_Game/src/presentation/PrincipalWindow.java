package presentation;

import javax.swing.*;
import java.awt.*;

public class PrincipalWindow extends JFrame{
	
	private static final String MENU_SCREEN = "MENU";
	private static final String MODE_SCREEN = "PLAYING MODE";
	private static final String LEVELS_SCREEN = "SELECT LEVEL";
	private static final String CHARACTER_SCREEN = "SELECT CHARACTER";
	
	private CardLayout cardLayout;
	private JPanel container;
	
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
	
	public void showMenu() {
		cardLayout.show(container, MENU_SCREEN);
	}
	
	public void showModeSelection() {
		cardLayout.show(container, MODE_SCREEN);
	}
	
	public void showLevelsScreen() {
		cardLayout.show(container, LEVELS_SCREEN);
	}
	
	public void showCharacterSelection() {
		cardLayout.show(container, CHARACTER_SCREEN);
	}
	
	public void startGame(String skin) {
		System.out.println("Starting game with: " + skin);
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new PrincipalWindow().setVisible(true);
		});
	}

}
