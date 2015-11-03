package com.huayu.bracelet.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.huayu.bracelet.BaseApplication;
import com.huayu.bracelet.R;
import com.huayu.bracelet.vo.DevicesInfo;

public class BindActivity extends PActivity implements OnClickListener,OnItemClickListener{

	private ImageView bindIvBack;
	private LinearLayout bindLayoutBind;
	private LinearLayout bindLayoutData;
	private TextView bindTvDevice;
	private ListView bindLvDevices;

	private List<String> data;
	private List<DevicesInfo> devicesInfos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_bind);
		bindIvBack = (ImageView)findViewById(R.id.bindIvBack);
		bindLayoutBind = (LinearLayout)findViewById(R.id.bindLayoutBind);
		bindLayoutData= (LinearLayout)findViewById(R.id.bindLayoutData);
		bindTvDevice= (TextView)findViewById(R.id.bindTvDevice);
		bindLvDevices = (ListView)findViewById(R.id.bindLvDevices);
		bindLvDevices.setOnItemClickListener(this);
		bindIvBack.setOnClickListener(this);
		bindLayoutBind.setOnClickListener(this);
		setData();
	}

	private void setData(){
		devicesInfos = BaseApplication.getInstance().getDevicesInfo();
		if(devicesInfos!=null&&devicesInfos.size()>0){
			bindLayoutData.setVisibility(View.VISIBLE);
			bindTvDevice.setText(BaseApplication.devicesId);
			data = new ArrayList<String>();
			for(DevicesInfo devicesInfo : devicesInfos){
				data.add(devicesInfo.getId());
			}
			bindLvDevices.setAdapter(new ArrayAdapter<String>(this, 
					android.R.layout.simple_expandable_list_item_1,data));
		}else{
			bindLayoutData.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bindIvBack:
			finish();
			break;
		case R.id.bindLayoutBind:
			Intent intent = new Intent(BindActivity.this, BTsearchActivity.class);
			startActivity(intent);
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		for(int i=0; i<devicesInfos.size() ;i++){
			DevicesInfo devicesInfo = devicesInfos.get(i);
			if(devicesInfo.getId().equals(data.get(position))){
				devicesInfos.remove(i);
			}
		}
		BaseApplication.getInstance().setDevicesInfo(devicesInfos);
		Toast.makeText(BindActivity.this, "已解除绑定", Toast.LENGTH_SHORT).show();
		if(BaseApplication.devicesId.equals(data.get(position))){
			BaseApplication.devicesId ="";
			Intent intent = new Intent(BindActivity.this, BTsearchActivity.class);
			startActivity(intent);
		}
		setData();
	}


}
