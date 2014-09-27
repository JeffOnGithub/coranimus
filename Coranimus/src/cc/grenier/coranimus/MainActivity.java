package cc.grenier.coranimus;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

public class MainActivity extends Activity {
	private BluetoothAdapter mBluetoothAdapter;
	private GraphView graphView;
	private GraphViewSeries exampleSeries;
	private double graphX = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Initializes a Bluetooth adapter. For API level 18 and above, get a
		// reference to
		// BluetoothAdapter through BluetoothManager.
		final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		mBluetoothAdapter = bluetoothManager.getAdapter();

        final Button button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	Intent intent = new Intent(MainActivity.this, ViewRecordsActivity.class);
    			startActivity(intent);
             }
        });
	        
		// Control the Watch Service
		Switch switchWatchHeart = (Switch) findViewById(R.id.switchWatchHeart);
		switchWatchHeart
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {

							startService(new Intent(MainActivity.this,
									WatchHeartService.class));

							registerReceiver(uiUpdated, new IntentFilter(
									"RATE_UPDATED"));
						} else {
							// The toggle is disabled
							stopService(new Intent(MainActivity.this,
									WatchHeartService.class));
						}

					}
				});

		GraphView graphView = new LineGraphView(this // context
				, "Heart rate history" // heading
		);
		// init example series data
		exampleSeries = new GraphViewSeries(
				new GraphViewData[] { new GraphViewData(graphX++, 0),
						new GraphViewData(graphX++, 0),
						new GraphViewData(graphX++, 0),
						new GraphViewData(graphX++, 0),
						new GraphViewData(graphX++, 0),
						new GraphViewData(graphX++, 0),
						new GraphViewData(graphX++, 0),
						new GraphViewData(graphX++, 0),
						new GraphViewData(graphX++, 0),
						new GraphViewData(graphX++, 0) });
		graphView.setScrollable(true);
		graphView.setManualYAxis(true);
		graphView.setManualYAxisBounds(200, 40);
		graphView.addSeries(exampleSeries); // data
		LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayout1);
		layout.addView(graphView);
	}

	@Override
	protected void onStart() {
		super.onStart();
		SharedPreferences prefs = getSharedPreferences("cc.grenier.coranimus",
				MODE_PRIVATE);
		if (prefs.getBoolean("isWatchHeartServiceRunning", false)) {
			Switch switchWatchHeart = (Switch) findViewById(R.id.switchWatchHeart);
			switchWatchHeart.setChecked(true);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(uiUpdated);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
			return true;
		}
		if (id == R.id.action_about) {
			Context context = getApplicationContext();
			CharSequence text = "TODO: About activity";
			int duration = Toast.LENGTH_LONG;

			Toast toast = Toast.makeText(context, text, duration);
			toast.show();

			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
 	// Receiver, update the current beat and the graphview
	private BroadcastReceiver uiUpdated = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String newRate = intent.getExtras().getString("rate");
			int intNewRate = Integer.parseInt(newRate);

			// Update the textview
			TextView textElement = (TextView) findViewById(R.id.textView3);
			textElement.setText(" " + newRate + " BPM");

			// Update the graphview
			GraphViewData updateDataPoint = new GraphViewData(graphX++,
					intNewRate);
			exampleSeries.appendData(updateDataPoint, false, 10);

		}
	};

}