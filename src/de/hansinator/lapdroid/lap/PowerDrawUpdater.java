package de.hansinator.lapdroid.lap;

import java.util.Locale;

import android.os.Handler;
import android.widget.TextView;
import de.hansinator.automation.lab.PowerMeter.PowerUpdateListener;

public class PowerDrawUpdater implements PowerUpdateListener {

	private final Handler handler;

	private final TextView view;
	
	private long next = System.currentTimeMillis();

	private long updateInterval;

	public PowerDrawUpdater(Handler handler, TextView view, long updateInterval) {
		this.handler = handler;
		this.view = view;
		this.updateInterval = updateInterval;
	}

	@Override
	public void onUpdate(final double watts) {
		if (System.currentTimeMillis() > next) {
			next = System.currentTimeMillis() + updateInterval;
			handler.post(new Runnable() {
				public void run() {
					view.setText(String.format(Locale.US, "Power usage %4.2f Watts", watts));
				}
			});
		}
	}
}