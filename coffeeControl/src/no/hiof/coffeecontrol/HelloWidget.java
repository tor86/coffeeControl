package no.hiof.coffeecontrol;

import java.text.SimpleDateFormat;
import java.util.Date;

import no.hiof.coffeecontrol.database.DataSource;
import no.hiof.coffeecontrol.database.SQLiteHelper;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;

/*
 * Shows current cups of coffee for same day
 * in a widget on home screen
 */

public class HelloWidget extends AppWidgetProvider {

	private DataSource datasource;
	//private static SQLiteHelper dbh;
	
	// UPDATE: Button code for updating Widget
	private static final String SYNC_CLICKED    = "automaticWidgetSyncButtonClick";
	
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
	        //remoteViews.setTextViewText( R.id.widget_textview, amount + " cups of coffee");
	        remoteViews.setTextViewText( R.id.widget_textview, amount + " cups");

	        // UPDATE: Button code for updating Widget
	        remoteViews.setOnClickPendingIntent(R.id.sync_button, getPendingSelfIntent(context, SYNC_CLICKED));
	        
	        // Updates the widget with a string. Android standard is 30 minutes
	        // to save battery. We can set custom update frequency by using timer
	        appWidgetManager.updateAppWidget( watchWidget, remoteViews );
	    }
	 
	 //UPDATE: Button code for updating Widget
	 @Override
	    public void onReceive(Context context, Intent intent) {
	        // TODO Auto-generated method stub
	        super.onReceive(context, intent);

	        if (SYNC_CLICKED.equals(intent.getAction())) {
	        	
	        	// Opens the database and gets amount from current day
		        datasource = new DataSource(context);
		        datasource.open();
		        datasource.updateRow(getDate(), 0);
		        int amount = datasource.sendToFront();
		        datasource.close();

	            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

	            RemoteViews remoteViews;
	            ComponentName watchWidget;

	            remoteViews = new RemoteViews(context.getPackageName(), R.layout.activity_widget);
	            watchWidget = new ComponentName(context, HelloWidget.class);

	            //remoteViews.setTextViewText( R.id.widget_textview, amount + " cups of coffee");
	            remoteViews.setTextViewText( R.id.widget_textview, amount + " cups");
	            
	            //remoteViews.setTextViewText(R.id.sync_button, "TESTING");

	            appWidgetManager.updateAppWidget(watchWidget, remoteViews);

	        }
	    }
	 
	 // UPDATE: Button code for updating Widget
	 protected PendingIntent getPendingSelfIntent(Context context, String action) {
	        Intent intent = new Intent(context, getClass());
	        intent.setAction(action);
	        return PendingIntent.getBroadcast(context, 0, intent, 0);
	    }
	 
}