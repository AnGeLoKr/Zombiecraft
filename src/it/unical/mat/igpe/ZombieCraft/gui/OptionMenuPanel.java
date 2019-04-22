package it.unical.mat.igpe.ZombieCraft.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;

import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import it.unical.mat.igpe.ZombieCraft.Utilities.EnumPanel;
import it.unical.mat.igpe.ZombieCraft.Utilities.ImageLoader;
import it.unical.mat.igpe.ZombieCraft.Utilities.Settings;

public class OptionMenuPanel extends JPanel {
	private static Image Background_Blur = ImageLoader.getImageBackground_Blur();
	private static Image Back = ImageLoader.getImageBack();

	private static Image Save = ImageLoader.getImageSave();

	private static Image OptionsTitle = ImageLoader.getImageOptionsTitle();

	private static Image scores = ImageLoader.getImageScores();

	private static int OPTIONS_TITLE_WIDTH = 252;
	private static int OPTIONS_TITLE_HEIGHT = 50;

	private static int BACK_WIDTH = 75;
	private static int BACK_HEIGHT = 663;

	private static int SAVE_WIDTH = 649;
	private static int SAVE_HEIGHT = 663;

	private static int SCORES_WIDTH = 362;
	private static int SCORES_HEIGHT = 563;

	MainFrame frame;
	JLabel lblnickname;
	JLabel lblresolution;
	JLabel lblfullscreen;
	JLabel lblSaveSuccess;

	JTextField txtnickname;
	JComboBox<String> ResolutionList;
	JRadioButton radioFullYes;
	JRadioButton radioFullNo;

