package no.hiof.coffeecontrol;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class service extends Service implements SensorEventListener {

	int counter = 0;
	SensorManager sensorMgr;
	Sensor mySensor;
	
	//For sensorChange
	float x,y,z = 0;
	float last_x,last_y,last_z = 0;
	long lastUpdate = 0;
	
	// For doVibrate
	long updat1=0;
	long updat2=0;
	
	public static final float SHAKE_TRESHOLD = 3000;
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{	
		sensorMgr = (SensorManager)getSystemService(SENSOR_SERVICE);
		mySensor = sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		
		//lastUpdate=System.currentTimeMillis();
		
		sensorMgr.registerListener(this,
				mySensor,
				SensorManager.SENSOR_DELAY_NORMAL);
		
		
		Log.i("MyService", "onStartCommand, vibrate");
		
		String action = intent.getAction();
		
		if (action.equals("no.hiof.action.VIBRATE")) {
			//doVibrate();
		} 
		else {
			Log.i("MyService", "Missing or unrecognized action");
		}
		
		counter++;
		
		//doVibrate();
		
		return START_NOT_STICKY;
	}
	
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent arg0) {
		// TODO Auto-generated method stub
		float values[] = arg0.values;
//		float x,y,z = 0;
//		float last_x,last_y,last_z = 0;
//		long lastUpdate;
		long time1,time2;
		
		if (arg0.sensor.equals(mySensor)) {
		//if (sensor == SensorManager.SENSOR_ACCELEROMETER) {
		//if (arg0.sensor.equals(Sensor.TYPE_ACCELEROMETER)){
		    long curTime = System.currentTimeMillis();
		    if (lastUpdate==0)lastUpdate=System.currentTimeMillis();
		    // only allow one update every 100ms.
		    if ((curTime - lastUpdate) > 100) {
		      long diffTime = (curTime - lastUpdate);
		      lastUpdate = curTime;

		      x = values[0];
		      y = values[1];
		      z = values[2];

		      //Using shake within time limit:
		      // When shake is detected, check current time. 
		      // Then check next shake detection. If difftime is
		      // smaller than given time, don't do anything.
		      
		      //time1 = System.currentTimeMillis();
		      Log.d("intent running", "");
		      //float speed = Math.abs(x+y+z-last_x-last_y-last_z) / diffTime * 10000;
		      
		      float deltax1 = Math.abs(x);
		      float deltax2 = Math.abs(last_x);
		      float deltax = x-last_x;
		      
		      float speed = Math.abs(deltax1+y+z-last_y-last_z-deltax2) / diffTime * 10000;
		      
		      ///////////////////////
		      //float speed = Math.abs(x+y+z-last_x-last_y-last_z) / diffTime * 10000;
		      //////////////////////
		      
		      //float speed = Math.abs(x+y-last_x-last_y) / diffTime * 10000;
		      
		      //Log.d("Values", "x: " + x + " and y: " + y + " and z: " + z);
		      Log.d("sensor", "shake detected w/ speed: " + speed);
		      if (speed > SHAKE_TRESHOLD) {
		    	//time2 = (System.currentTimeMillis()-curTime)*1000;
		    	//Log.d("Time2",(int)time2);
		    	
		    	//if ((time2-time1)>3) {
		        //Log.d("sensor", "shake detected w/ speed: " + speed);
		        Toast.makeText(this, "shake detected w/ speed: " + speed, Toast.LENGTH_SHORT).show();
		        doVibrate();
		    	//}
		      }
		      last_x = x;
		      last_y = y;
		      last_z = z;
		    }
		  }
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void doVibrate() {
		updat1=System.currentTimeMillis();
		if(updat2==0)updat2=System.currentTimeMillis();
		// Get instance of Vibrator from current Context
		if((Math.abs(updat2-updat1))>2000) {
		Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

		Log.d("doVibrate", "In doVibrate");
		
		// Vibrate for 400 milliseconds
		v.vibrate(400);
		}
		updat2=updat1;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		try {
		sensorMgr.unregisterListener(this);
		}
		catch (Exception e) {
		Log.d("Unregister", "Unregisterlistener Failed" + e);
		}
	}

}
