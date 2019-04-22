package it.unical.mat.igpe.ZombieCraft.Client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;

import it.unical.mat.igpe.ZombieCraft.Network.Messages.EnemyMessage;
import it.unical.mat.igpe.ZombieCraft.Network.Messages.HelloMessage;
import it.unical.mat.igpe.ZombieCraft.Network.Messages.MapMessage;
import it.unical.mat.igpe.ZombieCraft.Network.Messages.PlayerListMessage;
import it.unical.mat.igpe.ZombieCraft.Network.Messages.PlayerMessage;
import it.unical.mat.igpe.ZombieCraft.Network.Messages.PowerupMessage;

public class ClientListener implements MessageListener<Client> {

	private byte[] bMapFile;
	private List<MapMessage> mapMessages = new ArrayList<MapMessage>();
	private Map<Integer, PlayerListMessage> playerListMessages = new ConcurrentHashMap<Integer, PlayerListMessage>();
	private List<PlayerMessage> playerMessages = new CopyOnWriteArrayList<PlayerMessage>();
	private Map<Integer, EnemyMessage> enemyMessages = new ConcurrentHashMap<Integer, EnemyMessage>();
	private HelloMessage helloMessage;
	private boolean loaded = false;
	private PowerupMessage powerupMessage;

	@Override
	public void messageReceived(Client source, Message message) {
		if (message instanceof MapMessage) {
			MapMessage mapMessage = (MapMessage) message;
			mapMessages.add(mapMessage);
		} else if (message instanceof PlayerMessage) {
			PlayerMessage playerMessage = (PlayerMessage) message;
			playerMessages.add(playerMessage);
		} else if (message instanceof HelloMessage) {
			helloMessage = (HelloMessage) message;
			System.out.println("ID:" + helloMessage.getId() + " NAME: " + helloMessage.getName());
		} else if (message instanceof PlayerListMessage) {
			PlayerListMessage playerListMessage = (PlayerListMessage) message;
			if (!playerListMessages.containsKey(playerListMessage.getId())) {
				playerListMessages.put(playerListMessage.getId(), playerListMessage);
			}
		} else if (message instanceof EnemyMessage) {
			EnemyMessage enemyMessage = (EnemyMessage) message;
			if (!enemyMessages.containsKey(enemyMessage.getId()))
				enemyMessages.put(enemyMessage.getId(), enemyMessage);
			else {
				enemyMessages.replace(enemyMessage.getId(), enemyMessage);
			}

		} else if (message instanceof PowerupMessage) {
			powerupMessage = (PowerupMessage) message;
		}

	}

	public HelloMessage getHelloMessage() {
		return helloMessage;
	}

	public byte[] getBFile() {
		if (mapMessages != null && (!loaded)) {
			bMapFile = new byte[mapMessages.size() * 1024];
			int position = 0;
			for (int i = 0; i < mapMessages.size(); i++) {
				for (int j = 0; j < 1024 && position < (i * 1024) && position < bMapFile.length; j++, position++)
					bMapFile[position] = mapMessages.get(i).getBFile()[j];
			}
			loaded = true;
		}
		return bMapFile;
	}

	public boolean isMapDownloaded() {
		return mapMessages.size() == mapMessages.get(0).getMapSize();
	}

	public Map<Integer, PlayerListMessage> getPlayerListMessages() {
		return playerListMessages;
	}

	public List<PlayerMessage> getPlayerMessages() {
		return playerMessages;
	}

	public Map<Integer, EnemyMessage> getEnemyMessages() {
		return enemyMessages;
	}

	public PowerupMessage getPowerupMessage() {
		try {
			return powerupMessage;
		} catch (Exception e) {
			Logger.getGlobal().setLevel(Level.SEVERE);
			System.err.println("POWERUP NULL ON CLIENT");
		} finally {
			powerupMessage = null;
		}
		return null;
	}

}
