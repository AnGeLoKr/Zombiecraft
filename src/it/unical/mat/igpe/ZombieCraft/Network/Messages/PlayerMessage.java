package it.unical.mat.igpe.ZombieCraft.Network.Messages;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

@Serializable
public class PlayerMessage extends AbstractMessage {

	private int id;
	private int damage;
	private int healthIncreasement;
	private int healthDecreasement;
	private String name;
	private Vector3f position;
	private Quaternion rotation;
	private Vector3f shootPoint;
	private boolean shoot;
	private int pointsToAdd;
	private int ammoIncreasement;
	private boolean dead;
	private int points;
	private String action;
	private int healthPoints;
	private Vector3f weaponTranslation;
	private Quaternion weaponRotation;

	public PlayerMessage() {
		name = "";
		// position = Vector3f.NAN;
		rotation = Quaternion.ZERO;
		position = Vector3f.ZERO;
		shootPoint = Vector3f.NAN;
		weaponTranslation = Vector3f.NAN;
		weaponRotation = Quaternion.ZERO;
		action = "Idle";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Quaternion getRotation() {
		return rotation;
	}

	public void setRotation(Quaternion rotation) {
		this.rotation = rotation;
	}

	@Override
	public String toString() {
		return "PlayerName: " + name + " " + position + " " + rotation;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Vector3f getShootPoint() {
		return shootPoint;
	}

	public void setShootPoint(Vector3f shootPoint) {
		this.shootPoint = shootPoint;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public int getHealthDecreasement() {
		return healthDecreasement;
	}

	public void setHealthDecreasement(int damage) {
		this.healthDecreasement = damage;
	}

	public int getPointsToAdd() {
		return pointsToAdd;
	}

	public void addPoints(int points) {
		this.pointsToAdd = points;
	}
	
	public int getPoints() {
		return points;
	}
	
	public void setPoints(int points) {
		this.points = points;
	}

	public boolean isShoot() {
		return shoot;
	}

	public void setShoot(boolean shoot) {
		this.shoot = shoot;
	}

	public int getHealthIncreasement() {
		return healthIncreasement;
	}

	public void setHealthIncreasement(int healthIncreasement) {
		this.healthIncreasement = healthIncreasement;
	}

	public int getAmmoIncreasement() {
		return ammoIncreasement;
	}

	public void setAmmoIncreasement(int ammo) {
		this.ammoIncreasement = ammo;
	}

	public boolean isDead() {
		return dead;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}

	public String getAction() {
		return action;
	}
	
	public void setAction(String action) {
		this.action = action;
	}
	
	public int getHealthPoints() {
		return healthPoints;
	}
	
	public void setHealthPoints(int healthPoints) {
		this.healthPoints = healthPoints;
	}
	
	public Vector3f getWeaponTranslation() {
		return weaponTranslation;
	}

	public void setWeaponTranslation(Vector3f weaponTranslation) {
		this.weaponTranslation = weaponTranslation;
	}
	
	public Quaternion getWeaponRotation() {
		return weaponRotation;
	}

	public void setWeaponRotation(Quaternion weaponRotation) {
		this.weaponRotation = weaponRotation;
	}
	
}
