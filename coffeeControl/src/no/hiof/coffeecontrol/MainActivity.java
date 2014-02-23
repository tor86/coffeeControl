package no.hiof.coffeecontrol;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

	// This is an old version, which cause app to crash on resume:
	// Intent startServiceIntent
	Intent startServiceIntent = new Intent("no.hiof.action.VIBRATE");;
	boolean isRunning;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void startMyService(View view) {
		// This is an old version, which cause app to crash on resume:
    	// startServiceIntent = new Intent("no.hiof.action.VIBRATE");

    	startService(startServiceIntent);
    }
    
    public void stopMyService(View view) {
    	stopService(startServiceIntent);
    }
	
}
