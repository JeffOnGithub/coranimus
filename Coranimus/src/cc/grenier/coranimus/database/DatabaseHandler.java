package cc.grenier.coranimus.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "RecordsManager";

	// Records table name
	private static final String TABLE_RECORDS = "records";

	// Records Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_WHEN_RATE_MEAS = "when_rate_measured";
	private static final String KEY_RATE_MEAS = "rate_measured";
	private static final String KEY_DEV_ADR_USED = "device_adress_used";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_RECORDS_TABLE = "CREATE TABLE " + TABLE_RECORDS + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_WHEN_RATE_MEAS
				+ " TEXT," + KEY_RATE_MEAS + " INTEGER," + KEY_DEV_ADR_USED
				+ " TEXT" + ")";
		db.execSQL(CREATE_RECORDS_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECORDS);

		// Create tables again
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new record
	public void addRecord(Record contact) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_WHEN_RATE_MEAS, contact.getWhenRateMeasured());
		values.put(KEY_RATE_MEAS, contact.getRateMeasured());
		values.put(KEY_DEV_ADR_USED, contact.getDeviceAdressUsed());

		// Inserting Row
		db.insert(TABLE_RECORDS, null, values);
		db.close(); // Closing database connection
	}

	// Getting single record
	public Record getRecord(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_RECORDS, new String[] { KEY_ID,
				KEY_WHEN_RATE_MEAS, KEY_RATE_MEAS, KEY_DEV_ADR_USED }, KEY_ID
				+ "=?", new String[] { String.valueOf(id) }, null, null, null,
				null);
		if (cursor != null)
			cursor.moveToFirst();

		Record record = new Record(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1), Integer.parseInt(cursor.getString(2)),
				cursor.getString(2));
		// return record
		return record;
	}

	// Getting All records
	public List<Record> getAllRecords() {
		List<Record> recordList = new ArrayList<Record>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_RECORDS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Record record = new Record();
				record.setId(Integer.parseInt(cursor.getString(0)));
				record.setWhenRateMeasured(cursor.getString(1));
				record.setRateMeasured(Integer.parseInt(cursor.getString(2)));
				record.setDeviceAdressUsed(cursor.getString(3));
				// Adding record to list
				recordList.add(record);
			} while (cursor.moveToNext());
		}

		// return records list
		return recordList;
	}

	// Updating single record
	public int updateRecord(Record record) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_WHEN_RATE_MEAS, record.getWhenRateMeasured());
		values.put(KEY_RATE_MEAS, record.getRateMeasured());
		values.put(KEY_DEV_ADR_USED, record.getDeviceAdressUsed());

		// updating row
		return db.update(TABLE_RECORDS, values, KEY_ID + " = ?",
				new String[] { String.valueOf(record.getId()) });
	}

	// Deleting single record
	public void deleteRecord(Record contact) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_RECORDS, KEY_ID + " = ?",
				new String[] { String.valueOf(contact.getId()) });
		db.close();
	}

	// Getting records Count
	public int getRecordsCount() {
		String countQuery = "SELECT  * FROM " + TABLE_RECORDS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		// return count
		return cursor.getCount();
	}

}