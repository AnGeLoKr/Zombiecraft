package Models.MyOgreExportCharacter;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

public class HelloAnimation extends SimpleApplication {
	private AnimChannel channel;
	private AnimControl control;
	Node player;

	public static void main(String[] args) {
		HelloAnimation app = new HelloAnimation();
		app.start();
	}

	@Override
	public void simpleInitApp() {
		viewPort.setBackgroundColor(ColorRGBA.LightGray);
		//initKeys();
		DirectionalLight dl = new DirectionalLight();
		dl.setDirection(new Vector3f(-0.1f, -1f, -1).normalizeLocal());
		rootNode.addLight(dl);
		player = (Node) assetManager.loadModel("/Users/host759/Documents/workspace/ProgettoIGPEMinecraft/resources/Models/MyOgreExportCharacter/Cube.skeleton.xml");
		player.setLocalScale(0.5f); // resize
		rootNode.attachChild(player);
	}
}