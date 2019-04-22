package it.unical.mat.igpe.ZombieCraft.Data.Cubes;

import com.cubes.BlockManager;
import com.cubes.BlockSkin;
import com.cubes.BlockSkin_TextureLocation;

public class StoneCube extends Cube {

	private static BlockSkin[] skin = new BlockSkin[] { new BlockSkin(new BlockSkin_TextureLocation(9, 0), false) };

	public StoneCube() {
		super(skin);
		BlockManager.register(this);
	}

}
