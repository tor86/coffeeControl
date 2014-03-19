package no.hiof.coffeecontrol.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "coffee.db";
	private static final int DATABASE_VERSION = 1;
	
	public SQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		CoffeeTable.onCreate(database);
		//EpisodeTable.onCreate(database);
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		CoffeeTable.onUpgrade(database, oldVersion, newVersion);
		//EpisodeTable.onUpgrade(database, oldVersion, newVersion);
	}

}