	public OptionMenuPanel(MainFrame frame) {

		Font font = null;
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, new File("resources/font/Minecrafter.Reg.ttf"));
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(font);

		} catch (Exception e) {
			// Ignore
		}

		this.frame = frame;

		this.setLayout(null);
		setPreferredSize(new Dimension(1024, 768));

		lblSaveSuccess = new JLabel("Options saved");
		lblSaveSuccess.setFont(new Font(font.getFontName(), Font.TRUETYPE_FONT, 35));
		Dimension saveSuccessSize = lblSaveSuccess.getPreferredSize();

		lblnickname = new JLabel("NICKNAME");
		lblnickname.setFont(new Font(font.getFontName(), Font.TRUETYPE_FONT, 35));
		Dimension nicknameSize = lblnickname.getPreferredSize();

		lblresolution = new JLabel("Resolution");
		lblresolution.setFont(new Font(font.getFontName(), Font.TRUETYPE_FONT, 35));
		Dimension resolutionSize = lblresolution.getPreferredSize();

		lblfullscreen = new JLabel("Fullscreen");
		lblfullscreen.setFont(new Font(font.getFontName(), Font.TRUETYPE_FONT, 35));
		Dimension fullscreenSize = lblfullscreen.getPreferredSize();

		txtnickname = new JTextField(Settings.options[0]);
		txtnickname.setFont(new Font(font.getFontName(), Font.TRUETYPE_FONT, 35));
		txtnickname.setSize(350, txtnickname.getPreferredSize().height);
		Dimension txtnickSize = txtnickname.getSize();

		ResolutionList = new JComboBox<>(Settings.resolutions);
		ResolutionList.setFont(new Font(font.getFontName(), Font.TRUETYPE_FONT, 35));
		ResolutionList.setSelectedIndex(Settings.resolutions.length - 1);
		ResolutionList.setSize(350, txtnickname.getPreferredSize().height);
		Dimension ResolutionListSize = ResolutionList.getSize();
		ResolutionList.setSelectedIndex(Integer.parseInt(Settings.options[1]));

		radioFullYes = new JRadioButton("Yes");
		radioFullYes.setFont(new Font(font.getFontName(), Font.TRUETYPE_FONT, 35));
		Dimension RadioFullYesSize = radioFullYes.getPreferredSize();
		radioFullYes.setOpaque(false);

		radioFullNo = new JRadioButton("No");
		radioFullNo.setFont(new Font(font.getFontName(), Font.TRUETYPE_FONT, 35));
		Dimension RadioFullNoSize = radioFullNo.getPreferredSize();
		radioFullNo.setOpaque(false);

		ButtonGroup Radiogroup = new ButtonGroup();
		Radiogroup.add(radioFullYes);
		Radiogroup.add(radioFullNo);

		if (Settings.options[2].equals("Yes"))
			radioFullYes.doClick();
		else
			radioFullNo.doClick();

		lblnickname.setBounds(200, 270, nicknameSize.width, nicknameSize.height);
		lblresolution.setBounds(200, 345, resolutionSize.width, resolutionSize.height);

		lblfullscreen.setBounds(200, 420, fullscreenSize.width, fullscreenSize.height);

		txtnickname.setBounds(470, 270, txtnickSize.width, txtnickSize.height);

		ResolutionList.setBounds(470, 345, ResolutionListSize.width, ResolutionListSize.height);

		radioFullYes.setBounds(470, 420, RadioFullYesSize.width, RadioFullYesSize.height);

		radioFullNo.setBounds(580, 420, RadioFullNoSize.width, RadioFullNoSize.height);

		lblSaveSuccess.setBounds(360, 495, saveSuccessSize.width, saveSuccessSize.height);

		add(lblnickname);
		add(lblresolution);
		add(lblfullscreen);

		add(txtnickname);
		add(ResolutionList);
		add(radioFullYes);
		add(radioFullNo);
		add(lblSaveSuccess);
		requestFocus();

		addMouseMotionListener(new MouseMotionAdapter() {

			@Override
			public void mouseMoved(MouseEvent e) {
				// Back Start
				if (e.getX() >= BACK_WIDTH && e.getX() <= BACK_WIDTH + ImageLoader.getImageBack().getWidth(null)
						&& e.getY() >= BACK_HEIGHT
						&& e.getY() <= BACK_HEIGHT + ImageLoader.getImageBack().getHeight(null)) {

					Back = ImageLoader.getImageBack_Pressed();

				} else
					Back = ImageLoader.getImageBack();
				// Back End

				// Scores Start
				if (e.getX() >= SCORES_WIDTH && e.getX() <= SCORES_WIDTH + ImageLoader.getImageScores().getWidth(null)
						&& e.getY() >= SCORES_HEIGHT
						&& e.getY() <= SCORES_HEIGHT + ImageLoader.getImageScores().getHeight(null)) {

					scores = ImageLoader.getImageScores_Pressed();

				} else
					scores = ImageLoader.getImageScores();
				// Scores End

				// Save Start
				if (e.getX() >= SAVE_WIDTH && e.getX() <= SAVE_WIDTH + ImageLoader.getImageSave().getWidth(null)
						&& e.getY() >= SAVE_HEIGHT
						&& e.getY() <= SAVE_HEIGHT + ImageLoader.getImageSave().getHeight(null)) {

					Save = ImageLoader.getImageSave_Pressed();
				} else
					Save = ImageLoader.getImageSave();
				repaint();
			}
		});

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				// pulsante back
				if (e.getX() >= BACK_WIDTH && e.getX() <= BACK_WIDTH + ImageLoader.getImageBack().getWidth(null)
						&& e.getY() >= BACK_HEIGHT
						&& e.getY() <= BACK_HEIGHT + ImageLoader.getImageBack().getHeight(null)) {
					frame.DrawPanel(EnumPanel.MENU_PANEL);
				}
				// pulsante salva opzioni

				if (e.getX() >= SAVE_WIDTH && e.getX() <= SAVE_WIDTH + ImageLoader.getImageSave().getWidth(null)
						&& e.getY() >= SAVE_HEIGHT
						&& e.getY() <= SAVE_HEIGHT + ImageLoader.getImageSave().getHeight(null)) {

					saveCurrentOptions();
					lblSaveSuccess.setVisible(true);

				}

				// Scores Start
				if (e.getX() >= SCORES_WIDTH && e.getX() <= SCORES_WIDTH + ImageLoader.getImageScores().getWidth(null)
						&& e.getY() >= SCORES_HEIGHT
						&& e.getY() <= SCORES_HEIGHT + ImageLoader.getImageScores().getHeight(null)) {

					frame.DrawPanel(EnumPanel.SCORES_MENU_PANEL);

				}

			}

		});
	}

	private void saveCurrentOptions() {
		Settings.options[0] = txtnickname.getText();
		Settings.options[1] = Integer.toString(ResolutionList.getSelectedIndex());

		if (radioFullYes.isSelected())
			Settings.options[2] = "Yes";
		else
			Settings.options[2] = "No";

		Settings.saveOptions();

	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(Background_Blur, 0, 0, null);

		g.drawImage(OptionsTitle, OPTIONS_TITLE_WIDTH, OPTIONS_TITLE_HEIGHT, null);
		g.drawImage(Back, BACK_WIDTH, BACK_HEIGHT, null);
		g.drawImage(Save, SAVE_WIDTH, BACK_HEIGHT, null);
		g.drawImage(scores, SCORES_WIDTH, SCORES_HEIGHT, null);

	}

	public void refreshSettings() {
		txtnickname.setText(Settings.options[0]);
		ResolutionList.setSelectedIndex(Integer.parseInt(Settings.options[1]));

		if (Settings.options[2].equals("Yes"))
			radioFullYes.doClick();
		else
			radioFullNo.doClick();

		lblSaveSuccess.setVisible(false);

	}
}
