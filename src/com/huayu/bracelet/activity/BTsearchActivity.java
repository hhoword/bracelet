package com.huayu.bracelet.activity;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.huayu.bracelet.R;
import com.huayu.bracelet.services.UartService;


public class BTsearchActivity extends PActivity {

	private TextView SearchTvTitle;
	private TextView SearchTvSearch;
	private TextView SearchTvUndo;
	private ListView Searchlv;
	private UartService mService = null;
	public static final String TAG = "BTsearchActivity";
	private Map<String, Integer> devRssiValues;
	private BluetoothAdapter mBluetoothAdapter;
	private List<BluetoothDevice> deviceList;
	private DeviceAdapter deviceAdapter;
	private static final long SCAN_PERIOD = 10000; //10 seconds
	private Handler mHandler;
	private boolean mScanning;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_btsearch);
		mHandler = new Handler();
		SearchTvTitle = (TextView)findViewById(R.id.SearchTvTitle);
		SearchTvSearch = (TextView)findViewById(R.id.SearchTvSearch);
		SearchTvUndo = (TextView)findViewById(R.id.SearchTvUndo);
		Searchlv = (ListView)findViewById(R.id.Searchlv);
		service_init();
//		if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
//			Toast.makeText(this, "电量过低，不足支持蓝牙", Toast.LENGTH_SHORT).show();
//			finish();
//		}
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

	private void scanLeDevice(final boolean enable) {
		if (enable) {
			// Stops scanning after a pre-defined scan period.
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					mScanning = false;
					mBluetoothAdapter.stopLeScan(mLeScanCallback);
					SearchTvSearch.setVisibility(View.VISIBLE);

				}
			}, SCAN_PERIOD);

			mScanning = true;
			mBluetoothAdapter.startLeScan(mLeScanCallback);
		} else {
			mScanning = false;
			mBluetoothAdapter.stopLeScan(mLeScanCallback);
			SearchTvSearch.setVisibility(View.VISIBLE);
		}

	}

	private BluetoothAdapter.LeScanCallback mLeScanCallback =
			new BluetoothAdapter.LeScanCallback() {

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
//			mEmptyList.setVisibility(View.GONE);




			deviceAdapter.notifyDataSetChanged();
		}
	}

	private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			BluetoothDevice device = deviceList.get(position);
			mBluetoothAdapter.stopLeScan(mLeScanCallback);

			Bundle b = new Bundle();
			b.putString(BluetoothDevice.EXTRA_DEVICE, deviceList.get(position).getAddress());

			Intent result = new Intent();
			result.putExtras(b);
			setResult(Activity.RESULT_OK, result);
			finish();

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

	private ServiceConnection mServiceConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder rawBinder) {
			mService = ((UartService.LocalBinder) rawBinder).getService();
			Log.d(TAG, "onServiceConnected mService= " + mService);
			if (!mService.initialize()) {
				Log.e(TAG, "Unable to initialize Bluetooth");
				finish();
			}

		}

		public void onServiceDisconnected(ComponentName classname) {
			////     mService.disconnect(mDevice);
			mService = null;
		}
	};

	private final BroadcastReceiver UARTStatusChangeReceiver = new BroadcastReceiver() {

		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			final Intent mIntent = intent;
			//*********************//
			if (action.equals(UartService.ACTION_GATT_CONNECTED)) {
				runOnUiThread(new Runnable() {
					public void run() {
						String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
						Log.d(TAG, "UART_CONNECT_MSG");
						//						btnConnectDisconnect.setText("Disconnect");
						//						edtMessage.setEnabled(true);
						//						btnSend.setEnabled(true);
						//						((TextView) findViewById(R.id.deviceName)).setText(mDevice.getName()+ " - ready");
						//						listAdapter.add("["+currentDateTimeString+"] Connected to: "+ mDevice.getName());
						//						messageListView.smoothScrollToPosition(listAdapter.getCount() - 1);
						//						mState = UART_PROFILE_CONNECTED;
					}
				});
			}

			//*********************//
			if (action.equals(UartService.ACTION_GATT_DISCONNECTED)) {
				runOnUiThread(new Runnable() {
					public void run() {
						String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
						Log.d(TAG, "UART_DISCONNECT_MSG");
						//						btnConnectDisconnect.setText("Connect");
						//						edtMessage.setEnabled(false);
						//						btnSend.setEnabled(false);
						//						((TextView) findViewById(R.id.deviceName)).setText("Not Connected");
						//						listAdapter.add("["+currentDateTimeString+"] Disconnected to: "+ mDevice.getName());
						//						mState = UART_PROFILE_DISCONNECTED;
						//						mService.close();
						//setUiState();

					}
				});
			}


			//*********************//
			if (action.equals(UartService.ACTION_GATT_SERVICES_DISCOVERED)) {
				mService.enableTXNotification();
			}
			//*********************//
			if (action.equals(UartService.ACTION_DATA_AVAILABLE)) {

				final byte[] txValue = intent.getByteArrayExtra(UartService.EXTRA_DATA);
				runOnUiThread(new Runnable() {
					public void run() {
						try {
							String text = new String(txValue, "UTF-8");
							String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
							//							listAdapter.add("["+currentDateTimeString+"] RX: "+text);
							//                    	 	messageListView.smoothScrollToPosition(listAdapter.getCount() - 1);
						} catch (Exception e) {
							Log.e(TAG, e.toString());
						}
					}
				});
			}
			//*********************//
			if (action.equals(UartService.DEVICE_DOES_NOT_SUPPORT_UART)){
				Toast.makeText(BTsearchActivity.this, "蓝牙不可用", Toast.LENGTH_LONG).show();
				mService.disconnect();
			}


		}
	};


	private void service_init() {
		Intent bindIntent = new Intent(this, UartService.class);
		bindService(bindIntent, mServiceConnection, Context.BIND_AUTO_CREATE);

		LocalBroadcastManager.getInstance(this).registerReceiver(UARTStatusChangeReceiver, makeGattUpdateIntentFilter());
	}
	private static IntentFilter makeGattUpdateIntentFilter() {
		final IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(UartService.ACTION_GATT_CONNECTED);
		intentFilter.addAction(UartService.ACTION_GATT_DISCONNECTED);
		intentFilter.addAction(UartService.ACTION_GATT_SERVICES_DISCOVERED);
		intentFilter.addAction(UartService.ACTION_DATA_AVAILABLE);
		intentFilter.addAction(UartService.DEVICE_DOES_NOT_SUPPORT_UART);
		return intentFilter;
	}

}
