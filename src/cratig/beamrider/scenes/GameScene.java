package cratig.beamrider.scenes;

import java.util.Iterator;
import java.util.LinkedList;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.input.touch.TouchEvent;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.modifier.ease.EaseCircularOut;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import cratig.beamrider.GameLoopUpdateHandler;
import cratig.beamrider.MainActivity;
import cratig.beamrider.SensorListener;
import cratig.beamrider.ship.Ship;
import cratig.beamrider.ship.ShipBullet;
import cratig.beamrider.ship.ShipBulletTimer;

public class GameScene extends Scene implements IOnSceneTouchListener {
	public Ship ship;
	Camera camera;
	public float accelerometerSpeedX;
	private SensorManager sensorManager;

	public LinkedList<ShipBullet> shipBulletList;
	public int shipBulletCount = 0;

	public GameScene() {
		setBackground(new Background(0.09804f, 0.6274f, 0.8784f));

		camera = MainActivity.getSharedInstance().camera;

		ship = Ship.getSharedInstance();
		attachChild(ship.sprite);

		shipBulletList = new LinkedList<ShipBullet>();

		MainActivity.getSharedInstance().setCurrentScene(this);
		sensorManager = (SensorManager) MainActivity.getSharedInstance()
				.getSystemService(BaseGameActivity.SENSOR_SERVICE);

		SensorListener.getSharedInstance();

		sensorManager.registerListener(SensorListener.getSharedInstance(),
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_GAME);

		registerUpdateHandler(new GameLoopUpdateHandler());
		setOnSceneTouchListener(this);

	}

	public void moveShip() {
		ship.moveShip(accelerometerSpeedX);
	}

	@Override
	public boolean onSceneTouchEvent(Scene scene, TouchEvent touchEvent) {
		synchronized (this) {
			if (touchEvent.isActionDown()) {
				if (!ShipBulletTimer.getSharedInstance().isValid()) {
					return false;
				}
				
				ship.shoot();
			}
		}

		return true;
	}

	public void shipBulletCleaner() {
		synchronized (this) {
			Iterator<ShipBullet> iterator = shipBulletList.iterator();

			while (iterator.hasNext()) {
				ShipBullet bullet = iterator.next();

				if (bullet.sprite.getY() <= bullet.sprite.getHeight()) {
					
					ScaleModifier expandXModifier = new ScaleModifier(0.5f, 1.0f, 8.0f, 1.0f, 0.6f, EaseCircularOut.getInstance());
					AlphaModifier alphaModifier = new AlphaModifier(0.5f, 1.0f, 0.0f);
					
					ParallelEntityModifier parModifier = new ParallelEntityModifier(expandXModifier, alphaModifier);
					
					bullet.sprite.registerEntityModifier(parModifier);
					
					//ShipBulletPool.sharedShipBulletPool().recyclePoolItem(bullet);
					//iterator.remove();
					continue;
				}
			}
		}
	}
}
