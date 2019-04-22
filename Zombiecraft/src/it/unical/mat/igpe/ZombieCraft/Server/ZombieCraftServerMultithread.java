package it.unical.mat.igpe.ZombieCraft.Server;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import com.cubes.BlockChunkControl;
import com.cubes.BlockChunkListener;
import com.cubes.BlockNavigator;
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
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.MeshCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.collision.CollisionResults;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.material.MaterialList;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.network.ConnectionListener;
import com.jme3.network.HostedConnection;
import com.jme3.network.Network;
import com.jme3.network.Server;
import com.jme3.network.serializing.Serializer;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.SceneProcessor;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.plugins.ogre.OgreMeshKey;
import com.jme3.scene.shape.Sphere;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeContext;
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
import it.unical.mat.igpe.ZombieCraft.Network.Messages.EnemyMessage;
import it.unical.mat.igpe.ZombieCraft.Network.Messages.HelloMessage;
import it.unical.mat.igpe.ZombieCraft.Network.Messages.MapMessage;
import it.unical.mat.igpe.ZombieCraft.Network.Messages.PlayerListMessage;
import it.unical.mat.igpe.ZombieCraft.Network.Messages.PlayerMessage;
import it.unical.mat.igpe.ZombieCraft.Network.Messages.PowerupMessage;
import it.unical.mat.igpe.ZombieCraft.Utilities.Utility;

public class ZombieCraftServerMultithread extends SimpleApplication implements ConnectionListener, AnimEventListener {
	public static JFileChooser modifyChooser;
	private static int port;

	private static File mapDirectory = new File(System.getProperty("user.home") + "/Documents/ZombieMap");

	private static final Vector3f lightDirection = new Vector3f(-0.8F, -1.0F, -0.8F).normalizeLocal();
	private String filePath;

