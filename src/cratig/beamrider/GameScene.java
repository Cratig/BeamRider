package cratig.beamrider;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.ui.activity.BaseGameActivity;

import android.hardware.Sensor;
import android.hardware.SensorManager;

public class GameScene extends Scene{
	public Ship ship;
	Camera camera;
	float accelerometerSpeedX;
	private SensorManager sensorManager;
	
	public GameScene() {
		setBackground(new Background(0.09804f, 0.6274f, 0.8784f));
		
		camera = MainActivity.getSharedInstance().camera;
		
		ship = Ship.getSharedInstance();
		
		attachChild(ship.sprite);
		
		MainActivity.getSharedInstance().setCurrentScene(this);
		sensorManager = (SensorManager) MainActivity.getSharedInstance().getSystemService(BaseGameActivity.SENSOR_SERVICE);
		
		SensorListener.getSharedInstance();
		
		sensorManager.registerListener(SensorListener.getSharedInstance(), sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
		
		registerUpdateHandler(new GameLoopUpdateHandler());
	}
	
	public void moveShip() {
		ship.moveShip(accelerometerSpeedX);
	}
}
