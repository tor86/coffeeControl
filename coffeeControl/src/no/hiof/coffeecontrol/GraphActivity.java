package no.hiof.coffeecontrol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import no.hiof.coffeecontrol.database.DataSource;

import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewDataInterface;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.widget.LinearLayout;

/*
 * This is a class making a bargraph from
 * a custom jar file library. It is tested
 * and is lacking some functions, but we decided to use it anyway.
 * This will show all days in database in a bargraph, one after another.
 */

public class GraphActivity extends Activity {

	public int yMax = 10;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_graph);
		
		// Makes a list of data from database
		List<CoffeeData> coffeeList = new ArrayList();
		DataSource datasource = new DataSource(this);
		datasource.open();
		
		//Gets data from listactivity
		String filterDate = getIntent().getExtras().getString("date");
		String amountString = getIntent().getExtras().getString("amountToGraph");
		
		// Makes a selection based on which filter was used in listactivity
		switch(getIntent().getExtras().getInt("selected")) {
		case 1:
			coffeeList = datasource.getAllCoffee();
			break;
		case 2:
			coffeeList = datasource.getSelection(1, amountString);
			Log.i("Case 2", "in case 2, with amount "+amountString);
			break;
		case 3:
			coffeeList = datasource.getSelection(2, amountString);
			Log.i("Case 3", "in case 3, with amount "+amountString);
			break;
		case 4:
			coffeeList = datasource.getSelection(4, filterDate);
			Log.i("Case 4", "in case 4, with date "+filterDate);
			break;
		case 5:
			coffeeList = datasource.getSelection(3, filterDate);
			Log.i("Case 5", "in case 5, with date "+filterDate);
			break;
		default:
			coffeeList = datasource.getAllCoffee();
			break;
		}
		
		Log.i("Selected",Integer.toString(getIntent().getExtras().getInt("selected")) );
		
		datasource.close();
		
		// Tried this, did not work directly
//		CoffeeList refList = new CoffeeList();
//		coffeeList = refList.coffeedata;
		
		//List<String> coffeeList=getIntent().getExtras().getStringArrayList("shareCoffee");
		
		
		
		// Sorts the data
		Comparator<CoffeeData> comparator = new Comparator<CoffeeData>() {
		    public int compare(CoffeeData c1, CoffeeData c2) {
		    	int num1=Integer.parseInt(c1.getDate())-Integer.parseInt(c2.getDate());
		    	return num1;
		        //return c2.getDate() - c1.getDate(); // use your logic
		    }
		};
		
		Collections.sort(coffeeList, comparator);
		
		// New set for bargraph
		
		// The bar x lenght has a fixed value. If there is few entries
		// , the entries will fill  a lot of size. Ex. one entry takes as much 
		// space as 10 entries, making it very "fat". We use empty values
		// to make it thinner.
		int arrayOriginal = coffeeList.size();
		int arrayNew = arrayOriginal;
		if (arrayOriginal<5)arrayNew=5;
		int diff = arrayNew-arrayOriginal;
		
		//GraphViewData[] dataset = new GraphViewData[coffeeList.size()];
		GraphViewData[] dataset = new GraphViewData[arrayNew];
		
		String[] dates = new String[coffeeList.size()];
		
		// The code below makes new entries into the bargraph, based
		// on the arraylist
		
		for (int i=0;i<arrayOriginal;i++) {
		//for (int i=0;i<coffeeList.size();i++) {
					
			int cups = coffeeList.get(i).getAmount();
			if (cups>yMax)cups=yMax;
			String date = coffeeList.get(i).getDate();
			String mmdd = date.substring(4, 8);
			int dateint = Integer.parseInt(date);
			
			dates[i]=mmdd;
			
			dataset[i] = new GraphViewData(0,cups);
			//int diff = arrayNew-arrayOriginal;
			//if (diff>0)dataset[i+diff+arrayOriginal] = new GraphViewData(0,0);
		}
		
		// Adding zero values
		for (int i=0;i<diff;i++) {
			dataset[arrayOriginal+i] = new GraphViewData(0,0);
		}
		
		GraphViewSeries coffeeSeries = new GraphViewSeries(dataset);
		
		// init example series data
//		GraphViewSeries exampleSeries = new GraphViewSeries(new GraphViewData[] {
//		    new GraphViewData(1, 2)
//		    , new GraphViewData(2, 3)
//		    , new GraphViewData(3, 2)
//		    , new GraphViewData(4, 1)
//		});
		 
		
		
		GraphView graphView = new BarGraphView(
		    this // context
		    , "CoffeeGraph" // heading
		);
		
		// Setting some custom stuff for the graph, like color
		
		graphView.setBackgroundColor(Color.rgb(255, 255, 255));
		graphView.setManualYAxisBounds(yMax, 0);
		graphView.getGraphViewStyle().setNumVerticalLabels(yMax+1);
		
		graphView.getGraphViewStyle().setVerticalLabelsColor(Color.rgb(0, 0, 0));
		graphView.getGraphViewStyle().setHorizontalLabelsColor(Color.rgb(0, 0, 0));
		
		graphView.getGraphViewStyle().setNumHorizontalLabels(1);
		
		//graphView.setHorizontalLabels(dates);
		
		// Adds the data to the bargraph and shows it in the 
		// layout
		
		graphView.addSeries(coffeeSeries);
		//graphView.addSeries(exampleSeries); // data
		 
		LinearLayout layout = (LinearLayout) findViewById(R.id.linearBar);
		layout.addView(graphView);
			
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.graph, menu);
		return true;
	}

	// Inner class for using the library
	public class GraphViewData implements GraphViewDataInterface {
	    private double x,y;

	    public GraphViewData(double x, double y) {
	        this.x = x;
	        this.y = y;
	    }

	    @Override
	    public double getX() {
	        return this.x;
	    }

	    @Override
	    public double getY() {
	        return this.y;
	    }
	}
	

}