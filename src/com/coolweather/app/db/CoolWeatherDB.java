package com.coolweather.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class CoolWeatherDB {
	private CoolWeatherOpenHelper dbHelper;
	private SQLiteDatabase db;
	
	public CoolWeatherDB(Context context) {
		dbHelper = new CoolWeatherOpenHelper(context, "coolweather", null, 1);
		db = dbHelper.getWritableDatabase();
	}
}
