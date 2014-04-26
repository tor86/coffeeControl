package no.hiof.coffeecontrol;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import no.hiof.coffeecontrol.database.DataSource;
import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemLongClickListener;

public class CoffeeList extends ListActivity {
	private DataSource datasource;
	CoffeeAdapter adapter;
	List<CoffeeData> coffeedata;
	String filterDate;
	String filterAmount;
	
	//Tried to send List directly to graph, but could not make it work
	//Sending therefore an int which says which filtering, and date and amount
	// via the Intent.putExtra.... Then the graph makes a new query to datasource
	//List<String> coffeeToGraph;
	
	String amountString;
	
	// For checking which selected filter
	int selection = 1;
	
	// Sorts the data
	Comparator<CoffeeData> comparator = new Comparator<CoffeeData>() {
	    public int compare(CoffeeData c1, CoffeeData c2) {
	    	int num1=Integer.parseInt(c1.getDate())-Integer.parseInt(c2.getDate());
	    	int num2=Integer.parseInt(c2.getDate())-Integer.parseInt(c1.getDate());
	    	return num2;
	        //return c2.getDate() - c1.getDate(); // use your logic
	    }
	};

	
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_coffee_list);
		
		ListView listView = getListView();
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
				//deleteShowDialog(position);
		        return true;
            }
		});
		
		datasource = new DataSource(this);
		datasource.open();
		
		// Declares a new one, not used
		//List<CoffeeData> coffeedata = datasource.getAllCoffee();
		
		// Makes new list of coffee and dates
		coffeedata = datasource.getAllCoffee();
		selection=1;
		// Sorts the list
		Collections.sort(coffeedata, comparator);
		
		///// For making a selection /////
		//List<CoffeeData> coffeedata = datasource.getSelection();

	    //CoffeeAdapter adapter = new CoffeeAdapter(this, android.R.layout.simple_list_item_1);
		
		adapter = new CoffeeAdapter(this, android.R.layout.simple_list_item_1);
	    setListAdapter(adapter);
	    
	    // Code below adds coffeelist to the adapter
	    
	    /// This is for newer versions 3.0+
	    //adapter.addAll(coffeedata);
	    
	    // This is for compatibility with older versions.
	    adapter.setData(coffeedata);
	    
	}

	
	@SuppressWarnings("unchecked")
	public void getFromMain(CoffeeData coffeedata) {
		ArrayAdapter<CoffeeData> adapter = (ArrayAdapter<CoffeeData>) getListAdapter();
		
		adapter.add(coffeedata);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.coffee_list, menu);
		return true;
	}
	
	@Override
	public void onPause() {
		datasource.close();
		super.onPause();
	}
	
	@Override
	public void onResume() {
		datasource.open();
		
		// Makes new list of coffee and dates
				coffeedata = datasource.getAllCoffee();
				// Sorts the list
				Collections.sort(coffeedata, comparator);
		
		super.onResume();
	}
	
	@Override
	public void onDestroy() {
		datasource.close();
		super.onDestroy();
	}
	
	// Filtering options:
	
	// Gets all entries with cups greater than set number
	public void updateGreater(View view) {
		//datasource.getSelection();
		EditText et = (EditText)findViewById(R.id.editText1);
		
		amountString = et.getText().toString();
		
		try{
		coffeedata = datasource.getSelection(1, et.getText().toString());
		
		Collections.sort(coffeedata, comparator);
		
		selection=2;
		
		adapter.clear();
		
		/// This is for newer versions 3.0+
		//adapter.addAll(coffeedata);
		
		// This is for compatibility with older versions.
	    adapter.setData(coffeedata);
		}
		catch(Exception ex){
			Log.d("adapter", ex.toString());
		}
	}
	
	// Gets all entries with cups less than set number
	public void updateLesser(View view) {
		EditText et = (EditText)findViewById(R.id.editText1);
		
		amountString = et.getText().toString();
		
		try{
		coffeedata = datasource.getSelection(2, et.getText().toString());
		
		selection=3;
		
		Collections.sort(coffeedata, comparator);
		
		adapter.clear();
		
		/// This is for newer versions 3.0+
		//adapter.addAll(coffeedata);
		
		// This is for compatibility with older versions.
	    adapter.setData(coffeedata);
		}
		catch(Exception ex){
			Log.d("adapter", ex.toString());
		}
	}
	
	// Gets all data before set date
	public void updateBefore(View view) {
		
		dateBuilder();
		
		try{
			coffeedata = datasource.getSelection(4, filterDate);
			
			Collections.sort(coffeedata, comparator);
			
			selection=4;
			
			adapter.clear();
			
			/// This is for newer versions 3.0+
			//adapter.addAll(coffeedata);
			
			// This is for compatibility with older versions.
		    adapter.setData(coffeedata);
			}
			catch(Exception ex){
				Log.d("adapter", ex.toString());
			}
		
	}
	
	// Gets all data after set date
	public void updateAfter(View view) {
		dateBuilder();
		
		try{
			coffeedata = datasource.getSelection(3, filterDate);
			
			Collections.sort(coffeedata, comparator);
			
			selection=5;
			
			adapter.clear();
			
			/// This is for newer versions 3.0+
			//adapter.addAll(coffeedata);
			
			// This is for compatibility with older versions.
		    adapter.setData(coffeedata);
		    
			}
			catch(Exception ex){
				Log.d("adapter", ex.toString());
			}
	}
	
	// Resets selection and adds all items
	public void reset(View view) {
		coffeedata = datasource.getAllCoffee();
		
		Collections.sort(coffeedata, comparator);
		//coffeedata = datasource.getSelection(0,"null");
		
		selection=1;
		
		adapter.clear();
		
		/// This is for newer versions 3.0+
		//adapter.addAll(coffeedata);
		
		// This is for compatibility with older versions.
	    adapter.setData(coffeedata);
	}
	
	// Building a date string
	public void dateBuilder() {
		EditText et = (EditText)findViewById(R.id.editText2);
		EditText et2 = (EditText)findViewById(R.id.editText3);
		EditText et3 = (EditText)findViewById(R.id.editText4);
		String date1 = et.getText().toString();
		String date2 = et2.getText().toString();
		String date3 = et3.getText().toString();
		
		filterDate = date1+date2+date3;
	}
	
	public void showGraph(View view) {
		// Sends extra data to the graphactivity, for easier sharing
		Intent i = new Intent(this,GraphActivity.class);
		i.putExtra("selected", selection);
		i.putExtra("date", filterDate);
		i.putExtra("amountToGraph", amountString);
		startActivity(i);
		//startActivity(new Intent(this,GraphActivity.class));
	}
	
	
	
}
