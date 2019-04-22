package it.unical.mat.igpe.ZombieCraft.Network.Messages;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

@Serializable
public class PowerupMessage extends AbstractMessage {

	private int x, y, z;
	private String type;
	private boolean delete;
	private int amount;

	public PowerupMessage() {
		type = null;
	}

	public PowerupMessage(int x, int y, int z, String type) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.type = type;
	}

	// public Vector3Int getLocation() {
	// return location;
	// }
	//
	// public void setLocation(Vector3Int location) {
	// this.location = location;
	// }

	public String getType() {
		return type;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isDelete() {
		return delete;
	}

	public void setDelete(boolean delete) {
		this.delete = delete;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

}
