package it.unical.mat.igpe.ZombieCraft.Data.Enemies;

import it.unical.mat.igpe.ZombieCraft.Data.Player;

public abstract class Enemy {

	// private boolean dead;
	private int healthPoints;
	private float moveSpeed;
	private int damage;
	private float timeBetweenAttacks, timeCounter;
	private Player target;
	private float x, y, z;

	public float getTimeBetweenAttacks() {
		return timeBetweenAttacks;
	}

	public void setTimeBetweenAttacks(float timeBetweenAttacks) {
		this.timeBetweenAttacks = timeBetweenAttacks;
	}

	public float getMoveSpeed() {
		return moveSpeed;
	}

	public void setMoveSpeed(float moveSpeed) {
		this.moveSpeed = moveSpeed;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public int getHealthPoints() {
		return healthPoints;
	}

	public void setHealthPoints(int healthPoints) {
		this.healthPoints = healthPoints;
	}

	public void decreaseHealthPoints(int healthPoints) {
		this.healthPoints -= healthPoints;
	}

	public float getTimeCounter() {
		return timeCounter;
	}

	public void increaseTimeCounter(float timeCounter) {
		this.timeCounter += timeCounter;
	}

	public void resetTimeCounter() {
		if (timeCounter >= timeBetweenAttacks)
			timeCounter = 0;
	}

	public boolean canAttack() {
		return timeCounter >= timeBetweenAttacks;
	}

	// public boolean isDead() {
	// return dead;
	// }
	//
	// public void setDead(boolean dead) {
	// this.dead = dead;
	// }

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}

	public Player getTarget() {
		return target;
	}

	public void setTarget(Player player) {
		target = player;
	}

	public void followTarget() {
		if (getZ() < target.getZ())
			setZ(getZ() + 1f);
		else
			setZ(getZ() - 1f);
		if (getY() < target.getY())
			setY(getY() + 1f);
		else
			setY(getY() - 1f);

		if (getX() < target.getX())
			setX(getX() + 1f);
		else
			setX(getX() - 1f);
	}

}
