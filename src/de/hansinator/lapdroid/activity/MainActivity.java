package de.hansinator.lapdroid.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import de.hansinator.incubator.LightMaster;
import de.hansinator.lapdroid.R;
import de.hansinator.lapdroid.lap.Labor;
import de.hansinator.lapdroid.lap.PowerDrawUpdater;

public class MainActivity extends Activity implements OnSeekBarChangeListener {

	PowerDrawUpdater powerUpdater;

	// useful fragment: EditText editText = (EditText) findViewById(R.id.edit_message);
	// intent.putExtra(EXTRA_LIGHTMASTER_OBJ, lm);
	// public final static String EXTRA_LIGHTMASTER_OBJ = "de.hansinator.lapdroid.LIGHTMASTER";

	/*
	 * Implement watcher service that reacts to conenctions to l1 network when connected to l1, send
	 * a "you're in the lab" welcome notification and offer to participate in LAME voluntarily
	 */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v("main", "onCreate");

		// Set the user interface layout for this Activity
		// The layout file is defined in the project res/layout/main_activity.xml file
		setContentView(R.layout.activity_main);

		// get our lab
		Labor lab = Labor.getInstance();

		// the connection may have died before, enable auto restart
		if (!lab.start(10000, true)) {
			AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
			dlgAlert.setTitle("Error");
			dlgAlert.setMessage("Can't connect to cand");
			dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					finish();
				}
			});
			dlgAlert.create().show();
		}

		// add seekbar listeners
		((SeekBar) findViewById(R.id.loungeDimAll)).setOnSeekBarChangeListener(this);
		((SeekBar) findViewById(R.id.lectureDimAll)).setOnSeekBarChangeListener(this);
		((SeekBar) findViewById(R.id.bastelDimAll)).setOnSeekBarChangeListener(this);
		((SeekBar) findViewById(R.id.kitchenDim)).setOnSeekBarChangeListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.v("main", "onResume");

		// create power draw data listener and attach to power meter
		Labor.getInstance().pm.setListener(new PowerDrawUpdater(new Handler(), (TextView) findViewById(R.id.power_label), 999));
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.v("main", "onPause");
		Labor.getInstance().pm.setListener(null);
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.v("main", "onDestroy");
		Labor.getInstance().stop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void ringBell(View view) {
		Labor.getInstance().bell.ring();
	}

	public void kitchenOn(View view) {
		Labor.getInstance().lm.powerCommander.switchKitchen(true);
	}

	public void kitchenOff(View view) {
		Labor.getInstance().lm.powerCommander.switchKitchen(false);
	}

	public void lectureOn(View view) {
		Labor.getInstance().lm.powerCommander.switchLectureAll(true);
	}

	public void lectureOff(View view) {
		Labor.getInstance().lm.powerCommander.switchLectureAll(false);
	}

	public void loungeOn(View view) {
		Labor.getInstance().lm.switchLoungeAll(true);
	}

	public void loungeOff(View view) {
		Labor.getInstance().lm.switchLoungeAll(false);
	}

	public void bastelOn(View view) {
		Labor.getInstance().lm.bastelControl.switchBastelAll(true);
	}

	public void bastelOff(View view) {
		Labor.getInstance().lm.bastelControl.switchBastelAll(false);
	}

	public void loungeViewDetail(View view) {
		Intent intent = new Intent(this, LoungeDetailActivity.class);
		startActivity(intent);
	}

	public void lectureViewDetail(View view) {
		Intent intent = new Intent(this, LectureDetailActivity.class);
		startActivity(intent);
	}

	public void bastelViewDetail(View view) {
		Intent intent = new Intent(this, BastelDetailActivity.class);
		startActivity(intent);
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		LightMaster lm = Labor.getInstance().lm;
		switch (seekBar.getId()) {
		case R.id.loungeDimAll:
			lm.dimLoungeAll(seekBar.getProgress());
			break;
		case R.id.lectureDimAll:
			lm.powerCommander.dimLectureAll(seekBar.getProgress());
			break;
		case R.id.bastelDimAll:
			lm.bastelControl.dimBastelAll(seekBar.getProgress());
			break;
		case R.id.kitchenDim:
			lm.powerCommander.dimKitchen(seekBar.getProgress());
			break;
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
	}
}
