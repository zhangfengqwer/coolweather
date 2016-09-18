package com.coolweather.app.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class HttpUtils {
	
	public static void sendHttpRequest(final String address, final HttpCallbackListener listener){
		new Thread(new Runnable() {
			
			private HttpURLConnection conn;

			@Override
			public void run() {
				URL url;
				try {
					url = new URL(address);
					conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(8000);
					conn.setReadTimeout(8000);
					conn.connect();
					String result = null;
					int responseCode = conn.getResponseCode();
					if(responseCode == 200){
						InputStream in = conn.getInputStream();
						result = StreamUtils.readFromStream(in);
					}
					if(listener != null){
						listener.onFinish(result);
					}
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally{
					if(conn != null){
						conn.disconnect();
					}
				}
			}
		}).start();
	}
}
