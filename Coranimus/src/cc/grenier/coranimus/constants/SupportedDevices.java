package cc.grenier.coranimus.constants;

public class SupportedDevices {

	// Device Name, Device Bluetooth MAC adress, servicePosition, characteristicPosition
	private static final String[][] LIST = {
		
	// Device #0 - MIO Link wristband (JF)
	{ "MIO Link", "F3:60:0B:B7:BB:18", "2", "0" } 
	};

	public static String getDeviceName(int deviceNumber) {
		return LIST[deviceNumber][0];
	}

	public static String getDeviceAdress(int deviceNumber) {
		return LIST[deviceNumber][1];
	}

	public static int getDeviceServicePosition(int deviceNumber) {
		return Integer.parseInt(LIST[deviceNumber][2]);
	}

	public static int getDeviceCharacteristicPosition(int deviceNumber) {
		return Integer.parseInt(LIST[deviceNumber][3]);
	}

}
