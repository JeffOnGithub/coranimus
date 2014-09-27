package cc.grenier.coranimus.constants;

import java.util.UUID;

import android.bluetooth.BluetoothGattCharacteristic;
import android.util.Log;

public class BleProfiles {

	private static final String TAG = "BleProfiles";

	public final static UUID UUID_HEART_RATE_MEASUREMENT =
            UUID.fromString(BleCharacteristics.HEART_RATE_MEASUREMENT);
    public final static UUID UUID_MANUFACTURER_NAME =
            UUID.fromString(BleCharacteristics.MANUFACTURER_NAME_STRING);

	public static String processCharacteristic(
			BluetoothGattCharacteristic characteristic) {
		String result = "";
        if (UUID_HEART_RATE_MEASUREMENT.equals(characteristic.getUuid())) {
            int flag = characteristic.getProperties();
            int format = -1;
            if ((flag & 0x01) != 0) {
                format = BluetoothGattCharacteristic.FORMAT_UINT16;
                Log.d(TAG, "Heart rate format UINT16.");
            } else {
                format = BluetoothGattCharacteristic.FORMAT_UINT8;
                Log.d(TAG, "Heart rate format UINT8.");
            }
            final int heartRate = characteristic.getIntValue(format, 1);
            result = String.valueOf(heartRate);

        } else if(UUID_MANUFACTURER_NAME.equals(characteristic.getUuid() )){
        	new String(characteristic.getValue());
        } else {
            // For all other profiles, writes the data formatted in HEX.
            final byte[] data = characteristic.getValue();
            if (data != null && data.length > 0) {
                final StringBuilder stringBuilder = new StringBuilder(data.length);
                for(byte byteChar : data)
                    stringBuilder.append(String.format("%02X ", byteChar));
                result = new String(data) + "\n" + stringBuilder.toString();
            }
        }
		return result;
	}

}
