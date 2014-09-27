package cc.grenier.coranimus.database;

public class Record {
	// private variables
	int _id;
	String _when_rate_measured;
	int _rate_measured;
	String _device_adress_used;

	// Empty constructor
	public Record() {

	}

	// constructor
	public Record(int id, String _when_rate_measured, int _rate_measured, String _device_adress_used) {
		this._id = id;
		this._when_rate_measured = _when_rate_measured;
		this._rate_measured = _rate_measured;
		this._device_adress_used = _device_adress_used;
	}

	// constructor
	public Record(String _when_rate_measured, int _rate_measured, String _device_adress_used) {
		this._when_rate_measured = _when_rate_measured;
		this._rate_measured = _rate_measured;
		this._device_adress_used = _device_adress_used;
	}

	// getting id
	public int getId() {
		return this._id;
	}

	// setting id
	public void setId(int id) {
		this._id = id;
	}

	// getting _when_rate_measured
	public String getWhenRateMeasured() {
		return this._when_rate_measured;
	}

	// setting _when_rate_measured
	public void setWhenRateMeasured(String whenRateMeasured) {
		this._when_rate_measured = whenRateMeasured;
	}
	
	// getting _rate_measured
	public int getRateMeasured() {
		return this._rate_measured;
	}

	// setting _rate_measured
	public void setRateMeasured(int rateMeasured) {
		this._rate_measured = rateMeasured;
	}
	
	// getting _device_adress_used
	public String getDeviceAdressUsed() {
		return this._device_adress_used;
	}

	// setting _device_adress_used
	public void setDeviceAdressUsed(String deviceAdressUsed) {
		this._device_adress_used = deviceAdressUsed;
	}
	
}
