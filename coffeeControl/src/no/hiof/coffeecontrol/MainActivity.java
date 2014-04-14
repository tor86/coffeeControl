package no.hiof.coffeecontrol;

import java.text.SimpleDateFormat;
import java.util.Date;
//import java.util.List;

import no.hiof.coffeecontrol.database.DataSource;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.ListView;
//import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	// Connection to database data
	private DataSource datasource;
	
	// This is an old version, which cause app to crash on resume:
	// Intent startServiceIntent
	// Using the one below, it works, don't know why
	Intent startServiceIntent = new Intent("no.hiof.action.VIBRATE");
	
	boolean isRunning;
	public String dateToday;
	
	// This is for collecting accelerometer data
	// This is not used. We update database directly from service
	public static int amountFromService;
	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
//	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Makes a connection to data from database
		datasource = new DataSource(this);
		datasource.open();
		
		dateToday = getDate();
		
		// This updates the GUI with the current cup data
		// From todays date
		updateAmount(0);
		onUpdateCups();
		
//		ListView listView = getListView();
//		listView.setOnItemLongClickListener(new OnItemLongClickListener() {
//			public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
//				//deleteShowDialog(position);
//		        return true;
//            }
//		});
//		
//		List<TestData> testdata = datasource.getAllShows();
	}
	
	// Runs a task in background for adding database entries
	public class CreateCoffee extends AsyncTask<String, Void, CoffeeData> {

		@Override
		protected CoffeeData doInBackground(String... params) {
			// A demo entry
			// CoffeeData coffeedata = datasource.createCoffee("Date",1,"This is first SQL Data");
			
			CoffeeData coffeedata = datasource.createCoffee(params[0],0,"Cups of coffee");
			
			// This adds data for testing purposes
			if (params[0].equals("20140101")) {
				datasource.createCoffee("20140110", 4, "Cups of coffee");
				datasource.createCoffee("20140115", 2, "Cups of coffee");
				datasource.createCoffee("20140220", 6, "Cups of coffee");
				datasource.createCoffee("20140205", 1, "Cups of coffee");
				datasource.createCoffee("20140325", 8, "Cups of coffee");
			}
			
			return coffeedata;
			//return null;
		}

		@Override
		protected void onPostExecute(CoffeeData coffeedata) {
			//@SuppressWarnings("unchecked")
			// Here we can do something when async is finished
			// We currently do not use this for now
			// asyncDone=true;
		}
	}
	
	@Override
  	protected void onResume() {
		datasource.open();

// This is old. New code makes service update directly
		
// Updates cup data from service when activity is reopened
//		if (amountFromService>0) {
//			Log.d("OnResume", "In onResume, with update from service data");
//			updateAmount(amountFromService);
//			Log.d("updating", Integer.toString(amountFromService));
//			amountFromService=0;
//			onUpdateCups();
//		}
		
		// Gets amount each time activity is reopened
		updateAmount(0);
		onUpdateCups();
		
    	super.onResume();
	}

	@Override
	protected void onPause() {
		datasource.close();
		super.onPause();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void startMyService(View view) {
		// This is an old version, which cause app to crash on resume:
    	// startServiceIntent = new Intent("no.hiof.action.VIBRATE");

    	startService(startServiceIntent);
    }
    
    public void stopMyService(View view) {
    	stopService(startServiceIntent);
    }
    
    // Opens the list with the coffee dates and amount
    public void openHistory(View view) {
    	startActivity(new Intent(this,CoffeeList.class));
    }
    
    // Adds todays date if not already added
	public void itemAddOnClick(View view) {
	CreateCoffee createcoffee = new CreateCoffee();
	createcoffee.execute(dateToday);
	}
	
	// Deletes database after confirmation
	public void deleteLastEntry(View view) {
		
	deleteShowDialog();
		
	//datasource.deleteRow();	
	
	}
	
	public void addTestData(View view) {
		CreateCoffee testdata = new CreateCoffee();
		testdata.execute("20140101");
	}
	
	public String getDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd"); 
    	String date = sdf.format(new Date());
    	return date;
	}
	
	// Updates todays cups with a new amount
	public void updateAmount(int amount) {
		datasource.updateRow(dateToday, amount);
	}
	
	// Adds one cup. Creates new day if not already made
	public void addOne(View view) {
		CreateCoffee createcoffee = new CreateCoffee();
		createcoffee.execute(dateToday);
		
		if (amountFromService>0)updateAmount(amountFromService);
		else updateAmount(1);
		onUpdateCups();
	}
	
	// Minus one cup
	public void reduceOne(View view) {
		if(datasource.sendToFront()>0)updateAmount(-1);
		onUpdateCups();
	}
	
	// Updates UI, the number in main window
	public void onUpdateCups() {
		TextView tv1 = (TextView)findViewById(R.id.textView1);
		tv1.setText(Integer.toString(datasource.sendToFront()));
	}
	
	// Not used
	public void getFromService() {
		amountFromService+=1;
		Log.d("Counter", Integer.toString(amountFromService));
	}
	
	// Not used
	public void checkServiceData() {
		if(amountFromService>0) {
			updateAmount(amountFromService);
		}
		onUpdateCups();
	    amountFromService=0;
	}
	
//	public void updateAfterShake() {
//		updateAmount(0);
//		onUpdateCups();
//	}
	
	// Deletes database after confirmation message
	private void deleteShowDialog() {
		new AlertDialog.Builder(this)
	        .setTitle("Delete database?")
	        .setMessage("Are you sure you want to delete the database?")
	        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) { 
	            	datasource.deleteRow();	
	            }
	         })
	        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) { 
	                // Do nothing
	            }
	         })
	        .setIcon(android.R.drawable.ic_dialog_alert)
	         .show();
	}
	
}
