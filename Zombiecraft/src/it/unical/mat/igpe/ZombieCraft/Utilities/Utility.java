package it.unical.mat.igpe.ZombieCraft.Utilities;

import it.unical.mat.igpe.ZombieCraft.Data.Cubes.AmmoCube;
import it.unical.mat.igpe.ZombieCraft.Data.Cubes.BrickCube;
import it.unical.mat.igpe.ZombieCraft.Data.Cubes.DirtCube;
import it.unical.mat.igpe.ZombieCraft.Data.Cubes.EnemySpawnerCube;
import it.unical.mat.igpe.ZombieCraft.Data.Cubes.GlassCube;
import it.unical.mat.igpe.ZombieCraft.Data.Cubes.HealthCube;
import it.unical.mat.igpe.ZombieCraft.Data.Cubes.IceCube;
import it.unical.mat.igpe.ZombieCraft.Data.Cubes.StoneCube;
import it.unical.mat.igpe.ZombieCraft.Data.Cubes.WoodCube;

public class Utility {

	public static final DirtCube DIRT_CUBE = new DirtCube();
	public static final WoodCube WOOD_CUBE = new WoodCube();
	public static final StoneCube STONE_CUBE = new StoneCube();
	public static final GlassCube GLASS_CUBE = new GlassCube();
	public static final BrickCube BRICK_CUBE = new BrickCube();
	public static final IceCube ICE_CUBE = new IceCube();
	public static final AmmoCube AMMO_CUBE = new AmmoCube();
	public static final HealthCube HEALTH_CUBE = new HealthCube();
	public static final EnemySpawnerCube ENEMY_SPAWNER_CUBE = new EnemySpawnerCube();
	public static String filePath;

	public static String[] splitString(String stringValue, String separator) {

		String[] splittedString = stringValue.split(separator);
		return splittedString;
	}

}
