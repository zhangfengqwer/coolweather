package com.coolweather.app.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class HttpUtils {
	public static String sendHttpRequest(final String address){
//		new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
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
						String result = StreamUtils.readFromStream(in);
						System.out.println(result);
						return result;
						
					}
					
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}
//		}).start();
	}

