package com.coolweather.app.db;

import java.util.ArrayList;
import java.util.List;

import com.coolweather.app.bean.City;
import com.coolweather.app.bean.County;
import com.coolweather.app.bean.Province;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CoolWeatherDB {
	
	/**
	 * 数据库名
	 */
	public static final String DB_NAME = "cool_weather";
	
	/**
	 * 数据库版本
	 */
	public static final int VERSION = 1;
	
	private static  CoolWeatherOpenHelper dbHelper;
	
	private SQLiteDatabase db;
	
	private static CoolWeatherDB coolWeatherDB;
	
	/**
	 * 讲构造方法私有化
	 */
	private  CoolWeatherDB(Context context) {
		dbHelper = new CoolWeatherOpenHelper(context, DB_NAME, null, VERSION);
		db = dbHelper.getWritableDatabase();
	}
	
	/**
	 * 获取CoolWeatherDB的实例
	 */
	public static CoolWeatherDB getInstance(Context context){
		if(coolWeatherDB == null){
			coolWeatherDB = new CoolWeatherDB(context);
		}
		return coolWeatherDB;
	}
	
	/**
	 * 将province实例存入数据库
	 */
	public void saveProvince(Province province){
		if(province != null){
			ContentValues values = new ContentValues();
			values.put("province_name", province.getProvinceName());
			values.put("province_code", province.getProvinceCode());
			db.insert("province", null, values);
		}
	}
	
	/**
	 * 从数据库中读取全国所有的省份信息
	 */
	public List<Province> loadProvince(){
		List<Province> list = new ArrayList<Province>();
		Cursor cursor = db.query("province", null, null, null, null, null, null, null);
		if(cursor.moveToNext()){
			Province province = new Province();
			province.setId(cursor.getInt(cursor.getColumnIndex("_id")));
			province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
			province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
			list.add(province);
		}
		return list;
	}
	
	/**
	 * 将city实例存入数据库
	 */
	public void saveCity(City city){
		if(city != null){
			ContentValues values = new ContentValues();
			values.put("city_name", city.getCityName());
			values.put("city_code", city.getCityCode());
			values.put("province_id", city.getProvince_id());
			db.insert("city", null, values);
		}
	}
	
	/**
	 * 从数据库中读取某省所有的城市信息
	 */
	public List<City> loadCity(int province_id){
		List<City> list = new ArrayList<City>();
		Cursor cursor = db.query("city", null, "province_id = ?", new String[]{province_id + ""},
				null, null, null, null);
		if(cursor.moveToNext()){
			City city = new City();
			city.setId(cursor.getInt(cursor.getColumnIndex("_id")));
			city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
			city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
			city.setProvince_id(cursor.getInt(cursor.getColumnIndex("province_id")));
			list.add(city);
		}
		return list;
	}
	
	/**
	 * 将county实例存入数据库
	 */
	public void saveCounty(County county){
		if(county != null){
			ContentValues values = new ContentValues();
			values.put("county_name", county.getCountyName());
			values.put("county_code", county.getCountyCode());
			values.put("province_id", county.getCity_id());
			db.insert("county", null, values);
		}
	}
	
	/**
	 * 从数据库中读取某市所有的县信息
	 */
	public List<County> loadCounty(int city_id){
		List<County> list = new ArrayList<County>();
		Cursor cursor = db.query("county", null, "city_id = ?", new String[]{city_id + ""},
				null, null, null, null);
		if(cursor.moveToNext()){
			County county = new County();
			county.setId(cursor.getInt(cursor.getColumnIndex("_id")));
			county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
			county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
			county.setCity_id(cursor.getInt(cursor.getColumnIndex("city_id")));
			list.add(county);
		}
		return list;
	}
}
