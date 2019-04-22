package it.unical.mat.igpe.ZombieCraft.Data.Weapons;

public class Revolver extends Weapon {

	public Revolver() {

		setAmmoPerClip(6);
		setAmmo(60);
		name = "Revolver";

	}

	@Override
	public int getMaxClipAmmo() {
		return 6;
	}

	@Override
	public int getMaxAmmo() {
		return 60;
	}

	@Override
	public boolean shoot() {
		if (getAmmoPerClip() == 0)
			return false;

		setAmmoPerClip(getAmmoPerClip() - 1);
		return true;
	}

	@Override
	public float getRechargeTime() {
		return 6;
	}
}
