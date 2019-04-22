package it.unical.mat.igpe.ZombieCraft;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.cubes.BlockChunkControl;
import com.cubes.BlockChunkListener;
import com.cubes.BlockNavigator;
import com.cubes.BlockTerrainControl;
import com.cubes.CubesSettings;
import com.cubes.Vector3Int;
import com.cubes.network.CubesSerializer;
import com.cubes.test.CubesTestAssets;
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
import com.jme3.input.controls.MouseAxisTrigger;
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
import it.unical.mat.igpe.ZombieCraft.Data.Spawner.EnemySpawner;
import it.unical.mat.igpe.ZombieCraft.Data.Weapons.AK47;
import it.unical.mat.igpe.ZombieCraft.Utilities.Scoreboard;
import it.unical.mat.igpe.ZombieCraft.Utilities.Settings;
import it.unical.mat.igpe.ZombieCraft.Utilities.Utility;

public class GameManager extends SimpleApplication implements ActionListener, AnimEventListener {

	private Picture healthPic = new Picture("Health Picture");
	private Picture ammoPic = new Picture("Ammo Picture");

	private BitmapFont myFont;
	private String filePath;

	private Node terrainNode;
	private BlockTerrainControl blockTerrain;
	private WoodCube woodCube;
	private DirtCube dirtCube;
	private StoneCube stoneCube;
	private GlassCube glassCube;
	private BrickCube brickCube;
	private IceCube iceCube;
	private AmmoCube ammoCube;
	private EnemySpawnerCube enemySpawnerCube;

	private int killForHealth = 0;
	private float timerAmmo = 0;
	private Random random = new Random();
	public boolean audio_dead = true;
	public boolean gameOver;
	public boolean pause;
	private Player player;
	private BulletAppState bulletAppState;
	private Vector<Enemy> enemies;
	private Vector<Spatial> enemySpatials;
	private Vector<EnemySpawner> enemySpawners;
	private Map<Vector3f, Cube> powerups;

	BitmapText crosshair;
	private BitmapText ammoText;
	private BitmapText gameOverText;
	private BitmapText pauseText;
	private BitmapText pauseSubText;
	private BitmapText gameOverSubText;
	private boolean ak47Shoot;
	private boolean strafe_left;
	private float shootTime;
	private float spawnTimeCounter;
	private Node shootables;
	private boolean firstShot;
	private AudioNode audio_gun;
	private AudioNode audio_player_die;
	private AudioNode audio_zombie_attack;
	private AudioNode audio_background;
	private AudioNode audio_pick_health;
	private AudioNode audio_pick_ammo;
	private AudioNode audio_appear_powerup;
	private AK47 ak47;
	float gameOverCount = 0.0F;
	private Node gunNode;
	private Node mainNode = new Node("Main Node");
	private Geometry mark;
	private Vector3f walkDirection = new Vector3f();
	private boolean[] arrowKeys = new boolean[4];
	private CharacterControl playerControl;
	private Node playerSpatial;
	private CameraNode cameraNode;
	private BitmapText healthText;
	private BitmapText scoreText;
	private CubesSettings cubesSettings;
	private AnimChannel zombieChannel;
	private AnimControl zombieControl;

	private static final Vector3f lightDirection = new Vector3f(-0.8F, -1.0F, -0.8F).normalizeLocal();

	public static void main(String[] args) {
		Logger.getLogger("").setLevel(Level.SEVERE);
		GameManager app = new GameManager();
		app.start();
	}

