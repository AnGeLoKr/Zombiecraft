
package it.unical.mat.igpe.ZombieCraft.gui;

import javax.swing.JFrame;

import it.unical.mat.igpe.ZombieCraft.Utilities.EnumPanel;
import it.unical.mat.igpe.ZombieCraft.Utilities.Scoreboard;

public class MainFrame extends JFrame {

	private MenuPanel menuPanel;
	private OptionMenuPanel optionMenu;
	public ScoresMenuPanel scoresMenu;
	public EditorMenuPanel editorMenu;
	public MultiplayerMenuPanel multiplayerMenu;

	public static void main(String[] args) {
		Scoreboard.loadScoreBoard();
		JFrame mainFrame = new MainFrame();
		mainFrame.setVisible(true);

	}

	public MainFrame() {
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		setTitle("ZombieCraft");
		menuPanel = new MenuPanel(this);
		optionMenu = new OptionMenuPanel(this);
		scoresMenu = new ScoresMenuPanel(this);
		editorMenu = new EditorMenuPanel(this);
		multiplayerMenu = new MultiplayerMenuPanel(this);

		setUndecorated(true);
		// setExtendedState(MAXIMIZED_BOTH);
		this.setContentPane(menuPanel);
		pack();
		setLocationRelativeTo(null);
	}

	public void DrawPanel(EnumPanel panel) {

		switch (panel) {
		case MENU_PANEL:
			this.setContentPane(menuPanel);
			break;
		case OPTION_MENU_PANEL:
			optionMenu.refreshSettings();
			this.setContentPane(optionMenu);
			break;
		case SCORES_MENU_PANEL:
			ScoresMenuPanel.refreshScoreboard();
			this.setContentPane(scoresMenu);
			break;
		case EDITOR_MENU_PANEL:
			this.setContentPane(editorMenu);
			break;
		case MultiPlayer_MENU_PANEL:
			this.setContentPane(multiplayerMenu);
			break;

		}

		pack();
	}
}
