package cc.grenier.coranimus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import cc.grenier.coranimus.database.DatabaseHandler;
import cc.grenier.coranimus.database.Record;

public class ViewRecordsActivity extends Activity {
	private ListView mainListView ;  
	private ArrayAdapter<String> listAdapter ;  

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_records);

		// Reading all records
		DatabaseHandler db = new DatabaseHandler(this);
        Log.d("Reading: ", "Reading all records.."); 
        List<Record> records = db.getAllRecords();     
   
        ArrayList<String> recordList = new ArrayList<String>();  
         
        for (Record record : records) {
        	String assembledString = record.getWhenRateMeasured() + " " + record.getRateMeasured() + " BPM";
        	recordList.add(assembledString);
        }
        // Add to listView
        // Find the ListView resource.   
        mainListView = (ListView) findViewById( R.id.listViewRecords ); 
        
     // Create ArrayAdapter using the planet list.  
        listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, recordList);  
        
     // Set the ArrayAdapter as the ListView's adapter.  
        mainListView.setAdapter( listAdapter ); 
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_records, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


}
