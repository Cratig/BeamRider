package cratig.beamrider;

import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.RotationAtModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.entity.modifier.*;
import org.andengine.entity.*;
import org.andengine.util.modifier.*;

public class SplashScene extends Scene {

	MainActivity activity;

	public SplashScene() {
		setBackground(new Background(0.8f, 0.75f, 0.7f));

		activity = MainActivity.getSharedInstance();

		Text title1 = new Text(0, 0, activity.mFont,
				activity.getString(R.string.splashTitle_1),
				activity.getVertexBufferObjectManager());
		Text title2 = new Text(0, 0, activity.mFont,
				activity.getString(R.string.splashTitle_2),
				activity.getVertexBufferObjectManager());

		title1.setPosition(-title1.getWidth(), activity.camera.getHeight() / 2);
		title2.setPosition(activity.camera.getWidth(),
				activity.camera.getHeight() / 2);

		this.attachChild(title1);
		this.attachChild(title2);

		title1.registerEntityModifier(new MoveXModifier(2.0f, title1.getX(),
				activity.camera.getWidth() / 2 - title1.getWidth()));
		title2.registerEntityModifier(new MoveXModifier(2.0f, title2.getX(),
				activity.camera.getWidth() / 2));

		title1.registerEntityModifier(new RotationAtModifier(2.0f, -37.0f,
				0.0f, 0.0f, this.getRotationCenterY()));
		title2.registerEntityModifier(new RotationAtModifier(2.0f, 85.0f, 0.0f,
				0.0f, this.getRotationCenterY()));

		DelayModifier delayModifier = new DelayModifier(4,

		new IEntityModifier.IEntityModifierListener() {

			@Override
			public void onModifierStarted(IModifier modifier, IEntity entity) {

			}

			public void onModifierFinished(IModifier modifier, IEntity entity) {
				activity.setCurrentScene(new MainMenuScene());
			}
		}

		);

		registerEntityModifier(delayModifier);

	}

}
