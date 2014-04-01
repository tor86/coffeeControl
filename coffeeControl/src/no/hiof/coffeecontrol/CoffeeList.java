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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemLongClickListener;

public class CoffeeList extends ListActivity {
	private DataSource datasource;
	CoffeeAdapter adapter;
	List<CoffeeData> coffeedata;
	String filterDate;
	String filterAmount;
	
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
	
	@Override
	public void onResume() {
		datasource.open();
		super.onResume();
	}
	
	@Override
	public void onDestroy() {
		datasource.close();
		super.onDestroy();
	}
	
	public void updateGreater(View view) {
		//datasource.getSelection();
		EditText et = (EditText)findViewById(R.id.editText1);
		
		try{
		coffeedata = datasource.getSelection(1, et.getText().toString());
		
		adapter.clear();
		adapter.addAll(coffeedata);
		}
		catch(Exception ex){
			Log.d("adapter", ex.toString());
		}
	}
	
	public void updateLesser(View view) {
		EditText et = (EditText)findViewById(R.id.editText1);
		
		try{
		coffeedata = datasource.getSelection(2, et.getText().toString());
		
		adapter.clear();
		adapter.addAll(coffeedata);
		}
		catch(Exception ex){
			Log.d("adapter", ex.toString());
		}
	}
	
	public void updateBefore(View view) {
//		EditText et = (EditText)findViewById(R.id.editText2);
//		EditText et2 = (EditText)findViewById(R.id.editText3);
//		EditText et3 = (EditText)findViewById(R.id.editText4);
//		String date1 = et.getText().toString();
//		String date2 = et2.getText().toString();
//		String date3 = et3.getText().toString();
//		
//		filterDate = date1+date2+date3;
		
		dateBuilder();
		
		try{
			coffeedata = datasource.getSelection(4, filterDate);
			
			adapter.clear();
			adapter.addAll(coffeedata);
			}
			catch(Exception ex){
				Log.d("adapter", ex.toString());
			}
		
	}
	
	public void updateAfter(View view) {
		dateBuilder();
		
		try{
			coffeedata = datasource.getSelection(3, filterDate);
			
			adapter.clear();
			adapter.addAll(coffeedata);
			}
			catch(Exception ex){
				Log.d("adapter", ex.toString());
			}
	}
	
	public void reset(View view) {
		coffeedata = datasource.getAllCoffee();
		//coffeedata = datasource.getSelection(0,"null");
		adapter.clear();
		adapter.addAll(coffeedata);
	}
	
	public void dateBuilder() {
		EditText et = (EditText)findViewById(R.id.editText2);
		EditText et2 = (EditText)findViewById(R.id.editText3);
		EditText et3 = (EditText)findViewById(R.id.editText4);
		String date1 = et.getText().toString();
		String date2 = et2.getText().toString();
		String date3 = et3.getText().toString();
		
		filterDate = date1+date2+date3;
	}

//	List<CoffeeData> coffeedata = datasource.getAllCoffee();
//	setListAdapter(adapter);
//    adapter.addAll(coffeedata);
	
//	EditText editText = (EditText) findViewById(R.id.editText1);
//	String value = editText.getText().toString();
	
}
