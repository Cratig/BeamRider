package cratig.beamrider;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.TextMenuItem;

public class MainMenuScene extends MenuScene implements IOnMenuItemClickListener{

	MainActivity activity;
	final int MENU_START = 0;
	
	public MainMenuScene() {
		super(MainActivity.getSharedInstance().camera);
		
		activity = MainActivity.getSharedInstance();
		
		setBackground(new Background(0.09804f, 0.6274f, 0.8784f));
		
		IMenuItem startButton = new TextMenuItem(MENU_START, activity.mFont, activity.getString(R.string.menuStart), activity.getVertexBufferObjectManager());
	}
	
	@Override
	public boolean onMenuItemClicked(MenuScene arg0, IMenuItem arg1,
			float arg2, float arg3) {

		return false;
	}

}
