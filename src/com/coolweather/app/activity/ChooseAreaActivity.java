package com.coolweather.app.activity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.coolweather.app.R;
import com.coolweather.app.bean.City;
import com.coolweather.app.bean.County;
import com.coolweather.app.bean.Province;
import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.utils.HttpCallbackListener;
import com.coolweather.app.utils.HttpUtils;
import com.coolweather.app.utils.StreamUtils;
import com.coolweather.app.utils.Utility;

public class ChooseAreaActivity extends Activity {

	// Handler handler = new Handler() {
	// public void handleMessage(Message msg) {
	//
	// String response = (String) msg.obj;
	// // System.out.println(response);
	// Utility.handleProvincesResponse(coolWeatherDB, response);
	//
	// queryProvince();
	// }
	// };

	private int LEVEL_PROVINCE = 0;
	private int LEVEL_CITY = 1;
	private int LEVEL_COUNTY = 2;

	private ListView lv;
	private TextView tv_title;
	private ArrayAdapter<String> adapter;
	private List<String> dataList = new ArrayList<String>();
	private CoolWeatherDB coolWeatherDB;
	private Province selectedProvince;
	private City selectedCity;
	private int currentLevel;

	/**
	 * 列表
	 */
	private List<Province> provinceList;
	private List<City> cityList;
	private List<County> countyList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		lv = (ListView) findViewById(R.id.lv);
		tv_title = (TextView) findViewById(R.id.tv_title);
		coolWeatherDB = CoolWeatherDB.getInstance(this);
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, dataList);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (currentLevel == LEVEL_PROVINCE) {
					selectedProvince = provinceList.get(position);
					queryCity();
				} else if (currentLevel == LEVEL_CITY) {
					selectedCity = cityList.get(position);
					queryCounty();
				}
			}

		});
		queryProvince();
	}

	/**
	 * 查询省信息，先从服务器
	 */
	private void queryProvince() {
		provinceList = coolWeatherDB.loadProvince();
		if (provinceList.size() > 0) {
			dataList.clear();
			for (Province province : provinceList) {
				dataList.add(province.getProvinceName());
			}
			adapter.notifyDataSetChanged();
			lv.setSelection(0);
			tv_title.setText("中国");
			currentLevel = LEVEL_PROVINCE;
		} else {
			queryFromServer(null, "province");
		}
	}
	
	/**
	 * 查询市的信息
	 */
	private void queryCity() {
		// TODO Auto-generated method stub
		cityList = coolWeatherDB.loadCity(selectedProvince.getId());
		if (cityList.size() > 0) {
			dataList.clear();
			for (City city : cityList) {
				dataList.add(city.getCityName());
			}
			adapter.notifyDataSetChanged();
			lv.setSelection(0);
			tv_title.setText(selectedProvince.getProvinceName());
			currentLevel = LEVEL_CITY;
		} else {
			queryFromServer(selectedProvince.getProvinceCode(), "city");
		}
	}
	
	/**
	 * 查询县的信息
	 */
	private void queryCounty() {
		countyList = coolWeatherDB.loadCounty(selectedCity.getId());
		if (countyList.size() > 0) {
			dataList.clear();
			for (County county : countyList) {
				dataList.add(county.getCountyName());
			}
			adapter.notifyDataSetChanged();
			lv.setSelection(0);
			tv_title.setText(selectedProvince.getProvinceName());
			currentLevel = LEVEL_COUNTY;
		} else {
			queryFromServer(selectedCity.getCityCode(), "county");
		}
	}

	// public void sendHttpRequest(final String address) {
	//
	// new Thread(new Runnable() {
	// @Override
	// public void run() {
	// HttpURLConnection conn = null;
	// try {
	// URL url = new URL(address);
	// conn = (HttpURLConnection) url.openConnection();
	// conn.setRequestMethod("GET");
	// conn.setConnectTimeout(8000);
	// conn.setReadTimeout(8000);
	// conn.connect();
	// String result = null;
	// if (conn.getResponseCode() == 200) {
	// InputStream in = conn.getInputStream();
	// result = StreamUtils.readFromStream2(in);
	// // System.out.println(result);
	// Message msg = handler.obtainMessage();
	// msg.obj = result;
	// handler.sendMessage(msg);
	// }
	//
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } finally {
	// if (conn != null) {
	// conn.disconnect();
	// }
	// }
	// }
	//
	// }).start();
	// }

	/**
	 * 通过传入的代号和类型从服务器上查询数据
	 */
	private void queryFromServer(final String code, final String type) {
		String address;
		if (TextUtils.isEmpty(code)) {
			address = "http://www.weather.com.cn/data/list3/city.xml";
		} else {
			address = "http://www.weather.com.cn/data/list3/city" + code
					+ ".xml";
		}
		HttpUtils.sendHttpRequest(address, new HttpCallbackListener() {

			@Override
			public void onFinish(final String response) {
				boolean flag = false;
				if ("province".equals(type)) {
					flag = Utility.handleProvincesResponse(coolWeatherDB,
							response);
				} else if ("city".equals(type)) {
					flag = Utility.handlCitysResponse(coolWeatherDB, response,
							selectedProvince.getId());
				} else if ("county".equals(type)) {
					flag = Utility.handleCountysResponse(coolWeatherDB, response,
							selectedCity.getId());
				}
				if(flag){
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if("province".equals(type)){
								queryProvince();
							} else if("city".equals(type)){
								queryCity();
							} else if("county".equals(type)){
								queryCounty();
							}
						}
					});
				}
			}
			
			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		if(currentLevel == LEVEL_COUNTY){
			queryCity();
		}else if(currentLevel == LEVEL_CITY){
			queryProvince();
		}else{
			finish();
		}
	}
}
