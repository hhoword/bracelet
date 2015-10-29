package com.huayu.bracelet.fragment;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.huayu.bracelet.R;
import com.huayu.bracelet.services.UartService;

public class HomeFragment extends Fragment implements OnClickListener{

	public static final String TAG = "HomeFragment";
	private UartService mService = null;
	private BluetoothDevice mDevice = null;
	private TextView homeTvSync;
	private TextView homeTvStep;
	private boolean isConnect = false;
	private String stepInfo;
	private String[] deviceInfo;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.framgent_home, null);
		homeTvSync = (TextView)view.findViewById(R.id.homeTvSync);
		homeTvStep = (TextView)view.findViewById(R.id.homeTvStep);
		homeTvSync.setOnClickListener(this);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		service_init();

	}

	private ServiceConnection mServiceConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder rawBinder) {
			mService = ((UartService.LocalBinder) rawBinder).getService();
			Log.d(TAG, "onServiceConnected mService= " + mService);
			if (!mService.initialize()) {
				Log.e(TAG, "Unable to initialize Bluetooth");
				getActivity().finish();
			}
			String deviceAddress = getActivity().getIntent().getStringExtra(BluetoothDevice.EXTRA_DEVICE);
			if(deviceAddress!=null){
				mDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(deviceAddress);
				Log.d(TAG, "... onActivityResultdevice.address==" + mDevice + "mserviceValue" + mService);
				isConnect = mService.connect(deviceAddress);
				//				String message = "#BF,request,ID,时间,BF#";
				//				byte[] value;
				//				try {
				//					//send data to service
				//					value = message.getBytes("UTF-8");
				//					mService.writeRXCharacteristic(value);
				//					//Update the log with time stamp
				//				} catch (UnsupportedEncodingException e) {
				//					// TODO Auto-generated catch block
				//					e.printStackTrace();
				//				}
			}


		}

		public void onServiceDisconnected(ComponentName classname) {
			mService = null;
		}
	};

	private final BroadcastReceiver UARTStatusChangeReceiver = new BroadcastReceiver() {

		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			final Intent mIntent = intent;
			//*********************//
			if (action.equals(UartService.ACTION_GATT_CONNECTED)) {
				getActivity().runOnUiThread(new Runnable() {
					public void run() {
						isConnect = true;
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
				getActivity().runOnUiThread(new Runnable() {
					public void run() {
						isConnect = false;
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
				getActivity().runOnUiThread(new Runnable() {
					public void run() {
						try {
							String text = new String(txValue, "UTF-8");
							Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
							stepInfo+=text;
							deviceInfo = text.split(",");
							if(deviceInfo!=null&&"BF#".equals(deviceInfo[deviceInfo.length])){
								deviceInfo = stepInfo.split(",");
								String step = deviceInfo[3];
								stepInfo = "";
								homeTvStep.setText(step);
							}
						} catch (Exception e) {
							Log.e(TAG, e.toString());
						}
					}
				});
			}
			//*********************//
			if (action.equals(UartService.DEVICE_DOES_NOT_SUPPORT_UART)){
				Toast.makeText(getActivity(), "蓝牙不可用", Toast.LENGTH_LONG).show();
				mService.disconnect();
			}


		}
	};


	private void service_init() {
		Intent bindIntent = new Intent(getActivity(), UartService.class);
		getActivity().bindService(bindIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(UARTStatusChangeReceiver, makeGattUpdateIntentFilter());
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

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try {
			LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(UARTStatusChangeReceiver);
		} catch (Exception ignore) {
			Log.e(TAG, ignore.toString());
		} 
		getActivity().unbindService(mServiceConnection);
		mService.stopSelf();
		mService= null;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.homeTvSync:
			if(!isConnect){
				Toast.makeText(getActivity(), "未连接设备", Toast.LENGTH_LONG).show();
				return;
			}
			Date d = new Date();  
			SimpleDateFormat ss = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");//12小时制  
			System.out.println(ss.format(d));  
			String time = ss.format(d);
			String message = "#BF,request,123456,"+time.substring(0, 1);
			String message2 = time.substring(1, time.length())+",B";
			String message3 = "F#";
			byte[] value;
			try {
				//send data to service
				value = message.getBytes("UTF-8");
				mService.writeRXCharacteristic(value);
				value = message2.getBytes("UTF-8");
				mService.writeRXCharacteristic(value);
				value = message3.getBytes("UTF-8");
				mService.writeRXCharacteristic(value);
				//Update the log with time stamp
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		default:
			break;
		}
	}

}
