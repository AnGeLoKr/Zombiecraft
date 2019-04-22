package it.unical.mat.igpe.ZombieCraft.Server;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.jme3.network.Filters;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;

import it.unical.mat.igpe.ZombieCraft.Network.Messages.EnemyMessage;
import it.unical.mat.igpe.ZombieCraft.Network.Messages.HelloMessage;
import it.unical.mat.igpe.ZombieCraft.Network.Messages.MapMessage;
import it.unical.mat.igpe.ZombieCraft.Network.Messages.PlayerListMessage;
import it.unical.mat.igpe.ZombieCraft.Network.Messages.PlayerMessage;

public class ServerListener implements MessageListener<HostedConnection> {

	private List<MapMessage> map;
	private Map<Integer, PlayerMessage> playerMessageQueue;
	private Map<Integer, PlayerListMessage> playerListMessages;

	// private int lastId = 1;

	public ServerListener(List<MapMessage> mapSplit) {
		map = mapSplit;
		playerMessageQueue = new ConcurrentHashMap<Integer, PlayerMessage>();
		playerListMessages = new ConcurrentHashMap<Integer, PlayerListMessage>();
	}

	@Override
	public void messageReceived(HostedConnection source, Message message) {
		if (message instanceof MapMessage) {
			for (MapMessage tmp : map) {
				source.getServer().broadcast(Filters.in(source), tmp);
				// source.getServer().broadcast(tmp);
			}
		} else if (message instanceof PlayerMessage) {
			PlayerMessage playerMessage = (PlayerMessage) message;
			if (!playerMessageQueue.containsKey(playerMessage.getId()))
				playerMessageQueue.put(playerMessage.getId(), playerMessage);
			else
				playerMessageQueue.replace(playerMessage.getId(), playerMessage);

			if (playerMessage.isDead())
				playerListMessages.remove(playerMessage.getId());

		} else if (message instanceof HelloMessage) {
			HelloMessage helloMessage = (HelloMessage) message;
			// helloMessage.setId( lastId++ );
			helloMessage.setId(source.getId());
			System.out.println("ID ASSEGNATO: " + helloMessage.getId());
			source.getServer().getConnection(source.getId()).send(helloMessage);
		} else if (message instanceof PlayerListMessage) {
			PlayerListMessage playerListMessage = (PlayerListMessage) message;
			playerListMessage.setId(source.getId());
			if (!playerListMessages.containsKey(playerListMessage.getId())) {
				playerListMessages.put(playerListMessage.getId(), playerListMessage);
				for (int key : playerListMessages.keySet())
					source.getServer().broadcast(playerListMessages.get(key));
			}
		} else if (message instanceof EnemyMessage) {
			source.getServer().broadcast((message));
		}
	}

	// private int assignId() {
	// return lastId++;
	// }

	public Map<Integer, PlayerMessage> getPlayerMessageQueue() {
		return playerMessageQueue;
	}

	public Map<Integer, PlayerListMessage> getPlayerListMessages() {
		return playerListMessages;
	}

}
