package com.coolweather.app.activity;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.coolweather.app.R;
import com.coolweather.app.bean.Province;
import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.utils.HttpCallbackListener;
import com.coolweather.app.utils.HttpUtils;
import com.coolweather.app.utils.Utility;

public class ChooseAreaActivity extends Activity {
	
	private ListView lv;
	private TextView tv_title;
	private ArrayAdapter<String> adapter;
	private List<String> dataList = new ArrayList<String>();
	private CoolWeatherDB coolWeatherDB;
	private Province selectedProvince;
	
	/**
	 * 省列表
	 */
	private List<Province> provinceList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_area);
		lv = (ListView) findViewById(R.id.lv);
		tv_title = (TextView) findViewById(R.id.tv_title);
		coolWeatherDB = CoolWeatherDB.getInstance(this);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataList);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
			}
			
		});
		queryProvince();
	}
	/**
	 * 查询省信息，先从服务器
	 */
	private void queryProvince() {
		provinceList = coolWeatherDB.loadProvince();
		if(provinceList.size() > 0){
			dataList.clear();
			for (Province province : provinceList) {
				dataList.add(province.getProvinceName());
			}
			adapter.notifyDataSetChanged();
			lv.setSelection(0);
			tv_title.setText("中国");
		}
		else{
			queryFromServer(null, "province");
		}
	}
	
	/**
	 * 通过传入的代号和类型从服务器上查询数据
	 */
	private void queryFromServer(final String code, final String type){
		String address;
		if(TextUtils.isEmpty(code)){
			address = "http://www.weather.com.cn/data/list3/city.xml";
		}
		else{
			address = "http://www.weather.com.cn/data/list3/city" + code + ".xml";
		}
		HttpUtils.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(String response) {
				// TODO Auto-generated method stub
				boolean flag = false;
				if("province".equals(type)){
					flag = Utility.handleProvincesResponse(coolWeatherDB, response);
				}
//				else if("city".equals(type)){
//					flag = Utility.handlCitysResponse(coolWeatherDB, response, selecedProvince.getId());
//				}
//				else if("county".equals(type)){
//					flag = Utility.handleCountysResponse(coolWeatherDB, response, cityId);
//				}
				if(flag){
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							if("province".equals(type)){
								queryProvince();
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
}
