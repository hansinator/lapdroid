package de.hansinator.lapdroid.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;
import de.hansinator.automation.lab.LoungeDimmer;
import de.hansinator.automation.lap.LAPDevice.LAPStateUpdateListener;
import de.hansinator.lapdroid.R;
import de.hansinator.lapdroid.lap.Labor;

public class LoungeDetailActivity extends Activity implements OnSeekBarChangeListener {

	private LightAdapter sliderSwitches;

	public class LightAdapter extends BaseAdapter {
		private final LayoutInflater inflater;
		private final String[] names;
		private int[] sliderVals;
		private boolean[] switchVals;
		private final View[] views;

		public LightAdapter(Context context, String[] names) {
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.names = names;
			sliderVals = new int[names.length];
			switchVals = new boolean[names.length];
			views = new View[names.length];
		}

		@Override
		public synchronized View getView(int position, View convertView, ViewGroup parent) {
			if (views[position] == null)
				views[position] = createView(position, parent);
			return views[position];
		}

		public View createView(int position, ViewGroup parent) {
			Log.v("lightadapter", "create view " + position);
			View rowView = inflater.inflate(R.layout.sliderswitch_control, parent, false);
			TextView textView = (TextView) rowView.findViewById(R.id.label);
			SeekBar seekBar = (SeekBar) rowView.findViewById(R.id.seekBar);
			ToggleButton tb = (ToggleButton) rowView.findViewById(R.id.toggleButton);
			textView.setText(names[position]);
			seekBar.setProgress(sliderVals[position]);
			tb.setChecked(switchVals[position]);
			return rowView;
		}

		@Override
		public int getCount() {
			return names.length;
		}

		@Override
		public Object getItem(int position) {
			return names[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		public void updateSlider(int position, int value) {
			sliderVals[position] = value;
			notifyDataSetChanged();
		}

		public void updateSwitch(int position, boolean value) {
			switchVals[position] = value;
			notifyDataSetChanged();
		}

		@Override
		public void notifyDataSetChanged() {
			Log.v("lightadapter", "update-dataset");
			super.notifyDataSetChanged();
		}
	}

	private Integer decodeSliderPosition(LoungeDimmer.Objects key) {
		switch (key) {
		case pwm_spot1:
			return 0;
		case pwm_spot2:
			return 1;
		case pwm_spot3:
			return 2;
		case pwm_neon:
			return 3;
		default:
			return null;
		}
	}

	private Integer decodeSwitchPosition(LoungeDimmer.Objects key) {
		switch (key) {
		case switch_spot1:
			return 0;
		case switch_spot2:
			return 1;
		case switch_spot3:
			return 2;
		case switch_neon:
			return 3;
		default:
			return null;
		}
	}

	final LAPStateUpdateListener listenerWall = new LAPStateUpdateListener() {

		@Override
		public void onUpdate(int key, Object value, Object lastValue) {
			Integer slider = decodeSliderPosition(LoungeDimmer.Objects.values()[key]);
			if (slider != null)
				sliderSwitches.updateSlider(slider, (Integer) value);
			else {
				Integer sw = decodeSwitchPosition(LoungeDimmer.Objects.values()[key]);
				if (sw != null)
					sliderSwitches.updateSwitch(sw, (Boolean) value);
			}
		}
	};

	final LAPStateUpdateListener listenerDoor = new LAPStateUpdateListener() {

		@Override
		public void onUpdate(int key, Object value, Object lastValue) {
			Integer slider = decodeSliderPosition(LoungeDimmer.Objects.values()[key]);
			if (slider != null)
				sliderSwitches.updateSlider(slider + 4, (Integer) value);
			else {
				Integer sw = decodeSwitchPosition(LoungeDimmer.Objects.values()[key]);
				if (sw != null)
					sliderSwitches.updateSwitch(sw + 4, (Boolean) value);
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v("lounge", "onCreate");
		setContentView(R.layout.activity_lounge_detail);
		// getActionBar().setDisplayHomeAsUpEnabled(true);

		// create sliderswitches
		sliderSwitches = new LightAdapter(this, new String[] { "Wand Spot 1", "Wand Spot 2", "Wand Spot 3", "Wand Neonlicht",
				"Tür Spot 1", "Tür Spot 2", "Tür Spot 3", "Tür Neonlicht" });

		((ListView) findViewById(R.id.lightList)).setAdapter(sliderSwitches);

		// add seekbar listeners
		/*
		 * ((SeekBar) findViewById(R.id.loungeDimNeonWall)).setOnSeekBarChangeListener(this);
		 * ((SeekBar) findViewById(R.id.loungeDimSpotWall1)).setOnSeekBarChangeListener(this);
		 * ((SeekBar) findViewById(R.id.loungeDimSpotWall2)).setOnSeekBarChangeListener(this);
		 * ((SeekBar) findViewById(R.id.loungeDimSpotWall3)).setOnSeekBarChangeListener(this);
		 * ((SeekBar) findViewById(R.id.loungeDimNeonDoor)).setOnSeekBarChangeListener(this);
		 * ((SeekBar) findViewById(R.id.loungeDimSpotDoor1)).setOnSeekBarChangeListener(this);
		 * ((SeekBar) findViewById(R.id.loungeDimSpotDoor2)).setOnSeekBarChangeListener(this);
		 * ((SeekBar) findViewById(R.id.loungeDimSpotDoor3)).setOnSeekBarChangeListener(this);
		 */
	}

	@Override
	protected void onResume() {
		Log.v("lounge", "onResume");
		LoungeDimmer door = Labor.getInstance().lm.loungeDimmerDoor, wall = Labor.getInstance().lm.loungeDimmerWall;

		// install state update listeners
		wall.setListener(listenerWall);
		door.setListener(listenerDoor);

		// read current values
		for (int i = 0; i < 4; i++) {
			sliderSwitches.updateSlider(i, wall.getPwmVals()[i]);
			sliderSwitches.updateSwitch(i, wall.getSwitchVals()[i]);
		}
		for (int i = 0; i < 4; i++) {
			sliderSwitches.updateSlider(i+4, door.getPwmVals()[i]);
			sliderSwitches.updateSwitch(i+4, door.getSwitchVals()[i]);
		}

		// request current states (will not update if there was no change)
		wall.requestState();
		door.requestState();
		super.onResume();
	}

	@Override
	protected void onPause() {
		Log.v("lounge", "onPause");
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
		/*
		 * if (fromUser) { LightMaster lm = Labor.getInstance().lm; switch (seekBar.getId()) { case
		 * R.id.loungeDimNeonWall: lm.loungeDimmerWall.dimNeonTube(progress); break; case
		 * R.id.loungeDimSpotWall1: lm.loungeDimmerWall.dimSpot1(progress); break; case
		 * R.id.loungeDimSpotWall2: lm.loungeDimmerWall.dimSpot2(progress); break; case
		 * R.id.loungeDimSpotWall3: lm.loungeDimmerWall.dimSpot3(progress); break; case
		 * R.id.loungeDimNeonDoor: lm.loungeDimmerDoor.dimNeonTube(progress); break; case
		 * R.id.loungeDimSpotDoor1: lm.loungeDimmerDoor.dimSpot1(progress); break; case
		 * R.id.loungeDimSpotDoor2: lm.loungeDimmerDoor.dimSpot2(progress); break; case
		 * R.id.loungeDimSpotDoor3: lm.loungeDimmerDoor.dimSpot3(progress); break; } }
		 */
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
	}
}
