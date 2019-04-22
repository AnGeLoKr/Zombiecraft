package it.unical.mat.igpe.ZombieCraft.Client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import com.cubes.BlockChunkControl;
import com.cubes.BlockChunkListener;
import com.cubes.BlockTerrainControl;
import com.cubes.CubesSettings;
import com.cubes.Vector3Int;
import com.cubes.network.CubesSerializer;
import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.MeshCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.material.MaterialList;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.network.Client;
import com.jme3.network.Network;
import com.jme3.network.serializing.Serializer;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.SceneProcessor;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.CameraControl.ControlDirection;
import com.jme3.scene.plugins.ogre.OgreMeshKey;
import com.jme3.scene.shape.Sphere;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.system.AppSettings;
import com.jme3.ui.Picture;
import com.jme3.util.SkyFactory;
import com.jme3.water.WaterFilter;

import it.unical.mat.igpe.ZombieCraft.Data.Player;
import it.unical.mat.igpe.ZombieCraft.Data.Cubes.AmmoCube;
import it.unical.mat.igpe.ZombieCraft.Data.Cubes.BrickCube;
import it.unical.mat.igpe.ZombieCraft.Data.Cubes.Cube;
import it.unical.mat.igpe.ZombieCraft.Data.Cubes.DirtCube;
import it.unical.mat.igpe.ZombieCraft.Data.Cubes.EnemySpawnerCube;
import it.unical.mat.igpe.ZombieCraft.Data.Cubes.GlassCube;
import it.unical.mat.igpe.ZombieCraft.Data.Cubes.HealthCube;
import it.unical.mat.igpe.ZombieCraft.Data.Cubes.IceCube;
import it.unical.mat.igpe.ZombieCraft.Data.Cubes.StoneCube;
import it.unical.mat.igpe.ZombieCraft.Data.Cubes.WoodCube;
import it.unical.mat.igpe.ZombieCraft.Data.Enemies.Enemy;
import it.unical.mat.igpe.ZombieCraft.Data.Enemies.Zombie;
import it.unical.mat.igpe.ZombieCraft.Data.Weapons.AK47;
import it.unical.mat.igpe.ZombieCraft.Data.Weapons.Weapon;
import it.unical.mat.igpe.ZombieCraft.Network.Messages.EnemyMessage;
import it.unical.mat.igpe.ZombieCraft.Network.Messages.HelloMessage;
import it.unical.mat.igpe.ZombieCraft.Network.Messages.MapMessage;
import it.unical.mat.igpe.ZombieCraft.Network.Messages.PlayerListMessage;
import it.unical.mat.igpe.ZombieCraft.Network.Messages.PlayerMessage;
import it.unical.mat.igpe.ZombieCraft.Network.Messages.PowerupMessage;
import it.unical.mat.igpe.ZombieCraft.Utilities.Settings;
import it.unical.mat.igpe.ZombieCraft.Utilities.Utility;

public class ZombieCraftClient extends SimpleApplication implements ActionListener, AnimEventListener {

	private static String ip;
	private static String port;

	public static void main(String[] args) {
		Logger.getLogger("").setLevel(Level.SEVERE);
		Logger.getLogger("com.jme3").setLevel(Level.SEVERE);
		ip = args[0];
		port = args[1];

		ZombieCraftClient client = new ZombieCraftClient();

		client.start();
	}

	public ZombieCraftClient() {
		settings = new AppSettings(true);
		settings.setFrameRate(60);
		this.setShowSettings(false);
		this.setDisplayStatView(false);
		this.setDisplayFps(false);

		String[] resolutionString = Utility.splitString(Settings.resolutions[Integer.parseInt(Settings.options[1])],
				"x");

		settings.setWidth(Integer.parseInt(resolutionString[0]));
		settings.setHeight(Integer.parseInt(resolutionString[1]));

		settings.setTitle("WELCOME TO ZOMBIECRAFT");

		if (Settings.options[2].equals("Yes"))
			settings.setFullscreen(true);
		else
			settings.setFullscreen(false);
	}

	private static final Vector3f lightDirection = new Vector3f(-0.8F, -1.0F, -0.8F).normalizeLocal();

	private ClientListener clientListener;
	private BlockTerrainControl blockTerrain;
	private Node terrainNode;
	private BulletAppState bulletAppState;
	private DirtCube dirtCube;
	private WoodCube woodCube;
	private StoneCube stoneCube;
	private EnemySpawnerCube enemySpawnerCube;
	private GlassCube glassCube;
	private BrickCube brickCube;
	private IceCube iceCube;
	private AmmoCube ammoCube;
	private boolean[] arrowKeys = new boolean[4];
	private Client client;
	private Vector3f walkDirection = new Vector3f();
	private CameraNode cameraNode;
	private Node gunNode;
	private Geometry mark;
	private BitmapText healthText;
	private BitmapText player1ScoreText;
	private BitmapText player2ScoreText;
	private BitmapText loadingScreen;
	private BitmapText resultText;
	private int maxScore;
	private String winnerName;

	float counter = 0;
	boolean loaded = false;

	private Node mainNode = new Node("Main Node");;
	@SuppressWarnings("deprecation")
	private CharacterControl playerControl;
	private Map<Integer, Player> players;
	private Map<Integer, Spatial> playerSpatials;
	private Map<Integer, Spatial> weaponSpatials;
	private Map<Integer, Enemy> enemies;
	private Map<Integer, Spatial> enemySpatials;
	private Map<Vector3Int, Cube> powerups;
	private int id = -1;

	private CubesSettings cubesSettings;
	private Weapon weapon;

	private Node shootables;

	private boolean weaponShoot;

	private float shootTime;

	private boolean firstShot;
	private Picture healthPic = new Picture("Health Picture");
	private Picture ammoPic = new Picture("Ammo Picture");
	BitmapText crosshair;
	private BitmapText ammoText;
	private BitmapText gameOverText;
	private BitmapText gameOverSubText;

	private BitmapFont myFont;

	private HashMap<Integer, CharacterControl> enemyCharacterControls;

	private AnimChannel zombieChannel;

