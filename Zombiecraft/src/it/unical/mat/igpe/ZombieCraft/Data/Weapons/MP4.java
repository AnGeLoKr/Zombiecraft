package it.unical.mat.igpe.ZombieCraft.Data.Weapons;

public class MP4 extends Weapon {

	public MP4() {

		setAmmoPerClip(32);
		setAmmo(320);
		name = "MP4";
	}

	@Override
	public int getMaxClipAmmo() {

		return 32;
	}

	@Override
	public int getMaxAmmo() {
		return 320;
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
		return 3;
	}

}
