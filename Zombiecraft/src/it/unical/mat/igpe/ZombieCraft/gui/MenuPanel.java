package it.unical.mat.igpe.ZombieCraft.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.UIManager;

import it.unical.mat.igpe.ZombieCraft.GameManager;
import it.unical.mat.igpe.ZombieCraft.Utilities.EnumPanel;
import it.unical.mat.igpe.ZombieCraft.Utilities.ImageLoader;

public class MenuPanel extends JPanel {
	public JFileChooser modifyChooser;
	File mapDirectory = new File(System.getProperty("user.home") + "/Documents/ZombieMap");

	private static Image Single_Player_Button = ImageLoader.getImageSingle_Player();
	private static Image Multi_Player_Button = ImageLoader.getImageMulti_Player();
	private static Image Options_Button = ImageLoader.getImageOptions();
	private static Image Editor_Button = ImageLoader.getImageEditor();
	private static Image Exit_Button = ImageLoader.getImageExit();
	private static Image Title = ImageLoader.getImageTitle();
	private static Image Background = ImageLoader.getImageBackground();

	private static int BACKGROUND_WIDTH = 0;
	private static int BACKGROUND_HEIGHT = 0;

	private static int TITLE_WIDTH = 100;
	private static int TITLE_HEIGHT = 50;

	private static int SINGLE_PLAYER_WIDTH = 75;
	private static int SINGLE_PLAYER_HEIGHT = 443;

	private static int MULTI_PLAYER_WIDTH = 649;
	private static int MULTI_PLAYER_HEIGHT = 443;

	private static int OPTIONS_WIDTH = 649;
	private static int OPTIONS_HEIGHT = 543;

	private static int EDITOR_WIDTH = 75;
	private static int EDITOR_HEIGHT = 543;

	private static int EXIT_WIDTH = 362;
	private static int EXIT_HEIGHT = 643;

	MainFrame frame;

	public MenuPanel(MainFrame frame) {
		UIManager.put("FileChooser.readOnly", Boolean.TRUE);
		modifyChooser = new JFileChooser();

		modifyChooser.setDialogTitle("Choose Map");
		if (!mapDirectory.exists())
			mapDirectory.mkdirs();
		modifyChooser.setCurrentDirectory(mapDirectory);

		this.frame = frame;

		setPreferredSize(new Dimension(1024, 768));
		// setBackground(Color.DARK_GRAY);

		requestFocus();

		// setLayout(new GridBagLayout());
		// setLayout(new BorderLayout());

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getX() >= EXIT_WIDTH && e.getX() <= EXIT_WIDTH + ImageLoader.getImageExit().getWidth(null)
						&& e.getY() >= EXIT_HEIGHT
						&& e.getY() <= EXIT_HEIGHT + ImageLoader.getImageExit().getHeight(null)) {
					System.exit(0);
				}
				if (e.getX() >= SINGLE_PLAYER_WIDTH
						&& e.getX() <= SINGLE_PLAYER_WIDTH + ImageLoader.getImageSingle_Player().getWidth(null)
						&& e.getY() >= SINGLE_PLAYER_HEIGHT
						&& e.getY() <= SINGLE_PLAYER_HEIGHT + ImageLoader.getImageSingle_Player().getHeight(null)) {
					int option = modifyChooser.showOpenDialog(frame);
					if (option == JFileChooser.APPROVE_OPTION) {
						File selectedMap = modifyChooser.getSelectedFile();
						new GameManager().run(selectedMap.getAbsolutePath());
					}
				}
				if (e.getX() >= EDITOR_WIDTH
						&& e.getX() <= EDITOR_WIDTH + ImageLoader.getImageSingle_Player().getWidth(null)
						&& e.getY() >= EDITOR_HEIGHT
						&& e.getY() <= EDITOR_HEIGHT + ImageLoader.getImageSingle_Player().getHeight(null)) {

					frame.DrawPanel(EnumPanel.EDITOR_MENU_PANEL);
				}

				if (e.getX() >= OPTIONS_WIDTH
						&& e.getX() <= OPTIONS_WIDTH + ImageLoader.getImageOptions().getWidth(null)
						&& e.getY() >= OPTIONS_HEIGHT
						&& e.getY() <= OPTIONS_HEIGHT + ImageLoader.getImageOptions().getHeight(null)) {
					frame.DrawPanel(EnumPanel.OPTION_MENU_PANEL);
				}

