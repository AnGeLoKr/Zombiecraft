package it.unical.mat.igpe.ZombieCraft;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import com.cubes.Block;
import com.cubes.BlockNavigator;
import com.cubes.BlockTerrainControl;
import com.cubes.CubesSettings;
import com.cubes.Vector3Int;
import com.cubes.network.CubesSerializer;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;

import it.unical.mat.igpe.ZombieCraft.Data.Cubes.BrickCube;
import it.unical.mat.igpe.ZombieCraft.Data.Cubes.Cube;
import it.unical.mat.igpe.ZombieCraft.Data.Cubes.DirtCube;
import it.unical.mat.igpe.ZombieCraft.Data.Cubes.EnemySpawnerCube;
import it.unical.mat.igpe.ZombieCraft.Data.Cubes.GlassCube;
import it.unical.mat.igpe.ZombieCraft.Data.Cubes.IceCube;
import it.unical.mat.igpe.ZombieCraft.Data.Cubes.StoneCube;
import it.unical.mat.igpe.ZombieCraft.Data.Cubes.WoodCube;
import it.unical.mat.igpe.ZombieCraft.Utilities.Settings;
import it.unical.mat.igpe.ZombieCraft.Utilities.Utility;
import sun.applet.Main;

public class Editor extends SimpleApplication implements ActionListener {

	private String filePath;
	private BitmapFont myFont;
	private Node terrainNode;
	private BlockTerrainControl blockTerrain;
	private WoodCube woodCube;
	private DirtCube dirtCube;
	private StoneCube stoneCube;
	private GlassCube glassCube;
	private BrickCube brickCube;
	private EnemySpawnerCube enemySpawnerCube;
	private IceCube iceCube;
	private Cube[] cubes;
	private Block selectedBlock = new WoodCube();
	private String[] cubesToSelect = { "Grass Cube", "Wood Cube", "Brick Cube", "Stone Cube", "Ice Cube", "Glass Cube",
			"Enemy Spawner Cube" };
	private String currentBock = cubesToSelect[0];
	private BitmapText currentBlockUsed;

	public static void main(String[] args) {
		Logger.getLogger("com.jme3").setLevel(Level.SEVERE);

		Editor app = new Editor();
		app.start();
	}

	public Editor() {
		settings = new AppSettings(true);
		this.setShowSettings(false);
		this.setDisplayStatView(false);
		this.setDisplayFps(false);

		String[] resolutionString = Utility.splitString(Settings.resolutions[Integer.parseInt(Settings.options[1])],
				"x");

		settings.setWidth(Integer.parseInt(resolutionString[0]));
		settings.setHeight(Integer.parseInt(resolutionString[1]));

		settings.setTitle("ZOMBIECRAFT TEST EDITOR");

		if (Settings.options[2].equals("Yes"))
			settings.setFullscreen(true);
		else
			settings.setFullscreen(false);

		// GraphicsDevice device =
		// GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		// DisplayMode[] modes = device.getDisplayModes();
		// modes[0].getHeight(), modes[0].getWidth();
		// settings.setFrameRate(60);
	}

	@Override
	public void simpleInitApp() {
		// CubesTestAssets.registerBlocks();
		// String userHome = System.getProperty("user.home");
		// assetManager.registerLocator(userHome, FileLocator.class);
		// Node loadedNode =
		// (Node)assetManager.loadModel("/Documents/MyModel.j3o");
		// loadedNode.setName("loaded node");
		// rootNode.attachChild(loadedNode);
		initCubes();
		terrainNode = new Node();
		load();
		initControls();
		// if(blockTerrain == null)
		// initBlockTerrain();
		initGUI();

		cam.setLocation(new Vector3f(-16.6F, 46.0F, 97.6F));
		cam.lookAtDirection(new Vector3f(0.68F, -0.47F, -0.56F), Vector3f.UNIT_Y);
		flyCam.setMoveSpeed(250.0F);
		assetManager.registerLocator("resources/", FileLocator.class.getName());

	}

	private void load() {
		FileInputStream fileInputStream = null;
		// String userHome = System.getProperty("user.home");
		File file = new File(filePath);
		if (file.exists()) {
			byte[] bFile = new byte[(int) file.length()];

			// System.out.println(bFile.length);

			// JOptionPane.showMessageDialog(null, "Loading please wait...");
			try {
				// convert file into array of bytes
				fileInputStream = new FileInputStream(file);
				fileInputStream.read(bFile);
				fileInputStream.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

			blockTerrain = new BlockTerrainControl(getSettings(this), new Vector3Int(5, 1, 5));
			CubesSerializer.readFromBytes(blockTerrain, bFile);

			terrainNode.addControl(blockTerrain);
			rootNode.attachChild(terrainNode);
		} else {
			initBlockTerrain();
		}
	}

	@Override
	public void stop() {
		String userHome = System.getProperty("user.home");
		// BinaryExporter exporter = BinaryExporter.getInstance();

		try {
			byte[] serializedTerrainData = CubesSerializer.writeToBytes(blockTerrain);
			FileOutputStream out = new FileOutputStream(filePath);
			out.write(serializedTerrainData);
			out.flush();
			out.close();

		} catch (IOException ex) {
			String message = "Error: Failed to save game!";
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, message, ex);
			JOptionPane.showMessageDialog(null, message);
		}
		super.stop(); // continue quitting the game

	}

