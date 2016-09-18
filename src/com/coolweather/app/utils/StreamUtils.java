package com.coolweather.app.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

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
}