				if (e.getX() >= MULTI_PLAYER_WIDTH
						&& e.getX() <= MULTI_PLAYER_WIDTH + ImageLoader.getImageMulti_Player().getWidth(null)
						&& e.getY() >= MULTI_PLAYER_HEIGHT
						&& e.getY() <= MULTI_PLAYER_HEIGHT + ImageLoader.getImageMulti_Player().getHeight(null)) {
					frame.DrawPanel(EnumPanel.MultiPlayer_MENU_PANEL);
				}
			}

		});

		addMouseMotionListener(new MouseMotionAdapter() {

			@Override
			public void mouseMoved(MouseEvent e) {
				// Single_Player Start
				if (e.getX() >= SINGLE_PLAYER_WIDTH
						&& e.getX() <= SINGLE_PLAYER_WIDTH + ImageLoader.getImageSingle_Player().getWidth(null)
						&& e.getY() >= SINGLE_PLAYER_HEIGHT
						&& e.getY() <= SINGLE_PLAYER_HEIGHT + ImageLoader.getImageSingle_Player().getHeight(null)) {

					Single_Player_Button = ImageLoader.getImageSingle_Player_Pressed();

				} else
					Single_Player_Button = ImageLoader.getImageSingle_Player();
				// Single_Player End

				// Mutli_Player Start
				if (e.getX() >= MULTI_PLAYER_WIDTH
						&& e.getX() <= MULTI_PLAYER_WIDTH + ImageLoader.getImageMulti_Player().getWidth(null)
						&& e.getY() >= MULTI_PLAYER_HEIGHT
						&& e.getY() <= MULTI_PLAYER_HEIGHT + ImageLoader.getImageMulti_Player().getHeight(null)) {

					Multi_Player_Button = ImageLoader.getImageMulti_Player_Pressed();

				} else
					Multi_Player_Button = ImageLoader.getImageMulti_Player();
				// Multi_Player End

				// Editor Start
				if (e.getX() >= EDITOR_WIDTH && e.getX() <= EDITOR_WIDTH + ImageLoader.getImageEditor().getWidth(null)
						&& e.getY() >= EDITOR_HEIGHT
						&& e.getY() <= EDITOR_HEIGHT + ImageLoader.getImageEditor().getHeight(null)) {

					Editor_Button = ImageLoader.getImageEditor_Pressed();

				} else
					Editor_Button = ImageLoader.getImageEditor();
				// Editor End

				// Options Start
				if (e.getX() >= OPTIONS_WIDTH
						&& e.getX() <= OPTIONS_WIDTH + ImageLoader.getImageOptions().getWidth(null)
						&& e.getY() >= OPTIONS_HEIGHT
						&& e.getY() <= OPTIONS_HEIGHT + ImageLoader.getImageOptions().getHeight(null)) {

					Options_Button = ImageLoader.getImageOptions_Pressed();

				} else
					Options_Button = ImageLoader.getImageOptions();
				// Options End

				// Exit Start
				if (e.getX() >= EXIT_WIDTH && e.getX() <= EXIT_WIDTH + ImageLoader.getImageExit().getWidth(null)
						&& e.getY() >= EXIT_HEIGHT
						&& e.getY() <= EXIT_HEIGHT + ImageLoader.getImageExit().getHeight(null)) {

					Exit_Button = ImageLoader.getImageExit_Pressed();

				} else
					Exit_Button = ImageLoader.getImageExit();
				// Exit End

				repaint();
			}

		});
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.drawImage(Background, BACKGROUND_WIDTH, BACKGROUND_HEIGHT, null);

		g.drawImage(Title, TITLE_WIDTH, TITLE_HEIGHT, null);

		g.drawImage(Single_Player_Button, SINGLE_PLAYER_WIDTH, SINGLE_PLAYER_HEIGHT, null);
		g.drawImage(Multi_Player_Button, MULTI_PLAYER_WIDTH, MULTI_PLAYER_HEIGHT, null);
		g.drawImage(Editor_Button, EDITOR_WIDTH, EDITOR_HEIGHT, null);
		g.drawImage(Options_Button, OPTIONS_WIDTH, OPTIONS_HEIGHT, null);
		g.drawImage(Exit_Button, EXIT_WIDTH, EXIT_HEIGHT, null);

	}
}
