package it.unical.mat.igpe.ZombieCraft.Data;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

import it.unical.mat.igpe.ZombieCraft.Data.Weapons.AK47;
import it.unical.mat.igpe.ZombieCraft.Data.Weapons.Weapon;

//classe che si occupa del giocatore
public class Player {

	
	// nome del giocatore
	private String name;
	private int score = 0;
	private int healthPoints = 100; // da 0 a 100

	// velocit� del giocatore, la velocità pu� essere variabile
	// in caso di corsa pu� mutare quindi non conviene usarla
	// come costante
	private float moveSpeed = 0.25F;
	private float jumpSpeed = 20.0F;
	// rotazione
	private float xRot, yRot, zRot, angle;

	// posizione su asse X
	private float x;
	// posizione su asse Y
	private float y;
	// posizione su asse Z
	private float z;

	private Weapon selectedWeapon;

	//////
	private String action;

	/*
	 * public Rectangle getBounds() { return new Rectangle(x, y, 20, 20); }
	 */

	public Player() {
		this.x = 50;
		this.y = 100;
		this.z = 50;

		this.xRot = 0;
		this.yRot = 0;
		this.zRot = 0;
		this.angle = 0;

		this.action = "Idle";
		
		this.selectedWeapon = new AK47();
	}

	public Player(float x, float y, float z, float xRot, float yRot, float zRot) {
		this.x = x;
		this.y = y;
		this.z = z;

		this.xRot = xRot;
		this.yRot = yRot;
		this.zRot = zRot;

		this.action = "Idle";

	}

	public Player(Vector3f position, Vector3f rotation) {

		this(position.x, position.y, position.z, rotation.x, rotation.y, rotation.z);

	}

	public void addScore(final int score) {
		this.score += score;
	}

	public int getScore() {
		return score;
	}

	public void decreaseHealthPoints(final int value) {
		this.healthPoints -= value;
	}

	public void addHealthPoints(final int value) {
		this.healthPoints += value;
		if (healthPoints > 100)
			healthPoints = 100;
	}

	public int getHealthPoints() {
		return healthPoints;
	}

	// restituisce la direzione del giocatore

	

	// restituisce la posizione sull'asse X

	public float getX() {
		return x;
	}

	// restituisce la posizione sull'asse Y

	public float getY() {
		return y;
	}

	public float getZ() {
		return z;
	}

	// public void paint(Graphics g) {
	// // g.drawRect(x, y, 19, 19);
	// g.setColor(Color.red);
	// g.fillRect(x, y, 20, 20);
	//
	// }

	
	public float getMoveSpeed() {
		return moveSpeed;
	}

	public void setMoveSpeed(float moveSpeed) {
		this.moveSpeed = moveSpeed;
	}

	public void setPosition(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void setPosition(Vector3f vector) {
		this.x = vector.getX();
		this.y = vector.getY();
		this.z = vector.getZ();
	}

	public Vector3f getPosition() {
		return new Vector3f(x, y, z);
	}

	public void setRotation(float xRot, float yRot, float zRot, float angle) {
		this.xRot = xRot;
		this.yRot = yRot;
		this.zRot = zRot;
		this.angle = angle;
	}

	public void setRotation(Quaternion quaternion) {
		this.xRot = quaternion.getX();
		this.yRot = quaternion.getY();
		this.zRot = quaternion.getZ();
		this.angle = quaternion.getW();
	}

	public Quaternion getRotation() {
		return new Quaternion(xRot, yRot, zRot, angle);
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Weapon getSelectedWeapon() {
		return selectedWeapon;
	}

	public void setSelectedWeapon(Weapon selectedWeapon) {
		this.selectedWeapon = selectedWeapon;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void kill() {
		this.healthPoints = 0;
	}

	public void setHealthPoints(int healthPoints) {
		this.healthPoints = healthPoints;
	}

}