	private AnimControl zombieControl;

	private AudioNode audio_gun;

	private AudioNode audio_player_die;

	private AudioNode audio_zombie_attack;

	private AudioNode players_audio_gun;

	private boolean playersFirstShoot = true;

	private AudioNode audio_background;

	private boolean shoot;

	private HealthCube healthCube;

	private boolean gameOver;

	private boolean audio_dead;
	private AudioNode audio_appear_powerup;
	private AudioNode audio_pick_ammo;
	private AudioNode audio_pick_health;
	private AnimControl playerAnimControl;
	private AnimChannel playerAnimChannel;

	@Override
	public void simpleInitApp() {

		// flyCam.setMoveSpeed(50);

		Serializer.registerClass(MapMessage.class);
		Serializer.registerClass(PlayerMessage.class);
		Serializer.registerClass(HelloMessage.class);
		Serializer.registerClass(PlayerListMessage.class);
		Serializer.registerClass(EnemyMessage.class);
		Serializer.registerClass(PowerupMessage.class);

	//	setPauseOnLostFocus(false);

		players = new ConcurrentHashMap<Integer, Player>();
		playerSpatials = new ConcurrentHashMap<Integer, Spatial>();
		weaponSpatials = new ConcurrentHashMap<Integer, Spatial>();
		enemies = new ConcurrentHashMap<Integer, Enemy>();
		enemySpatials = new ConcurrentHashMap<Integer, Spatial>();
		enemyCharacterControls = new HashMap<Integer, CharacterControl>();
		powerups = new ConcurrentHashMap<Vector3Int, Cube>();

		try {
			client = Network.connectToServer(ip, Integer.parseInt(port));
			client.start();

			Player p = new Player();
			p.setName(Settings.options[0]);

			clientListener = new ClientListener();
			client.addMessageListener(clientListener, MapMessage.class, PlayerMessage.class, HelloMessage.class,
					PlayerListMessage.class, EnemyMessage.class, PowerupMessage.class);

			// client.addMessageListener(clientListener, PlayerMessage.class);
			// client.addMessageListener(clientListener, HelloMessage.class);
			// client.addMessageListener(clientListener,
			// PlayerListMessage.class);
			// client.addMessageListener(clientListener, EnemyMessage.class);
			// client.addMessageListener(clientListener, PowerupMessage.class);
			client.send(new HelloMessage(p.getName()));
			client.send(new MapMessage());
			while (clientListener.getHelloMessage() == null && id == -1) {
				if (clientListener.getHelloMessage() != null)
					id = clientListener.getHelloMessage().getId();
			}

			players.put(id, p);
			cubesSettings = getSettings(this);
			initCubes();
			shootables = new Node("ENEMY SHOOTABLES NODE");
			winnerName = p.getName();
			initializeEnvironment(this);
			initializeWater(this);
			initMark();
			initLoadingScreen();
			// client.send(new MapMessage());
			// initMap();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Server not found");
		}

	}

	private void initLoadingScreen() {
		myFont = assetManager.loadFont("font/MineCrafter3.fnt");
		loadingScreen = new BitmapText(myFont);
		loadingScreen.setText("LOADING...");
		loadingScreen.setSize((80 * (settings.getWidth() * settings.getHeight())) / (1920 * 1080));
		loadingScreen.setLocalTranslation(
				((settings.getWidth() / 2 - guiFont.getCharSet().getRenderedSize() / 3 * 2))
						- (loadingScreen.getLineWidth() / 2.0F) + 4.0F,
				settings.getHeight() / 2 + loadingScreen.getLineHeight() / 2.0F, 0.0F);
		guiNode.attachChild(loadingScreen);
	}

	private void initBlockTerrain() {
		terrainNode = new Node();
		blockTerrain = new BlockTerrainControl(getSettings(this), new Vector3Int(5, 1, 5));
		blockTerrain.setBlockArea(new Vector3Int(1, 1, 1), new Vector3Int(100, 1, 100), Utility.DIRT_CUBE);
		// blockTerrain.setBlocksFromNoise(new Vector3Int(1, 1, 1), new
		// Vector3Int(100, 5, 100), 0.5F, Utility.dirtCube);

		blockTerrain.addChunkListener(new BlockChunkListener() {
			@Override
			public void onSpatialUpdated(BlockChunkControl blockChunk) {
				Geometry optimizedGeometry = blockChunk.getOptimizedGeometry_Opaque();
				RigidBodyControl rigidBodyControl = optimizedGeometry.getControl(RigidBodyControl.class);
				if (rigidBodyControl == null) {
					rigidBodyControl = new RigidBodyControl(0.0F);
					optimizedGeometry.addControl(rigidBodyControl);
					bulletAppState.getPhysicsSpace().add(rigidBodyControl);
				}
				rigidBodyControl.setCollisionShape(new MeshCollisionShape(optimizedGeometry.getMesh()));
			}
		});

		terrainNode.addControl(blockTerrain);
		terrainNode.setShadowMode(RenderQueue.ShadowMode.Cast);

		rootNode.attachChild(terrainNode);
	}

	private void removePowerUps(String cubeType, int x, int y, int z, int amount) {
		Vector3Int location = new Vector3Int(x, y, z);
		if (cubeType.equals(AmmoCube.class.getName())) {// && blockLocation !=
														// null) {
			blockTerrain.removeBlock(location);
			powerups.remove(location, ammoCube);
			audio_appear_powerup.play();
		} else if (cubeType.equals(HealthCube.class.getName())) {
			blockTerrain.removeBlock(location);
			powerups.remove(location, ammoCube);
			audio_appear_powerup.play();
		}
	}

	private void dropPowerUps(String cubeType, int x, int y, int z) {
		Vector3Int location = new Vector3Int(x, y, z);
		if (cubeType.equals(AmmoCube.class.getName())) {
			ammoCube.setBlockLocation(location);
			powerups.put(location, ammoCube);
			blockTerrain.setBlock(location, ammoCube);
		} else if (cubeType.equals(HealthCube.class.getName())) {
			healthCube.setBlockLocation(location);
			powerups.put(location, healthCube);
			blockTerrain.setBlock(location, healthCube);
		}
	}

