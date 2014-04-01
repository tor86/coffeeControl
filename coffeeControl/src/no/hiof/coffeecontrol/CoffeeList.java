package no.hiof.coffeecontrol;

import java.util.List;

import no.hiof.coffeecontrol.MainActivity.CreateCoffee;
import no.hiof.coffeecontrol.database.DataSource;
import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemLongClickListener;

public class CoffeeList extends ListActivity {
	private DataSource datasource;
	CoffeeAdapter adapter;
	List<CoffeeData> coffeedata;
	
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
		//////////////SHould this be used?/////////////////
		//List<CoffeeData> coffeedata = datasource.getAllCoffee();
		coffeedata = datasource.getAllCoffee();
		
		///// For making a selection /////
		//List<CoffeeData> coffeedata = datasource.getSelection();

	    //CoffeeAdapter adapter = new CoffeeAdapter(this, android.R.layout.simple_list_item_1);
		adapter = new CoffeeAdapter(this, android.R.layout.simple_list_item_1);
	    setListAdapter(adapter);
	    adapter.addAll(coffeedata);
	    
	    //We do it here:
//	    MainActivity myMain = new MainActivity();
//	    MainActivity.CreateCoffee createdata = myMain.new CreateCoffee();
//	    //MainActivity.CreateCoffee createdata = MainActivity.new CreateCoffee();
//	    createdata.execute();
	}

	
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
	
	public void updateGreater(View view) {
		//datasource.getSelection();
	}
	
	public void updateLesser(View view) {
		
	}
	
	public void updateBefore(View view) {
		
	}
	
	public void updateAfter(View view) {
		
	}
	
	public void reset(View view) {
		coffeedata = datasource.getSelection();
		adapter.clear();
		adapter.addAll(coffeedata);
	}

//	List<CoffeeData> coffeedata = datasource.getAllCoffee();
//	setListAdapter(adapter);
//    adapter.addAll(coffeedata);
	
}
