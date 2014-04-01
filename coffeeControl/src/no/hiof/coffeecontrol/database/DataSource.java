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
	
	public CoffeeData createCoffee(String date, int amount, String desc) {
		ContentValues values = new ContentValues();
		values.put(CoffeeTable.COLUMN_DATE, date);
		values.put(CoffeeTable.COLUMN_AMOUNT, amount);
		values.put(CoffeeTable.COLUMN_DESC, desc);
		if(!exists(date)){
		long insertId = database.insert(CoffeeTable.TABLE_COFFEE, null, values);
		
		return getCoffee(insertId);
		}
		else return null;
	}
	
	public boolean exists(String date) {
		
		
		String sqlSearch = "SELECT * FROM coffee WHERE DATE = " + date;
		Cursor getRawData = database.rawQuery(sqlSearch, null);
		getRawData.moveToFirst();
		if(getRawData.getCount()>=1) {
			Log.d("Duplicate", "getRawData has value "+getRawData.getCount());
			return true;
		}
		else {
			Log.d("Cursor", "No data found");
			return false;
		}
			
	}
	
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
	
	public List<CoffeeData> getSelection() {
		List<CoffeeData> coffeedata = new ArrayList<CoffeeData>();
		
		//Cursor cursor = database.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
		Cursor cursor = database.query(CoffeeTable.TABLE_COFFEE, allCoffeeColumns, CoffeeTable.COLUMN_AMOUNT + " > " + 4, null, null, null, null);
		
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
	
	public void deleteTest(CoffeeData test) {
		long id = test.getId();
		
		//List<TestData> myData = getAllShows(test.getId()); 
		List<CoffeeData> myData = getAllCoffee(); 
		
//		for (Episode episode : episodes) {
//			deleteEpisode(episode);
//		}
		
		database.delete(CoffeeTable.TABLE_COFFEE, CoffeeTable.COLUMN_ID + " = " + id, null);
	}
	
	public void updateRow (String date,int amount) {
		ContentValues newValues = new ContentValues();
		String sqlSearch = "SELECT * FROM coffee WHERE DATE = " + date;
		Cursor getRawData = database.rawQuery(sqlSearch, null);
		getRawData.moveToFirst();
		if(getRawData.getCount()>=1) {
		Log.d("Cursor", "getRawData has value "+getRawData.getCount());
		int valueNow = getRawData.getInt(2);
		valueNow = valueNow+(amount);
		valueToFront=valueNow;
		sendToFront();
		//getRawData.
		Log.d("Value", Integer.toString(valueNow));
		//newValues.put("amount", "amount " + amount);
		newValues.put("amount", valueNow);
		String where = "date = " + date;
		database.update(CoffeeTable.TABLE_COFFEE, newValues, where, null);
		}
		else Log.d("Cursor", "No data found");
	}
	
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
