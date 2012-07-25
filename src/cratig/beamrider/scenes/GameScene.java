package cratig.beamrider.scenes;

import java.util.Iterator;
import java.util.LinkedList;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.input.touch.TouchEvent;
import org.andengine.ui.activity.BaseGameActivity;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import cratig.beamrider.GameLoopUpdateHandler;
import cratig.beamrider.MainActivity;
import cratig.beamrider.SensorListener;
import cratig.beamrider.enemy.Enemy;
import cratig.beamrider.enemy.EnemyLayer;
import cratig.beamrider.enemy.EnemyPool;
import cratig.beamrider.ship.Ship;
import cratig.beamrider.ship.ShipBullet;
import cratig.beamrider.ship.ShipBulletPool;
import cratig.beamrider.ship.ShipBulletTimer;

public class GameScene extends Scene implements IOnSceneTouchListener {
	public Ship ship;
	Camera camera;
	public float accelerometerSpeedX;
	private SensorManager sensorManager;

	public LinkedList<ShipBullet> shipBulletList;
	public int shipBulletCount = 0;
	int missCount = 0;;

	public GameScene() {
		setBackground(new Background(0.09804f, 0.6274f, 0.8784f));

		camera = MainActivity.getSharedInstance().camera;

		//Setup player
		ship = Ship.getSharedInstance();
		attachChild(ship.sprite);

		shipBulletList = new LinkedList<ShipBullet>();
		
		//Setup enemies
		attachChild(new EnemyLayer(15));
		
		
		MainActivity.getSharedInstance().setCurrentScene(this);
		sensorManager = (SensorManager) MainActivity.getSharedInstance()
				.getSystemService(BaseGameActivity.SENSOR_SERVICE);

		SensorListener.getSharedInstance();

		sensorManager.registerListener(SensorListener.getSharedInstance(),
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_GAME);

		setOnSceneTouchListener(this);

		resetScene();
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

	public void sceneCleaner() {
		synchronized (this) {
			Iterator<Enemy> enemyIterator = EnemyLayer.getIterator();
			
			while (enemyIterator.hasNext()) {
				Enemy enemy = enemyIterator.next();
				
				Iterator<ShipBullet> bulletIterator = shipBulletList.iterator();
				
				while (bulletIterator.hasNext()) {
					ShipBullet bullet = bulletIterator.next();
					
					if (bullet.sprite.getY() <= bullet.sprite.getHeight()) {
						ShipBulletPool.sharedShipBulletPool().recyclePoolItem(bullet);
						bulletIterator.remove();
						continue;
					}
					
					if (bullet.sprite.collidesWith(enemy.sprite)) {
						//It's a hit!
						if (!enemy.gotHit()) {
							EnemyLayer.getSharedInstance().createImageExplosion(enemy.sprite.getX(), enemy.sprite.getY(), enemy.sprite.getParent(), MainActivity.getSharedInstance());
							EnemyPool.sharedEnemyPool().recyclePoolItem(enemy);
							enemyIterator.remove();
						}
						
						ShipBulletPool.sharedShipBulletPool().recyclePoolItem(bullet);
						bulletIterator.remove();
						break;
					}
				}
			}
		}
		
		if (EnemyLayer.isEmtpy()) {
			setChildScene(new ResultScene(camera));
			clearUpdateHandlers();
		}
	}
	
	public void resetScene() {
		missCount = 0;
		shipBulletCount = 0;
		
		ship.restart();
		EnemyLayer.purgeAndRestart();
		clearChildScene();
		
		registerUpdateHandler(new GameLoopUpdateHandler());
	}
	
	public void detach() {
		clearUpdateHandlers();
		for (ShipBullet bullet : shipBulletList) {
			ShipBulletPool.sharedShipBulletPool().recyclePoolItem(bullet);
		}
		
		shipBulletList.clear();
		detachChildren();
		Ship.instance = null;
		EnemyPool.instance = null;
		ShipBulletPool.instance = null;
		clearUpdateHandlers();
	}
}
