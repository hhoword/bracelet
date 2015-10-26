package com.huayu.bracelet.fragment;

import java.text.DateFormat;
import java.util.Date;

import android.app.Activity;
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
import android.widget.Button;
import android.widget.Toast;

import com.huayu.bracelet.R;
import com.huayu.bracelet.activity.LoginActivity;
import com.huayu.bracelet.services.UartService;

public class HomeFragment extends Fragment{
	
	public static final String TAG = "HomeFragment";
	private UartService mService = null;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		service_init();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.framgent_home, null);
		Button button = (Button) view.findViewById(R.id.button1);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), LoginActivity.class);
				getActivity().startActivity(intent);
			}
		});
		return view;
	}
	
	private ServiceConnection mServiceConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder rawBinder) {
			mService = ((UartService.LocalBinder) rawBinder).getService();
			Log.d(TAG, "onServiceConnected mService= " + mService);
			if (!mService.initialize()) {
				Log.e(TAG, "Unable to initialize Bluetooth");
				getActivity().finish();
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
				getActivity().runOnUiThread(new Runnable() {
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
				getActivity().runOnUiThread(new Runnable() {
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
				getActivity().runOnUiThread(new Runnable() {
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
	
}
