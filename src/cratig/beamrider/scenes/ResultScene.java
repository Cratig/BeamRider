package cratig.beamrider.scenes;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.scene.CameraScene;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;

import cratig.beamrider.MainActivity;

public class ResultScene extends CameraScene implements IOnSceneTouchListener {

	boolean done;
	MainActivity activity;
	
	public ResultScene(Camera camera) {
		super(camera);
		
		activity = MainActivity.getSharedInstance();
		
		setBackgroundEnabled(false);
		
		GameScene scene = (GameScene) activity.currentScene;
		
		float accuracy = 1 - (float) scene.missCount / scene.shipBulletCount;
		
		if (Float.isNaN(accuracy)) {
			accuracy = 0;
		}
		
		accuracy *= 100;
		
		Text result = new Text(0, 0, activity.mFont, "Accuracy: " + String.format("%.2f", accuracy) + "%", MainActivity.getSharedInstance().getVertexBufferObjectManager());
		
		final int x = ((int) (camera.getWidth() / 2) - (int) (result.getWidth() / 2));
		final int y = ((int) (camera.getHeight() / 2) - (int) (result.getHeight() / 2));
		
		done = false;
		
		result.setPosition(x, camera.getHeight() + result.getHeight());
		
		MoveYModifier moveModifier = new MoveYModifier(5, result.getY(), y) {
			@Override
			protected void onModifierFinished(IEntity entity) {
				done = true;
			}
		};
		
		attachChild(result);
		result.registerEntityModifier(moveModifier);
		setOnSceneTouchListener(this);
	}
	
	@Override
	public boolean onSceneTouchEvent(Scene scene, TouchEvent touchEvent) {
		if (!done) {
			return true;
		}
		
		((GameScene) activity.currentScene).resetScene();
		return false;
	}

}
