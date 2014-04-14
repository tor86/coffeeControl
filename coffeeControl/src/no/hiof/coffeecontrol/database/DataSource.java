package no.hiof.coffeecontrol.database;

import java.util.ArrayList;
import java.util.List;

import no.hiof.coffeecontrol.CoffeeData;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DataSource {
	private SQLiteDatabase database;
	private SQLiteHelper dbHelper;
	private int valueToFront;
	
	private String[] allCoffeeColumns = { CoffeeTable.COLUMN_ID,
			CoffeeTable.COLUMN_DATE,
			CoffeeTable.COLUMN_AMOUNT,
			CoffeeTable.COLUMN_DESC};
	
	public DataSource(Context context) {
		dbHelper = new SQLiteHelper(context);
	}
	
	// Makes a entry in the database
	public CoffeeData createCoffee(String date, int amount, String desc) {
		ContentValues values = new ContentValues();
		values.put(CoffeeTable.COLUMN_DATE, date);
		values.put(CoffeeTable.COLUMN_AMOUNT, amount);
		values.put(CoffeeTable.COLUMN_DESC, desc);
		// Checks if the date is already added, and adds entry if not
		if(!exists(date)){
		long insertId = database.insert(CoffeeTable.TABLE_COFFEE, null, values);
		
		return getCoffee(insertId);
		}
		else return null;
	}
	
	// Check if date exists
	public boolean exists(String date) {
		
		String sqlSearch = "SELECT * FROM coffee WHERE DATE = " + date;
		Cursor getRawData = database.rawQuery(sqlSearch, null);
		getRawData.moveToFirst();
		if(getRawData.getCount()>=1) {
			Log.d("Duplicate", "getRawData has value "+getRawData.getCount());
			getRawData.close();	
			return true;
		}
		else {
			Log.d("Cursor", "No data found");
			getRawData.close();	
			return false;
		}
		
	}
	
	// Not used
	public CoffeeData deleteCoffee(String date, int amount, String desc) {
//		ContentValues values = new ContentValues();
//		values.put(CoffeeTable.COLUMN_DATE, date);
//		values.put(CoffeeTable.COLUMN_AMOUNT, amount);
//		values.put(CoffeeTable.COLUMN_DESC, desc);
//
//		long insertId = database.insert(CoffeeTable.TABLE_COFFEE, null, values);
//		
//		return getCoffee(insertId);
		database.delete(CoffeeTable.TABLE_COFFEE, "_id" + "=" + 1,null);
		//long insertId = database.delete(CoffeeTable.TABLE_COFFEE, "_id" + "=" + 1,null);
		return null;
		//return getCoffee(insertId);
	}
	
	public CoffeeData getCoffee(long id) {
		Cursor cursor = database.query(CoffeeTable.TABLE_COFFEE,
										allCoffeeColumns, 
										CoffeeTable.COLUMN_ID + " = " + id, 
										null, null, null, null);
		cursor.moveToFirst();
		CoffeeData coffee = cursorToShow(cursor);
		cursor.close();
		
		return coffee;
	}
	
	// Gets the entire database
	public List<CoffeeData> getAllCoffee() {
		List<CoffeeData> coffeedata = new ArrayList<CoffeeData>();
		
		Cursor cursor = database.query(CoffeeTable.TABLE_COFFEE,
										allCoffeeColumns, 
										null, null, null, null, null);
	
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			CoffeeData coffee = cursorToShow(cursor);
			coffeedata.add(coffee);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		
		return coffeedata;
	}
	
	// Gets a selection based on which filter is used in listactivity
	public List<CoffeeData> getSelection(int filter,String criteria) {
		List<CoffeeData> coffeedata = new ArrayList<CoffeeData>();
		
		Cursor cursor;
		
		// 1: Greater than , 2: Less than , 3: After , 4: Before 
		
		switch(filter) {
		case(1):
			 cursor = database.query(CoffeeTable.TABLE_COFFEE, allCoffeeColumns, CoffeeTable.COLUMN_AMOUNT + " > " + Integer.parseInt(criteria), null, null, null, null);
		break;
		case(2):
			 cursor = database.query(CoffeeTable.TABLE_COFFEE, allCoffeeColumns, CoffeeTable.COLUMN_AMOUNT + " < " + Integer.parseInt(criteria), null, null, null, null);
		break;
		case(3):
			 cursor = database.query(CoffeeTable.TABLE_COFFEE, allCoffeeColumns, CoffeeTable.COLUMN_DATE + " > " + Integer.parseInt(criteria), null, null, null, null);
		break;
		case(4):
			 cursor = database.query(CoffeeTable.TABLE_COFFEE, allCoffeeColumns, CoffeeTable.COLUMN_DATE + " < " + Integer.parseInt(criteria), null, null, null, null);
		break;
		default:
			cursor = database.query(CoffeeTable.TABLE_COFFEE, allCoffeeColumns, CoffeeTable.COLUMN_AMOUNT + " > " + -1, null, null, null, null);
		}
		//Cursor cursor = database.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
		//Cursor cursor = database.query(CoffeeTable.TABLE_COFFEE, allCoffeeColumns, CoffeeTable.COLUMN_AMOUNT + " > " + 4, null, null, null, null);
		
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			CoffeeData coffee = cursorToShow(cursor);
			coffeedata.add(coffee);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		
		return coffeedata;
	}
	
	// Not used
	public void deleteTest(CoffeeData test) {
		long id = test.getId();
		
		//List<TestData> myData = getAllShows(test.getId()); 
		List<CoffeeData> myData = getAllCoffee(); 
		
//		for (Episode episode : episodes) {
//			deleteEpisode(episode);
//		}
		
		database.delete(CoffeeTable.TABLE_COFFEE, CoffeeTable.COLUMN_ID + " = " + id, null);
	}
	
	// Updates a row with new values
	public void updateRow (String date,int amount) {
		ContentValues newValues = new ContentValues();
		String sqlSearch = "SELECT * FROM coffee WHERE DATE = " + date;
		Cursor getRawData = database.rawQuery(sqlSearch, null);
		getRawData.moveToFirst();
		
		// If data exists
		if(getRawData.getCount()>=1) {
		Log.d("Cursor", "getRawData has value "+getRawData.getCount());
		// Get current value, and update with new value
		int valueNow = getRawData.getInt(2);
		valueNow = valueNow+(amount);
		// Send value to main activity
		valueToFront=valueNow;
		sendToFront();
		//getRawData.
		Log.d("Value", Integer.toString(valueNow));
		//newValues.put("amount", "amount " + amount);
		// Updates database
		newValues.put("amount", valueNow);
		String where = "date = " + date;
		database.update(CoffeeTable.TABLE_COFFEE, newValues, where, null);
		}
		
		else Log.d("Cursor", "No data found");
		
		// Need to close cursor
		getRawData.close();
	}
	
	// Deletes the whole database
	public void deleteRow() {
		CoffeeData test = new CoffeeData();
		long id = test.getId();
		database.delete(CoffeeTable.TABLE_COFFEE, null, null);
		//database.delete(CoffeeTable.TABLE_COFFEE, CoffeeTable.COLUMN_ID + " = " + id,null);
	}
	
	private CoffeeData cursorToShow(Cursor cursor) {
		CoffeeData coffee = new CoffeeData();
		coffee.setId(cursor.getInt(0));
		coffee.setDate(cursor.getString(1));
		coffee.setAmount(Integer.parseInt(cursor.getString(2)));
		coffee.setDesc(cursor.getString(3));
		
		return coffee;
	}
	
	// Sends value to main UI
	public int sendToFront() {
		if (valueToFront!=0)return valueToFront;
		else return 0;
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close() {
		dbHelper.close();
	}
}
