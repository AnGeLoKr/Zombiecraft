package it.unical.mat.igpe.ZombieCraft.Data.Spawner;

import com.cubes.Vector3Int;

public class EnemySpawner extends Spawner {

	private Vector3Int position;

	public EnemySpawner(Vector3Int position) {
		this.position = position;
	}

	public Vector3Int getPosition() {
		return position;
	}

}
