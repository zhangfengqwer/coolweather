package com.coolweather.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.coolweather.app.R;
import com.coolweather.app.utils.HttpCallbackListener;
import com.coolweather.app.utils.HttpUtils;
import com.coolweather.app.utils.Utility;

public class WeatherActivity extends Activity {
	private TextView tvTitle;
	private TextView tvPTime;
	private TextView tvTime;
	private TextView tvContent;
	private TextView tvTemp1;
	private TextView tvTemp2;
	private Button switchCity;
	private Button update;
	private String countyCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		tvPTime = (TextView) findViewById(R.id.textView1);
		tvTime = (TextView) findViewById(R.id.tv_time);
		tvContent = (TextView) findViewById(R.id.tv_content);
		tvTemp1 = (TextView) findViewById(R.id.tv_temp1);
		tvTemp2 = (TextView) findViewById(R.id.tv_temp2);
		switchCity = (Button) findViewById(R.id.switch_city);
		update = (Button) findViewById(R.id.update);
		
		countyCode = getIntent().getStringExtra("county_code");
//		Log.i("1", countyCode);
		if(!TextUtils.isEmpty(countyCode)){
			queryWeatherCode(countyCode);
		}else{
			showWeather();
		}
	}
	
	public void update(View v){
		System.out.println("开始更新");
		queryWeatherCode(countyCode);
		System.out.println("结束更新");
	}
	
	public void switchCity(View v){
		Intent intent = new Intent(this, ChooseAreaActivity.class);
		intent.putExtra("from_weather", true);
		startActivity(intent);
		finish();
	}
	
	private void queryWeatherCode(String countyCode) {
		String address = "http://www.weather.com.cn/data/list3/city" + countyCode +".xml";
		queryFromServer(address, "county");
	}
	
	protected void queryWeather(String weatherCode) {
		String address = "http://www.weather.com.cn/data/cityinfo/"+ weatherCode +".html";
		queryFromServer(address, "weather");
	}

	/**
	 * 解析从服务器传来的数据，并储存
	 * @param address
	 */
	private void queryFromServer(final String address, final String type) {
		HttpUtils.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(final String response) {
				if("county".equals(type)){
					String[] arr = response.split("\\|");
					if(arr != null && arr.length == 2){
						String weatherCode = arr[1];
//						Log.i("1", weatherCode);
						queryWeather(weatherCode);
					}
				}else if("weather".equals(type)){
					Log.i("1", response);
					boolean flag = Utility.handleWeatherResponse(WeatherActivity.this, response);
//					if(flag){
//						Log.i("1", "处理好了");
//						
//					}
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							showWeather();
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
	
	

	private void showWeather(){
		SharedPreferences sf = PreferenceManager.getDefaultSharedPreferences(this);
		tvTitle.setText(sf.getString("city_name", ""));
		tvPTime.setText("今天" + sf.getString("pTime", "") + "发布");
		tvTime.setText(sf.getString("current_date", ""));
		tvContent.setText(sf.getString("weather", ""));
		tvTemp1.setText(sf.getString("temp1", ""));
		tvTemp2.setText(sf.getString("temp2", ""));
	} 
}
