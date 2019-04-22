package it.unical.mat.igpe.ZombieCraft.Utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Settings {

	public final static String[] resolutions = { "640x480", "800x600", "1024x768", "1280x720", "1366x768",
			"1920x1080" };

	public static String[] options = Settings.loadOptions();

	public static String getSavePath() {

		return System.getProperty("user.home") + "/Documents/";

	}

	public static void saveOptions() {

		FileWriter fw = null;
		try {
			fw = new FileWriter(new File(getSavePath() + "Options.conf"), false);

			BufferedWriter bw = new BufferedWriter(fw);

			for (int i = 0; i < options.length; i++) {

				bw.write(options[i] + "\r\n");
			}

			bw.flush();
			bw.close();

		} catch (IOException e) {
			System.err.println("File non trovato");
		}

	}

	public static String[] loadOptions() {
		BufferedReader br = null;
		String sCurrentLine = null;
		File optionsFile = new File(getSavePath() + "Options.conf");
		if (!optionsFile.exists()) {

			try {
				optionsFile.createNewFile();
				options = new String[3];

				options[0] = "Player1";
				options[1] = "3";
				options[2] = "Yes";

				saveOptions();

				return options;
			} catch (IOException e) {
				System.err.println("SCHIFO");
			}

		}

		String[] options = new String[3];
		try {
			br = new BufferedReader(new FileReader(optionsFile));

			int i = 0;
			while ((sCurrentLine = br.readLine()) != null) {

				options[i] = sCurrentLine;
				i++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		return options;
	}
}