	private void initCubes() {
		dirtCube = Utility.DIRT_CUBE;
		woodCube = Utility.WOOD_CUBE;
		stoneCube = Utility.STONE_CUBE;
		enemySpawnerCube = Utility.ENEMY_SPAWNER_CUBE;
		glassCube = Utility.GLASS_CUBE;
		brickCube = Utility.BRICK_CUBE;
		iceCube = Utility.ICE_CUBE;

		cubes = new Cube[] { dirtCube, woodCube, brickCube, stoneCube, iceCube, glassCube, enemySpawnerCube };
		selectedBlock = cubes[0];
	}

	private void initControls() {
		inputManager.addMapping("Block_Wood", new KeyTrigger(KeyInput.KEY_1));
		inputManager.addListener(this, new String[] { "Block_Wood" });
		inputManager.addMapping("Block_Brick", new KeyTrigger(KeyInput.KEY_2));
		inputManager.addListener(this, new String[] { "Block_Brick" });
		inputManager.addMapping("Block_Stone", new KeyTrigger(KeyInput.KEY_3));
		inputManager.addListener(this, new String[] { "Block_Stone" });
		inputManager.addMapping("Block_Ice", new KeyTrigger(KeyInput.KEY_4));
		inputManager.addListener(this, new String[] { "Block_Ice" });
		inputManager.addMapping("Block_Glass", new KeyTrigger(KeyInput.KEY_5));
		inputManager.addListener(this, new String[] { "Block_Glass" });
		inputManager.addMapping("Block_Grass", new KeyTrigger(KeyInput.KEY_6));
		inputManager.addListener(this, new String[] { "Block_Grass" });

		inputManager.addMapping("set_block", new Trigger[] { new MouseButtonTrigger(0) });
		inputManager.addListener(this, new String[] { "set_block" });
		inputManager.addMapping("remove_block", new Trigger[] { new MouseButtonTrigger(1) });
		inputManager.addListener(this, new String[] { "remove_block" });
	}

	private void initBlockTerrain() {
		blockTerrain = new BlockTerrainControl(getSettings(this), new Vector3Int(5, 1, 5));
		// blockTerrain.setBlockArea(new Vector3Int(0, 0, 0), new
		// Vector3Int(100, 1, 100), new StoneCube());
		// blockTerrain.setBlocksFromNoise(new Vector3Int(0, 1, 0), new
		// Vector3Int(100, 5, 100), 0.5F, new DirtCube());
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 80; j++)
				for (int z = 0; z < 80; z++)
					blockTerrain.setBlock(j, i, z, dirtCube);

		for (int i = 0; i < 80; i++) {
			blockTerrain.setBlock(i, 4, 0, stoneCube);
			blockTerrain.setBlock(i, 4, 79, stoneCube);
			blockTerrain.setBlock(0, 4, i, stoneCube);
			blockTerrain.setBlock(79, 4, i, stoneCube);
		}

		for (int j = 30; j < 50; j++)
			for (int z = 30; z < 50; z++) {
				blockTerrain.setBlock(j, 4, z, dirtCube);
			}

