package it.unical.mat.igpe.ZombieCraft.Network.Messages;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

@Serializable
public class PlayerListMessage extends AbstractMessage {

	private String name;
	private int id;

	public PlayerListMessage() {
		name = "";
	}

	public PlayerListMessage(String name) {
		this.name = name;
	}

	public PlayerListMessage(String name, int id) {
		this.name = name;
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
}
