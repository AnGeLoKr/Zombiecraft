package it.unical.mat.igpe.ZombieCraft.Data.Cubes;

import com.cubes.BlockManager;
import com.cubes.BlockSkin;
import com.cubes.BlockSkin_TextureLocation;

/*Questa classe si occupa di rappresentare un Cubo di legno*/
public class WoodCube extends Cube {

	private static BlockSkin[] skin = { new BlockSkin(new BlockSkin_TextureLocation(4, 0), false),
			new BlockSkin(new BlockSkin_TextureLocation(4, 0), false),
			new BlockSkin(new BlockSkin_TextureLocation(3, 0), false),
			new BlockSkin(new BlockSkin_TextureLocation(3, 0), false),
			new BlockSkin(new BlockSkin_TextureLocation(3, 0), false),
			new BlockSkin(new BlockSkin_TextureLocation(3, 0), false) };

	public WoodCube() {
		super(skin);
		BlockManager.register(this);
	}

}
