package cratig.beamrider.ship;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.util.modifier.ease.EaseCubicOut;
import org.andengine.util.modifier.ease.EaseQuadOut;

import cratig.beamrider.MainActivity;
import cratig.beamrider.scenes.GameScene;

public class Ship {
	public Rectangle sprite;
	public static Ship instance;
	Camera camera;

	boolean moveable = true;

	public static Ship getSharedInstance() {
		if (instance == null) {
			instance = new Ship();
		}

		return instance;
	}

	private Ship() {
		sprite = new Rectangle(0, 0, 70, 30, MainActivity.getSharedInstance()
				.getVertexBufferObjectManager());

		camera = MainActivity.getSharedInstance().camera;

		sprite.setPosition(((camera.getWidth() / 2) - (sprite.getWidth() / 2)),
				((camera.getHeight() - sprite.getHeight()) - 10));
	}

	public void moveShip(float accelerometerSpeedX) {
		if (!moveable) {
			return;
		}

		if (accelerometerSpeedX != 0) {
			int lL = 0;
			int rL = (int) (camera.getWidth() - (int) sprite.getWidth());
			float newX;

			if (sprite.getX() >= lL) {
				newX = sprite.getX() + accelerometerSpeedX;
			} else {
				newX = lL;
			}

			if (newX <= rL) {
				newX = sprite.getX() + accelerometerSpeedX;
			} else {
				newX = rL;
			}

			if (newX < lL) {
				newX = lL;
			} else if (newX > rL) {
				newX = rL;
			}

			sprite.setPosition(newX, sprite.getY());
		}
	}

	public void shoot() {
		if (!moveable) {
			return;
		}

		GameScene scene = (GameScene) MainActivity.getSharedInstance().currentScene;

		ShipBullet bullet = ShipBulletPool.sharedShipBulletPool()
				.obtainPoolItem();

		bullet.sprite.setPosition((sprite.getX() + (sprite.getWidth() / 2)),
				sprite.getY());

		MoveYModifier yModifier = new MoveYModifier(2.5f, bullet.sprite.getY(),
				-bullet.sprite.getHeight(), EaseCubicOut.getInstance());

		bullet.sprite.setVisible(true);
		bullet.sprite.detachSelf();

		scene.attachChild(bullet.sprite);
		scene.shipBulletList.add(bullet);

		bullet.sprite.registerEntityModifier(yModifier);

		scene.shipBulletCount++;
	}

	public void restart() {
		moveable = false;

		Camera camera = MainActivity.getSharedInstance().camera;

		MoveXModifier moveModifier = new MoveXModifier(0.2f, sprite.getX(),
				(camera.getWidth() / 2) - (sprite.getWidth() / 2),
				EaseQuadOut.getInstance()) {

			@Override
			protected void onModifierFinished(IEntity entity) {
				super.onModifierFinished(entity);
				moveable = true;
			}
		};
		
		sprite.registerEntityModifier(moveModifier);
	}
}
