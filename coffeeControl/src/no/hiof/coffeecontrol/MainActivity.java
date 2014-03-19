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
	//private int showNumber = 0;
	// This is an old version, which cause app to crash on resume:
	// Intent startServiceIntent
	Intent startServiceIntent = new Intent("no.hiof.action.VIBRATE");;
	boolean isRunning;
	public String dateToday;
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
		
		datasource = new DataSource(this);
		datasource.open();
		
		dateToday = getDate();
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
	
	public String getDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd"); 
    	String date = sdf.format(new Date());
    	return date;
	}
	
	public void updateAmount(int amount) {
		datasource.updateRow(dateToday, amount);
	}
	
	public void addOne(View view) {
		updateAmount(1);
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
}
