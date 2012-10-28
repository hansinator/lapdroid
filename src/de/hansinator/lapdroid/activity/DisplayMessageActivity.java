package de.hansinator.lapdroid.activity;

import android.app.Activity;
import android.os.Bundle;
import de.hansinator.lapdroid.R;

public class DisplayMessageActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_message);
		//getActionBar().setDisplayHomeAsUpEnabled(true);

		/*Intent i = getIntent();
		String msg = i.getStringExtra(MainActivity.EXTRA_MESSAGE);

		// Create the text view
		TextView textView = new TextView(this);
		textView.setTextSize(40);
		textView.setText(msg);

		// Set the text view as the activity layout
		setContentView(textView);*/
	}

}
