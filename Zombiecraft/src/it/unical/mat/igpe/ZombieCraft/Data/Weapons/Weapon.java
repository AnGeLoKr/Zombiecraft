package it.unical.mat.igpe.ZombieCraft.Data.Weapons;

public abstract class Weapon {

	// numero di proiettili
	protected int ammo;
	// numero di proiettili nel caricatore
	private int ammoPerClip;

	// danno dell'arma
	private int damage;

	// cadenza di fuoco
	private float ratio;

	protected String name;

	public String getName() {
		return name;
	}

	public int getAmmo() {
		return ammo;
	}

	public void setAmmo(int ammo) {
		this.ammo = ammo;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public float getRatio() {
		return ratio;
	}

	public void setRatio(float ratio) {
		this.ratio = ratio;
	}

	public int getAmmoPerClip() {
		return ammoPerClip;
	}

	public void setAmmoPerClip(int ammoPerClip) {
		this.ammoPerClip = ammoPerClip;
	}

	// numero massimo capienza caricatore
	public abstract int getMaxClipAmmo();

	// numero massimo di colpi che si possono portare esclusi quelli nel
	// caricatore
	public abstract int getMaxAmmo();

	public abstract boolean shoot();

	public abstract float getRechargeTime();

	public boolean recharge() {

		int missedAmmo = getMaxClipAmmo() - ammoPerClip;

		if (missedAmmo <= ammo) {
			ammoPerClip += missedAmmo;
			ammo -= missedAmmo;
			return true;
		} else {
			ammoPerClip += ammo;
			ammo -= ammo;
			return true;
		}
	}

}
