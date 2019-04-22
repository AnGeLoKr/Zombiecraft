package it.unical.mat.igpe.ZombieCraft.Data.Cubes;

import com.cubes.BlockChunkControl;
import com.cubes.BlockManager;
import com.cubes.BlockSkin;
import com.cubes.BlockSkin_TextureLocation;
import com.cubes.Vector3Int;

public class DirtCube extends Cube {

	private static BlockSkin[] skin = new BlockSkin[] { new BlockSkin(new BlockSkin_TextureLocation(0, 0), false),
			new BlockSkin(new BlockSkin_TextureLocation(1, 0), false),
			new BlockSkin(new BlockSkin_TextureLocation(2, 0), false) };

	public DirtCube() {
		super(skin);
		BlockManager.register(this);
	}

	@Override
	protected int getSkinIndex(BlockChunkControl chunk, Vector3Int location, Face face) {

		if (chunk.isBlockOnSurface(location)) {
			switch (face) {
			case Top:
				return 0;

			case Bottom:
				return 2;
			}
			return 1;
		}
		return 2;
	}

}
