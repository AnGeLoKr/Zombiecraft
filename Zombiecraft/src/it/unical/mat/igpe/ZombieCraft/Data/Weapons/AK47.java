package it.unical.mat.igpe.ZombieCraft.Data.Weapons;

public class AK47 extends Weapon {

	public AK47() {

		setAmmoPerClip(32);
		setAmmo(240);
		super.setDamage(30);
		name = "AK-47";
	}

	@Override
	public int getMaxClipAmmo() {
		return 32;
	}

	@Override
	public int getMaxAmmo() {
		return 240;
	}

	@Override
	public void setAmmo(int value) {
		ammo += value;
		if (ammo > 240)
			ammo = 240;
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

	@Override
	public float getRatio() {
		return 0.13F;
	}

}
