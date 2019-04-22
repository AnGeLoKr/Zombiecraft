package it.unical.mat.igpe.ZombieCraft.Utilities;

import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageLoader {
	private static Image scores;
	private static Image scores_pressed;

	private static Image multi_player;
	private static Image multi_player_pressed;

	private static Image single_player;
	private static Image single_player_pressed;

	private static Image options;
	private static Image options_pressed;

	private static Image editor;
	private static Image editor_pressed;

	private static Image exit;
	private static Image exit_pressed;

	private static Image back;
	private static Image back_pressed;

	private static Image save;
	private static Image save_pressed;

	private static Image reset;
	private static Image reset_pressed;

	private static Image newButton;
	private static Image newButton_pressed;

	private static Image modify;
	private static Image modify_pressed;

	private static Image background;
	private static Image background_blur;
	private static Image title;
	private static Image optionsTitle;
	private static Image scoresTitle;
	private static Image editorTitle;
	private static Image multiplayerTitle;

	private static Image join;
	private static Image join_pressed;

	private static Image host;
	private static Image host_pressed;

	private static Image stop;
	private static Image stop_pressed;

	static {
		try {
			stop_pressed = ImageIO
					.read(Thread.currentThread().getContextClassLoader().getResource("img/menu/Stop_Pressed.png"));
			stop = ImageIO.read(Thread.currentThread().getContextClassLoader().getResource("img/menu/Stop.png"));
			host_pressed = ImageIO
					.read(Thread.currentThread().getContextClassLoader().getResource("img/menu/Host_Pressed.png"));
			host = ImageIO.read(Thread.currentThread().getContextClassLoader().getResource("img/menu/Host.png"));
			join_pressed = ImageIO
					.read(Thread.currentThread().getContextClassLoader().getResource("img/menu/Join_Pressed.png"));
			join = ImageIO.read(Thread.currentThread().getContextClassLoader().getResource("img/menu/Join.png"));
			multiplayerTitle = ImageIO
					.read(Thread.currentThread().getContextClassLoader().getResource("img/menu/MultiplayerTitle.png"));
			editorTitle = ImageIO
					.read(Thread.currentThread().getContextClassLoader().getResource("img/menu/EditorTitle.png"));

			reset = ImageIO.read(Thread.currentThread().getContextClassLoader().getResource("img/menu/Reset.png"));

			reset_pressed = ImageIO
					.read(Thread.currentThread().getContextClassLoader().getResource("img/menu/Reset_Pressed.png"));

			scores = ImageIO.read(Thread.currentThread().getContextClassLoader().getResource("img/menu/Scores.png"));

			scores_pressed = ImageIO
					.read(Thread.currentThread().getContextClassLoader().getResource("img/menu/Scores_Pressed.png"));

			scoresTitle = ImageIO
					.read(Thread.currentThread().getContextClassLoader().getResource("img/menu/ScoresTitle.png"));

			optionsTitle = ImageIO
					.read(Thread.currentThread().getContextClassLoader().getResource("img/menu/OptionsTitle.png"));

			background = ImageIO
					.read(Thread.currentThread().getContextClassLoader().getResource("img/menu/Background.png"));

			background_blur = ImageIO
					.read(Thread.currentThread().getContextClassLoader().getResource("img/menu/Background_Blur.png"));

			title = ImageIO.read(Thread.currentThread().getContextClassLoader().getResource("img/menu/Title.png"));

			single_player = ImageIO
					.read(Thread.currentThread().getContextClassLoader().getResource("img/menu/Single_Player.png"));

			single_player_pressed = ImageIO.read(
					Thread.currentThread().getContextClassLoader().getResource("img/menu/Single_Player_Pressed.png"));

			multi_player = ImageIO
					.read(Thread.currentThread().getContextClassLoader().getResource("img/menu/Multi_Player.png"));

			multi_player_pressed = ImageIO.read(
					Thread.currentThread().getContextClassLoader().getResource("img/menu/Multi_Player_Pressed.png"));

			options = ImageIO.read(Thread.currentThread().getContextClassLoader().getResource("img/menu/Options.png"));

			options_pressed = ImageIO
					.read(Thread.currentThread().getContextClassLoader().getResource("img/menu/Options_Pressed.png"));

			editor = ImageIO.read(Thread.currentThread().getContextClassLoader().getResource("img/menu/Editor.png"));

			editor_pressed = ImageIO
					.read(Thread.currentThread().getContextClassLoader().getResource("img/menu/Editor_Pressed.png"));

			exit = ImageIO.read(Thread.currentThread().getContextClassLoader().getResource("img/menu/Exit.png"));

			exit_pressed = ImageIO
					.read(Thread.currentThread().getContextClassLoader().getResource("img/menu/Exit_Pressed.png"));

			back = ImageIO.read(Thread.currentThread().getContextClassLoader().getResource("img/menu/Back.png"));

			back_pressed = ImageIO
					.read(Thread.currentThread().getContextClassLoader().getResource("img/menu/Back_Pressed.png"));

			save = ImageIO.read(Thread.currentThread().getContextClassLoader().getResource("img/menu/Save.png"));
			save_pressed = ImageIO
					.read(Thread.currentThread().getContextClassLoader().getResource("img/menu/Save_Pressed.png"));

			newButton = ImageIO.read(Thread.currentThread().getContextClassLoader().getResource("img/menu/New.png"));
			newButton_pressed = ImageIO
					.read(Thread.currentThread().getContextClassLoader().getResource("img/menu/New_Pressed.png"));

			modify = ImageIO.read(Thread.currentThread().getContextClassLoader().getResource("img/menu/Modify.png"));
			modify_pressed = ImageIO
					.read(Thread.currentThread().getContextClassLoader().getResource("img/menu/Modify_Pressed.png"));
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public static Image getImageEditorTitle() {
		return editorTitle;
	}

	public static Image getImageModify() {
		return modify;
	}

	public static Image getImageModifyPressed() {
		return modify_pressed;
	}

	public static Image getImageNewButton() {
		return newButton;
	}

	public static Image getImageNewButton_Pressed() {
		return newButton_pressed;
	}

	public static Image getImageScores() {
		return scores;
	}

	public static Image getImageScores_Pressed() {
		return scores_pressed;
	}

	public static Image getImageBackground() {
		return background;
	}

	public static Image getImageBackground_Blur() {
		return background_blur;
	}

	public static Image getImageTitle() {
		return title;
	}

	public static Image getImageSingle_Player_Pressed() {

		return single_player_pressed;
	}

	public static Image getImageSingle_Player() {
		return single_player;
	}

	public static Image getImageMulti_Player() {
		return multi_player;
	}

	public static Image getImageMulti_Player_Pressed() {
		return multi_player_pressed;
	}

	public static Image getImageOptions() {
		return options;
	}

	public static Image getImageOptions_Pressed() {
		return options_pressed;
	}

	public static Image getImageEditor() {
		return editor;
	}

	public static Image getImageEditor_Pressed() {
		return editor_pressed;
	}

	public static Image getImageExit() {
		return exit;
	}

	public static Image getImageExit_Pressed() {
		return exit_pressed;
	}

	public static Image getImageBack() {
		return back;
	}

	public static Image getImageBack_Pressed() {
		return back_pressed;
	}

	public static Image getImageSave() {
		return save;
	}

	public static Image getImageSave_Pressed() {
		return save_pressed;
	}

	public static Image getImageOptionsTitle() {
		return optionsTitle;
	}

	public static Image getImageScoresTitle() {
		return scoresTitle;
	}

	public static Image getImageReset() {
		return reset;
	}

	public static Image getImageReset_Pressed() {
		return reset_pressed;
	}

	public static Image getImageMultiplayerTitle() {
		return multiplayerTitle;
	}

	public static Image getImageJoin() {
		return join;
	}

	public static Image getImageJoin_Pressed() {
		return join_pressed;
	}

	public static Image getImageHost() {
		return host;
	}

	public static Image getImageHost_Pressed() {
		return host_pressed;
	}

	public static Image getImageStop() {
		return stop;
	}

	public static Image getImageStop_Pressed() {
		return stop_pressed;
	}
}
