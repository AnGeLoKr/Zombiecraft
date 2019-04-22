package it.unical.mat.igpe.ZombieCraft.Data.Cubes;

import com.cubes.BlockManager;
import com.cubes.BlockSkin;
import com.cubes.BlockSkin_TextureLocation;

public class GlassCube extends Cube {

	private static BlockSkin[] skin = new BlockSkin[] { new BlockSkin(new BlockSkin_TextureLocation(8, 0), true) };

	public GlassCube() {
		super(skin);
		BlockManager.register(this);
	}

}