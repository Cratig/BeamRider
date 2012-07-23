package cratig.beamrider;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import android.graphics.Typeface;

public class MainActivity extends SimpleBaseGameActivity {

	// Define width / height
	private static final int CAMERA_WIDTH = 800;
	private static final int CAMERA_HEIGHT = 480;

	// Define camera
	Camera camera;

	// Define Scenes
	private Scene currentScene;

	// Define instance
	public static MainActivity instance;

	// EngineOptions
	private EngineOptions engineOptions;

	// Define font
	Font mFont;

	// Test sprite
	BitmapTextureAtlas bitmapTextureAtlas;
	TextureRegion playerTextureRegion;

	public EngineOptions onCreateEngineOptions() {
		instance = this;

		camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		engineOptions = new EngineOptions(true,
				ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(
						CAMERA_WIDTH, CAMERA_HEIGHT), this.camera);
		
		return this.engineOptions;
	}

	@Override
	protected void onCreateResources() {
		mFont = FontFactory.create(this.getFontManager(),
				this.getTextureManager(), 256, 256,
				Typeface.create(Typeface.MONOSPACE, Typeface.BOLD_ITALIC), 64);
		mFont.load();
	}

	@Override
	protected Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		currentScene = new SplashScene();
		
		return currentScene;
	}
	
	public static MainActivity getSharedInstance() {
		return instance;
	}
	
	public void setCurrentScene(Scene scene) {
		currentScene = scene;
		getEngine().setScene(this.currentScene);
	}

}