	private void initCamera() {

		cameraNode = new CameraNode("Camera Node", cam);

		cameraNode.setControlDir(ControlDirection.CameraToSpatial);

		cameraNode.setLocalTranslation(0, 0, 0);

		cameraNode.lookAt(mainNode.getLocalTranslation(), Vector3f.UNIT_Y);

		mainNode.attachChild(cameraNode);

	}

	private void initGUI() {
		// myFont = assetManager.loadFont("font/MineCrafter3.fnt");

		crosshair = new BitmapText(guiFont);
		crosshair.setText("+");
		crosshair.setSize(guiFont.getCharSet().getRenderedSize() * 2);
		crosshair.setLocalTranslation(settings.getWidth() / 2 - guiFont.getCharSet().getRenderedSize() / 3 * 2,
				settings.getHeight() / 2 + crosshair.getLineHeight() / 2.0F, 0.0F);

		guiNode.attachChild(crosshair);

		gameOverText = new BitmapText(myFont);
		gameOverText.setText("GAME OVER");
		gameOverText.setSize((80 * (settings.getWidth() * settings.getHeight())) / (1920 * 1080));

		gameOverText.setLocalTranslation(
				((settings.getWidth() / 2 - guiFont.getCharSet().getRenderedSize() / 3 * 2))
						- (gameOverText.getLineWidth() / 2.0F) + 4.0F,
				settings.getHeight() / 2 + gameOverText.getLineHeight() / 2.0F, 0.0F);

		gameOverSubText = new BitmapText(myFont);
		gameOverSubText.setText("Updating Scoreborads");
		gameOverSubText.setSize((40 * (settings.getWidth() * settings.getHeight())) / (1920 * 1080));

		gameOverSubText.setLocalTranslation(
				((settings.getWidth() / 2 - guiFont.getCharSet().getRenderedSize() / 3 * 2))
						- (gameOverSubText.getLineWidth() / 2.0F) + 4.0F,
				gameOverText.getLocalTranslation().y - (120.0F * settings.getHeight()) / 1080, 0.0F);

		ammoText = new BitmapText(myFont);
		ammoText.setText("" + weapon.getAmmoPerClip() + "/" + weapon.getAmmo());
		ammoText.setSize((60 * (settings.getWidth() * settings.getHeight())) / (1920 * 1080));
		ammoText.setLocalTranslation((470F * settings.getWidth()) / 1920, (128F * settings.getHeight()) / 1080F, 0.0F);

		healthText = new BitmapText(myFont, true);
		healthText.setText("" + players.get(id).getHealthPoints());
		healthText.setSize((60 * (settings.getWidth() * settings.getHeight())) / (1920 * 1080));

		healthText.setLocalTranslation((160F * settings.getWidth()) / 1920, (128F * settings.getHeight()) / 1080F, 0F);

		player1ScoreText = new BitmapText(myFont);
		player1ScoreText.setText("  " + players.get(id).getName() + ": " + players.get(id).getScore());
		player1ScoreText.setSize((60 * (settings.getWidth() * settings.getHeight())) / (1920 * 1080));
		player1ScoreText.setLocalTranslation(-5F, ((settings.getHeight() * 1060F) / 1080), 0F);

		player2ScoreText = new BitmapText(myFont);
		// player2ScoreText.setText(" " + players.get(id).getName() + ": "
		// + players.get(id).getScore());
		player2ScoreText.setSize((60 * (settings.getWidth() * settings.getHeight())) / (1920 * 1080));
		player2ScoreText.setLocalTranslation(-5F, ((settings.getHeight() * 980F) / 1080), 0F);

		healthPic.setImage(assetManager, "img/hud/lifeHeart.png", true);
		healthPic.setWidth((300 * settings.getWidth()) / 1920);
		healthPic.setHeight((123 * settings.getHeight()) / 1080);
		healthPic.setLocalTranslation((6F * settings.getWidth()) / 1920, (35F * settings.getHeight()) / 1080F, 0F);
		guiNode.attachChild(healthPic);

		ammoPic.setImage(assetManager, "img/hud/ammo.png", true);
		ammoPic.setWidth((116 * settings.getWidth()) / 1920);
		ammoPic.setHeight((123 * settings.getHeight()) / 1080);
		ammoPic.setLocalTranslation((320F * settings.getWidth()) / 1920, (35F * settings.getHeight()) / 1080F, 0F);

		resultText = new BitmapText(myFont);
		resultText.setText("YOU WON");
		resultText.setSize((80 * (settings.getWidth() * settings.getHeight())) / (1920 * 1080));

		resultText.setLocalTranslation(
				((settings.getWidth() / 2 - guiFont.getCharSet().getRenderedSize() / 3 * 2))
						- (resultText.getLineWidth() / 2.0F) + 4.0F,
				settings.getHeight() / 2 + resultText.getLineHeight() / 2.0F, 0.0F);

		guiNode.attachChild(ammoPic);
		guiNode.attachChild(healthText);
		guiNode.attachChild(player1ScoreText);
		guiNode.attachChild(player2ScoreText);
		guiNode.attachChild(ammoText);

	}

