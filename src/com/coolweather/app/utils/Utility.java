package com.coolweather.app.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.coolweather.app.bean.City;
import com.coolweather.app.bean.County;
import com.coolweather.app.bean.Province;
import com.coolweather.app.db.CoolWeatherDB;

public class Utility {

	/**
	 * 解析JSON数据，并存储到本地
	 */

	public static boolean handleWeatherResponse(Context context, String response) {
		boolean flag = false;
		try {
			JSONObject jsonObject = new JSONObject(response);
			JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
			String cityName = weatherInfo.getString("city");
			String cityId = weatherInfo.getString("cityid");
			String temp1 = weatherInfo.getString("temp1");
			String temp2 = weatherInfo.getString("temp2");
			String weather = weatherInfo.getString("weather");
			String pTime = weatherInfo.getString("ptime");
			saveWeatherInfo(context, cityName, cityId, temp1, temp2, weather,
					pTime);
			flag = true;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;
	}

	private static void saveWeatherInfo(Context context, String cityName,
			String cityId, String temp1, String temp2, String weather,String pTime) {
		SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
		edit.putBoolean("city_selected", true);
		edit.putString("city_name", cityName);
		edit.putString("cit_id", cityId);
		edit.putString("temp1", temp1);
		edit.putString("temp2", temp2);
		edit.putString("weather", weather);
		edit.putString("pTime", pTime);
		edit.putString("current_date", sdf.format(new Date()));
		edit.commit();
	}

	/**
	 * 解析和处理服务器返回的省级数据
	 */
	public static boolean handleProvincesResponse(CoolWeatherDB coolWeatherDB,
			String response) {
		if (!TextUtils.isEmpty(response)) {
			String[] allProvinces = response.split(",");

			if (allProvinces != null && allProvinces.length > 0) {
				for (String s : allProvinces) {
					String[] strings = s.split("\\|");
					Province p = new Province();
					p.setProvinceCode(strings[0]);
					p.setProvinceName(strings[1]);
					coolWeatherDB.saveProvince(p);
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * 解析和处理服务器返回的市级数据
	 */
	public static boolean handlCitysResponse(CoolWeatherDB coolWeatherDB,
			String response, int provinceId) {
		if (!TextUtils.isEmpty(response)) {
			String[] allCitys = response.split(",");
			if (allCitys != null && allCitys.length > 0) {
				for (String s : allCitys) {
					String[] strings = s.split("\\|");
					City p = new City();
					p.setCityCode(strings[0]);
					p.setCityName(strings[1]);
					p.setProvince_id(provinceId);
					coolWeatherDB.saveCity(p);
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * 解析和处理服务器返回的县级数据
	 */
	public static boolean handleCountysResponse(CoolWeatherDB coolWeatherDB,
			String response, int cityId) {
		if (!TextUtils.isEmpty(response)) {
			String[] allCountys = response.split(",");
			if (allCountys != null && allCountys.length > 0) {
				for (String s : allCountys) {
					String[] strings = s.split("\\|");
					County p = new County();
					p.setCountyCode(strings[0]);
					p.setCountyName(strings[1]);
					p.setCity_id(cityId);
					coolWeatherDB.saveCounty(p);
				}
				return true;
			}
		}
		return false;
	}
}
