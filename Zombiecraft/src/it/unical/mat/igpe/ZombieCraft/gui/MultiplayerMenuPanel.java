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

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import it.unical.mat.igpe.ZombieCraft.Client.ZombieCraftClient;
import it.unical.mat.igpe.ZombieCraft.Server.ZombieCraftServer;
import it.unical.mat.igpe.ZombieCraft.Utilities.EnumPanel;
import it.unical.mat.igpe.ZombieCraft.Utilities.ImageLoader;

public class MultiplayerMenuPanel extends JPanel {
	ZombieCraftServer server;
	ZombieCraftClient client;

	private static Image Background_Blur = ImageLoader.getImageBackground_Blur();

	private static Image join_Button = ImageLoader.getImageJoin();

	private static Image back = ImageLoader.getImageBack();

	private static Image multiplayer_Title = ImageLoader.getImageMultiplayerTitle();

	private static int MULTIPLAYER_TITLE_WIDTH = 121;
	private static int MULTIPLAYER_TITLE_HEIGHT = 50;

	private static int BACK_WIDTH = 75;
	private static int BACK_HEIGHT = 643;

	private static int JOIN_WIDTH = 649;
	private static int JOIN_HEIGHT = 643;

	MainFrame frame;
	JLabel lblIp;
	JLabel lblPort;
	JTextField txtIp;
	JTextField txtPort;

	public MultiplayerMenuPanel(MainFrame frame) {
		Font font = null;
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, new File("resources/font/Minercraftory.ttf"));
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(font);

		} catch (Exception e) {
			// Ignore
		}

		this.frame = frame;

		this.setLayout(null);
		setPreferredSize(new Dimension(1024, 768));

		lblIp = new JLabel("SERVER IP");
		lblIp.setFont(new Font(font.getFontName(), Font.TRUETYPE_FONT, 35));
		Dimension ipSize = lblIp.getPreferredSize();

		lblPort = new JLabel("SERVER PORT");
		lblPort.setFont(new Font(font.getFontName(), Font.TRUETYPE_FONT, 35));
		Dimension portSize = lblPort.getPreferredSize();

		txtIp = new JTextField("127.0.0.1");
		txtIp.setFont(new Font(font.getFontName(), Font.TRUETYPE_FONT, 35));
		txtIp.setSize(350, txtIp.getPreferredSize().height);
		txtIp.setHorizontalAlignment(JTextField.CENTER);
		Dimension txtIpSize = txtIp.getSize();

		txtPort = new JTextField("6143");
		txtPort.setFont(new Font(font.getFontName(), Font.TRUETYPE_FONT, 35));
		txtPort.setHorizontalAlignment(JTextField.CENTER);
		txtPort.setSize(350, txtPort.getPreferredSize().height);
		Dimension txtPortSize = txtPort.getSize();

		lblIp.setBounds(180, 320, ipSize.width, ipSize.height);
		lblPort.setBounds(180, 445, portSize.width, portSize.height);

		txtIp.setBounds(500, 320, txtIpSize.width, txtIpSize.height);

		txtPort.setBounds(500, 445, txtPortSize.width, txtPortSize.height);

		add(lblIp);
		add(lblPort);
		add(txtIp);
		add(txtPort);

		requestFocus();

		addMouseMotionListener(new MouseMotionAdapter() {

			@Override
			public void mouseMoved(MouseEvent e) {
				// Back Start
				if (e.getX() >= BACK_WIDTH && e.getX() <= BACK_WIDTH + ImageLoader.getImageBack().getWidth(null)
						&& e.getY() >= BACK_HEIGHT
						&& e.getY() <= BACK_HEIGHT + ImageLoader.getImageBack().getHeight(null)) {

					back = ImageLoader.getImageBack_Pressed();

				} else
					back = ImageLoader.getImageBack();
				// Back End

				// Join Start
				if (e.getX() >= JOIN_WIDTH && e.getX() <= JOIN_WIDTH + ImageLoader.getImageJoin().getWidth(null)
						&& e.getY() >= JOIN_HEIGHT
						&& e.getY() <= JOIN_HEIGHT + ImageLoader.getImageJoin().getHeight(null)) {

					join_Button = ImageLoader.getImageJoin_Pressed();

				} else
					join_Button = ImageLoader.getImageJoin();
				// New End
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

				// Pulsante JOIN
				if (e.getX() >= JOIN_WIDTH && e.getX() <= JOIN_WIDTH + ImageLoader.getImageJoin().getWidth(null)
						&& e.getY() >= JOIN_HEIGHT
						&& e.getY() <= JOIN_HEIGHT + ImageLoader.getImageJoin().getHeight(null)) {

					// INSERIRE IP DEL SERVER
					String ip = txtIp.getText();
					String port = txtPort.getText();
					// controla che sia un ip valido;
					// client = new ZombieCraftClient();
					String[] args = { ip, port };

					new Thread(new Runnable() {

						@Override
						public void run() {

							// new ZombieCraftClient().main(args);;
							// client = new ZombieCraftClient();
							// client.start();
							ZombieCraftClient.main(args);

						}

					}).start();
				}

			}

		});

	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(Background_Blur, 0, 0, null);
		g.drawImage(multiplayer_Title, MULTIPLAYER_TITLE_WIDTH, MULTIPLAYER_TITLE_HEIGHT, null);
		g.drawImage(back, BACK_WIDTH, BACK_HEIGHT, null);

		g.drawImage(join_Button, JOIN_WIDTH, JOIN_HEIGHT, null);

	}
}
