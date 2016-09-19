package com.coolweather.app.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StreamUtils {
	public static String readFromStream(InputStream in) throws IOException{
		ByteArrayOutputStream out = new ByteArrayOutputStream(); 
		int len = 0;
		byte[] b = new byte[1024];
		while((len = in.read(b)) != -1){
			out.write(b, 0, len);
		}
		String result = out.toString();
		return result;
	}
	
	public static String readFromStream2(InputStream in) throws IOException{
		BufferedReader buffer = new BufferedReader(new InputStreamReader(in));
		StringBuffer result = new StringBuffer();
		String s;
		while((s = buffer.readLine()) != null){
			result.append(s);
		}
		return result.toString();
	}
}
