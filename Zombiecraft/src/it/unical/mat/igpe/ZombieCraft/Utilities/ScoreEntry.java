package it.unical.mat.igpe.ZombieCraft.Utilities;

public class ScoreEntry {
	private final String name;
	private final String score;

	public ScoreEntry(String buffer) {
		int separatorIndex = buffer.indexOf(' ');
		score = buffer.substring(0, separatorIndex);
		name = buffer.substring(separatorIndex + 1);
	}

	public ScoreEntry(String name, String score) {
		this.name = name;
		this.score = score;
	}

	public String getName() {
		return name;
	}

	public String getScore() {
		return score;
	}

	public String stringForView() {
		return score + "         " + name;
	}

	public String stringForFile() {
		return score + " " + name;
	}
}
