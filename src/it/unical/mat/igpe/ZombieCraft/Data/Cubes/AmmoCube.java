package it.unical.mat.igpe.ZombieCraft.Data.Cubes;

import com.cubes.BlockManager;
import com.cubes.BlockSkin;
import com.cubes.BlockSkin_TextureLocation;
import com.cubes.Vector3Int;

public class AmmoCube extends Cube {

	private Vector3Int blockLocation;

	public Vector3Int getBlockLocation() {
		return blockLocation;
	}

	public void setBlockLocation(Vector3Int blockLocation) {
		this.blockLocation = blockLocation;
	}

	private static BlockSkin[] skin = { new BlockSkin(new BlockSkin_TextureLocation(1, 1), false),
			new BlockSkin(new BlockSkin_TextureLocation(1, 1), false),
			new BlockSkin(new BlockSkin_TextureLocation(1, 1), false),
			new BlockSkin(new BlockSkin_TextureLocation(1, 1), false),
			new BlockSkin(new BlockSkin_TextureLocation(1, 1), false),
			new BlockSkin(new BlockSkin_TextureLocation(1, 1), false) };

	public AmmoCube() {
		super(skin);
		blockLocation = null;
		BlockManager.register(this);
	}

	public AmmoCube(Vector3Int bl) {
		super(skin);
		blockLocation = bl;
		BlockManager.register(this);
	}

}
