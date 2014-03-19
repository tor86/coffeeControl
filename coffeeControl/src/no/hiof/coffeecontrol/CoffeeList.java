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
		List<CoffeeData> coffeedata = datasource.getAllCoffee();

	    CoffeeAdapter adapter = new CoffeeAdapter(this, android.R.layout.simple_list_item_1);
	    setListAdapter(adapter);
	    adapter.addAll(coffeedata);
	    Log.d("Break","Stops here");
	    
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

}
