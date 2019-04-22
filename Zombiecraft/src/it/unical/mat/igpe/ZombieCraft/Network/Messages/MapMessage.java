package it.unical.mat.igpe.ZombieCraft.Network.Messages;
import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

@Serializable
public class MapMessage extends AbstractMessage {

	private int size;
	private byte[] bFile;
	
	public MapMessage() {

	}
	
	public MapMessage(byte[] bFile) {
		this.bFile = bFile;
	}

	public MapMessage(byte[] bFile, int size) {
		this.size = size;
	}

	public byte[] getBFile() {
		return bFile;
	}

	public int getMapSize() {
		return size;
	}

}