	@Override
	public void simpleUpdate(float tpf) {
		try {
			super.simpleUpdate(tpf);
			if (clientListener.isMapDownloaded() && !loaded) {
				initBlockTerrain();
				initMap();
				initCamera();
				initPlayer();
				initWeapon();
				initControls();
				initAudio();
				drawPlayer(id);
				initGUI();

				maxScore = 0;
				gunNode.attachChild(audio_gun);
				loaded = !loaded;
				guiNode.detachChild(loadingScreen);
			}

			if (loaded) {
				updatePlayerList(tpf);
				updateEnemies(tpf);
				updatePlayers(tpf);
				updatePlayer(tpf);
				updateWeapon(tpf);
				updatePowerups(tpf);

			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Server unreachable");
			this.stop();
		}
	}

	private void updatePlayerList(float tpf) {
		client.send(new PlayerListMessage(players.get(id).getName()));

		for (int key : clientListener.getPlayerListMessages().keySet()) {
			if (!players.containsKey(clientListener.getPlayerListMessages().get(key).getId())) {
				Player p = new Player();
				p.setName(clientListener.getPlayerListMessages().get(key).getName());
				System.out.println("ID Giocatore: " + clientListener.getPlayerListMessages().get(key).getId());
				players.put(key, p);
				drawPlayer(key);
				drawWeapon(key);

			}
		}
	}

	private void updatePowerups(float tpf) {
		PowerupMessage powerupMessage = clientListener.getPowerupMessage();
		if (powerupMessage != null && powerupMessage.getType() != null) {
			if (powerupMessage.isDelete())
				removePowerUps(powerupMessage.getType(), powerupMessage.getX(), powerupMessage.getY(),
						powerupMessage.getZ(), powerupMessage.getAmount());
			else
				dropPowerUps(powerupMessage.getType(), powerupMessage.getX(), powerupMessage.getY(),
						powerupMessage.getZ());


		}
	}

	private void updateEnemies(float timePerFrame) {
		for (int key : clientListener.getEnemyMessages().keySet()) {
			EnemyMessage message = clientListener.getEnemyMessages().get(key);

			if (!enemySpatials.containsKey(key)) {
				makeEnemy(message.getPosition(), message.getRotation(), key, message.getAnimation());
				enemySpatials.get(key).setUserData("position", message.getPosition());
				enemySpatials.get(key).setUserData("count", 0.0F);
			}

			if (enemySpatials.get(key) != null) {
				if (message.isDead() || (Float) enemySpatials.get(key).getUserData("count") >= 0.2F) {
					// enqueue(new Callable<Void>() {

					// public Void call() {
					rootNode.detachChild(enemySpatials.get(key));
					bulletAppState.getPhysicsSpace().remove(enemySpatials.get(key));
					shootables.detachChild(enemySpatials.get(key));
					enemySpatials.remove(key);
					enemies.remove(key);

				} else {

					// enqueue(new Callable<Void>() {

					// @Override
					// public Void call() {
					if (enemySpatials.get(key).getUserData("position").equals(message.getPosition()))
						enemySpatials.get(key).setUserData("count",
								(Float) enemySpatials.get(key).getUserData("count") + timePerFrame);

					enemySpatials.get(key).setLocalTranslation(message.getPosition());
					enemySpatials.get(key).setLocalRotation(message.getRotation());
					if (message.getLinearVelocity() != Vector3f.NAN) {
						enemySpatials.get(key).getControl(RigidBodyControl.class)
								.setPhysicsLocation(message.getPosition());
						enemySpatials.get(key).getControl(RigidBodyControl.class)
								.setPhysicsRotation(message.getRotation());

						// enemyCharacterControls.get(key).setPhysicsLocation(message.getPosition());
					}

					// return null;
					// }
					// });
					if (!message.getAnimation().equals("")) {

						zombieChannel.setAnim(message.getAnimation());
					}
					// if (!message.getEnemySound().equals("") &&
					// message.getEnemySound().equals("Attack")) {
					// audio_zombie_attack.play();
					// }

				}
			}
			clientListener.getEnemyMessages().remove(message);
		}
	}

	private void initWeapon() {
		weapon = new AK47();
		players.get(id).setSelectedWeapon(weapon);
		drawWeapon(id);
	}

	private void initCubes() {
		dirtCube = Utility.DIRT_CUBE;
		woodCube = Utility.WOOD_CUBE;
		stoneCube = Utility.STONE_CUBE;
		enemySpawnerCube = Utility.ENEMY_SPAWNER_CUBE;
		glassCube = Utility.GLASS_CUBE;
		brickCube = Utility.BRICK_CUBE;
		iceCube = Utility.ICE_CUBE;
		ammoCube = new AmmoCube();
		healthCube = new HealthCube();
	}

	private void drawWeapon(int id) {
		assetManager.registerLocator("ProjectAssets/", FileLocator.class.getName());

		if (this.id == id) {
			gunNode = (Node) assetManager.loadModel("ak47/AK47.scene");
			gunNode.addLight(new AmbientLight());
			gunNode.scale(0.3f, 0.3f, 0.3f);

			gunNode.setLocalTranslation(-1, 2, 2);
			mainNode.attachChild(gunNode);
		} else {
			Spatial weaponSpatial = assetManager.loadModel("ak47/AK47.scene");
			weaponSpatial.addLight(new AmbientLight());
			weaponSpatial.scale(0.3f, 0.3f, 0.3f);

			weaponSpatial.setLocalTranslation(-1, 2, 2);
			// weaponNode.attachChild(playerSpatials.get(id));
			weaponSpatials.put(id, weaponSpatial);
			rootNode.attachChild(weaponSpatial);
		}
	}

	private void drawPlayer(int id) {
		assetManager.registerLocator("ProjectAssets/", FileLocator.class.getName());
		MaterialList matList = (MaterialList) assetManager.loadAsset("PlayerNew/Player.material");
		OgreMeshKey key = new OgreMeshKey("PlayerNew/Cube.mesh.xml", matList);
		Spatial sp = assetManager.loadModel("PlayerNew/ZombieCraftPlayerWithWeapon.scene");
		sp = assetManager.loadAsset(key);
		sp.addLight(new AmbientLight());
		sp.updateModelBound();

		sp.scale(1.5f, 1.5f, 1.5f);

		playerAnimControl = (sp.getControl(AnimControl.class));
		playerAnimControl.addListener(this);
		playerAnimChannel = playerAnimControl.createChannel();

		// for first player

		if (this.id == id) {
			playerSpatials.put(id, sp);
			mainNode.setLocalTranslation(players.get(id).getX(), players.get(id).getY(), players.get(id).getZ());
			// mainNode.attachChild(sp);
		} else {

			if (!playerSpatials.containsKey(id)) {
				playerSpatials.put(id, sp);
				rootNode.attachChild(sp);
			}
		}

		// playerSpatials.get(id).addControl(playerControl);
		players.get(id).setPosition(60, 60, 60);
		rootNode.attachChild(mainNode);

	}

	private void initPlayer() {

		playerControl = new CharacterControl(
				new CapsuleCollisionShape(cubesSettings.getBlockSize() / 2.0F, cubesSettings.getBlockSize() * 2.0F),
				0.05F);
		playerControl.setJumpSpeed(20.0F);
		playerControl.setFallSpeed(40.0F);
		playerControl.setGravity(70.0F);
		// playerControl.setPhysicsLocation(
		// new Vector3f(5.0F, terrainSize.getY() + 5,
		// 5.0F).multLocal(cubesSettings.getBlockSize()));

		playerControl.setSpatial(playerSpatials.get(id));
		// player.setPosition(playerControl.getPhysicsLocation());
		players.get(id).setPosition(50, 100, 50);
		mainNode.setLocalTranslation(players.get(id).getX(), players.get(id).getY(), players.get(id).getZ());
		playerControl.setPhysicsLocation(mainNode.getLocalTranslation());
		bulletAppState.getPhysicsSpace().add(playerControl);
		rootNode.attachChild(mainNode);
		// bulletAppState.getPhysicsSpace().add(); //aggiunto TEST
		// rootNode.att
	}

	private void initMap() {
		bulletAppState = new BulletAppState();
		stateManager.attach(bulletAppState);
		blockTerrain = new BlockTerrainControl(getSettings(this), new Vector3Int(5, 1, 5));
		// bulletAppState.getPhysicsSpace().enableDebug(assetManager);
		byte[] bFile = new byte[clientListener.getBFile().length];

		// assetManager.registerLocator("http://jmonkeyengine.googlecode.com/files/quake3level.zip",
		// HttpZipLocator.class);

		// assetManager.registerLocator("quake3level.zip", ZipLocator.class);

		if (bFile != null) {

			for (int i = 0; i < bFile.length; i++) {
				bFile[i] = clientListener.getBFile()[i];
			}

			System.out.println("received");
			System.out.println(bFile.length);
			CubesSerializer.readFromBytes(blockTerrain, bFile);

			blockTerrain.addChunkListener(new BlockChunkListener() {
				@Override
				public void onSpatialUpdated(BlockChunkControl blockChunk) {
					Geometry optimizedGeometry = blockChunk.getOptimizedGeometry_Opaque();
					RigidBodyControl rigidBodyControl = optimizedGeometry.getControl(RigidBodyControl.class);
					if (rigidBodyControl == null) {
						rigidBodyControl = new RigidBodyControl(0.0F);
						optimizedGeometry.addControl(rigidBodyControl);
						bulletAppState.getPhysicsSpace().add(rigidBodyControl);
					}
					rigidBodyControl.setCollisionShape(new MeshCollisionShape(optimizedGeometry.getMesh()));
				}
			});

			terrainNode.addControl(blockTerrain);
			rootNode.attachChild(terrainNode);
		}
	}

	private void updatePlayers(float lastTimePerFrame) {

		for (PlayerMessage message : clientListener.getPlayerMessages()) {
			if (message != null && message.getId() != id && !message.isDead()) {
				// enqueue(new Callable<Void>() {
				// @Override
				// public Void call() {
				if (playerSpatials.get(message.getId()) != null) {
					playerSpatials.get(message.getId()).setLocalTranslation(message.getPosition());
					playerSpatials.get(message.getId()).setLocalRotation(message.getRotation());
					players.get(message.getId()).setHealthPoints(message.getHealthPoints());
					players.get(message.getId()).getAction();
					if(playerAnimChannel.getTime() == playerAnimChannel.getAnimMaxTime())
						playerAnimChannel.setAnim(message.getAction());
				}

				if (weaponSpatials.get(message.getId()) != null) {
					weaponSpatials.get(message.getId()).setLocalTranslation(message.getWeaponTranslation());
					weaponSpatials.get(message.getId()).setLocalRotation(message.getWeaponRotation());
				}

				// in comune tra i player
				if (message.isShoot()) {
					if (playersFirstShoot) {
						players_audio_gun.play();
						playersFirstShoot = false;
					} else {
						players_audio_gun.playInstance();
					}
				}
				// return null;
				// }
				// });

			} else if (message.getId() == id && !message.isDead()) {
				// enqueue(new Callable<Void>() {
				// @Override
				// public Void call() {
				if (message.getHealthIncreasement() != 0) {
					players.get(id).addHealthPoints(message.getHealthIncreasement());
					audio_pick_health.play();
				} else if (message.getHealthDecreasement() != 0) {
					players.get(id).decreaseHealthPoints(message.getHealthDecreasement());
					if (!gameOver)
						audio_zombie_attack.play();
				}
				if (message.getAmmoIncreasement() != 0) {
					players.get(id).getSelectedWeapon().setAmmo(message.getAmmoIncreasement());
					audio_pick_ammo.play();
				}
				ammoText.setText("" + weapon.getAmmoPerClip() + "/" + weapon.getAmmo());

				player1ScoreText.setText("  " + players.get(id).getName() + ": " + players.get(id).getScore());

				healthText.setText("" + players.get(id).getHealthPoints());
				players.get(id).addScore(message.getPointsToAdd());
				// return null;
				// }
				// });
			}

			if (maxScore < message.getPoints() && message.getId() != id) {
				maxScore = message.getPoints();
				winnerName = message.getName();
				player2ScoreText.setText("  " + winnerName + ": " + maxScore);
			}

			if (message.isDead()) {
				// enqueue(new Callable<Void>() {
				//
				// @Override
				// public Void call() {
				if (playerSpatials.containsKey(message.getId())) {
					
					if(rootNode.hasChild(playerSpatials.get(message.getId())))
						rootNode.detachChild(playerSpatials.get(message.getId()));
					
					if(rootNode.hasChild(weaponSpatials.get(message.getId())))
						rootNode.detachChild(weaponSpatials.get(message.getId()));
					
					playerSpatials.remove(message.getId());
					weaponSpatials.remove(message.getId());
					players.get(message.getId()).kill();

				}
				// return null;
				// }
				//
				// });
			}
			clientListener.getPlayerMessages().remove(message);
		}
	}

	private void updatePlayer(float lastTimePerFrame) {

		if (playerSpatials.containsKey(id)) {

			if (!gameOver) {

				// enqueue(new Callable<Void>() {
				//
				// @Override
				// public Void call() {
				Vector3f camDir = cam.getDirection().multLocal(players.get(id).getMoveSpeed());
				Vector3f camLeft = cam.getLeft().multLocal(players.get(id).getMoveSpeed());
				walkDirection.set(0.0F, 0.0F, 0.0F);

				if (arrowKeys[0] != true) {
					walkDirection.addLocal(camDir);
					players.get(id).setAction("Walk");
				}
				if (arrowKeys[1] != true)
					walkDirection.addLocal(camLeft.negate());
				if (arrowKeys[2] != true)
					walkDirection.addLocal(camDir.negate());
				if (arrowKeys[3] != true)
					walkDirection.addLocal(camLeft);

				if (arrowKeys[0] != true || arrowKeys[1] != true || arrowKeys[2] != true || arrowKeys[3] != true
						|| arrowKeys[4] != true) {
					walkDirection.setY(0.0F);
					playerControl.setWalkDirection(walkDirection);

					cam.setLocation(new Vector3f(mainNode.getLocalTranslation().x, mainNode.getLocalTranslation().y + 3,
							mainNode.getLocalTranslation().z));

					players.get(id).setPosition(playerControl.getPhysicsLocation());
					mainNode.setLocalTranslation(players.get(id).getPosition());

					if (playerSpatials.get(id) != null)
						playerSpatials.get(id).setLocalTranslation(new Vector3f(mainNode.getLocalTranslation().x,
								mainNode.getLocalTranslation().y - 4, mainNode.getLocalTranslation().z));

					// .clone().divideLocal(playerSpatials.get(id).getLocalTranslation()));
					cameraNode.setLocalTranslation(mainNode.getLocalTranslation());
					Vector3f playerPosition = players.get(id).getPosition();

					// for (Vector3f key : powerups.keySet()) {
					// if (Math.abs(key.x - playerPosition.x) < 5f &&
					// Math.abs(key.z -
					// playerPosition.z) < 5f) {
					//
					// if (powerups.get(key) instanceof AmmoCube) {
					// blockTerrain.removeBlock(((AmmoCube)
					// powerups.get(key)).getBlockLocation());
					// ak47.setAmmo(60);
					// ammoText.setText("" + ak47.getAmmoPerClip() + "/"
					// +
					// ak47.getAmmo());
					// audio_pick_ammo.play();
					// } else {
					// blockTerrain.removeBlock(((HealthCube)
					// powerups.get(key)).getBlockLocation());
					// player.addHealthPoints(25);
					// healthText.setText("" +
					// player.getHealthPoints());
					// audio_pick_health.play();
					// }
					// powerups.remove(key);
					// break;
					// }
					//
					// }

					// if (powerups.containsKey(player.getPosition()))

					// playerSpatial.setLocalRotation(new Quaternion(0,
					// cam.getRotation().getY(), 0, 1));
					// mainNode.setLocalRotation(cam.getRotation());

					cameraNode.lookAt(mainNode.getLocalTranslation(), Vector3f.UNIT_Y);
				}

				PlayerMessage playerMessage = new PlayerMessage();
				Vector3f playerPosition = new Vector3f(players.get(id).getPosition());
				// Vector3f playerPosition = mainNode.getWorldTranslation();
				playerPosition.y = playerPosition.y - 4.5f;
				playerMessage.setName(players.get(id).getName());
				playerMessage.setId(id);
				playerMessage.setDamage(weapon.getDamage());
				playerMessage.setPosition(playerPosition);
				playerMessage.setAction(players.get(id).getAction());
				Vector3f playerDirection = cam.getDirection();
				playerDirection.y = 0;
				Quaternion rotation = new Quaternion();
				rotation.lookAt(playerDirection, Vector3f.UNIT_Y);
				playerMessage.setRotation(rotation);
				playerMessage.setShootPoint(mark.getLocalTranslation());
				playerMessage.setShoot(shoot);
				playerMessage.setPoints(players.get(id).getScore());
				playerMessage.setHealthPoints(players.get(id).getHealthPoints());
				Vector3f weaponPosition = new Vector3f(gunNode.getWorldTranslation());
				weaponPosition.y = players.get(id).getPosition().y;

				Vector3f weaponDirection = new Vector3f(cam.getDirection());
				weaponDirection.y = 0;
				Quaternion weaponRotation = new Quaternion();
				weaponRotation.lookAt(weaponDirection, Vector3f.UNIT_Y);

				playerMessage.setWeaponTranslation(weaponPosition);
				playerMessage.setWeaponRotation(weaponRotation);
				client.send(playerMessage);
				mark.setLocalTranslation(Vector3f.NAN);
				// return null;
				// }
				// });
			}
		}

		if (players.get(id).getHealthPoints() <= 0 || players.get(id).getY() <= -15) {

			players.get(id).kill();
			gameOver = true;

		}

		if (gameOver) {
			boolean results = false;
			// guiNode.attachChild(gameOverSubText);

			if (!audio_dead) {
				audio_player_die.play();
				flyCam.setEnabled(true);
				flyCam.setMoveSpeed(60);
				guiNode.attachChild(gameOverText);
				crosshair.removeFromParent();
				gunNode.removeFromParent();
				audio_dead = true;

			}

			results = checkResults();

			if (results) {
				;
				if (players.get(id).getScore() < maxScore)
					gameOverText.setText("YOU LOSE");
				else if (players.get(id).getScore() > maxScore && winnerName.equals(players.get(id).getName()))
					gameOverText.setText("YOU WIN");

			}
		}

	}

	private boolean checkResults() {
		for (int key : players.keySet())
			if (players.get(key).getHealthPoints() > 0)
				return false;

		return true;
	}

	@Override
	public void onAction(String actionName, boolean keyPressed, float lastTimePerFrame) {

		if (actionName.equals("exit") && keyPressed /* && pause */) {
			this.stop();
		}

		if (actionName.equals("move_up")) {
			arrowKeys[0] = keyPressed;
			players.get(id).setAction("Walk");
		} else if (actionName.equals("move_right")) {
			arrowKeys[1] = keyPressed;
		} else if (actionName.equals("move_left")) {
			arrowKeys[3] = keyPressed;
		} else if (actionName.equals("move_down")) {
			arrowKeys[2] = keyPressed;
			players.get(id).setAction("Walk");
		} else if (actionName.equals("jump")) {
			playerControl.jump();
		} else if (actionName.equals("shoot")) {
			weaponShoot = keyPressed;
			// players.get(id).setAction("Punch");
			shoot();
		} else if (actionName.equals("recharge")) {
			weapon.recharge();
			ammoText.setText("" + weapon.getAmmoPerClip() + "/" + weapon.getAmmo());
		} else if (actionName.equals("crouch")) {
			players.get(id).setAction("Crouch");
		}

	}

	@Override
	public void stop() {
		super.stop();
	}
	
	private void initControls() {
		// movimenti
		inputManager.addMapping("move_left", new Trigger[] { new KeyTrigger(32) });
		inputManager.addMapping("move_right", new Trigger[] { new KeyTrigger(30) });
		inputManager.addMapping("move_up", new Trigger[] { new KeyTrigger(31) });
		inputManager.addMapping("move_down", new Trigger[] { new KeyTrigger(17) });
		inputManager.addMapping("jump", new Trigger[] { new KeyTrigger(57) });
		inputManager.addListener(this, new String[] { "move_left" });
		inputManager.addListener(this, new String[] { "move_right" });
		inputManager.addListener(this, new String[] { "move_up" });
		inputManager.addListener(this, new String[] { "move_down" });

		// salta
		inputManager.addListener(this, new String[] { "jump" });

		// spara
		inputManager.addMapping("shoot", new Trigger[] { new MouseButtonTrigger(MouseInput.BUTTON_LEFT) });
		inputManager.addListener(this, new String[] { "shoot" });

		// ricarica
		inputManager.addMapping("recharge", new Trigger[] { new KeyTrigger(KeyInput.KEY_R) });
		inputManager.addListener(this, new String[] { "recharge" });

		// accovaccia
		inputManager.addMapping("crouch", new Trigger[] { new KeyTrigger(KeyInput.KEY_LCONTROL) });
		inputManager.addListener(this, new String[] { "crouch" });

		// pausa
		inputManager.addMapping("pause", new Trigger[] { new KeyTrigger(KeyInput.KEY_P) });
		inputManager.addListener(this, new String[] { "pause" });

		// esci (solo con pausa inserita)
		inputManager.addMapping("exit", new Trigger[] { new KeyTrigger(KeyInput.KEY_ESCAPE) });
		inputManager.addListener(this, new String[] { "exit" });
	}

	private void initMark() {
		Sphere sphere = new Sphere(30, 30, 0.2f);
		mark = new Geometry("BOOM!", sphere);
		Material mark_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mark_mat.setColor("Color", ColorRGBA.Red);
		mark.setMaterial(mark_mat);
		mark.setUserData("radius", 3.5f);
	}

	public static void initializeWater(SimpleApplication simpleApplication) {
		WaterFilter waterFilter = new WaterFilter(simpleApplication.getRootNode(), lightDirection);
		getFilterPostProcessor(simpleApplication).addFilter(waterFilter);
	}

	public static void initializeEnvironment(SimpleApplication simpleApplication) {
		DirectionalLight directionalLight = new DirectionalLight();
		directionalLight.setDirection(lightDirection);
		directionalLight.setColor(new ColorRGBA(1.0F, 1.0F, 1.0F, 1.0F));
		simpleApplication.getRootNode().addLight(directionalLight);
		simpleApplication.getRootNode()
				.attachChild(SkyFactory.createSky(simpleApplication.getAssetManager(), "Textures/cubes/sky.jpg", true));

		DirectionalLightShadowRenderer directionalLightShadowRenderer = new DirectionalLightShadowRenderer(
				simpleApplication.getAssetManager(), 2048, 3);
		directionalLightShadowRenderer.setLight(directionalLight);
		directionalLightShadowRenderer.setShadowIntensity(0.3F);
		simpleApplication.getViewPort().addProcessor(directionalLightShadowRenderer);
	}

	private static FilterPostProcessor getFilterPostProcessor(SimpleApplication simpleApplication) {
		List<SceneProcessor> sceneProcessors = simpleApplication.getViewPort().getProcessors();
		for (int i = 0; i < sceneProcessors.size(); i++) {
			SceneProcessor sceneProcessor = sceneProcessors.get(i);
			if ((sceneProcessor instanceof FilterPostProcessor)) {
				return (FilterPostProcessor) sceneProcessor;
			}
		}
		FilterPostProcessor filterPostProcessor = new FilterPostProcessor(simpleApplication.getAssetManager());
		simpleApplication.getViewPort().addProcessor(filterPostProcessor);
		return filterPostProcessor;
	}

	public static CubesSettings getSettings(Application application) {
		CubesSettings settings = new CubesSettings(application);
		settings.setDefaultBlockMaterial("Textures/cubes/terrain.png");
		return settings;
	}

	private void updateWeapon(float lastTimePerFrame) {
		// COMMENTATO PERCHE' PROVOCA RITARDO NEI MOVIMENTI DELL'ARMA
		// enqueue( new Callable<Void> () {
		//
		// public Void call() {
		if (!gameOver) {
			if (weaponShoot) {
				if (!firstShot && weapon.getAmmoPerClip() != 0) {
					weapon.shoot();
					firstShot = true;
					// audio_gun.setLooping(false);
					shoot = true;
					audio_gun.play();
				}
				shootTime += lastTimePerFrame;
				if (shootTime >= weapon.getRatio() && weapon.getAmmoPerClip() != 0) {
					weapon.shoot();
					shootTime = 0;
					if (weapon.getAmmoPerClip() != 0)
						audio_gun.playInstance();
					shoot = true;
					// collide con il punto sparato
					shoot();
				}
				ammoText.setText("" + weapon.getAmmoPerClip() + "/" + weapon.getAmmo());
				if (weapon.getAmmoPerClip() == 0)
					firstShot = false;
			}
			shoot = false;

			Vector3f vectorDifference = new Vector3f(cam.getLocation().subtract(gunNode.getWorldTranslation()));
			gunNode.setLocalTranslation(vectorDifference.addLocal(gunNode.getLocalTranslation()));

			Quaternion worldDiff = new Quaternion(cam.getRotation().mult(gunNode.getWorldRotation().inverse()));
			gunNode.setLocalRotation(worldDiff.multLocal(gunNode.getLocalRotation()));
			// Move gun to the bottom right of the screen
			gunNode.move(cam.getDirection().mult(3));
			gunNode.move(cam.getUp().mult(-0.8f));
			gunNode.move(cam.getLeft().mult(-1f));
			gunNode.rotate(0.0f, 0, 0);
		}
		// return null;
		// }
		// });
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

	private void makeEnemy(Vector3f position, Quaternion rotation, int id, String animation) {

		Spatial enemySpatial;
		Zombie zombie;

		enemySpatial = assetManager.loadModel("Zombie/ZombieCraftPlayerANIMATED.scene");

		MaterialList matList = (MaterialList) assetManager.loadAsset("Zombie/Player.material");

		OgreMeshKey key = new OgreMeshKey("Zombie/Cube.mesh.xml", matList);

		enemySpatial = assetManager.loadAsset(key);
		enemySpatial.updateModelBound();

		enemySpatial.scale(1.5f, 1.5f, 1.5f);
		enemySpatial.setName("Enemy");
		enemySpatial.addLight(new AmbientLight());

		// enemySpatial.lookAt(player.getPosition(), Vector3f.UNIT_Y);

		zombie = new Zombie();

		zombie.setX(position.getX());
		zombie.setY(position.getY() - 2);
		zombie.setZ(position.getZ());

		// enemySpatial.setLocalTranslation(zombie.getX(), zombie.getY(),
		// zombie.getZ());
		enemySpatial.setLocalTranslation(zombie.getX(), zombie.getY(), zombie.getZ());
		enemySpatial.setLocalRotation(rotation);
		enemySpatial.setUserData("radius", 3.5f);

		enemies.put(id, zombie);
		enemySpatials.put(id, enemySpatial);

		com.jme3.bullet.collision.shapes.CollisionShape enemyCollisionShape = CollisionShapeFactory
				.createDynamicMeshShape(enemySpatial);
		RigidBodyControl enemyCharacterControl = new RigidBodyControl(enemyCollisionShape);
		bulletAppState.getPhysicsSpace().add(enemyCharacterControl);
		enemySpatial.addControl(enemyCharacterControl);

		enemyCharacterControl.setAngularFactor(0);

		// enemyCharacterControls.put(id, enemyCharacterControl);

		shootables.attachChild(enemySpatial);

		rootNode.attachChild(enemySpatial);

		zombieControl = (enemySpatials.get(id).getControl(AnimControl.class));
		zombieControl.addListener(this);
		zombieChannel = zombieControl.createChannel();
	}

	private void shoot() {

		if (weapon.getAmmoPerClip() > 0) {
			// 1. Reset results list.
			CollisionResults results = getRayCastingResults(rootNode);
			// 2. Aim the ray from cam loc to cam direction.
			Ray ray = new Ray(cam.getLocation(), cam.getDirection());
			// 3. Collect intersections between Ray and Shootables in
			// results list.
			shootables.collideWith(ray, results);
			// 4. Use the results (we mark the hit object)
			if (results.size() > 0) {
				// The closest collision point is what was truly hit:
				CollisionResult closest = results.getClosestCollision();
				// Let's interact - we mark the hit with a red dot.
				mark.setLocalTranslation(closest.getContactPoint());
				// rootNode.attachChild(mark);

			}

		}
	}

	@Override
	public void destroy() {
		try {
			client.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "This client has been closed.");
		}
		super.destroy();
	}

	@Override
	public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {

	}

	@Override
	public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
		channel.setAnim("Walk");
	}