		for (int i = 29; i < 51; i++) {

			blockTerrain.setBlock(i, 4, 29, stoneCube);
			blockTerrain.setBlock(i, 4, 50, stoneCube);
			blockTerrain.setBlock(50, 4, i, stoneCube);
			blockTerrain.setBlock(29, 4, i, stoneCube);

		}
		terrainNode = new Node();
		terrainNode.addControl(blockTerrain);
		rootNode.attachChild(terrainNode);
	}

	private void initGUI() {
		myFont = assetManager.loadFont("font/MineCrafter3.fnt");

		BitmapText crosshair = new BitmapText(guiFont);
		crosshair.setText("+");
		crosshair.setSize(guiFont.getCharSet().getRenderedSize() * 2);
		crosshair.setLocalTranslation(settings.getWidth() / 2 - guiFont.getCharSet().getRenderedSize() / 3 * 2,
				settings.getHeight() / 2 + crosshair.getLineHeight() / 2.0F, 0.0F);

		guiNode.attachChild(crosshair);

		BitmapText instructionsText1 = new BitmapText(myFont);
		instructionsText1.setText(" INSTRUCTIONS: \n Left Click: Set");
		instructionsText1.setSize((20 * (settings.getWidth() * settings.getHeight())) / (1920 * 1080));
		instructionsText1.setLocalTranslation(0.0F, settings.getHeight(), 0.0F);
		guiNode.attachChild(instructionsText1);
		BitmapText instructionsText2 = new BitmapText(myFont);
		instructionsText2.setText("\n Right Click: Remove");
		instructionsText2.setSize((20 * (settings.getWidth() * settings.getHeight())) / (1920 * 1080));
		instructionsText2.setLocalTranslation(0.0F, settings.getHeight() - instructionsText2.getLineHeight(), 0.0F);
		guiNode.attachChild(instructionsText2);
		BitmapText instructionsText3 = new BitmapText(myFont);
		instructionsText3.setText("\n (Bottom layer is marked as indestructible)");
		instructionsText3.setSize((20 * (settings.getWidth() * settings.getHeight())) / (1920 * 1080));
		instructionsText3.setLocalTranslation(0.0F, settings.getHeight() - 2.0F * instructionsText3.getLineHeight(),
				0.0F);
		guiNode.attachChild(instructionsText3);
		BitmapText instructionsText4 = new BitmapText(myFont);
		instructionsText4.setText(
				"\n press 1 for Wood Cube \n press 2 for Brick Cube \n press 3 for Stone Cube \n press 4 for Ice Cube \n press 5 for Glass Cube \n press 6 for Grass/Stone Cube \n press ESC for save and exit");
		instructionsText4.setSize((20 * (settings.getWidth() * settings.getHeight())) / (1920 * 1080));
		instructionsText4.setLocalTranslation(0.0F, settings.getHeight() - 3.0F * instructionsText3.getLineHeight(),
				0.0F);
		guiNode.attachChild(instructionsText4);
		currentBlockUsed = new BitmapText(myFont);
		currentBlockUsed.setText("SELECTED BLOCK: " + currentBock);
		currentBlockUsed.setSize((40 * (settings.getWidth() * settings.getHeight())) / (1920 * 1080));
		currentBlockUsed.setLocalTranslation(settings.getWidth() / 2 - (currentBlockUsed.getLineWidth() / 2),
				settings.getHeight(), 0.0F);

		// currentBlockUsed.setColor(new ColorRGBA(0, 1, 1, 1));
		guiNode.attachChild(currentBlockUsed);

	}

	@Override
	public void onAction(String action, boolean value, float lastTimePerFrame) {
		if ((action.equals("set_block")) && (value)) {
			Vector3Int blockLocation = getCurrentPointedBlockLocation(true);
			if (blockLocation != null) {
				blockTerrain.setBlock(blockLocation, selectedBlock);
			}
		} else if ((action.equals("remove_block")) && (value)) {
			Vector3Int blockLocation = getCurrentPointedBlockLocation(false);
			if ((blockLocation != null) && (blockLocation.getY() > 0)) {
				blockTerrain.removeBlock(blockLocation);
			}
		} else if (action.equals("Block_Brick") && (value)) {
			selectedBlock = cubes[2];
			currentBock = cubesToSelect[2];
			currentBlockUsed.setText("SELECTED BLOCK: " + currentBock);
		} else if (action.equals("Block_Wood") && (value)) {
			selectedBlock = cubes[1];
			currentBock = cubesToSelect[1];
			currentBlockUsed.setText("SELECTED BLOCK: " + currentBock);
		} else if (action.equals("Block_Stone") && (value)) {
			selectedBlock = cubes[3];
			currentBock = cubesToSelect[3];
			currentBlockUsed.setText("SELECTED BLOCK: " + currentBock);

		} else if (action.equals("Block_Glass") && (value)) {
			selectedBlock = cubes[5];
			currentBock = cubesToSelect[5];
			currentBlockUsed.setText("SELECTED BLOCK: " + currentBock);
		} else if (action.equals("Block_Grass") && (value)) {
			selectedBlock = cubes[0];
			currentBock = cubesToSelect[0];
			currentBlockUsed.setText("SELECTED BLOCK: " + currentBock);
		} else if (action.equals("Block_Ice") && (value)) {
			selectedBlock = cubes[4];
			currentBock = cubesToSelect[4];
			currentBlockUsed.setText("SELECTED BLOCK: " + currentBock);
		}
	}

	private Vector3Int getCurrentPointedBlockLocation(boolean getNeighborLocation) {
		CollisionResults results = getRayCastingResults(terrainNode);
		if (results.size() > 0) {
			Vector3f collisionContactPoint = results.getClosestCollision().getContactPoint();
			return BlockNavigator.getPointedBlockLocation(blockTerrain, collisionContactPoint, getNeighborLocation);
		}
		return null;
	}

	private CollisionResults getRayCastingResults(Node node) {
		Vector3f origin = cam.getWorldCoordinates(new Vector2f(settings.getWidth() / 2, settings.getHeight() / 2),
				0.0F);
		Vector3f direction = cam.getWorldCoordinates(new Vector2f(settings.getWidth() / 2, settings.getHeight() / 2),
				0.3F);
		direction.subtractLocal(origin).normalizeLocal();
		Ray ray = new Ray(origin, direction);
		CollisionResults results = new CollisionResults();
		node.collideWith(ray, results);
		return results;
	}

	public void run(String filePath) {
		Logger.getLogger("com.jme3").setLevel(Level.SEVERE);
		this.filePath = filePath;
		this.start();

	}

	public static CubesSettings getSettings(Application application) {
		CubesSettings settings = new CubesSettings(application);
		settings.setDefaultBlockMaterial("Textures/cubes/terrain.png");
		return settings;
	}

}
