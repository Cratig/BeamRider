package cratig.beamrider.enemy;

import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.util.color.Color;

import cratig.beamrider.MainActivity;

public class Enemy {
	public Sprite sprite;
	public int hp;

	protected final int MAX_HEALTH = 2;

	public final static Color colorHealthFull = Color.GREEN;
	public final static Color colorHealthHalf = Color.RED;

	public Enemy() {
		sprite = new Sprite(0, 0,
				MainActivity.getSharedInstance().enemyTextureRegion,
				MainActivity.getSharedInstance().getVertexBufferObjectManager());
		// sprite.setColor(colorHealthFull);

		init();
	}

	public void init() {
		hp = MAX_HEALTH;

		sprite.registerEntityModifier(new LoopEntityModifier(
				new RotationModifier(5, 0, -360)));
	}

	public void clean() {
		sprite.clearEntityModifiers();
		sprite.clearUpdateHandlers();
	}

	public boolean gotHit() {
		synchronized (this) {
			hp--;

			if (hp == 1) {
				sprite.setColor(colorHealthHalf);
			}

			if (hp == 0) {
				return false;
			}

			return true;
		}
	}
}
