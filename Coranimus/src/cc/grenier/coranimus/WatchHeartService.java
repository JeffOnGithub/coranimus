package cc.grenier.coranimus;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;
import cc.grenier.coranimus.database.DatabaseHandler;
import cc.grenier.coranimus.database.Record;

public class WatchHeartService extends Service {
	private NotificationManager mNM;

	// Unique Identification Number for the Notification.
	// We use it on Notification start, and to cancel it.
	private int NOTIFICATION = R.string.local_service_started;

	// Open the DB
	DatabaseHandler db = new DatabaseHandler(this);

	static public boolean isServiceRunning() {
		return true;
	}

	/**
	 * Class for clients to access. Because we know this service always runs in
	 * the same process as its clients, we don't need to deal with IPC.
	 */
	public class LocalBinder extends Binder {
		WatchHeartService getService() {
			return WatchHeartService.this;
		}

	}

	@Override
	public void onCreate() {
		mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		// Display a notification about us starting. We put an icon in the
		// status bar.
		showNotification();

		// Tell the user we started.
		Toast.makeText(this, R.string.local_service_started, Toast.LENGTH_SHORT)
				.show();

		// Get the Device Mac Adress from sharedpreferences
		SharedPreferences prefs = getSharedPreferences("cc.grenier.coranimus",
				MODE_PRIVATE);
		// Bind to BLE
		mDeviceAddress = prefs.getString("deviceID", "");
		Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
		bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

		// Register the receiver
		final IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
		intentFilter
				.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
		intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
		registerReceiver(mGattUpdateReceiver, intentFilter);

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i("WatchHeartService", "Received start id " + startId + ": "
				+ intent);

		// We want this service to continue running until it is explicitly
		// stopped, so return sticky.
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		// Cancel the persistent notification.
		mNM.cancel(NOTIFICATION);

		// Tell the user we stopped.
		Toast.makeText(this, R.string.local_service_stopped, Toast.LENGTH_SHORT)
				.show();

		// Destroy the receiver
		unregisterReceiver(mGattUpdateReceiver);

		// Unbind BLE
		unbindService(mServiceConnection);
		mBluetoothLeService = null;

	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	// This is the object that receives interactions from clients. See
	// RemoteService for a more complete example.
	private final IBinder mBinder = new LocalBinder();

	/**
	 * Show a notification while this service is running.
	 */
	private void showNotification() {
		// The PendingIntent to launch our activity if the user selects this
		// notification
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, MainActivity.class), 0);

		// Set the icon, scrolling text and timestamp
		NotificationCompat.Builder notification = new NotificationCompat.Builder(
				this).setSmallIcon(R.drawable.stat_sample)
				.setContentTitle(getText(R.string.local_service_started))
				.setContentText(getText(R.string.local_service_label))
				.setOngoing(true).setContentIntent(contentIntent);

		// Send the notification.
		mNM.notify(NOTIFICATION, notification.build());
	}

	/**
	 * Bluetooth LE Service
	 */
	private BluetoothLeService mBluetoothLeService;
	private String mDeviceAddress;
	private final static String TAG = "WatchHeart Service";
	private boolean mConnected = false;

	// Code to manage BLE Service lifecycle.
	private final ServiceConnection mServiceConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName componentName,
				IBinder service) {
			mBluetoothLeService = ((BluetoothLeService.LocalBinder) service)
					.getService();
			if (!mBluetoothLeService.initialize()) {
				Log.e(TAG, "Unable to initialize Bluetooth");
				// this.stopSelf();
			}
			// Automatically connects to the device upon successful start-up
			// initialization.
			mBluetoothLeService.connect(mDeviceAddress);

		}

		@Override
		public void onServiceDisconnected(ComponentName componentName) {
			mBluetoothLeService = null;
		}
	};

	// Handles various events fired by the Service.
	// ACTION_GATT_CONNECTED: connected to a GATT server.
	// ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
	// ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
	// ACTION_DATA_AVAILABLE: received data from the device. This can be a
	// result of read or notification operations.
	private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i(TAG, "Broadcast received");
			final String action = intent.getAction();

			if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
				mConnected = true;
				// Device connected
				Log.i(TAG, "Connected");
			} else if (BluetoothLeService.ACTION_GATT_DISCONNECTED
					.equals(action)) {
				// Device disconnected
				Log.i(TAG, "Disconnected");
			} else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED
					.equals(action)) {
				// Device returned services
				Log.i(TAG, "Services discovered");
				// Connect to the heart measurement characteristic
				BluetoothGattCharacteristic characteristic = mBluetoothLeService
						.getSupportedGattServices().get(2).getCharacteristics()
						.get(0);
				mBluetoothLeService.setCharacteristicNotification(
						characteristic, true);
			} else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
				// Get the new rate and log it
				String currentHeartRate = intent
						.getStringExtra(BluetoothLeService.EXTRA_DATA);
				Log.i(TAG, "Current heart rate: " + currentHeartRate + " bpm");

				// Broadcast the new value
				Intent i = new Intent("RATE_UPDATED");
				i.putExtra("rate", currentHeartRate);
				sendBroadcast(i);

				// Insert the value in the DB String
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
				sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
				String currentDateTime = sdf.format(new Date());
				int currentHeartRateInt = Integer.parseInt(currentHeartRate);
				db.addRecord(new Record(currentDateTime, currentHeartRateInt,
						mDeviceAddress));

			}
		}
	};

}