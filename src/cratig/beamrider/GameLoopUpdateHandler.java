package cratig.beamrider;

import org.andengine.engine.handler.IUpdateHandler;

import cratig.beamrider.scenes.GameScene;

public class GameLoopUpdateHandler implements IUpdateHandler{

	@Override
	public void onUpdate(float secondsElapsed) {
		((GameScene) MainActivity.getSharedInstance().currentScene).moveShip();
		((GameScene) MainActivity.getSharedInstance().currentScene).shipBulletCleaner();
	}

	@Override
	public void reset() {
		
	}

}
