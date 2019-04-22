package it.unical.mat.igpe.ZombieCraft.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import it.unical.mat.igpe.ZombieCraft.Utilities.EnumPanel;
import it.unical.mat.igpe.ZombieCraft.Utilities.ImageLoader;
import it.unical.mat.igpe.ZombieCraft.Utilities.ScoreEntry;
import it.unical.mat.igpe.ZombieCraft.Utilities.Scoreboard;

public class ScoresMenuPanel extends JPanel {
	private static Image Background_Blur = ImageLoader
			.getImageBackground_Blur();

	private static Image scoresTitle = ImageLoader.getImageScoresTitle();

	private static Image Back = ImageLoader.getImageBack();
	private static Image reset = ImageLoader.getImageReset();

	private static int BACK_WIDTH = 75;
	private static int BACK_HEIGHT = 663;

	private static int SCORES_TITLE_WIDTH = 276;
	private static int SCORES_TITLE_HEIGHT = 50;

	private static int RESET_WIDTH = 649;
	private static int RESET_HEIGHT = 663;

	MainFrame frame;
	static GridLayout layGrid = new GridLayout(18, 2);;

	static JPanel scoreListPanel = new JPanel(layGrid);

	private static List<ScoreEntry> scoreboard = new ArrayList<>();
	static Font font = null;

	public ScoresMenuPanel(MainFrame frame) {

		try {
			font = Font.createFont(Font.TRUETYPE_FONT, new File(
					"resources/font/Minecrafter.Reg.ttf"));
			GraphicsEnvironment ge = GraphicsEnvironment
					.getLocalGraphicsEnvironment();
			ge.registerFont(font);

		} catch (Exception e) {
			// Ignore
		}

		this.frame = frame;

		this.setLayout(null);
		setPreferredSize(new Dimension(1024, 768));

		addMouseMotionListener(new MouseMotionAdapter() {

			@Override
			public void mouseMoved(MouseEvent e) {
				// Back Start
				if (e.getX() >= BACK_WIDTH
						&& e.getX() <= BACK_WIDTH
								+ ImageLoader.getImageBack().getWidth(null)
						&& e.getY() >= BACK_HEIGHT
						&& e.getY() <= BACK_HEIGHT
								+ ImageLoader.getImageBack().getHeight(null)) {

					Back = ImageLoader.getImageBack_Pressed();

				} else
					Back = ImageLoader.getImageBack();

				// Reset Start
				if (e.getX() >= RESET_WIDTH
						&& e.getX() <= RESET_WIDTH
								+ ImageLoader.getImageReset().getWidth(null)
						&& e.getY() >= RESET_HEIGHT
						&& e.getY() <= RESET_HEIGHT
								+ ImageLoader.getImageReset().getHeight(null)) {

					reset = ImageLoader.getImageReset_Pressed();

				} else
					reset = ImageLoader.getImageReset();

				// Reset End

				repaint();
			}
		});

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				// pulsante back
				if (e.getX() >= BACK_WIDTH
						&& e.getX() <= BACK_WIDTH
								+ ImageLoader.getImageBack().getWidth(null)
						&& e.getY() >= BACK_HEIGHT
						&& e.getY() <= BACK_HEIGHT
								+ ImageLoader.getImageBack().getHeight(null)) {
					frame.DrawPanel(EnumPanel.OPTION_MENU_PANEL);
				}

				// pulsante reset

				if (e.getX() >= RESET_WIDTH
						&& e.getX() <= RESET_WIDTH
								+ ImageLoader.getImageReset().getWidth(null)
						&& e.getY() >= RESET_HEIGHT
						&& e.getY() <= RESET_HEIGHT
								+ ImageLoader.getImageReset().getHeight(null)) {

					Scoreboard.resetScoreboard();
					showScoreboard();
					repaint();
				}
			}

		});

		// ScoreBoard
		showScoreboard();

	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(Background_Blur, 0, 0, null);

		g.drawImage(Back, BACK_WIDTH, BACK_HEIGHT, null);

		g.drawImage(reset, RESET_WIDTH, RESET_HEIGHT, null);
		g.drawImage(scoresTitle, SCORES_TITLE_WIDTH, SCORES_TITLE_HEIGHT, null);

	}

	public void showScoreboard() {
		scoreListPanel.removeAll();

		scoreboard = Scoreboard.getScoreboard();

		for (int i = 0; i < 10; i++) {

			JLabel temp = new JLabel(scoreboard.get(i).stringForView());
			temp.setFont(new Font(font.getFontName(), Font.TRUETYPE_FONT, 35));
			scoreListPanel.add(temp);

		}

		scoreListPanel.setBounds(310, 200, 800, 800);
		scoreListPanel.setOpaque(false);
		scoreListPanel.setVisible(true);

		add(scoreListPanel);

		revalidate();
	}

	public static void refreshScoreboard() {
		scoreListPanel.removeAll();
		scoreboard = Scoreboard.getScoreboard();

		for (int i = 0; i < 10; i++) {

			JLabel temp = new JLabel(scoreboard.get(i).stringForView());
			temp.setFont(new Font(font.getFontName(), Font.TRUETYPE_FONT, 35));
			scoreListPanel.add(temp);

		}

		scoreListPanel.setBounds(310, 200, 800, 800);
		scoreListPanel.setOpaque(false);
		scoreListPanel.setVisible(true);
	}

}
