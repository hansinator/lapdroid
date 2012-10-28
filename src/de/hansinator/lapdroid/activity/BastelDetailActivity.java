package de.hansinator.lapdroid.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import de.hansinator.incubator.BastelControl.BastelStateUpdateListener;
import de.hansinator.incubator.LightMaster;
import de.hansinator.lapdroid.R;
import de.hansinator.lapdroid.lap.Labor;

public class BastelDetailActivity extends Activity implements OnSeekBarChangeListener {
	
	final int lastPwmVals[] = new int[] { 0, 0, 0 };
	
	final BastelStateUpdateListener listener = new BastelStateUpdateListener() {

		@Override
		public void onUpdate(boolean[] switchVals, int[] pwmVals) {
			if (pwmVals[0] != lastPwmVals[0]) {
				lastPwmVals[0] = pwmVals[0];
				((SeekBar) findViewById(R.id.bastelDimWindow)).setProgress(pwmVals[0]);
			}
			if (pwmVals[1] != lastPwmVals[1]) {
				lastPwmVals[1] = pwmVals[1];
				((SeekBar) findViewById(R.id.bastelDimBanner)).setProgress(pwmVals[1]);
			}
			if (pwmVals[2] != lastPwmVals[2]) {
				lastPwmVals[2] = pwmVals[2];
				((SeekBar) findViewById(R.id.bastelDimOrgatable)).setProgress(pwmVals[2]);
			}

		}
	}; 

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bastel_detail);
		// getActionBar().setDisplayHomeAsUpEnabled(true);

		// add seekbar listeners
		((SeekBar) findViewById(R.id.bastelDimBanner)).setOnSeekBarChangeListener(this);
		((SeekBar) findViewById(R.id.bastelDimOrgatable)).setOnSeekBarChangeListener(this);
		((SeekBar) findViewById(R.id.bastelDimWindow)).setOnSeekBarChangeListener(this);
	}
	
	@Override
	protected void onResume() {
		// install state update listener
		Labor.getInstance().lm.bastelControl.setListener(listener);

		//request current state
		Labor.getInstance().lm.bastelControl.requestState();
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		Labor.getInstance().lm.bastelControl.setListener(null);
		super.onPause();
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
		if(fromUser)
		{
			LightMaster lm = Labor.getInstance().lm;
			switch(seekBar.getId())
			{
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
