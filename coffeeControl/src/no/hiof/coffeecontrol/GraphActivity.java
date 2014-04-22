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
import android.view.Menu;
import android.widget.LinearLayout;

/*
 * This is a class making a bargraph from
 * a custom jar file library. It is tested
 * and is lacking some functions, but we decided to use it anyway.
 * This will show all days in database in a bargraph, one after another.
 */

public class GraphActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_graph);
		
		// Makes a list of data from database
		List<CoffeeData> coffeeList = new ArrayList();
		DataSource datasource = new DataSource(this);
		datasource.open();
		coffeeList = datasource.getAllCoffee();
		datasource.close();
		
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
		GraphViewData[] dataset = new GraphViewData[coffeeList.size()];
		
		String[] dates = new String[coffeeList.size()];
		
		// The code below makes new entries into the bargraph, based
		// on the arraylist
		
		for (int i=0;i<coffeeList.size();i++) {
			int cups = coffeeList.get(i).getAmount();
			String date = coffeeList.get(i).getDate();
			String mmdd = date.substring(4, 8);
			int dateint = Integer.parseInt(date);
			
			dates[i]=mmdd;
			
			dataset[i] = new GraphViewData(0,cups);
			
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
		
		// Setting som custom stuff for the graph, like color
		
		graphView.setBackgroundColor(Color.rgb(255, 255, 255));
		graphView.setManualYAxisBounds(10, 0);
		graphView.getGraphViewStyle().setNumVerticalLabels(11);
		
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