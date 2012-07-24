package cratig.beamrider.ship;

import java.util.Timer;
import java.util.TimerTask;

public class ShipBulletTimer {
	private boolean valid;
	private Timer timer;
	private long delay = 400;
	private static ShipBulletTimer instance = null;
	
	public static ShipBulletTimer getSharedInstance() {
		if (instance == null) {
			instance = new ShipBulletTimer();
		}
		
		return instance;
	}
	
	private ShipBulletTimer() {
		timer = new Timer();
		valid = true;
	}
	
	public boolean isValid() {
		if (valid) {
			valid = false;
			timer.schedule(new Task(), delay);
			
			return true;
		}
		
		return false;
	}
	
	class Task extends TimerTask {

		@Override
		public void run() {
			valid = true;
		}
		
	}
}
