package com.huayu.bracelet.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.huayu.bracelet.BaseApplication;
import com.huayu.bracelet.R;
import com.huayu.bracelet.vo.DevicesInfo;


public class BTsearchActivity extends PActivity implements OnClickListener{

	private TextView SearchTvTitle;
	private TextView SearchTvSearch;
	private TextView SearchTvUndo;
	private ListView Searchlv;
	public static final String TAG = "BTsearchActivity";
	private Map<String, Integer> devRssiValues;
	private BluetoothAdapter mBluetoothAdapter;
	private List<BluetoothDevice> deviceList;
	private DeviceAdapter deviceAdapter;
	private static final long SCAN_PERIOD = 10000; //10 seconds
	private Handler mHandler;
	private ProgressBar SearchProgress;
	private List<DevicesInfo> devicesInfos;
//	private boolean mScanning;

	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_btsearch);
		mHandler = new Handler();
		SearchTvTitle = (TextView)findViewById(R.id.SearchTvTitle);
		SearchTvSearch = (TextView)findViewById(R.id.SearchTvSearch);
		SearchTvUndo = (TextView)findViewById(R.id.SearchTvUndo);
		Searchlv = (ListView)findViewById(R.id.Searchlv);
		SearchProgress = (ProgressBar)findViewById(R.id.SearchProgress);
		SearchTvSearch.setOnClickListener(this);
		SearchTvUndo.setOnClickListener(this);
		if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
			Toast.makeText(this, "该手机不支持蓝牙4.0", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			finish();
		}
		final BluetoothManager bluetoothManager =
				(BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		mBluetoothAdapter = bluetoothManager.getAdapter();
		deviceList = new ArrayList<BluetoothDevice>();
		deviceAdapter = new DeviceAdapter(this, deviceList);
		devRssiValues = new HashMap<String, Integer>();

		Searchlv.setAdapter(deviceAdapter);
		Searchlv.setOnItemClickListener(mDeviceClickListener);

		scanLeDevice(true);
	}

	@SuppressLint("NewApi") 
	private void scanLeDevice(final boolean enable) {
		deviceList.clear();
		if (enable) {
			// Stops scanning after a pre-defined scan period.
			mHandler.postDelayed(new Runnable() {
				@SuppressLint("NewApi") @Override
				public void run() {
//					mScanning = false;
					mBluetoothAdapter.stopLeScan(mLeScanCallback);
					SearchTvSearch.setVisibility(View.VISIBLE);
					if(deviceList.size()==0){
						SearchProgress.setVisibility(View.GONE);
						SearchTvTitle.setText("未找到华宇设备");
					}else if(deviceList.size()>0){
						SearchTvTitle.setText("连接华宇设备");
						SearchProgress.setVisibility(View.GONE);
					}
				}
			}, SCAN_PERIOD);

//			mScanning = true;
			SearchTvTitle.setText("正在搜索华宇设备");
			SearchTvSearch.setVisibility(View.INVISIBLE);
			SearchProgress.setVisibility(View.VISIBLE);
			mBluetoothAdapter.startLeScan(mLeScanCallback);
		} else {
//			mScanning = false;
			mBluetoothAdapter.stopLeScan(mLeScanCallback);
			SearchTvSearch.setVisibility(View.VISIBLE);
			SearchProgress.setVisibility(View.GONE);
		}

	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		scanLeDevice(false);
	}

	@SuppressLint("NewApi") 
	private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

		@Override
		public void onLeScan(final BluetoothDevice device, final int rssi, byte[] scanRecord) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							addDevice(device,rssi);
						}
					});

				}
			});
		}
	};

	private void addDevice(BluetoothDevice device, int rssi) {
		boolean deviceFound = false;

		for (BluetoothDevice listDev : deviceList) {
			if (listDev.getAddress().equals(device.getAddress())) {
				deviceFound = true;
				break;
			}
		}
		devRssiValues.put(device.getAddress(), rssi);
		if (!deviceFound) {
			deviceList.add(device);
			SearchTvSearch.setVisibility(View.VISIBLE);
			deviceAdapter.notifyDataSetChanged();
		}
	}

	private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {

		@SuppressLint("NewApi") @Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			mBluetoothAdapter.stopLeScan(mLeScanCallback);
			boolean isBind = false;
			final String mac =  deviceList.get(position).getAddress();
			devicesInfos = BaseApplication.getInstance().getDevicesInfo();
			if(devicesInfos!=null&&devicesInfos.size()>0){
				for(DevicesInfo devicesInfo : devicesInfos){
					if(mac.equals(devicesInfo.getMac())){
						BaseApplication.devicesId = devicesInfo.getId();
						isBind = true;
					}
				}
			}
			if(!isBind){
				View v = getLayoutInflater().inflate(R.layout.dialog_set_id, null);
				final EditText setEdId = (EditText)v.findViewById(R.id.setEdId); 
				TextView setTvComfirm = (TextView)v.findViewById(R.id.setTvComfirm);
				new AlertDialog.Builder(BTsearchActivity.this)
				.setView(v)
				.setCancelable(false)
				.show();
				setTvComfirm.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if(TextUtils.isEmpty(setEdId.getText().toString())&&
								setEdId.getText().toString().length()!=6){
							Toast.makeText(BTsearchActivity.this, "请输入6位手环ID",
									Toast.LENGTH_SHORT).show();
							return;
						}
						BaseApplication.devicesId = setEdId.getText().toString();
						if(devicesInfos==null){
							devicesInfos = new ArrayList<DevicesInfo>();
						}
						DevicesInfo devicesInfo = new DevicesInfo();
						devicesInfo.setId(setEdId.getText().toString());
						devicesInfo.setMac(mac);
						devicesInfos.add(devicesInfo);
						BaseApplication.getInstance().setDevicesInfo(devicesInfos);
						Bundle b = new Bundle();
						b.putString(BluetoothDevice.EXTRA_DEVICE,mac);
						Intent intent = new Intent(BTsearchActivity.this,MainActivity.class);
						intent.putExtras(b);
						startActivity(intent);
						finish();
					}
				});
			}else{
				Bundle b = new Bundle();
				b.putString(BluetoothDevice.EXTRA_DEVICE,mac);
				Intent intent = new Intent(BTsearchActivity.this,MainActivity.class);
				intent.putExtras(b);
				startActivity(intent);
				finish();
			}
		}
	};

	class DeviceAdapter extends BaseAdapter {
		Context context;
		List<BluetoothDevice> devices;
		LayoutInflater inflater;

		public DeviceAdapter(Context context, List<BluetoothDevice> devices) {
			this.context = context;
			inflater = LayoutInflater.from(context);
			this.devices = devices;
		}

		@Override
		public int getCount() {
			return devices.size();
		}

		@Override
		public Object getItem(int position) {
			return devices.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewGroup vg;

			if (convertView != null) {
				vg = (ViewGroup) convertView;
			} else {
				vg = (ViewGroup) inflater.inflate(R.layout.device_element, null);
			}

			BluetoothDevice device = devices.get(position);
			final TextView tvadd = ((TextView) vg.findViewById(R.id.address));
			final TextView tvname = ((TextView) vg.findViewById(R.id.name));
			final TextView tvpaired = (TextView) vg.findViewById(R.id.paired);
			final TextView tvrssi = (TextView) vg.findViewById(R.id.rssi);

			tvrssi.setVisibility(View.VISIBLE);
			byte rssival = (byte) devRssiValues.get(device.getAddress()).intValue();
			if (rssival != 0) {
				tvrssi.setText("Rssi = " + String.valueOf(rssival));
			}

			tvname.setText(device.getName());
			tvadd.setText(device.getAddress());
			if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
				Log.i(TAG, "device::"+device.getName());
				tvname.setTextColor(Color.WHITE);
				tvadd.setTextColor(Color.WHITE);
				tvpaired.setTextColor(Color.GRAY);
				tvpaired.setVisibility(View.VISIBLE);
				tvpaired.setText("配对");
				tvrssi.setVisibility(View.VISIBLE);
				tvrssi.setTextColor(Color.WHITE);

			} else {
				tvname.setTextColor(Color.WHITE);
				tvadd.setTextColor(Color.WHITE);
				tvpaired.setVisibility(View.GONE);
				tvrssi.setVisibility(View.VISIBLE);
				tvrssi.setTextColor(Color.WHITE);
			}
			return vg;
		}
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.SearchTvSearch:
			scanLeDevice(true);
			break;
		case R.id.SearchTvUndo:
			Intent intent = new Intent(BTsearchActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
			break;
		default:
			break;
		}
	}

}
