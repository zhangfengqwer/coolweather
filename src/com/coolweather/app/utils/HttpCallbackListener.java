package com.coolweather.app.utils;

public interface HttpCallbackListener {
	void onFinish(String response);
	
	void onError(Exception e);
}
