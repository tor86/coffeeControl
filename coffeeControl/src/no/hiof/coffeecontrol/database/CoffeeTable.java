package no.hiof.coffeecontrol.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class CoffeeTable {
	public static final String TABLE_COFFEE = "coffee";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_DATE = "date";
	public static final String COLUMN_AMOUNT = "amount";
	public static final String COLUMN_DESC = "description";
	
	// Database creation SQL
	private static final String DATABASE_CREATE_COFFEE = 
					  "create table " + TABLE_COFFEE + "(" + 
					  COLUMN_ID + " integer primary key autoincrement, " + 
					  COLUMN_DATE + " text not null, " +
					  COLUMN_AMOUNT + " integer, " +
					  COLUMN_DESC + " text" + ");";
	
	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE_COFFEE);
	}
	
	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		Log.v(CoffeeTable.class.getName(), "Upgrading database from " + oldVersion + " to " + newVersion + ". All data are lost.");
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_COFFEE);
		onCreate(database);
	}
}
