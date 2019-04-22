package it.unical.mat.igpe.ZombieCraft.Data.Cubes;

import com.cubes.BlockManager;
import com.cubes.BlockSkin;
import com.cubes.BlockSkin_TextureLocation;
import com.cubes.Vector3Int;

public class HealthCube extends Cube {

	private Vector3Int blockLocation;

	public Vector3Int getBlockLocation() {
		return blockLocation;
	}

	public void setBlockLocation(Vector3Int blockLocation) {
		this.blockLocation = blockLocation;
	}

	private static BlockSkin[] SKIN = { new BlockSkin(new BlockSkin_TextureLocation(5, 1), false),
			new BlockSkin(new BlockSkin_TextureLocation(5, 1), false),
			new BlockSkin(new BlockSkin_TextureLocation(5, 1), false),
			new BlockSkin(new BlockSkin_TextureLocation(5, 1), false),
			new BlockSkin(new BlockSkin_TextureLocation(5, 1), false),
			new BlockSkin(new BlockSkin_TextureLocation(5, 1), false) };

	public HealthCube() {
		super(SKIN);
		blockLocation = null;
		BlockManager.register(this);
	}

	public HealthCube(Vector3Int bl) {
		super(SKIN);
		blockLocation = bl;
		BlockManager.register(this);
	}

}
