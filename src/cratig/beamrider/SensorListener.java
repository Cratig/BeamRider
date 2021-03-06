package cratig.beamrider;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import cratig.beamrider.scenes.GameScene;

public class SensorListener implements SensorEventListener {
	static SensorListener instance;
	GameScene scene;

	public static SensorListener getSharedInstance() {
		if (instance == null) {
			instance = new SensorListener();
		}

		return instance;
	}

	public SensorListener() {
		instance = this;

		scene = (GameScene) MainActivity.getSharedInstance().currentScene;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		synchronized (this) {
			switch (event.sensor.getType()) {
			case Sensor.TYPE_ACCELEROMETER:
				scene.accelerometerSpeedX = event.values[1];
				break;
			default:
				break;
			}
		}
	}

}
