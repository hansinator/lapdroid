package de.hansinator.lapdroid.lap;

import android.util.Log;
import de.hansinator.automation.lab.Bell;
import de.hansinator.automation.lab.LightMaster;
import de.hansinator.automation.lab.PowerMeter;
import de.hansinator.automation.lap.LAPTCPCanGateway;
import de.hansinator.message.bus.MessageBus;
import de.hansinator.message.protocol.LAPMessage;

public final class Labor {
	private static String LNET = "Network";

	private static Object lock = new Object();

	private static Labor instance = null;

	// messaging bus
	public final MessageBus<LAPMessage> bus;

	// lap-cantcp message gateway
	public final LAPTCPCanGateway gateway;

	public final LightMaster lm;

	public final PowerMeter pm;

	public final Bell bell;

	private Labor() {
		Log.v("singleton", "instantiating");

		// create message bus
		this.bus = new MessageBus<LAPMessage>();

		// create devices
		lm = new LightMaster(bus);
		pm = new PowerMeter(bus);
		bell = new Bell(bus);

		// create gateway
		gateway = LAPTCPCanGateway.makeGateway(bus, "10.0.1.2", 2342, true);
	}

	public static Labor getInstance() {
		synchronized (lock) {
			if (instance == null)
				instance = new Labor();
		}

		return instance;
	}

	public synchronized boolean start(int timeout, boolean autoRestart) {
		Log.v(LNET, "Starting endpoint");
		gateway.setAutoConnect(autoRestart);
		boolean ret = gateway.blockingStart(timeout);

		if (!ret)
			Log.d(LNET, "Failed to start endpoint");
		return ret;
	}

	public synchronized void stop() {
		Log.v(LNET, "Stopping endpoint");
		gateway.stop();
	}
}