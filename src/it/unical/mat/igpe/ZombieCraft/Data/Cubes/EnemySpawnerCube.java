package it.unical.mat.igpe.ZombieCraft.Data.Cubes;

import com.cubes.BlockManager;
import com.cubes.BlockSkin;
import com.cubes.BlockSkin_TextureLocation;
import com.cubes.Vector3Int;

import it.unical.mat.igpe.ZombieCraft.Data.Spawner.EnemySpawner;

public class EnemySpawnerCube extends Cube {

	private Vector3Int blockLocation;
	private EnemySpawner enemySpawner;

	public Vector3Int getBlockLocation() {
		return blockLocation;
	}

	public void setBlockLocation(Vector3Int blockLocation) {
		this.blockLocation = blockLocation;
	}

	private static BlockSkin[] SKIN = { new BlockSkin(new BlockSkin_TextureLocation(7, 1), false),
			new BlockSkin(new BlockSkin_TextureLocation(7, 1), false),
			new BlockSkin(new BlockSkin_TextureLocation(7, 1), false),
			new BlockSkin(new BlockSkin_TextureLocation(7, 1), false),
			new BlockSkin(new BlockSkin_TextureLocation(7, 1), false),
			new BlockSkin(new BlockSkin_TextureLocation(7, 1), false) };

	public EnemySpawnerCube() {
		super(SKIN);
		blockLocation = null;
		BlockManager.register(this);
	}

	public EnemySpawnerCube(Vector3Int bl) {
		super(SKIN);
		blockLocation = bl;
		enemySpawner = new EnemySpawner(bl);
		BlockManager.register(this);
	}

	public EnemySpawner getEnemySpawner() {
		return enemySpawner;
	}

	public Vector3Int getPosition() {
		return enemySpawner.getPosition();
	}
}
