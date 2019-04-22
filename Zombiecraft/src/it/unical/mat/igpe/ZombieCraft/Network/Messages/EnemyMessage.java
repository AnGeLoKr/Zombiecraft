package it.unical.mat.igpe.ZombieCraft.Network.Messages;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

@Serializable
public class EnemyMessage extends AbstractMessage {

	private int id;
	private Vector3f position;
	private Quaternion rotation;
	private boolean dead;
	private Vector3f linearVelocity;
	private String animation;
	private String enemySound;

	public EnemyMessage() {
		// position = Vector3f.NAN;
		rotation = Quaternion.ZERO;
		position = Vector3f.ZERO;
		linearVelocity = Vector3f.NAN;
		animation = "";
		setEnemySound("");
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
		return "ZombieID: " + id + " " + position + " " + rotation;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isDead() {
		return dead;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}

	public Vector3f getLinearVelocity() {
		return linearVelocity;
	}

	public void setLinearVelocity(Vector3f linearVelocity) {
		this.linearVelocity = linearVelocity;
	}

	public String getAnimation() {
		return animation;
	}

	public void setAnimation(String animation) {
		this.animation = animation;
	}

	public String getEnemySound() {
		return enemySound;
	}

	public void setEnemySound(String enemySound) {
		this.enemySound = enemySound;
	}

}
