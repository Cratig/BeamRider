package cratig.beamrider;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.primitive.Rectangle;

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
}
