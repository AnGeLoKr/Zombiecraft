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

import it.unical.mat.igpe.ZombieCraft.Editor;
import it.unical.mat.igpe.ZombieCraft.Utilities.EnumPanel;
import it.unical.mat.igpe.ZombieCraft.Utilities.ImageLoader;

public class EditorMenuPanel extends JPanel {

	public JFileChooser modifyChooser;
	File mapDirectory = new File(System.getProperty("user.home") + "/Documents/ZombieMap");

	private static Image Background_Blur = ImageLoader.getImageBackground_Blur();
	private static Image New_Button = ImageLoader.getImageNewButton();
	private static Image Modify_Button = ImageLoader.getImageModify();
	private static Image Back = ImageLoader.getImageBack();

	private static Image editor_Title = ImageLoader.getImageEditorTitle();

	private static int EDITOR_TITLE_WIDTH = 271;
	private static int EDITOR_TITLE_HEIGHT = 50;

	private static int BACK_WIDTH = 362;
	private static int BACK_HEIGHT = 643;

	private static int MODIFY_WIDTH = 649;
	private static int MODIFY_HEIGHT = 443;

	private static int NEW_WIDTH = 75;
	private static int NEW_HEIGHT = 443;

	MainFrame frame;

	public EditorMenuPanel(MainFrame frame) {

		UIManager.put("FileChooser.readOnly", Boolean.TRUE);
		modifyChooser = new JFileChooser();

		modifyChooser.setDialogTitle("Choose Map");

		if (!mapDirectory.exists())
			mapDirectory.mkdirs();
		modifyChooser.setCurrentDirectory(mapDirectory);
		this.frame = frame;

		this.setLayout(null);
		setPreferredSize(new Dimension(1024, 768));

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

				// New Start
				if (e.getX() >= NEW_WIDTH && e.getX() <= NEW_WIDTH + ImageLoader.getImageNewButton().getWidth(null)
						&& e.getY() >= NEW_HEIGHT
						&& e.getY() <= NEW_HEIGHT + ImageLoader.getImageNewButton().getHeight(null)) {

					New_Button = ImageLoader.getImageNewButton_Pressed();

				} else
					New_Button = ImageLoader.getImageNewButton();
				// New End

				// Modify Start
				if (e.getX() >= MODIFY_WIDTH && e.getX() <= MODIFY_WIDTH + ImageLoader.getImageModify().getWidth(null)
						&& e.getY() >= MODIFY_HEIGHT
						&& e.getY() <= MODIFY_HEIGHT + ImageLoader.getImageModify().getHeight(null)) {

					Modify_Button = ImageLoader.getImageModifyPressed();

				} else
					Modify_Button = ImageLoader.getImageModify();
				// Modify End

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
				// Pulsante nuovo
				if (e.getX() >= NEW_WIDTH && e.getX() <= NEW_WIDTH + ImageLoader.getImageBack().getWidth(null)
						&& e.getY() >= NEW_HEIGHT
						&& e.getY() <= NEW_HEIGHT + ImageLoader.getImageBack().getHeight(null)) {

					int option = modifyChooser.showOpenDialog(frame);
					if (option == JFileChooser.APPROVE_OPTION) {
						File selectedMap = modifyChooser.getSelectedFile();
						String filePath = selectedMap.getAbsolutePath();
						if (selectedMap.exists()) {
							selectedMap.delete();
						}
						new Editor().run(filePath);
					}

				}

				// Pulsante modifica
				if (e.getX() >= MODIFY_WIDTH && e.getX() <= MODIFY_WIDTH + ImageLoader.getImageBack().getWidth(null)
						&& e.getY() >= MODIFY_HEIGHT
						&& e.getY() <= MODIFY_HEIGHT + ImageLoader.getImageBack().getHeight(null)) {
					int option = modifyChooser.showOpenDialog(frame);
					if (option == JFileChooser.APPROVE_OPTION) {
						File selectedMap = modifyChooser.getSelectedFile();
						new Editor().run(selectedMap.getAbsolutePath());
					}
				}
			}

		});
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(Background_Blur, 0, 0, null);
		g.drawImage(editor_Title, EDITOR_TITLE_WIDTH, EDITOR_TITLE_HEIGHT, null);
		g.drawImage(Back, BACK_WIDTH, BACK_HEIGHT, null);

		g.drawImage(Modify_Button, MODIFY_WIDTH, MODIFY_HEIGHT, null);
		g.drawImage(New_Button, NEW_WIDTH, NEW_HEIGHT, null);

	}
}
