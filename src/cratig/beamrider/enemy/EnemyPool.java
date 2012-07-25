package cratig.beamrider.enemy;

import org.andengine.util.adt.pool.GenericPool;

public class EnemyPool extends GenericPool<Enemy>{

	public static EnemyPool instance;
	
	public static EnemyPool sharedEnemyPool() {
		if (instance == null) {
			instance = new EnemyPool();
		}
		
		return instance;
	}
	
	private EnemyPool() {
		super();
	}
	
	@Override
	protected void onHandleObtainItem(Enemy enemy) {
		enemy.init();
	}
	
	@Override
	protected Enemy onAllocatePoolItem() {
		return new Enemy();
	}
	
	protected void onHandleRecycleItem(final Enemy enemy) {
		enemy.sprite.setVisible(false);
		enemy.sprite.detachSelf();
		enemy.clean();
	}
}
