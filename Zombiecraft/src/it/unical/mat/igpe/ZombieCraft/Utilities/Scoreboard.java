package it.unical.mat.igpe.ZombieCraft.Utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Scoreboard {

	private static File scoreboardFile;
	private static List<ScoreEntry> scoreboard = new ArrayList<>();

	public static String getSavePath() {

		return System.getProperty("user.home") + "/Documents/";

	}

	public static List<ScoreEntry> getScoreboard() {
		loadScoreBoard();
		return scoreboard;
	}

	public static void loadScoreBoard() {
		scoreboardFile = new File(getSavePath() + "scoreboard.txt");
		if (!scoreboardFile.exists()) {
			try {
				scoreboardFile.createNewFile();

				for (int i = 0; i < 10; i++) {
					ScoreEntry scoreTemp = new ScoreEntry("aaaaaa", "00000");
					scoreboard.add(i, scoreTemp);

				}

				saveScores();
			} catch (IOException e) {
				e.printStackTrace();
				System.err.println("Cannot read scoreboard file");
			}

		}

		if (scoreboardFile.exists()) {
			try {
				List<String> allLines = Files.readAllLines(scoreboardFile.toPath());
				for (String buffer : allLines) {
					scoreboard.add(new ScoreEntry(buffer));
				}
			} catch (IOException e1) {
				e1.printStackTrace();
				System.err.println("Cannot read scoreboard file");

			}

		} else {
			try {
				scoreboardFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				System.err.println("Cannot read scoreboard file");
			}
		}
	}

	private static void saveScores() {
		FileWriter fw = null;
		try {
			fw = new FileWriter(new File(getSavePath() + "Scoreboard.txt"), false);

			BufferedWriter bw = new BufferedWriter(fw);

			for (int i = 0; i < 10; i++) {

				bw.write(scoreboard.get(i).stringForFile() + "\r\n");
			}

			bw.flush();
			bw.close();

		} catch (IOException e) {
			System.err.println("File non trovato");
		}

	}

	public static void addScore(int score, String name) {
		String scoreWithZero = "";

		if (score < 10)
			scoreWithZero = "0000" + score;
		else if (score < 100)
			scoreWithZero = "000" + score;
		else if (score < 1000)
			scoreWithZero = "00" + score;
		else if (score < 10000)
			scoreWithZero = "0" + score;
		else
			scoreWithZero = "" + score;

		ScoreEntry newScore = new ScoreEntry(name, scoreWithZero);

		scoreboard.set(9, newScore);

		sortScore();

		saveScores();
	}

	private static void sortScore() {
		ScoreEntry entryTemp = new ScoreEntry("0", "a");

		for (int i = 0; i <= 9; i++) {

			for (int j = i + 1; j < 10; j++) {

				if (Integer.parseInt(scoreboard.get(i).getScore()) < Integer.parseInt(scoreboard.get(j).getScore())) {
					entryTemp = scoreboard.get(i);
					scoreboard.set(i, scoreboard.get(j));
					scoreboard.set(j, entryTemp);
				}

			}
		}

	}

	public static void resetScoreboard() {
		ScoreEntry temp = new ScoreEntry("aaa", "00000");

		for (int i = 0; i < 10; i++)
			scoreboard.set(i, temp);

		saveScores();
	}
}
