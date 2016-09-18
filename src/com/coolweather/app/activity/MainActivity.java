package com.coolweather.app.activity;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.coolweather.app.R;
import com.coolweather.app.utils.StreamUtils;

public class MainActivity extends Activity {
	final String address = "http://www.weather.com.cn/data/cityinfo/101190401.html";
	private TextView tv;
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			String result = (String) msg.obj;
			tv.setText(result);
		}
		
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		tv = (TextView) findViewById(R.id.tv);
		
		Thread t = new Thread(){
			@Override
			public void run() {
				URL url;
				try {
					url = new URL(address);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(8000);
					conn.setReadTimeout(8000);
					conn.connect();
					int responseCode = conn.getResponseCode();
					if(responseCode == 200){
						InputStream in = conn.getInputStream();
						System.out.println(in);
						String result = StreamUtils.readFromStream(in);
						System.out.println(result);
						Message msg = handler.obtainMessage();
						msg.obj = result;
						handler.sendMessage(msg);
					}
					
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}; 
		t.start();
		
	}
}