	private void initAudio() {
		/* gun shot sound is to be triggered by a mouse click. */
		audio_gun = new AudioNode(assetManager, "ak47/ak47-shot.wav", false);
		audio_gun.setPositional(false);
		audio_gun.setLooping(false);
		audio_gun.setVolume(10);

		players_audio_gun = new AudioNode(assetManager, "ak47/ak47-shot.wav", false);
		players_audio_gun.setPositional(false);
		players_audio_gun.setLooping(false);
		players_audio_gun.setVolume(6);

		audio_player_die = new AudioNode(assetManager, "audio/PlayerDie.wav", false);
		audio_player_die.setPositional(false);
		audio_player_die.setLooping(false);
		audio_player_die.setVolume(9);

		audio_zombie_attack = new AudioNode(assetManager, "audio/ZombieAttack.wav", false);
		audio_zombie_attack.setPositional(false);
		audio_zombie_attack.setLooping(false);
		audio_zombie_attack.setVolume(10);

		audio_background = new AudioNode(assetManager, "audio/BackTrack.wav", false);
		audio_background.setPositional(false);
		audio_background.setLooping(true);
		audio_background.setVolume(1);

		audio_appear_powerup = new AudioNode(assetManager, "audio/PowerUpAppear.wav", false);
		audio_appear_powerup.setPositional(false);
		audio_appear_powerup.setLooping(false);
		audio_appear_powerup.setVolume(13);

		audio_pick_ammo = new AudioNode(assetManager, "audio/pickAmmoCube.wav", false);
		audio_pick_ammo.setPositional(false);
		audio_pick_ammo.setLooping(false);
		audio_pick_ammo.setVolume(13);

		audio_pick_health = new AudioNode(assetManager, "audio/pickHealthCube.wav", false);
		audio_pick_health.setPositional(false);
		audio_pick_health.setLooping(false);
		audio_pick_health.setVolume(13);

		audio_background.play();
	}

}
