package it.unical.mat.igpe.ZombieCraft.Data.Spawner;

public abstract class Spawner {

	// maxSlots rappresenta il numero massimo di posti occupabili e current
	// slots indica i posti occupati
	private int maxSlots, currentSlots;
	private float spawnTime;

	// this method instantiate a new object
	// public abstract void instantiate();

	public Spawner() {
		this.currentSlots = 0;
		this.maxSlots = 10;
		this.spawnTime = 2;
	}

	public void decreaseCurrentSlots() {

		if (currentSlots > 0)
			currentSlots--;

	}

	public int getCurrentSlots() {
		return currentSlots;
	}

	public void setCurrentSlots(int currentSlots) {
		this.currentSlots = currentSlots;
	}

	public int getMaxSlots() {
		return maxSlots;
	}

	public void setMaxSlots(int maxSlots) {
		this.maxSlots = maxSlots;
	}

	public float getSpawnTime() {
		return spawnTime;
	}

	public void setSpawnTime(int spawnTime) {
		this.spawnTime = spawnTime;
	}

}
