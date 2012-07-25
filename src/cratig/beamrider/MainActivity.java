package cratig.beamrider;

import java.io.IOException;
import java.io.InputStream;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.adt.io.in.IInputStreamOpener;
import org.andengine.util.debug.Debug;

import android.graphics.Typeface;
import cratig.beamrider.scenes.GameScene;
import cratig.beamrider.scenes.SplashScene;

public class MainActivity extends SimpleBaseGameActivity {

	// Define width / height
	private static final int CAMERA_WIDTH = 800;
	private static final int CAMERA_HEIGHT = 480;

	// Define camera
	public Camera camera;

	// Define Scenes
	public Scene currentScene;

	// Define instance
	public static MainActivity instance;

	// EngineOptions
	private EngineOptions engineOptions;

	// Define font
	public Font mFont;

	// Test sprite
	BitmapTextureAtlas bitmapTextureAtlas;
	TextureRegion playerTextureRegion;
	
	public ITexture enemyTexture;
	public ITextureRegion enemyTextureRegion;

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
		
		try {
			enemyTexture = new BitmapTexture(getTextureManager(), new IInputStreamOpener() {
				
				@Override
				public InputStream open() throws IOException {
					return getAssets().open("face_box.png");
				}
			});
			
			enemyTexture.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Debug.e(e);
		}
		
		enemyTextureRegion = TextureRegionFactory.extractFromTexture(enemyTexture);
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

	@Override
	public void onBackPressed() {
		if (currentScene instanceof GameScene) {
			((GameScene) currentScene).detach();
		}
		
		currentScene = null;
		SensorListener.instance = null;
		super.onBackPressed();
	}
}