	public GameManager() {
		Logger.getLogger("com.jme3").setLevel(Level.SEVERE);

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

	@Override
	public void simpleInitApp() {
		// toglie l'esc di JM
		cubesSettings = getSettings(this);
		terrainNode = new Node();
		load();
		terrainNode.setShadowMode(RenderQueue.ShadowMode.Cast);
		initializeEnvironment(this);
		initializeWater(this);
		inputManager.deleteMapping(SimpleApplication.INPUT_MAPPING_EXIT);

		gameOver = false;
		flyCam.setMoveSpeed(0);
		flyCam.setZoomSpeed(0);
		player = new Player();
		bulletAppState = new BulletAppState();
		stateManager.attach(bulletAppState);

		powerups = new HashMap<Vector3f, Cube>();
		enemySpawners = new Vector<EnemySpawner>();

		initControls();

		initBlockTerrain();
		initPlayer();
		initWeapon();

		initAudio();
		initCamera();

		initGUI();

		initMark();

		shootables = new Node("Shootables Objects");
		rootNode.attachChild(shootables);

		enemies = new Vector<Enemy>();
		enemySpatials = new Vector<Spatial>();
		mainNode.setUserData("radius", 3.5f);

		gunNode.attachChild(audio_gun);
		gunNode.attachChild(audio_player_die);
		cam.lookAtDirection(new Vector3f(1.0F, 0.0F, 1.0F), Vector3f.UNIT_Y);

		initSpawners();
		assetManager.registerLocator("resources/", FileLocator.class.getName());

	}

	private void load() {
		FileInputStream fileInputStream = null;

		File file = new File(filePath);
		if (file.exists()) {
			byte[] bFile = new byte[(int) file.length() + 1024];

			try {
				// convert file into array of bytes
				fileInputStream = new FileInputStream(file);
				fileInputStream.read(bFile);
				fileInputStream.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

			blockTerrain = new BlockTerrainControl(CubesTestAssets.getSettings(this), new Vector3Int(5, 1, 5));
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

		} else {
			Exception e = new FileNotFoundException(
					"The file you are trying to load could be corrupted or maybe has been deleted.");
			System.err.println(e.getMessage());
		}
	}

	@Override
	public void simpleUpdate(float lastTimePerFrame) {
		if (!gameOver) {
			if (!pause) {

				if (cam.getUp().y < 0) {
					cam.lookAtDirection(new Vector3f(0, cam.getDirection().y, 0),
							new Vector3f(cam.getUp().x, 0, cam.getUp().z));
				} else if (cam.getUp().y > 180) {
					cam.lookAtDirection(new Vector3f(0, cam.getDirection().y, 0),
							new Vector3f(cam.getUp().x, 0, cam.getUp().z));
				}

				updateSpawner(lastTimePerFrame);
				updatePlayer(lastTimePerFrame);

				updateWeapon(lastTimePerFrame);

				updateZombie(lastTimePerFrame);

				dropPowerUps(lastTimePerFrame);

			}
		} else {
			pause = true;

			guiNode.attachChild(gameOverText);
			guiNode.attachChild(gameOverSubText);
			crosshair.removeFromParent();

			if (audio_dead) {
				audio_dead = false;
				audio_player_die.play();
			}
			gameOverCount += lastTimePerFrame;
			if (gameOverCount >= 4.0F) {
				Scoreboard.loadScoreBoard();
				Scoreboard.addScore(player.getScore(), Settings.options[0]);

				this.stop();

			}
		}
	}

	@Override
	public void stop() {
		super.stop(); // continue quitting the game
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

		// rotazioni
		inputManager.addMapping("strafe_left", new Trigger[] { new MouseAxisTrigger(MouseInput.AXIS_X, false) });
		inputManager.addListener(this, new String[] { "strafe_left" });
		inputManager.addMapping("strafe_right", new Trigger[] { new MouseAxisTrigger(MouseInput.AXIS_X, true) });
		inputManager.addListener(this, new String[] { "strafe_right" });

		// salta
		inputManager.addListener(this, new String[] { "jump" });

		// spara
		inputManager.addMapping("shoot", new Trigger[] { new MouseButtonTrigger(MouseInput.BUTTON_LEFT) });
		inputManager.addListener(this, new String[] { "shoot" });

		// ricarica
		inputManager.addMapping("recharge", new Trigger[] { new KeyTrigger(KeyInput.KEY_R) });
		inputManager.addListener(this, new String[] { "recharge" });

		// pausa
		inputManager.addMapping("pause", new Trigger[] { new KeyTrigger(KeyInput.KEY_P) });
		inputManager.addListener(this, new String[] { "pause" });

		// esci (solo con pausa inserita)
		inputManager.addMapping("exit", new Trigger[] { new KeyTrigger(KeyInput.KEY_ESCAPE) });
		inputManager.addListener(this, new String[] { "exit" });
	}

	private void initBlockTerrain() {

		blockTerrain = new BlockTerrainControl(getSettings(this), new Vector3Int(5, 1, 5));
		blockTerrain.setBlockArea(new Vector3Int(1, 1, 1), new Vector3Int(100, 1, 100), Utility.DIRT_CUBE);

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

	private void initGUI() {
		myFont = assetManager.loadFont("font/MineCrafter3.fnt");

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

		pauseText = new BitmapText(myFont);
		pauseText.setText("PAUSE");
		pauseText.setSize((80 * (settings.getWidth() * settings.getHeight())) / (1920 * 1080));

		pauseText.setLocalTranslation(
				((settings.getWidth() / 2 - guiFont.getCharSet().getRenderedSize() / 3 * 2))
						- (pauseText.getLineWidth() / 2.0F) + 4.0F,
				settings.getHeight() / 2 + pauseText.getLineHeight() / 2.0F, 0.0F);

		pauseSubText = new BitmapText(myFont);
		pauseSubText.setText("Press ESC button to exit");
		pauseSubText.setSize((40 * (settings.getWidth() * settings.getHeight())) / (1920 * 1080));

		pauseSubText.setLocalTranslation(
				((settings.getWidth() / 2 - guiFont.getCharSet().getRenderedSize() / 3 * 2))
						- (pauseSubText.getLineWidth() / 2.0F) + 4.0F,
				pauseText.getLocalTranslation().y - (85.0F * settings.getHeight()) / 1080, 0.0F);

		ammoText = new BitmapText(myFont);
		ammoText.setText("" + ak47.getAmmoPerClip() + "/" + ak47.getAmmo());
		ammoText.setSize((60 * (settings.getWidth() * settings.getHeight())) / (1920 * 1080));
		ammoText.setLocalTranslation((470F * settings.getWidth()) / 1920, (128F * settings.getHeight()) / 1080F, 0.0F);

		healthText = new BitmapText(myFont, true);
		healthText.setText("" + player.getHealthPoints());
		healthText.setSize((60 * (settings.getWidth() * settings.getHeight())) / (1920 * 1080));

		healthText.setLocalTranslation((160F * settings.getWidth()) / 1920, (128F * settings.getHeight()) / 1080F, 0F);

		scoreText = new BitmapText(myFont);
		scoreText.setText("  SCORE: " + player.getScore());
		scoreText.setSize((60 * (settings.getWidth() * settings.getHeight())) / (1920 * 1080));
		scoreText.setLocalTranslation(-5F, settings.getHeight() - 20F, 0F);

		healthPic.setImage(assetManager, "img/hud/lifeHeart.png", true);
		healthPic.setWidth((300 * settings.getWidth()) / 1920);
		healthPic.setHeight((123 * settings.getHeight()) / 1080);
		healthPic.setLocalTranslation((6F * settings.getWidth()) / 1920, (35F * settings.getHeight()) / 1080F, 0F);
		guiNode.attachChild(healthPic);

		ammoPic.setImage(assetManager, "img/hud/ammo.png", true);
		ammoPic.setWidth((116 * settings.getWidth()) / 1920);
		ammoPic.setHeight((123 * settings.getHeight()) / 1080);
		ammoPic.setLocalTranslation((320F * settings.getWidth()) / 1920, (35F * settings.getHeight()) / 1080F, 0F);
		guiNode.attachChild(ammoPic);

		guiNode.attachChild(healthText);
		guiNode.attachChild(scoreText);
		guiNode.attachChild(ammoText);

	}

	@Override
	public void onAction(String actionName, boolean keyPressed, float lastTimePerFrame) {
		if (actionName.equals("pause") && keyPressed) {
			pause = !pause;
			if (pause == true) {
				guiNode.attachChild(pauseText);
				guiNode.attachChild(pauseSubText);
				crosshair.removeFromParent();

				for (Spatial s : enemySpatials) {
					s.getControl(RigidBodyControl.class).setKinematic(true);

				}

			} else {
				guiNode.attachChild(crosshair);
				pauseText.removeFromParent();
				pauseSubText.removeFromParent();

				for (Spatial s : enemySpatials) {
					s.getControl(RigidBodyControl.class).setKinematic(false);
				}

			}
		}

		if (actionName.equals("exit") && keyPressed && pause) {
			this.stop();
		}

		if (!pause) {
			if (actionName.equals("move_up")) {
				arrowKeys[0] = keyPressed;
				player.setAction("Walk");
			} else if (actionName.equals("move_right")) {
				arrowKeys[1] = keyPressed;
			} else if (actionName.equals("move_left")) {
				arrowKeys[3] = keyPressed;
			} else if (actionName.equals("move_down")) {
				arrowKeys[2] = keyPressed;
				player.setAction("Walk");
			} else if (actionName.equals("jump")) {
				playerControl.jump();
			} else if (actionName.equals("shoot")) {
				ak47Shoot = keyPressed;
				player.setAction("Punch");
				shoot();

			} else if (actionName.equals("recharge")) {

				ak47.recharge();
				ammoText.setText("" + ak47.getAmmoPerClip() + "/" + ak47.getAmmo());
			} else if (actionName.equals("strafe_left")) {
				strafe_left = true;
			} else
				strafe_left = false;
		}
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

	private void updateSpawner(float lastTimePerFrame) {

		spawnTimeCounter += lastTimePerFrame;

		Vector<EnemySpawner> spawnersFireable = new Vector<EnemySpawner>();
		EnemySpawner tmp = null;

		for (int i = 0; i < enemySpawners.size(); i++) {
			tmp = enemySpawners.get(i);
			if (spawnTimeCounter >= tmp.getSpawnTime() && tmp.getCurrentSlots() < tmp.getMaxSlots())
				spawnersFireable.add(tmp);
		}
		int rand = 0;

		if (spawnersFireable.size() != 0) {
			rand = ThreadLocalRandom.current().nextInt(0, spawnersFireable.size());

			tmp = spawnersFireable.get(rand);
			makeEnemy(tmp.getPosition());

			tmp.setCurrentSlots(tmp.getCurrentSlots() + 1);

			spawnTimeCounter = 0;
		}

	}

	private void makeEnemy(Vector3Int position) {

		Spatial enemySpatial;
		Zombie zombie;

		enemySpatial = assetManager.loadModel("Zombie/ZombieCraftPlayerANIMATED.scene");

		MaterialList matList = (MaterialList) assetManager.loadAsset("Zombie/Player.material");
		OgreMeshKey key = new OgreMeshKey("Zombie/Cube.mesh.xml", matList);

		// enemySpatial = (Node)
		// assetManager.loadModel("PlayerNew/ZombieCraftPlayerWithWeapon.scene");

		// MaterialList matList = (MaterialList)
		// assetManager.loadAsset("PlayerNew/Player.material");
		// OgreMeshKey key = new OgreMeshKey("PlayerNew/Cube.mesh.xml",
		// matList);

		enemySpatial = assetManager.loadAsset(key);
		enemySpatial.updateModelBound();

		enemySpatial.scale(1.5f, 1.5f, 1.5f);
		enemySpatial.setName("Enemy");
		enemySpatial.addLight(new AmbientLight());

		zombie = new Zombie();

		zombie.setX(position.getX());
		zombie.setY(position.getY() - 2);
		zombie.setZ(position.getZ());

		enemySpatial.setLocalTranslation(zombie.getX(), zombie.getY(), zombie.getZ());
		enemySpatial.setUserData("radius", 3.5f);
		enemies.add(zombie);
		enemySpatials.add(enemySpatial);

		com.jme3.bullet.collision.shapes.CollisionShape enemyCollisionShape = CollisionShapeFactory
				.createDynamicMeshShape(enemySpatial);
		RigidBodyControl enemyCharacterControl = new RigidBodyControl(enemyCollisionShape);
		bulletAppState.getPhysicsSpace().add(enemyCharacterControl);
		enemySpatial.addControl(enemyCharacterControl);

		enemyCharacterControl.setAngularFactor(0);

		// ANIMAZIONI
		zombieControl = (enemySpatial.getControl(AnimControl.class));
		zombieControl.addListener(this);
		zombieChannel = zombieControl.createChannel();
		zombieChannel.setAnim("Walk");

		enemySpatial.addControl(zombieControl);

		shootables.attachChild(enemySpatial);

		rootNode.attachChild(enemySpatial);
	}

	private void updateWeapon(float lastTimePerFrame) {

		if (ak47Shoot) {
			if (!firstShot && ak47.getAmmoPerClip() != 0) {
				ak47.shoot();
				firstShot = true;
				audio_gun.setLooping(false);
				audio_gun.play();
			}
			shootTime += lastTimePerFrame;
			if (shootTime >= ak47.getRatio() && ak47.getAmmoPerClip() != 0) {
				ak47.shoot();
				shootTime = 0;
				if (ak47.getAmmoPerClip() != 0)
					audio_gun.playInstance();
				// collide con il punto sparato
				shoot();
			}
			ammoText.setText("" + ak47.getAmmoPerClip() + "/" + ak47.getAmmo());
			if (ak47.getAmmoPerClip() == 0)
				firstShot = false;
		}

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

	private void initWeapon() {
		ak47 = new AK47();
		drawWeapon();

	}

	private void drawWeapon() {
		assetManager.registerLocator("ProjectAssets/", FileLocator.class.getName());

		gunNode = (Node) assetManager.loadModel("ak47/AK47.scene");
		gunNode.addLight(new AmbientLight());
		gunNode.scale(0.3f, 0.3f, 0.3f);

		gunNode.setLocalTranslation(-1, 2, 2);
		mainNode.attachChild(gunNode);
	}

	private void shoot() {

		if (ak47.getAmmoPerClip() > 0) {
			// 1. Reset results list.
			CollisionResults results = getRayCastingResults(rootNode);
			// 2. Aim the ray from cam loc to cam direction.
			Ray ray = new Ray(cam.getLocation(), cam.getDirection());
			// 3. Collect intersections between Ray and Shootables in
			// results list.
			shootables.collideWith(ray, results);
			// 5. Use the results (we mark the hit object)
			if (results.size() > 0) {
				// The closest collision point is what was truly hit:
				CollisionResult closest = results.getClosestCollision();
				// Let's interact - we mark the hit with a red dot.
				mark.setLocalTranslation(closest.getContactPoint());
				// rootNode.attachChild(mark);

				for (int i = 0; i < enemySpatials.size(); i++) {
					if (checkCollision(mark, enemySpatials.get(i))) {
						enemies.get(i).decreaseHealthPoints(ak47.getDamage());
						mark.setLocalTranslation(Vector3f.NAN);
						break;
					}

				}

			}
		}
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

	private boolean checkCollision(Spatial a, Spatial b) {

		float distance = a.getLocalTranslation().distance(b.getLocalTranslation());
		float maxDistance = (Float) a.getUserData("radius") + (Float) b.getUserData("radius");
		return distance <= maxDistance;

	}

	private boolean checkCollisionWithTerrain(Spatial a, Spatial b) {

		Vector3f v1 = a.getLocalTranslation();
		Vector3f v2 = b.getLocalTranslation();

		double distance = 666;

		if (v1.x >= (v2.x - 1) && v1.x <= (v2.x + 1) && v1.z >= (v2.z - 1) && v1.z >= (v2.z + 1)) {
			distance = Math.sqrt((v1.y - v2.y));
		}

		return distance != 666;

	}

	private void updatePlayer(float lastTimePerFrame) {

		if (player.getHealthPoints() <= 0 || player.getY() < -15)
			gameOver = true;

		Vector3f camDir = cam.getDirection().multLocal(player.getMoveSpeed());
		Vector3f camLeft = cam.getLeft().multLocal(player.getMoveSpeed());
		walkDirection.set(0.0F, 0.0F, 0.0F);

		if (arrowKeys[0] != true) {
			walkDirection.addLocal(camDir);
			player.setAction("Walk");
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

			player.setPosition(playerControl.getPhysicsLocation());
			mainNode.setLocalTranslation(player.getX(), player.getY(), player.getZ());
			playerSpatial.setLocalTranslation(
					new Vector3f(mainNode.getLocalTranslation().x, mainNode.getLocalTranslation().y - 2,
							mainNode.getLocalTranslation().z).clone().divideLocal(playerSpatial.getLocalTranslation()));

			cameraNode.setLocalTranslation(mainNode.getLocalTranslation());
			Vector3f playerPosition = player.getPosition();

			for (Vector3f key : powerups.keySet()) {
				if (Math.abs(key.x - playerPosition.x) < 5f && Math.abs(key.z - playerPosition.z) < 5f) {

					if (powerups.get(key) instanceof AmmoCube) {
						blockTerrain.removeBlock(((AmmoCube) powerups.get(key)).getBlockLocation());
						ak47.setAmmo(60);
						ammoText.setText("" + ak47.getAmmoPerClip() + "/" + ak47.getAmmo());
						audio_pick_ammo.play();
					} else {
						blockTerrain.removeBlock(((HealthCube) powerups.get(key)).getBlockLocation());
						player.addHealthPoints(25);
						healthText.setText("" + player.getHealthPoints());
						audio_pick_health.play();
					}
					powerups.remove(key);
					break;
				}

			}

			cameraNode.lookAt(mainNode.getLocalTranslation(), Vector3f.UNIT_Y);
		}
	}

	private void updateZombie(float lastTimePerFrame) {

		try {
			for (int i = 0; i < enemies.size(); i++) {

				Enemy e = enemies.get(i);
				Spatial eS = enemySpatials.get(i);

				if (e.getHealthPoints() <= 0 || e.getY() < -15) {
					rootNode.detachChild(enemySpatials.get(i));
					bulletAppState.getPhysicsSpace().remove(enemySpatials.get(i));
					shootables.detachChild(eS);
					enemySpatials.remove(i);
					enemies.remove(i);
					System.gc();
					player.addScore(15);
					killForHealth++;
					scoreText.setText("  SCORE: " + player.getScore());
					i--;

					int rand = 0;
					do {
						rand = ThreadLocalRandom.current().nextInt(0, 3 + 1);

					} while (enemySpawners.get(rand).getCurrentSlots() == 0);

					enemySpawners.get(rand).decreaseCurrentSlots();

				} else {

					for (int l = 0; l < terrainNode.getChildren().size(); l++)
						if (checkCollisionWithTerrain(eS, terrainNode.getChild(l)))
							eS.getControl(RigidBodyControl.class).setLinearVelocity(new Vector3f(0, 6, 0));

					boolean collide = false;

					for (int j = i + 1; j < enemies.size() - 1; j++) {

						Spatial eS2 = enemySpatials.get(j);
						collide = checkCollision(eS, eS2);
						if (collide) {
							// spostati a destra

							eS.getControl(RigidBodyControl.class).setLinearVelocity(
									new Vector3f(4, eS.getControl(RigidBodyControl.class).getLinearVelocity().y, 0));

						}
					}

					e.increaseTimeCounter(lastTimePerFrame);
					e.setTarget(player);
					e.followTarget();

					RigidBodyControl rb = eS.getControl(RigidBodyControl.class);

					Vector3f v = new Vector3f(0, rb.getLinearVelocity().y, 0);

					Vector3f playerPosition = e.getTarget().getPosition();
					if (playerPosition.getX() < eS.getLocalTranslation().x) {
						v.x -= 6.5f;
					}

					if (playerPosition.getX() > eS.getLocalTranslation().x) {
						v.x += 6.5f;
					}

					if (playerPosition.getZ() < eS.getLocalTranslation().z) {
						v.z -= 6.5f;
					}

					if (playerPosition.getZ() > eS.getLocalTranslation().z) {
						v.z += 6.5f;
					}
					Vector3f monsterLocation = eS.getLocalTranslation();

					Vector3f playerDirection = playerPosition.subtract(monsterLocation);

					playerDirection.y = 0;

					Quaternion targetRotation = new Quaternion();

					targetRotation.lookAt(playerDirection, Vector3f.UNIT_Y);

					rb.setPhysicsRotation(targetRotation);

					enemySpatials.get(i).getControl(RigidBodyControl.class).setLinearVelocity(v);

					handleCollisions((Node) eS);
				}

			}

		} catch (NullPointerException npe) {
			// Ignore
		}

	}

	private void initPlayer() {
		drawPlayer();

		playerControl = new CharacterControl(
				new CapsuleCollisionShape(cubesSettings.getBlockSize() / 2.0F, cubesSettings.getBlockSize() * 2.0F),
				0.05F);
		playerControl.setJumpSpeed(20.0F);
		playerControl.setFallSpeed(40.0F);
		playerControl.setGravity(70.0F);
		playerControl.setSpatial(playerSpatial);
		player.setPosition(50, 100, 50);
		mainNode.setLocalTranslation(player.getX(), player.getY(), player.getZ());
		playerControl.setPhysicsLocation(mainNode.getLocalTranslation());
		bulletAppState.getPhysicsSpace().add(playerControl);
		rootNode.attachChild(mainNode);
	}

	private void handleCollisions(Node enemySpatial) {

		for (int i = 0; i < enemies.size(); i++) {
			if (enemies.get(i).getHealthPoints() > 0) {
				if (checkCollision(mainNode, enemySpatials.get(i)) && enemies.get(i).canAttack()) {

					hitPlayer(enemies.get(i).getDamage());
					audio_zombie_attack.play();
					enemies.get(i).resetTimeCounter();
					// zombieControl =
					// (enemySpatials.get(i).getControl(AnimControl.class));
					// zombieChannel.setAnim("Attack");
				}
			}
		}
	}

	public void removeItem(Vector3f location) {
		if (powerups.containsKey(location)) {
			if (powerups.get(location) instanceof AmmoCube)
				blockTerrain.removeBlock(((AmmoCube) powerups.get(location)).getBlockLocation());
			else
				blockTerrain.removeBlock(((HealthCube) powerups.get(location)).getBlockLocation());
		}
	}

	public void dropPowerUps(float tpf) {

		timerAmmo += tpf;

		if (timerAmmo > 8) {
			timerAmmo = 0;

			Vector3f location = new Vector3f(random.nextInt(300) + 1, 200F, random.nextInt(300) + 1);
			Vector3f direction = new Vector3f(0.0F, -1.0F, 0.0F);

			Vector3Int blockLocation = getCurrentPointedBlockLocation(true, location, direction);

			if (blockLocation != null) {

				AmmoCube ac = new AmmoCube(blockLocation);
				powerups.put(location, ac);
				audio_appear_powerup.play();
				blockTerrain.setBlock(blockLocation, ac);
			}

		}

		int randKill = ThreadLocalRandom.current().nextInt(8, 13 + 1);

		if (killForHealth >= randKill) {

			killForHealth = 0;
			Vector3f location = new Vector3f(random.nextInt(300) + 1, 200F, random.nextInt(300) + 1);
			Vector3f direction = new Vector3f(0.0F, -1.0F, 0.0F);

			Vector3Int blockLocation = getCurrentPointedBlockLocation(true, location, direction);

			if (blockLocation != null) {

				HealthCube hc = new HealthCube(blockLocation);
				powerups.put(location, hc);
				audio_appear_powerup.play();
				blockTerrain.setBlock(blockLocation, hc);
			}

		}

	}

	private void drawPlayer() {
		assetManager.registerLocator("ProjectAssets/", FileLocator.class.getName());
		playerSpatial = (Node) assetManager.loadModel("Player/ZombieCraftPlayerANIMATED.scene");

		MaterialList matList = (MaterialList) assetManager.loadAsset("Player/Player.material");
		OgreMeshKey key = new OgreMeshKey("Player/Cube.mesh.xml", matList);
		playerSpatial = (Node) assetManager.loadAsset(key);
		playerSpatial.addLight(new AmbientLight());
		playerSpatial.updateModelBound();
		playerSpatial.scale(1.5f, 1.5f, 1.5f);

		mainNode.setLocalTranslation(player.getX(), player.getY(), player.getZ());
		// mainNode.attachChild(playerSpatial);
		rootNode.attachChild(mainNode);

	}

	private void hitPlayer(int damage) {
		player.decreaseHealthPoints(damage);
		healthText.setText("" + player.getHealthPoints());
	}

	private void initMark() {
		Sphere sphere = new Sphere(30, 30, 0.2f);
		mark = new Geometry("BOOM!", sphere);
		Material mark_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mark_mat.setColor("Color", ColorRGBA.Red);
		mark.setMaterial(mark_mat);
		mark.setUserData("radius", 3.5f);
	}

	private void initAudio() {
		/* gun shot sound is to be triggered by a mouse click. */
		audio_gun = new AudioNode(assetManager, "ak47/ak47-shot.wav", false);
		audio_gun.setPositional(false);
		audio_gun.setLooping(false);
		audio_gun.setVolume(10);

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

	private void initSpawners() {

		EnemySpawnerCube enemySpawnerCube = new EnemySpawnerCube(new Vector3Int(0, 30, 0));

		enemySpawners.add(enemySpawnerCube.getEnemySpawner());

		EnemySpawnerCube enemySpawnerCube2 = new EnemySpawnerCube(new Vector3Int(230, 25, 0));

		enemySpawners.add(enemySpawnerCube2.getEnemySpawner());

		EnemySpawnerCube enemySpawnerCube3 = new EnemySpawnerCube(new Vector3Int(230, 25, 230));

		enemySpawners.add(enemySpawnerCube3.getEnemySpawner());

		EnemySpawnerCube enemySpawnerCube4 = new EnemySpawnerCube(new Vector3Int(0, 25, 235));

		enemySpawners.add(enemySpawnerCube4.getEnemySpawner());

	}

	private void initCamera() {

		cameraNode = new CameraNode("Camera Node", cam);

		cameraNode.setControlDir(ControlDirection.CameraToSpatial);

		cameraNode.setLocalTranslation(0, 0, 0);

		cameraNode.lookAt(mainNode.getLocalTranslation(), Vector3f.UNIT_Y);

		mainNode.attachChild(cameraNode);

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

	@Override
	public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {

	}

	@Override
	public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
		if (!pause) {

			channel.setAnim("Walk");
		} else {
			channel.setAnim("Idle");
		}
	}

	private Vector3Int getCurrentPointedBlockLocation(boolean getNeighborLocation, Vector3f location,
			Vector3f direction) {
		CollisionResults results = getRayCastingResults(terrainNode, location, direction);
		if (results.size() > 0) {
			Vector3f collisionContactPoint = results.getClosestCollision().getContactPoint();
			return BlockNavigator.getPointedBlockLocation(blockTerrain, collisionContactPoint, getNeighborLocation);
		}
		return null;
	}

	private CollisionResults getRayCastingResults(Node node, Vector3f location, Vector3f direction) {
		Ray ray = new Ray(location, direction);
		CollisionResults results = new CollisionResults();
		node.collideWith(ray, results);
		return results;
	}

}
