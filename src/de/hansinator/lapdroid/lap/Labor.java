package de.hansinator.lapdroid.lap;

import java.io.IOException;

import android.util.Log;
import de.hansinator.incubator.Bell;
import de.hansinator.incubator.LAPDevice;
import de.hansinator.incubator.LAPTCPCanGateway;
import de.hansinator.incubator.LightMaster;
import de.hansinator.incubator.PowerMeter;
import de.hansinator.message.bus.MessageBus;
import de.hansinator.message.net.AsyncWriteMessageProxy;
import de.hansinator.message.protocol.CANTCPMessage;
import de.hansinator.message.protocol.LAPMessage;

public final class Labor {
	private static String LNET = "Network";

	private static Object lock = new Object();

	private static Labor instance = null;

	// messaging bus
	public final MessageBus<LAPMessage> bus;

	// lap-tcp endpoint
	AsyncWriteMessageProxy<CANTCPMessage> endpoint;

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
		boolean ret = gateway.up(timeout, autoRestart);
		
		if (!ret)
			Log.d(LNET, "Failed to start endpoint");
		return ret;
	}

	public synchronized void stop() {
		Log.v(LNET, "Stopping endpoint");
		try {
			gateway.disconnect();
		} catch (IOException e) {
			Log.d(LNET, "Failed to stop endpoint", e);
		}
	}
}