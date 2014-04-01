package no.hiof.coffeecontrol;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import no.hiof.coffeecontrol.database.DataSource;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.TextView;

public class MainActivity extends Activity {
	private DataSource datasource;
	
	// This is an old version, which cause app to crash on resume:
	// Intent startServiceIntent
	// Using the one below, it works, don't know why
	Intent startServiceIntent = new Intent("no.hiof.action.VIBRATE");
	
	boolean isRunning;
	public String dateToday;
	
	// This is for collecting accelerometer data
	public static int amountFromService;
	
	//public int cupsToday=0;
	
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
//
//	    TestAdapter adapter = new TestAdapter(this, android.R.layout.simple_list_item_1);
//	    setListAdapter(adapter);
//	    adapter.addAll(testdata);
//	    Log.d("Break","Stops here");
//	    
//	    //We do it here:
//	    CreateShow createdata = new CreateShow();
//	    createdata.execute();
	}
	
	
	public class CreateCoffee extends AsyncTask<String, Void, CoffeeData> {

		@Override
		protected CoffeeData doInBackground(String... params) {
			//String[] testdata = getResources().getStringArray(R.array.testdata);
			//if (params[0].equals("add"))CoffeeData coffeedata = datasource.createCoffee("date", 1, "Some Data");
			
			//////////////////We are using this ////////////////////
			//CoffeeData coffeedata = datasource.createCoffee("Date",1,"This is first SQL Data");
			
			CoffeeData coffeedata = datasource.createCoffee(params[0],0,"Cups of coffee");
			
			//if (params[0].equals("add"))datasource.createCoffee("Date",1,"This is first SQL Data");
				//CoffeeData coffeeData = datasource.createCoffee("Date",1,"This is first SQL Data");
//			datasource.createEpisode(show.getId(), "Pilot", 1, 1, "Brilliant physicist roommates Leonard and Sheldon meet their new neighbor Penny, who begins showing them that as much as they know about science, they know little about actual living.");
//			datasource.createEpisode(show.getId(), "The Big Bran Hypothesis", 2, 1, "Brilliant physicist roommates Leonard and Sheldon meet their new neighbor Penny, who begins showing them that as much as they know about science, they know little about actual living.");
//			
			//if (params[0].equals("delete"))datasource.deleteCoffee(null, 0, null);
			
			return coffeedata;
			//return null;
		}

		@Override
		protected void onPostExecute(CoffeeData coffeedata) {
			//@SuppressWarnings("unchecked")
			
//			CoffeeList myList = new CoffeeList();
//			myList.getFromMain(coffeedata);
			
//			ArrayAdapter<CoffeeData> adapter = (ArrayAdapter<CoffeeData>) getListAdapter();
//			
//			adapter.add(testdata);
		}
	}
	
	@Override
  	protected void onResume() {
		datasource.open();
		//checkServiceData();
//		if(amountFromService>0) {
//		updateAmount(amountFromService);
//		}
//		onUpdateCups();
//    	amountFromService=0;
		if (amountFromService>0) {
			Log.d("OnResume", "In onResume, with update from service data");
			updateAmount(amountFromService);
			Log.d("updating", Integer.toString(amountFromService));
			amountFromService=0;
			onUpdateCups();
		}
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
    
    public void openHistory(View view) {
    	startActivity(new Intent(this,CoffeeList.class));
    }
    
//	public void itemAddOnClick(MenuItem item) {
//	CreateShow createShow = new CreateShow();
//	createShow.execute();
//}
    
	public void itemAddOnClick(View view) {
	CreateCoffee createcoffee = new CreateCoffee();
	createcoffee.execute(dateToday);
	}
	
	public void deleteLastEntry(View view) {
//	CreateCoffee createcoffee = new CreateCoffee();
//	createcoffee.execute("delete");	
		
	datasource.deleteRow();	
	
	//datasource.deleteCoffee(null, 0, null);
		
//	@SuppressWarnings("unchecked")
//	ArrayAdapter<Coffee> adapter = (ArrayAdapter<Coffee>) getListAdapter();
//		
//	while (adapter.getCount() > 0) {
//		Show show = adapter.getItem(0);
//		datasource.deleteShow(show);
//		adapter.remove(show);
//	}
	
	}
	
	public void addTestData(View view) {
		CreateCoffee createcoffee = new CreateCoffee();
		try {
		createcoffee.execute("20140215");
		createcoffee.execute("20140217");
		createcoffee.execute("20140219");
		createcoffee.execute("20140221");
		}
		catch(Exception ex) {
			
		}
		CreateCoffee createcoffee2 = new CreateCoffee();
		try {
		createcoffee2.execute("20140217");
		}
		catch(Exception ex) {
			
		}
		CreateCoffee createcoffee3 = new CreateCoffee();
		try {
		createcoffee3.execute("20140219");
		}
		catch(Exception ex) {
			
		}
	}
	
	public String getDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd"); 
    	String date = sdf.format(new Date());
    	return date;
	}
	
	public void updateAmount(int amount) {
		datasource.updateRow(dateToday, amount);
	}
	
	public void addOne(View view) {
		if (amountFromService>0)updateAmount(amountFromService);
		else updateAmount(1);
		onUpdateCups();
	}
	
	public void reduceOne(View view) {
		updateAmount(-1);
		onUpdateCups();
	}
	
	public void onUpdateCups() {
		TextView tv1 = (TextView)findViewById(R.id.textView1);
		tv1.setText(Integer.toString(datasource.sendToFront()));
	}
	
	public void getFromService() {
		
//		datasource.open();
//		updateAmount(1);
//		//onUpdateCups();
//		datasource.close();
		amountFromService+=1;
		Log.d("Counter", Integer.toString(amountFromService));
	}
	
	public void checkServiceData() {
		if(amountFromService>0) {
			updateAmount(amountFromService);
		}
		onUpdateCups();
	    amountFromService=0;
	}
	
//	public void startWidget(View view) {
//		//startActivity(new Intent(this,WidgetActivity.class));
//		Intent widget = new Intent("android.appwidget.action.APPWIDGET_UPDATE");
//		startActivity(widget);
//	}
}
