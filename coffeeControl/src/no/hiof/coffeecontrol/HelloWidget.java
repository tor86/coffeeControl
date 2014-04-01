package no.hiof.coffeecontrol;

import java.text.SimpleDateFormat;
import java.util.Date;

import no.hiof.coffeecontrol.database.DataSource;
import no.hiof.coffeecontrol.database.SQLiteHelper;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;

public class HelloWidget extends AppWidgetProvider {

	private DataSource datasource;
	private static SQLiteHelper dbh;
	
	public String getDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd"); 
    	String date = sdf.format(new Date());
    	return date;
	}
	
	 @Override
	    public void onUpdate( Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds )
	    {
	        RemoteViews remoteViews;
	        ComponentName watchWidget;
//	        DateFormat format = SimpleDateFormat.getTimeInstance( SimpleDateFormat.MEDIUM, Locale.getDefault() );
//
//	        remoteViews = new RemoteViews( context.getPackageName(), R.layout.main );
//	        watchWidget = new ComponentName( context, WatchWidget.class );
//	        remoteViews.setTextViewText( R.id.widget_textview, "Time = " + format.format( new Date()));
//	        appWidgetManager.updateAppWidget( watchWidget, remoteViews );
//	        try {
	        datasource = new DataSource(context);
	        datasource.open();
	        datasource.updateRow(getDate(), 0);
	        int amount = datasource.sendToFront();
	        datasource.close();
	        
	        remoteViews = new RemoteViews( context.getPackageName(), R.layout.activity_widget );
	        watchWidget = new ComponentName( context, HelloWidget.class );
	        remoteViews.setTextViewText( R.id.widget_textview, amount + " cups of coffee");
	        //remoteViews.setTextViewText( R.id.widget_textview, "Time = " + format.format( new Date()));
//	        }
//	        catch(Exception ex) {
//	        	Log.d("Widget", ex.toString());
//	        }
	        appWidgetManager.updateAppWidget( watchWidget, remoteViews );
	    }
	
	 
//	 
//	 private static SQLiteHelper getDatabaseHelper(){
//		  if(dbh==null){
//		     dbh = new SQLiteHelper(Context context);
//		     dbh.openDatabase();
//		  }
//		  return dbh;
//		}
	 
	 
}


////public void onUpdateCups() {
////	TextView tv1 = (TextView)findViewById(R.id.textView1);
////	tv1.setText(Integer.toString(datasource.sendToFront()));
////}
//updateamount()
//datasource.updateRow(dateToday, amount);
//
//dateToday = getDate();
//
//// This updates the GUI with the current cup data
//// From todays date
//updateAmount(0);
//onUpdateCups();


