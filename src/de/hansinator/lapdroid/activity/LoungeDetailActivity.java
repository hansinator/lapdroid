package de.hansinator.lapdroid.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import de.hansinator.incubator.LightMaster;
import de.hansinator.incubator.LoungeDimmer.LoungeStateUpdateListener;
import de.hansinator.lapdroid.R;
import de.hansinator.lapdroid.lap.Labor;

public class LoungeDetailActivity extends Activity implements OnSeekBarChangeListener {

	final int lastPwmVals[] = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };

	final LoungeStateUpdateListener listenerWall = new LoungeStateUpdateListener() {

		@Override
		public void onUpdate(boolean[] switchVals, int[] pwmVals) {
			if (pwmVals[3] != lastPwmVals[0]) {
				lastPwmVals[0] = pwmVals[3];
				((SeekBar) findViewById(R.id.loungeDimNeonWall)).setProgress(pwmVals[3]);
			}
			if (pwmVals[0] != lastPwmVals[1]) {
				lastPwmVals[1] = pwmVals[0];
				((SeekBar) findViewById(R.id.loungeDimSpotWall1)).setProgress(pwmVals[0]);
			}
			if (pwmVals[1] != lastPwmVals[2]) {
				lastPwmVals[2] = pwmVals[1];
				((SeekBar) findViewById(R.id.loungeDimSpotWall2)).setProgress(pwmVals[1]);
			}
			if (pwmVals[2] != lastPwmVals[3]) {
				lastPwmVals[3] = pwmVals[2];
				((SeekBar) findViewById(R.id.loungeDimSpotWall3)).setProgress(pwmVals[2]);
			}

		}
	};

	final LoungeStateUpdateListener listenerDoor = new LoungeStateUpdateListener() {

		@Override
		public void onUpdate(boolean[] switchVals, int[] pwmVals) {
			if (pwmVals[3] != lastPwmVals[4]) {
				lastPwmVals[4] = pwmVals[3];
				((SeekBar) findViewById(R.id.loungeDimNeonDoor)).setProgress(pwmVals[3]);
			}
			if (pwmVals[0] != lastPwmVals[5]) {
				lastPwmVals[5] = pwmVals[0];
				((SeekBar) findViewById(R.id.loungeDimSpotDoor1)).setProgress(pwmVals[0]);
			}
			if (pwmVals[1] != lastPwmVals[6]) {
				lastPwmVals[6] = pwmVals[1];
				((SeekBar) findViewById(R.id.loungeDimSpotDoor2)).setProgress(pwmVals[1]);
			}
			if (pwmVals[2] != lastPwmVals[7]) {
				lastPwmVals[7] = pwmVals[2];
				((SeekBar) findViewById(R.id.loungeDimSpotDoor3)).setProgress(pwmVals[2]);
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lounge_detail);
		// getActionBar().setDisplayHomeAsUpEnabled(true);

		// add seekbar listeners
		((SeekBar) findViewById(R.id.loungeDimNeonWall)).setOnSeekBarChangeListener(this);
		((SeekBar) findViewById(R.id.loungeDimSpotWall1)).setOnSeekBarChangeListener(this);
		((SeekBar) findViewById(R.id.loungeDimSpotWall2)).setOnSeekBarChangeListener(this);
		((SeekBar) findViewById(R.id.loungeDimSpotWall3)).setOnSeekBarChangeListener(this);
		((SeekBar) findViewById(R.id.loungeDimNeonDoor)).setOnSeekBarChangeListener(this);
		((SeekBar) findViewById(R.id.loungeDimSpotDoor1)).setOnSeekBarChangeListener(this);
		((SeekBar) findViewById(R.id.loungeDimSpotDoor2)).setOnSeekBarChangeListener(this);
		((SeekBar) findViewById(R.id.loungeDimSpotDoor3)).setOnSeekBarChangeListener(this);
	}

	@Override
	protected void onResume() {
		// install state update listeners
		Labor.getInstance().lm.loungeDimmerWall.setListener(listenerWall);
		Labor.getInstance().lm.loungeDimmerDoor.setListener(listenerDoor);

		// request current states
		Labor.getInstance().lm.loungeDimmerWall.requestState();
		Labor.getInstance().lm.loungeDimmerDoor.requestState();
		super.onResume();
	}

	@Override
	protected void onPause() {
		Labor.getInstance().lm.loungeDimmerWall.setListener(null);
		Labor.getInstance().lm.loungeDimmerDoor.setListener(null);
		super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_lounge_detail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void loungeNeonWallOn(View view) {
		Labor.getInstance().lm.loungeDimmerWall.switchNeonTube(true);
	}

	public void loungeNeonWallOff(View view) {
		Labor.getInstance().lm.loungeDimmerWall.switchNeonTube(false);
	}

	public void loungeNeonDoorOn(View view) {
		Labor.getInstance().lm.loungeDimmerDoor.switchNeonTube(true);
	}

	public void loungeNeonDoorOff(View view) {
		Labor.getInstance().lm.loungeDimmerDoor.switchNeonTube(false);
	}

	public void loungeSpotWall1On(View view) {
		Labor.getInstance().lm.loungeDimmerWall.switchSpot1(true);
	}

	public void loungeSpotWall1Off(View view) {
		Labor.getInstance().lm.loungeDimmerWall.switchSpot1(false);
	}

	public void loungeSpotWall2On(View view) {
		Labor.getInstance().lm.loungeDimmerWall.switchSpot2(true);
	}

	public void loungeSpotWall2Off(View view) {
		Labor.getInstance().lm.loungeDimmerWall.switchSpot2(false);
	}

	public void loungeSpotWall3On(View view) {
		Labor.getInstance().lm.loungeDimmerWall.switchSpot3(true);
	}

	public void loungeSpotWall3Off(View view) {
		Labor.getInstance().lm.loungeDimmerWall.switchSpot3(false);
	}

	public void loungeSpotDoor1On(View view) {
		Labor.getInstance().lm.loungeDimmerDoor.switchSpot1(true);
	}

	public void loungeSpotDoor1Off(View view) {
		Labor.getInstance().lm.loungeDimmerDoor.switchSpot1(false);
	}

	public void loungeSpotDoor2On(View view) {
		Labor.getInstance().lm.loungeDimmerDoor.switchSpot2(true);
	}

	public void loungeSpotDoor2Off(View view) {
		Labor.getInstance().lm.loungeDimmerDoor.switchSpot2(false);
	}

	public void loungeSpotDoor3On(View view) {
		Labor.getInstance().lm.loungeDimmerDoor.switchSpot3(true);
	}

	public void loungeSpotDoor3Off(View view) {
		Labor.getInstance().lm.loungeDimmerDoor.switchSpot3(false);
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		if (fromUser) {
			LightMaster lm = Labor.getInstance().lm;
			switch (seekBar.getId()) {
			case R.id.loungeDimNeonWall:
				lm.loungeDimmerWall.dimNeonTube(progress);
				break;
			case R.id.loungeDimSpotWall1:
				lm.loungeDimmerWall.dimSpot1(progress);
				break;
			case R.id.loungeDimSpotWall2:
				lm.loungeDimmerWall.dimSpot2(progress);
				break;
			case R.id.loungeDimSpotWall3:
				lm.loungeDimmerWall.dimSpot3(progress);
				break;
			case R.id.loungeDimNeonDoor:
				lm.loungeDimmerDoor.dimNeonTube(progress);
				break;
			case R.id.loungeDimSpotDoor1:
				lm.loungeDimmerDoor.dimSpot1(progress);
				break;
			case R.id.loungeDimSpotDoor2:
				lm.loungeDimmerDoor.dimSpot2(progress);
				break;
			case R.id.loungeDimSpotDoor3:
				lm.loungeDimmerDoor.dimSpot3(progress);
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