	public static void main(String[] args) {
		Logger.getLogger("").setLevel(Level.SEVERE);
		Logger.getLogger("com.jme3").setLevel(Level.SEVERE);
		try {
			port = Integer.parseInt(JOptionPane.showInputDialog("Server Port", 6143));

			UIManager.put("FileChooser.readOnly", Boolean.TRUE);
			modifyChooser = new JFileChooser();

			modifyChooser.setDialogTitle("Choose Map");
			if (!mapDirectory.exists())
				mapDirectory.mkdirs();
			modifyChooser.setCurrentDirectory(mapDirectory);

			int option = modifyChooser.showOpenDialog(null);
			if (option == JFileChooser.APPROVE_OPTION) {

				Utility.filePath = modifyChooser.getSelectedFile().getAbsolutePath();

				ZombieCraftServerMultithread zombieServer = new ZombieCraftServerMultithread();

				AppSettings newSettings = new AppSettings(true);

				newSettings.setFrameRate(60);

				zombieServer.setSettings(newSettings);

				String[] options = new String[] { "Headless", "Spectator" };
				int response = JOptionPane.showOptionDialog(null, "Server or Spectator mode?", "Server Camera Options",
						JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

				switch (response) {
				case 0:
					zombieServer.start(JmeContext.Type.Headless);
					break;
				case 1:
					zombieServer.start();
					break;
				default:
					JOptionPane.showMessageDialog(null, "Server Closed");
				}

				// zombieServer.start();
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Invalid Port");
		}

		// filePath = args[0];

		// zombieServer.start();
	}

	private Server server;
	private DirtCube dirtCube;
	private WoodCube woodCube;
	private StoneCube stoneCube;
	private EnemySpawnerCube enemySpawnerCube;
	private GlassCube glassCube;
	private BrickCube brickCube;
	private IceCube iceCube;
	private AmmoCube ammoCube;
	private Cube[] cubes;
	private BlockTerrainControl blockTerrain;
	private CubesSettings cubesSettings;
	private Node terrainNode;
	private Vector<EnemySpawner> enemySpawners;
	private BulletAppState bulletAppState;
	private ServerListener serverListener;
	private Geometry mark;
	private List<Node> clientMainNodes;
	private Map<Integer, Spatial> clientSpatials;
	private Map<Integer, Spatial> weaponSpatials;
	private Map<Integer, CharacterControl> clientCharacterControls;
	private Map<Integer, Player> clientPlayers;
	private float spawnTimeCounter;
	private Vector<Enemy> enemies;
	private Vector<Spatial> enemySpatials;
	private Node shootables;
	private int killForHealth = 0;
	private float timerAmmo = 0;
	private Random random = new Random();
	private Map<Vector3f, Cube> powerups;
	private float gameOverCounter = 0;
	private AnimControl playerAnimControl;
	private AnimChannel playerAnimChannel;

	@Override
	public void simpleInitApp() {

		flyCam.setMoveSpeed(50);
		cam.setLocation(new Vector3f(0, 100, 0));
		cam.lookAt(new Vector3f(50,50,50), Vector3f.UNIT_Y);

		setPauseOnLostFocus(false);
		setDisplayStatView(false);
		setDisplayFps(false);

		Serializer.registerClass(MapMessage.class);
		Serializer.registerClass(PlayerMessage.class);
		Serializer.registerClass(HelloMessage.class);
		Serializer.registerClass(PlayerListMessage.class);
		Serializer.registerClass(EnemyMessage.class);
		Serializer.registerClass(PowerupMessage.class);
		try {
			server = Network.createServer(port);
			server.start();

			server.addConnectionListener(this);
			String serverIP = InetAddress.getLocalHost().getHostAddress();
			StringSelection stringToSave = new StringSelection(serverIP);

			String[] options = new String[] { "OK", "Copy IP" };
			int response = JOptionPane.showOptionDialog(null, "YOUR IP ADDRESS: " + serverIP + "\n YOUR PORT: " + port,
					"Server Options", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

			switch (response) {

			case 0: {
				JOptionPane.showMessageDialog(null, "Server Started");
				initGame();
				break;
			}
			case 1: {
				Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
				clpbrd.setContents(stringToSave, null);
				JOptionPane.showMessageDialog(null, "Server IP copied in clipboard");
				initGame();
				break;
			}
			default: {
				this.stop();
			}
			}

		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Cannot Start Server");
		}

	}

	public void initGame() {
		// settings = new AppSettings(true);
		// settings.setFrameRate(60);
		filePath = Utility.filePath;
		clientPlayers = new ConcurrentHashMap<Integer, Player>();
		clientSpatials = new ConcurrentHashMap<Integer, Spatial>();
		clientCharacterControls = new ConcurrentHashMap<Integer, CharacterControl>();
		weaponSpatials = new ConcurrentHashMap<Integer, Spatial>();
		bulletAppState = new BulletAppState();
		stateManager.attach(bulletAppState);
		enemySpawners = new Vector<EnemySpawner>();
		bulletAppState = new BulletAppState();
		stateManager.attach(bulletAppState);
		terrainNode = new Node();
		enemies = new Vector<Enemy>();
		enemySpatials = new Vector<Spatial>();
		shootables = new Node("ENEMY SHOOTABLES NODE");
		powerups = new ConcurrentHashMap<Vector3f, Cube>();

		initCubes();
		initializeEnvironment(this);
		initializeWater(this);
		initMark();
		initBlockTerrain();
		initSpawners();
		load();
		assetManager.registerLocator("ProjectAssets/", FileLocator.class.getName());
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

	public void dropPowerUps(float tpf) {

		if (clientSpatials.size() > 0) {

			timerAmmo += tpf;

			if (timerAmmo > 8) {
				timerAmmo = 0;

				Vector3f location = new Vector3f(random.nextInt(300) + 1, 200F, random.nextInt(300) + 1);
				Vector3f direction = new Vector3f(0.0F, -1.0F, 0.0F);

				Vector3Int blockLocation = getCurrentPointedBlockLocation1(true, location, direction);

				if (blockLocation != null) {

					AmmoCube ac = new AmmoCube(blockLocation);
					powerups.put(location, ac);
					// audio_appear_powerup.play();
					blockTerrain.setBlock(blockLocation, ac);
					PowerupMessage powerupMessage = new PowerupMessage(blockLocation.getX(), blockLocation.getY(),
							blockLocation.getZ(), ac.getClass().getName());
					server.broadcast(powerupMessage);
				}

			}

			int randKill = ThreadLocalRandom.current().nextInt(8, 13 + 1);

			if (killForHealth >= randKill) {

				killForHealth = 0;
				Vector3f location = new Vector3f(random.nextInt(300) + 1, 200F, random.nextInt(300) + 1);
				Vector3f direction = new Vector3f(0.0F, -1.0F, 0.0F);

				Vector3Int blockLocation = getCurrentPointedBlockLocation1(true, location, direction);

				if (blockLocation != null) {
					HealthCube hc = new HealthCube(blockLocation);
					powerups.put(location, hc);
					// audio_appear_powerup.play();
					blockTerrain.setBlock(blockLocation, hc);
					PowerupMessage powerupMessage = new PowerupMessage(blockLocation.getX(), blockLocation.getY(),
							blockLocation.getZ(), hc.getClass().getName());
					server.broadcast(powerupMessage);
				}

			}
		}
	}

	private Vector3Int getCurrentPointedBlockLocation1(boolean getNeighborLocation, Vector3f location,
			Vector3f direction) {
		CollisionResults results = getRayCastingResults1(terrainNode, location, direction);
		if (results.size() > 0) {
			Vector3f collisionContactPoint = results.getClosestCollision().getContactPoint();
			return BlockNavigator.getPointedBlockLocation(blockTerrain, collisionContactPoint, getNeighborLocation);
		}
		return null;
	}

	private CollisionResults getRayCastingResults1(Node node, Vector3f location, Vector3f direction) {
		// direction.subtractLocal(location).normalizeLocal();
		Ray ray = new Ray(location, direction);
		CollisionResults results = new CollisionResults();
		node.collideWith(ray, results);
		return results;
	}

	private void initBlockTerrain() {

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
		// terrainNode.setShadowMode(RenderQueue.ShadowMode.Cast);

		rootNode.attachChild(terrainNode);

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
	}

	private void initMark() {
		Sphere sphere = new Sphere(30, 30, 0.2f);
		mark = new Geometry("BOOM!", sphere);
		Material mark_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mark_mat.setColor("Color", ColorRGBA.Red);
		mark.setMaterial(mark_mat);
		mark.setUserData("radius", 3.5f);
		rootNode.attachChild(mark);
	}

	private void initSpawners() {

		enemySpawners = new Vector<EnemySpawner>();

		EnemySpawnerCube enemySpawnerCube = new EnemySpawnerCube(new Vector3Int(0, 30, 0));

		enemySpawners.add(enemySpawnerCube.getEnemySpawner());
		// blockTerrain.setBlock(enemySpawnerCube.getPosition(),
		// enemySpawnerCube);

		EnemySpawnerCube enemySpawnerCube2 = new EnemySpawnerCube(new Vector3Int(230, 25, 0));

		enemySpawners.add(enemySpawnerCube2.getEnemySpawner());
		// blockTerrain.setBlock(enemySpawnerCube2.getPosition(),
		// enemySpawnerCube2);

		EnemySpawnerCube enemySpawnerCube3 = new EnemySpawnerCube(new Vector3Int(230, 25, 230));

		enemySpawners.add(enemySpawnerCube3.getEnemySpawner());
		// blockTerrain.setBlock(enemySpawnerCube3.getPosition(),
		// enemySpawnerCube3);

		EnemySpawnerCube enemySpawnerCube4 = new EnemySpawnerCube(new Vector3Int(0, 25, 235));

		enemySpawners.add(enemySpawnerCube4.getEnemySpawner());
		// blockTerrain.setBlock(enemySpawnerCube4.getPosition(),
		// enemySpawnerCube4);

	}

	private boolean checkCollision(Spatial a, Spatial b) {

		float distance = a.getLocalTranslation().distance(b.getLocalTranslation());
		float maxDistance = (Float) a.getUserData("radius") + (Float) b.getUserData("radius");
		return distance <= maxDistance;

	}

	private void handleCollisions(Node enemySpatial, int id) {

		for (int i = 0; i < enemies.size(); i++) {
			if (clientSpatials.containsKey(id)) {
				if (enemies.get(i).getHealthPoints() > 0) {
					if (checkCollision(clientSpatials.get(id), enemySpatials.get(i)) && enemies.get(i).canAttack()) {
						hitPlayer(enemies.get(i).getDamage(), id);
						// audio_zombie_attack.play();
						enemies.get(i).resetTimeCounter();

						EnemyMessage enemyMessage = new EnemyMessage();
						enemyMessage.setId(i);
						enemyMessage.setPosition(enemySpatial.getLocalTranslation());
						enemyMessage.setRotation(enemySpatial.getLocalRotation());
						enemyMessage.setAnimation("Attack");
						enemyMessage.setEnemySound("Attack");
						enemyMessage
								.setLinearVelocity(enemySpatial.getControl(RigidBodyControl.class).getLinearVelocity());
						server.broadcast(enemyMessage);

					}
				}
			}
		}

	}

	private void hitPlayer(int damage, int id) {
		PlayerMessage playerMessage = new PlayerMessage();

		if (clientPlayers.get(id).getHealthPoints() > 0) {
			clientPlayers.get(id).decreaseHealthPoints(damage);
			playerMessage.setHealthDecreasement(damage);
		}

		playerMessage.setId(id);
		playerMessage.setName(clientPlayers.get(id).getName());

		if (clientPlayers.get(id).getHealthPoints() <= 0 || clientPlayers.get(id).getPosition().y <= -15) {
			playerMessage.setDead(true);
			// clientPlayers.remove(id);
			clientPlayers.get(id).kill();
			if(rootNode.hasChild(clientSpatials.get(id)))
				rootNode.detachChild(clientSpatials.get(id));
			
			if(rootNode.hasChild(weaponSpatials.get(id)))
				rootNode.detachChild(weaponSpatials.get(id));
			
			clientSpatials.remove(id);
			weaponSpatials.remove(id);
			// clientPlayers.remove(id);
			for (int i = 0; i < enemies.size(); i++)
				enemies.get(i).setTarget(null);
		}
		server.broadcast(playerMessage);

	}

	private void load() {
		FileInputStream fileInputStream = null;

		File file = new File(filePath);
		if (file.exists()) {
			blockTerrain = new BlockTerrainControl(getSettings(this), new Vector3Int(5, 1, 5));
			byte[] bFile = new byte[(int) file.length()];

			try {
				// convert file into array of bytes
				fileInputStream = new FileInputStream(file);
				fileInputStream.read(bFile);
				fileInputStream.close();

			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Cannot load map");
				e.printStackTrace();
			}

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

			System.out.println(bFile.length);

			List<MapMessage> mapSplit = new ArrayList<MapMessage>();

			int _splitNumbers = (bFile.length / 1024) + 1;

			int position = 0;

			for (int i = 0; i < _splitNumbers; i++) {
				byte[] tmp = new byte[1024];
				for (int j = 0; j < 1024 && position < (i * 1024) && position < bFile.length; j++, position++)
					tmp[j] = bFile[position];

				MapMessage message = null;

				if (i != 0)
					message = new MapMessage(tmp);
				else
					message = new MapMessage(tmp, _splitNumbers);
				mapSplit.add(message);
			}

			System.out.println(mapSplit.size());
			serverListener = new ServerListener(mapSplit);
			server.addMessageListener(serverListener, MapMessage.class, PlayerMessage.class, HelloMessage.class,
					PlayerListMessage.class, EnemyMessage.class, PowerupMessage.class);
		}
	}

	private void makeEnemy(Vector3Int position) {

		if (clientSpatials.size() > 0) {

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
			enemySpatial.setUserData("radius", 3.5f);
			enemies.add(zombie);
			enemySpatials.add(enemySpatial);

			com.jme3.bullet.collision.shapes.CollisionShape enemyCollisionShape = CollisionShapeFactory
					.createDynamicMeshShape(enemySpatial);
			RigidBodyControl enemyCharacterControl = new RigidBodyControl(enemyCollisionShape);
			bulletAppState.getPhysicsSpace().add(enemyCharacterControl);
			enemySpatial.addControl(enemyCharacterControl);

			enemyCharacterControl.setAngularFactor(0);

			int choice = ThreadLocalRandom.current().nextInt(0, clientSpatials.size());
			if (clientSpatials.get(choice) != null && clientPlayers.get(choice).getHealthPoints() > 0)
				zombie.setTarget(clientPlayers.get(choice));

			EnemyMessage enemyMessage = new EnemyMessage();
			enemyMessage.setId(enemies.size() - 1);
			enemyMessage.setPosition(enemySpatial.getLocalTranslation());
			enemyMessage.setRotation(enemySpatial.getLocalRotation());
			enemyMessage.setLinearVelocity(enemySpatial.getControl(RigidBodyControl.class).getLinearVelocity());

			server.broadcast(enemyMessage);
			shootables.attachChild(enemySpatial);
			rootNode.attachChild(enemySpatial);
		}

	}

	@SuppressWarnings("deprecation")
	@Override
	public void simpleUpdate(float tpf) {
		try {
		super.simpleUpdate(tpf);
		updateClientList(tpf);
		updateClients(tpf);

		if (clientPlayers.size() > 0) {
			updateSpawner(tpf);
			updateZombie(tpf);
			dropPowerUps(tpf);
		}

		// server closing check
		boolean running = true;
		for (int key : clientPlayers.keySet()) {
			Player p = clientPlayers.get(key);
			if (p.getHealthPoints() > 0) {
				running = true;
				break;
			} else
				running = false;
		}

		if (!running) {
			gameOverCounter += tpf;
			if (gameOverCounter >= 10)
				this.stop();
		}
		} catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Server crashed because of network problems");
		}
	}

	private void updateClients(float tpf) {
		if (serverListener.getPlayerMessageQueue() != null) {
			for (int key : serverListener.getPlayerMessageQueue().keySet()) {
				PlayerMessage message = serverListener.getPlayerMessageQueue().get(key);
				if (message.getPosition() != null && message.getRotation() != null) {
					Spatial sp = clientSpatials.get(key);
					Player p = clientPlayers.get(key);
					Spatial weapon = weaponSpatials.get(key);
					if (sp != null) {
						if (p.getHealthPoints() <= 0 || p.getPosition().y <= -15) {
							PlayerMessage playerMessage = new PlayerMessage();
							playerMessage.setDead(true);
							// clientPlayers.remove(key);
							p.kill();
							rootNode.detachChild(sp);
							if (rootNode.hasChild(weapon))
								rootNode.detachChild(weapon);
							weaponSpatials.remove(key);
							clientSpatials.remove(sp);
							server.broadcast(playerMessage);
							serverListener.getPlayerMessageQueue().remove(message);
							continue;
						} else {
							System.out.println("MESSAGE " + message.getRotation());
							sp.setLocalTranslation(message.getPosition());
							sp.setLocalRotation(message.getRotation());
							System.out.println("PLAYER " + sp.getLocalRotation());
							weapon.setLocalTranslation(message.getWeaponTranslation());
							weapon.setLocalRotation(message.getWeaponRotation());
							p.setPosition(message.getPosition());
							p.setRotation(message.getRotation());
							if(playerAnimChannel.getTime() == playerAnimChannel.getAnimMaxTime())
								playerAnimChannel.setAnim(message.getAction());
							// DEBUG
							// p.setHealthPoints(message.getHealthPoints());
							// System.out.println(p.getHealthPoints());
						//	clientCharacterControls.get(key).setPhysicsLocation(p.getPosition());
					//		clientCharacterControls.get(key).set

							Vector3f playerPosition = p.getPosition();

							for (Vector3f _key : powerups.keySet()) {
								if (Math.abs(_key.x - playerPosition.x) < 5f
										&& Math.abs(_key.z - playerPosition.z) < 5f) {

									if (powerups.get(_key) instanceof AmmoCube) {
										AmmoCube ammoCube = (AmmoCube) powerups.get(_key);
										Vector3Int blockLocation = ammoCube.getBlockLocation();
										p.getSelectedWeapon().setAmmo(60);
										PlayerMessage playerMessage = new PlayerMessage();
										playerMessage.setId(key);
										playerMessage.setAmmoIncreasement(60);
										server.broadcast(playerMessage);

										PowerupMessage powerupMessage = new PowerupMessage();
										powerupMessage.setDelete(true);
										powerupMessage.setType(ammoCube.getClass().getName());
										powerupMessage.setX(blockLocation.getX());
										powerupMessage.setY(blockLocation.getY());
										powerupMessage.setZ(blockLocation.getZ());
										powerupMessage.setAmount(25);
										server.broadcast(powerupMessage);

										blockTerrain.removeBlock(((AmmoCube) powerups.get(_key)).getBlockLocation());

										// ak47.setAmmo(60);
										// ammoText.setText("" +
										// ak47.getAmmoPerClip() + "/" +
										// ak47.getAmmo());
										// audio_pick_ammo.play();
									} else {
										HealthCube healthCube = (HealthCube) powerups.get(_key);
										Vector3Int blockLocation = healthCube.getBlockLocation();
										p.addHealthPoints(25);
										PlayerMessage playerMessage = new PlayerMessage();
										playerMessage.setId(key);
										playerMessage.setHealthIncreasement(25);
										server.broadcast(playerMessage);
										PowerupMessage powerupMessage = new PowerupMessage();
										powerupMessage.setDelete(true);
										powerupMessage.setType(healthCube.getClass().getName());
										powerupMessage.setX(blockLocation.getX());
										powerupMessage.setY(blockLocation.getY());
										powerupMessage.setZ(blockLocation.getZ());
										powerupMessage.setAmount(25);
										server.broadcast(powerupMessage);
										// audio_pick_health.play();
										blockTerrain.removeBlock(((HealthCube) powerups.get(_key)).getBlockLocation());
									}
									powerups.remove(_key);
									break;
								}

							}

							if (Vector3f.isValidVector(message.getShootPoint())) {

								for (int i = 0; i < enemySpatials.size(); i++) {
									mark.setLocalTranslation(message.getShootPoint());

									if (checkCollision(mark, enemySpatials.get(i))) {
										enemies.get(i).decreaseHealthPoints(message.getDamage());
										// mark.setLocalTranslation(Vector3f.NAN);
										if (enemies.get(i).getHealthPoints() <= 0) {
											PlayerMessage playerMessage = new PlayerMessage();
											playerMessage.setId(key);
											playerMessage.addPoints(15);
											server.broadcast(playerMessage);
										}
										break;
									}

								}
							}
						}
					}
				}
				server.broadcast(message);
				serverListener.getPlayerMessageQueue().remove(message);
				// //client
				//
			}

		}
	}

	private void updateClientList(float tpf) {
		if (serverListener.getPlayerListMessages() != null) {

			for (int key : serverListener.getPlayerListMessages().keySet()) {

				if (!clientPlayers.containsKey(key)) {
					Player p = new Player();
					p.setName(serverListener.getPlayerListMessages().get(key).getName());

					//draw player
					MaterialList matList = (MaterialList) assetManager.loadAsset("PlayerNew/Player.material");
					OgreMeshKey ogreKey = new OgreMeshKey("PlayerNew/Cube.mesh.xml", matList);
					Spatial sp = assetManager.loadModel("PlayerNew/ZombieCraftPlayerWithWeapon.scene");
					sp = assetManager.loadAsset(ogreKey);
					sp.addLight(new AmbientLight());
					sp.updateModelBound();
					// sp.scale(0.8f, 0.8f, 0.8f);
					sp.scale(1.5f, 1.5f, 1.5f);
					
					sp.setUserData("radius", 3.5f);
					sp.addLight(new AmbientLight());
					CharacterControl playerCharacterControl = new CharacterControl(new CapsuleCollisionShape(
							getSettings(this).getBlockSize() / 2.0F, getSettings(this).getBlockSize() * 2.0F), 0.05F);
					playerCharacterControl.setSpatial(sp);
					//sp.addControl(playerCharacterControl);

					playerAnimControl = (sp.getControl(AnimControl.class));
					playerAnimControl.addListener(this);
					playerAnimChannel = playerAnimControl.createChannel();
					
					clientCharacterControls.put(key, playerCharacterControl);
					clientPlayers.put(serverListener.getPlayerListMessages().get(key).getId(), p);
					clientSpatials.put(serverListener.getPlayerListMessages().get(key).getId(), sp);

					//draw weapon
					Spatial weapon = assetManager.loadModel("ak47/AK47.scene");
					weapon.addLight(new AmbientLight());
					weapon.scale(0.3f, 0.3f, 0.3f);
					weaponSpatials.put(key, weapon);

					
					rootNode.attachChild(sp);
					rootNode.attachChild(weapon);
				}
			}

		}
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

	private void updateZombie(float lastTimePerFrame) {

		try {
			for (int i = 0; i < enemies.size(); i++) {

				Enemy e = enemies.get(i);
				Spatial eS = enemySpatials.get(i);

				if (e.getHealthPoints() <= 0 || e.getY() < -15) {
					rootNode.detachChild(enemySpatials.get(i));
					bulletAppState.getPhysicsSpace().remove(enemySpatials.get(i));
					EnemyMessage enemyMessage = new EnemyMessage();
					enemyMessage.setId(i);
					enemyMessage.setPosition(eS.getLocalTranslation());
					enemyMessage.setRotation(eS.getLocalRotation());
					enemyMessage.setDead(true);
					server.broadcast(enemyMessage);

					shootables.detachChild(eS);
					enemySpatials.remove(i);
					enemies.remove(i);
					System.gc();
					i--;
					// player.addScore(15);
					killForHealth++;
					// scoreText.setText(" SCORE: " + player.getScore());

					int rand = 0;
					do {
						rand = ThreadLocalRandom.current().nextInt(0, 3 + 1);

					} while (enemySpawners.get(rand).getCurrentSlots() == 0);

					enemySpawners.get(rand).decreaseCurrentSlots();

					continue;
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

					// if (!collide) {

					e.increaseTimeCounter(lastTimePerFrame);

					if (clientSpatials.size() > 0 && e.getTarget() == null) {
						int choice = ThreadLocalRandom.current().nextInt(0, clientPlayers.size());
						if (clientPlayers.get(choice) != null && clientPlayers.get(choice).getHealthPoints() > 0) {
							e.setTarget(clientPlayers.get(choice));
						}

					}

					if (e.getTarget() != null && e.getTarget().getHealthPoints() <= 0) {
						e.setTarget(null);
					}

					if (e.getTarget() != null) {
						e.followTarget();

						RigidBodyControl rb = eS.getControl(RigidBodyControl.class);

						Vector3f v = new Vector3f(0, rb.getLinearVelocity().y, 0);
						e.setX(eS.getLocalTranslation().x);
						e.setY(eS.getLocalTranslation().y);
						e.setZ(eS.getLocalTranslation().z);

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

						if (e.getTarget() != null && e.getTarget().getPosition() != Vector3f.NAN) {

							Vector3f monsterLocation = eS.getLocalTranslation();

							Vector3f playerDirection = playerPosition.subtract(monsterLocation);

							playerDirection.y = 0;

							Quaternion targetRotation = new Quaternion();

							targetRotation.lookAt(playerDirection, Vector3f.UNIT_Y);

							rb.setPhysicsRotation(targetRotation);

							eS.getControl(RigidBodyControl.class).setLinearVelocity(v);

							if (clientPlayers.containsValue(e.getTarget())) {
								for (int key : clientPlayers.keySet())
									handleCollisions((Node) eS, key);
							}
						}

						e.setX(eS.getLocalTranslation().x);
						e.setY(eS.getLocalTranslation().y);
						e.setZ(eS.getLocalTranslation().z);
					}
				}
				EnemyMessage enemyMessage = new EnemyMessage();
				enemyMessage.setId(i);
				enemyMessage.setPosition(eS.getLocalTranslation());
				enemyMessage.setRotation(eS.getLocalRotation());
				enemyMessage.setAnimation("Walk");
				enemyMessage.setLinearVelocity(eS.getControl(RigidBodyControl.class).getLinearVelocity());

				server.broadcast(enemyMessage);

			}

		} catch (NullPointerException npe) {
			npe.printStackTrace();
		}

	}

	public static CubesSettings getSettings(Application application) {
		CubesSettings settings = new CubesSettings(application);
		settings.setDefaultBlockMaterial("Textures/cubes/terrain.png");
		return settings;
	}

	@Override
	public void destroy() {
		server.close();
		super.destroy();
		JOptionPane.showMessageDialog(null, "Server Closed");
	}

	@Override
	public void stop() {
		for (HostedConnection conn : server.getConnections())
			conn.close("Match Finished!");
		super.stop();
	}

	@Override
	public void connectionAdded(Server server, HostedConnection conn) {
		System.out.println(conn.getAddress() + "joined" + " id" + conn.getId());
	}

	@Override
	public void connectionRemoved(Server server, HostedConnection conn) {
		try {
			if (rootNode.hasChild(clientSpatials.get(conn.getId()))) {
				rootNode.detachChild(clientSpatials.get(conn.getId()));
				rootNode.detachChild(weaponSpatials.get(conn.getId()));
				clientPlayers.get(conn.getId()).kill();
//				PlayerMessage playerMessage = new PlayerMessage();
//				playerMessage.setDead(true);
//				server.broadcast(playerMessage);
//				clientSpatials.remove(conn.getId());
//				weaponSpatials.remove(conn.getId());
				conn.close("Connection Closed");
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Client Already Disconnected");
		}
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
	public void onAnimChange(AnimControl arg0, AnimChannel arg1, String arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnimCycleDone(AnimControl arg0, AnimChannel arg1, String arg2) {
		// TODO Auto-generated method stub
		
	}
}
