package it.unical.mat.igpe.ZombieCraft.Data.Cubes;

import com.cubes.BlockManager;
import com.cubes.BlockSkin;
import com.cubes.BlockSkin_TextureLocation;

public class IceCube extends Cube {

	private static BlockSkin[] SKIN = { new BlockSkin(new BlockSkin_TextureLocation(0, 1), false),
			new BlockSkin(new BlockSkin_TextureLocation(0, 1), false),
			new BlockSkin(new BlockSkin_TextureLocation(0, 1), false),
			new BlockSkin(new BlockSkin_TextureLocation(0, 1), false),
			new BlockSkin(new BlockSkin_TextureLocation(0, 1), false),
			new BlockSkin(new BlockSkin_TextureLocation(0, 1), false) };

	public IceCube() {
		super(SKIN);
		BlockManager.register(this);
	}

}
