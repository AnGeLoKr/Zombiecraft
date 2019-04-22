package it.unical.mat.igpe.ZombieCraft.Data.Weapons;

public class DesertEagle extends Weapon {

	public DesertEagle() {

		setAmmoPerClip(9);
		setAmmo(120);
		name = "Desert Eagle";
	}

	@Override
	public int getMaxClipAmmo() {
		return 9;
	}

	@Override
	public int getMaxAmmo() {

		return 120;
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
		return 4;
	}

}
