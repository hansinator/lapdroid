package de.hansinator.lapdroid.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import de.hansinator.automation.lab.LightMaster;
import de.hansinator.automation.lap.LAPDevice.LAPStateUpdateListener;
import de.hansinator.lapdroid.R;
import de.hansinator.lapdroid.lap.Labor;

public class LoungeDetailActivity extends Activity implements OnSeekBarChangeListener {

	final Integer lastPwmVals[] = new Integer[] { 0, 0, 0, 0, 0, 0, 0, 0 };

	final boolean lastSwitchVals[] = new boolean[] { false, false, false, false, false, false, false, false };
	
	public class LightAdapter extends ArrayAdapter<Integer> {
		  private final Context context;

		  public LightAdapter(Context context) {
		    super(context, R.layout.sliderswitch_control, lastPwmVals);
		    this.context = context;
		  }

		  @Override
		  public View getView(int position, View convertView, ViewGroup parent) {
		    LayoutInflater inflater = (LayoutInflater) context
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    View rowView = inflater.inflate(R.layout.sliderswitch_control, parent, false);
		    TextView textView = (TextView) rowView.findViewById(R.id.label);
		    SeekBar seekBar = (SeekBar) rowView.findViewById(R.id.seekBar);
		    textView.setText();
		    seekBar.setProgress(lastPwmVals[position]);
		    return rowView;
		  }
		} 

	private SeekBar decodeSeekbar(int num) {
		switch (num) {
		case 0:
			return (SeekBar) findViewById(R.id.loungeDimSpotWall1);
		case 1:
			return (SeekBar) findViewById(R.id.loungeDimSpotWall2);
		case 2:
			return (SeekBar) findViewById(R.id.loungeDimSpotWall3);
		case 3:
			return (SeekBar) findViewById(R.id.loungeDimNeonWall);
		case 4:
			return (SeekBar) findViewById(R.id.loungeDimSpotDoor1);
		case 5:
			return (SeekBar) findViewById(R.id.loungeDimSpotDoor2);
		case 6:
			return (SeekBar) findViewById(R.id.loungeDimSpotDoor3);
		case 7:
			return (SeekBar) findViewById(R.id.loungeDimNeonDoor);
		default:
			return null;
		}
	}

	final LAPStateUpdateListener listenerWall = new LAPStateUpdateListener() {

		@Override
		public void onUpdate(int key, Object value, Object lastValue) {
			for (int i = 0; i < 4; i++)
			{
				if (pwmVals[i] != lastPwmVals[i]) {
					lastPwmVals[i] = pwmVals[i];
					decodeSeekbar(i).setProgress(pwmVals[i]);
				}
				
				if(switchVals[i] != lastSwitchVals[i]) {
					lastSwitchVals[i] = switchVals[i];
				}
			}
		}
	};

	final LAPStateUpdateListener listenerDoor = new LAPStateUpdateListener() {

		@Override
		public void onUpdate(int key, Object value, Object lastValue) {
			for (int i = 0; i < 4; i++)
				if (pwmVals[i] != lastPwmVals[i + 4]) {
					lastPwmVals[i + 4] = pwmVals[i];
					decodeSeekbar(i + 4).setProgress(pwmVals[i]);
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
