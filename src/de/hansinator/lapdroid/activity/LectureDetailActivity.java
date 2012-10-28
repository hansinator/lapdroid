package de.hansinator.lapdroid.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import de.hansinator.incubator.LightMaster;
import de.hansinator.lapdroid.R;
import de.hansinator.lapdroid.lap.Labor;

public class LectureDetailActivity extends Activity implements OnSeekBarChangeListener {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_detail);
		// getActionBar().setDisplayHomeAsUpEnabled(true);
        
        //add seekbar listeners
		((SeekBar)findViewById(R.id.lectureDimBlackboard)).setOnSeekBarChangeListener(this);
		((SeekBar)findViewById(R.id.lectureDimBeamer)).setOnSeekBarChangeListener(this);
		((SeekBar)findViewById(R.id.lectureDimLocker)).setOnSeekBarChangeListener(this);
		((SeekBar)findViewById(R.id.lectureDimFlipper)).setOnSeekBarChangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_lecture_detail, menu);
        return true;
    }
    
	public void lectureBlackboardOn(View view) {
		Labor.getInstance().lm.powerCommander.switchLectureBlackboard(true);
	}

	public void lectureBlackboardOff(View view) {
		Labor.getInstance().lm.powerCommander.switchLectureBlackboard(false);
	}
	
	public void lectureBeamerLightOn(View view) {
		Labor.getInstance().lm.powerCommander.switchLectureBeamer(true);
	}

	public void lectureBeamerLightOff(View view) {
		Labor.getInstance().lm.powerCommander.switchLectureBeamer(false);
	}
	
	public void lectureLockerOn(View view) {
		Labor.getInstance().lm.powerCommander.switchLectureLocker(true);
	}

	public void lectureLockerOff(View view) {
		Labor.getInstance().lm.powerCommander.switchLectureLocker(false);
	}
	
	public void lectureFlipperOn(View view) {
		Labor.getInstance().lm.powerCommander.switchLectureFlipper(true);
	}

	public void lectureFlipperOff(View view) {
		Labor.getInstance().lm.powerCommander.switchLectureFlipper(false);
	}
	
	public void lectureDimFlipper(View view) {
		SeekBar sb = ((SeekBar)view.findViewById(R.id.lectureDimFlipper));
		if(!sb.isIndeterminate())
		{
			Labor.getInstance().lm.powerCommander.dimLectureFlipper(sb.getProgress());
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		LightMaster lm = Labor.getInstance().lm;
		switch(seekBar.getId())
		{
		case R.id.lectureDimBlackboard:
			lm.powerCommander.dimLectureBlackboard(seekBar.getProgress());
			break;
		case R.id.lectureDimBeamer:
			lm.powerCommander.dimLectureBeamer(seekBar.getProgress());
			break;
		case R.id.lectureDimLocker:
			lm.powerCommander.dimLectureLocker(seekBar.getProgress());
			break;
		case R.id.lectureDimFlipper:
			lm.powerCommander.dimLectureFlipper(seekBar.getProgress());
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
