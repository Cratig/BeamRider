package cratig.beamrider;

import org.andengine.engine.handler.IUpdateHandler;

public class GameLoopUpdateHandler implements IUpdateHandler{

	@Override
	public void onUpdate(float secondsElapsed) {
		((GameScene) MainActivity.getSharedInstance().currentScene).moveShip();
	}

	@Override
	public void reset() {
		
	}

}
