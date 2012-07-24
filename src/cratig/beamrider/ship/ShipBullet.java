package cratig.beamrider.ship;

import org.andengine.entity.primitive.Rectangle;

import cratig.beamrider.MainActivity;

public class ShipBullet {
	public Rectangle sprite;
	
	public ShipBullet() {
		sprite = new Rectangle(0, 0, 10, 10, MainActivity.getSharedInstance().getVertexBufferObjectManager());
		
		sprite.setColor(0.09904f, 0.0f, 0.1786f);
	}
}
