package cratig.beamrider.enemy;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.IEntityFactory;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.particle.ParticleSystem;
import org.andengine.entity.particle.emitter.PointParticleEmitter;
import org.andengine.entity.particle.initializer.VelocityParticleInitializer;
import org.andengine.entity.particle.modifier.AlphaParticleModifier;
import org.andengine.entity.particle.modifier.RotationParticleModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.util.modifier.LoopModifier;

import cratig.beamrider.MainActivity;

public class EnemyLayer extends Entity {
	private LinkedList<Enemy> enemies;
	public static EnemyLayer instance;
	public int enemyCount;

	public static EnemyLayer getSharedInstance() {
		return instance;
	}

	public EnemyLayer(int count) {
		instance = this;
		enemies = new LinkedList<Enemy>();
		enemyCount = count;
	}

	public static boolean isEmtpy() {
		return (instance.enemies.size() == 0) ? true : false;
	}

	public static Iterator<Enemy> getIterator() {
		return instance.enemies.iterator();
	}

	public void restart() {
		enemies.clear();
		clearEntityModifiers();
		clearUpdateHandlers();

		for (int i = 0; i < enemyCount; i++) {
			Enemy enemy = EnemyPool.sharedEnemyPool().obtainPoolItem();

			float finalX = (i % 5) * 4 * enemy.sprite.getWidth();
			float finalY = ((int) (i / 5)) * enemy.sprite.getHeight() * 2;

			Random random = new Random();

			float startX = random.nextInt(2) == 0 ? -enemy.sprite.getWidth() * 3
					: MainActivity.getSharedInstance().camera.getWidth()
							+ enemy.sprite.getWidth() * 3;
			float startY = random.nextInt(5) + 1 * enemy.sprite.getHeight();

			enemy.sprite.setPosition(startX, startY);

			enemy.sprite.setVisible(true);

			attachChild(enemy.sprite);

			enemy.sprite.registerEntityModifier(
					new LoopEntityModifier(
							new SequenceEntityModifier(
									new MoveModifier(2, enemy.sprite.getX(), +5, enemy.sprite.getY(), finalY),
									new MoveModifier(2, enemy.sprite.getX(), finalX, enemy.sprite.getY(), -5),
									new MoveModifier(2, enemy.sprite.getX(), finalX, enemy.sprite.getY(), +5),
									new MoveModifier(2, enemy.sprite.getX(), -5, enemy.sprite.getY(), finalY)
									)
							)
					);

			enemies.add(enemy);
		}

		setVisible(true);
		setPosition(50, 30);

		MoveXModifier moveRight = new MoveXModifier(1, 50, 120);
		MoveXModifier moveLeft = new MoveXModifier(1, 120, 50);

		MoveYModifier moveDown = new MoveYModifier(1, 30, 100);
		MoveYModifier moveUp = new MoveYModifier(1, 100, 30);

		registerEntityModifier(new LoopEntityModifier(
				new SequenceEntityModifier(moveRight, moveDown, moveLeft,
						moveUp)));
	}

	public void createExplosion(final float posX, final float posY,
			final IEntity target, final MainActivity activity) {
		int particles = 15;
		int timeParticles = 2;

		PointParticleEmitter particleEmitter = new PointParticleEmitter(posX,
				posY);
		IEntityFactory<Rectangle> entityFactory = new IEntityFactory<Rectangle>() {

			@Override
			public Rectangle create(float x, float y) {
				Rectangle rectangle = new Rectangle(posX, posY, 10, 10,
						activity.getVertexBufferObjectManager());
				rectangle.setColor(Enemy.colorHealthHalf);

				return rectangle;
			}
		};

		final ParticleSystem<Rectangle> particleSystem = new ParticleSystem<Rectangle>(
				entityFactory, particleEmitter, 300, 550, particles);

		particleSystem
				.addParticleInitializer(new VelocityParticleInitializer<Rectangle>(
						-75, 50, -75, 50));

		particleSystem
				.addParticleModifier(new AlphaParticleModifier<Rectangle>(0,
						0.6f * timeParticles, 1, 0));
		particleSystem
				.addParticleModifier(new RotationParticleModifier<Rectangle>(0,
						timeParticles, 0, 275));

		target.attachChild(particleSystem);

		target.registerUpdateHandler(new TimerHandler(timeParticles,
				new ITimerCallback() {

					@Override
					public void onTimePassed(TimerHandler timeHandler) {
						particleSystem.detachSelf();
						target.sortChildren();
						target.unregisterUpdateHandler(timeHandler);
					}

				}));
	}

	public void createImageExplosion(final float posX, final float posY,
			final IEntity target, final MainActivity activity) {
		int particles = 15;
		int timeParticles = 2;

		PointParticleEmitter particleEmitter = new PointParticleEmitter(posX,
				posY);

		IEntityFactory<Sprite> entityFactory = new IEntityFactory<Sprite>() {

			@Override
			public Sprite create(float x, float y) {
				Sprite sprite = new Sprite(posX, posY,
						MainActivity.getSharedInstance().enemyTextureRegion,
						MainActivity.getSharedInstance()
								.getVertexBufferObjectManager());

				sprite.setColor(Enemy.colorHealthHalf);

				return sprite;
			}
		};

		final ParticleSystem<Sprite> particleSystem = new ParticleSystem<Sprite>(
				entityFactory, particleEmitter, 300, 550, particles);

		particleSystem
				.addParticleInitializer(new VelocityParticleInitializer<Sprite>(
						-75, 50, -75, 50));

		particleSystem.addParticleModifier(new AlphaParticleModifier<Sprite>(0,
				0.6f * timeParticles, 1, 0));
		particleSystem
				.addParticleModifier(new RotationParticleModifier<Sprite>(0,
						timeParticles, 0, 275));

		// particleSystem.addParticleModifier(new
		// ScaleParticleModifier<Sprite>(0, (float) (0.7 * timeParticles),
		// 0.45f, 0.20f));

		target.attachChild(particleSystem);

		target.registerUpdateHandler(new TimerHandler(timeParticles,
				new ITimerCallback() {

					@Override
					public void onTimePassed(TimerHandler timeHandler) {
						particleSystem.detachSelf();
						target.sortChildren();
						target.unregisterUpdateHandler(timeHandler);
					}

				}));
	}

	public void purge() {
		detachChildren();

		for (Enemy enemy : enemies) {
			EnemyPool.sharedEnemyPool().recyclePoolItem(enemy);
		}

		enemies.clear();
	}

	public static void purgeAndRestart() {
		instance.purge();
		instance.restart();
	}

	@Override
	public void onDetached() {
		purge();
		super.onDetached();
	}
}
