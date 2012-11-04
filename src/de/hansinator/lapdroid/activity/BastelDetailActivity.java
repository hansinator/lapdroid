package de.hansinator.lapdroid.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import de.hansinator.automation.lab.BastelControl;
import de.hansinator.automation.lab.LightMaster;
import de.hansinator.automation.lap.LAPDevice.LAPStateUpdateListener;
import de.hansinator.lapdroid.R;
import de.hansinator.lapdroid.lap.Labor;

public class BastelDetailActivity extends Activity implements OnSeekBarChangeListener {

	private SeekBar decodeSeekbar(BastelControl.Objects key) {
		switch (key) {
		case pwm_window:
			return (SeekBar) findViewById(R.id.bastelDimWindow);
		case pwm_banner:
			return (SeekBar) findViewById(R.id.bastelDimBanner);
		case pwm_orgatable:
			return (SeekBar) findViewById(R.id.bastelDimOrgatable);
		default:
			return null;
		}
	}

	final LAPStateUpdateListener listener = new LAPStateUpdateListener() {

		@Override
		public void onUpdate(int key, Object value, Object lastValue) {
			SeekBar sb = decodeSeekbar(BastelControl.Objects.values()[key]);
			if (sb != null && !sb.isPressed())
			{
				Log.v("bastel","updateSeekbar");
				sb.setProgress((Integer) value);
			}

		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v("bastel", "onCreate");

		setContentView(R.layout.activity_bastel_detail);
		// getActionBar().setDisplayHomeAsUpEnabled(true);

		// add seekbar listeners
		((SeekBar) findViewById(R.id.bastelDimBanner)).setOnSeekBarChangeListener(this);
		((SeekBar) findViewById(R.id.bastelDimOrgatable)).setOnSeekBarChangeListener(this);
		((SeekBar) findViewById(R.id.bastelDimWindow)).setOnSeekBarChangeListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.v("bastel", "onResume");
		BastelControl bc = Labor.getInstance().lm.bastelControl;

		// install state update listener
		bc.setListener(listener);

		// read current values
		for (int i = 0; i < 3; i++)
			decodeSeekbar(BastelControl.Objects.values()[BastelControl.omap_pwm.get((byte)i)]).setProgress(bc.getPwmVals()[i]);

		// request current state
		bc.requestState();
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.v("bastel", "onPause");
		Labor.getInstance().lm.bastelControl.setListener(null);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_bastel_detail, menu);
		return true;
	}

	public void bastelPrinter1On(View view) {
		Labor.getInstance().lm.bastelControl.switchBastelPrinter1(true);
	}

	public void bastelPrinter1Off(View view) {
		Labor.getInstance().lm.bastelControl.switchBastelPrinter1(false);
	}

	public void bastelPrinter2On(View view) {
		Labor.getInstance().lm.bastelControl.switchBastelPrinter2(true);
	}

	public void bastelPrinter2Off(View view) {
		Labor.getInstance().lm.bastelControl.switchBastelPrinter2(false);
	}

	public void bastelHelmer1On(View view) {
		Labor.getInstance().lm.bastelControl.switchBastelHelmer1(true);
	}

	public void bastelHelmer1Off(View view) {
		Labor.getInstance().lm.bastelControl.switchBastelHelmer1(false);
	}

	public void bastelHelmer2On(View view) {
		Labor.getInstance().lm.bastelControl.switchBastelHelmer2(true);
	}

	public void bastelHelmer2Off(View view) {
		Labor.getInstance().lm.bastelControl.switchBastelHelmer2(false);
	}

	public void bastelBannerOn(View view) {
		Labor.getInstance().lm.bastelControl.switchBastelBanner(true);
	}

	public void bastelBannerOff(View view) {
		Labor.getInstance().lm.bastelControl.switchBastelBanner(false);
	}

	public void bastelOrgatableOn(View view) {
		Labor.getInstance().lm.bastelControl.switchBastelOrgatable(true);
	}

	public void bastelOrgatableOff(View view) {
		Labor.getInstance().lm.bastelControl.switchBastelOrgatable(false);
	}

	public void bastelWindowOn(View view) {
		Labor.getInstance().lm.bastelControl.switchBastelWindow(true);
	}

	public void bastelWindowOff(View view) {
		Labor.getInstance().lm.bastelControl.switchBastelWindow(false);
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		if (fromUser && seekBar.isPressed()) {
			LightMaster lm = Labor.getInstance().lm;
			switch (seekBar.getId()) {
			case R.id.bastelDimBanner:
				lm.bastelControl.dimBastelBanner(seekBar.getProgress());
				break;
			case R.id.bastelDimOrgatable:
				lm.bastelControl.dimBastelOrgatable(seekBar.getProgress());
				break;
			case R.id.bastelDimWindow:
				lm.bastelControl.dimBastelWindow(seekBar.getProgress());
				break;
			}
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
	}
}
