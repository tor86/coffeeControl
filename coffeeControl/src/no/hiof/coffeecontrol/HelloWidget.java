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

/*
 * Shows current cups of coffee for same day
 * in a widget on home screen
 */

public class HelloWidget extends AppWidgetProvider {

	private DataSource datasource;
	//private static SQLiteHelper dbh;
	
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
	        
	        // Opens the database and gets amount from current day
	        datasource = new DataSource(context);
	        datasource.open();
	        datasource.updateRow(getDate(), 0);
	        int amount = datasource.sendToFront();
	        datasource.close();
	        
	        remoteViews = new RemoteViews( context.getPackageName(), R.layout.activity_widget );
	        watchWidget = new ComponentName( context, HelloWidget.class );
	        remoteViews.setTextViewText( R.id.widget_textview, amount + " cups of coffee");

	        // Updates the widget with a string. Android standard is 30 minutes
	        // to save battery. We can set custom update frequency by using timer
	        appWidgetManager.updateAppWidget( watchWidget, remoteViews );
	    }
	 
}