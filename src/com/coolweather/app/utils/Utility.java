
package com.coolweather.app.utils;

import android.text.TextUtils;
import android.util.Log;

import com.coolweather.app.bean.City;
import com.coolweather.app.bean.County;
import com.coolweather.app.bean.Province;
import com.coolweather.app.db.CoolWeatherDB;

public class Utility {
	
	/**
	 * 解析和处理服务器返回的省级数据
	 */
	public synchronized static boolean handleProvincesResponse(CoolWeatherDB coolWeatherDB, 
			String response){
		if(!TextUtils.isEmpty(response)){
			String[] allProvinces = response.split(",");
			
			if(allProvinces != null && allProvinces.length > 0){
				for (String s : allProvinces) {
					Log.i("TAG", s);
					String[] strings = s.split("\\|");
					Province p = new Province();
					p.setProvinceCode(strings[0]);
					p.setProvinceName(strings[1]);
					coolWeatherDB.saveProvince(p);
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 解析和处理服务器返回的市级数据
	 */
	public static boolean handlCitysResponse(CoolWeatherDB coolWeatherDB, 
			String response, int provinceId){
		if(!TextUtils.isEmpty(response)){
			String[] allCitys = response.split(",");
			if(allCitys != null && allCitys.length > 0){
				for (String s : allCitys) {
					String[] strings = s.split("\\|");
					City p = new City();
					p.setCityCode(strings[0]);
					p.setCityName(strings[1]);
					p.setProvince_id(provinceId);
					coolWeatherDB.saveCity(p);
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 解析和处理服务器返回的县级数据
	 */
	public static boolean handleCountysResponse(CoolWeatherDB coolWeatherDB, 
			String response, int cityId){
		if(!TextUtils.isEmpty(response)){
			String[] allCountys = response.split(",");
			if(allCountys != null && allCountys.length > 0){
				for (String s : allCountys) {
					String[] strings = s.split("\\|");
					County p = new County();
					p.setCountyCode(strings[0]);
					p.setCountyName(strings[1]);
					p.setCity_id(cityId);
					coolWeatherDB.saveCounty(p);
					return true;
				}
			}
		}
		return false;
	}
}
