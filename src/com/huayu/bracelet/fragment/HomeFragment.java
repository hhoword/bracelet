package com.huayu.bracelet.fragment;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.huayu.bracelet.BaseApplication;
import com.huayu.bracelet.R;
import com.huayu.bracelet.activity.BTsearchActivity;
import com.huayu.bracelet.activity.IOnDataListener;
import com.huayu.bracelet.http.HttpUtil;
import com.huayu.bracelet.services.UartService;
import com.huayu.bracelet.vo.DeviceStepInfo;
import com.huayu.bracelet.vo.WeeKAvgStep;

public class HomeFragment extends Fragment implements OnClickListener{

	public static final String TAG = "HomeFragment";
	private UartService mService = null;
	private BluetoothDevice mDevice = null;
	private TextView homeTvSync;
	private TextView homeTvStep;
	private TextView homevTvDistance;
	private TextView homeTvCalorie;
	private TextView homeTvAvgStep;
	private boolean isConnect = false;
	private String stepInfo;
	private String[] deviceInfo;
	private ProgressDialog progressdialog;
	private Date d = new Date();  
	private SimpleDateFormat ss ;
	private String requestDate;
	private int requestNum = 0;

	@SuppressLint({ "SimpleDateFormat", "InflateParams" })
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.framgent_home, null);
		ss = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");//12小时制  
		progressdialog = new ProgressDialog(getActivity());
		progressdialog.setCancelable(false);
		homeTvSync = (TextView)view.findViewById(R.id.homeTvSync);
		homeTvStep = (TextView)view.findViewById(R.id.homeTvStep);
		homevTvDistance = (TextView)view.findViewById(R.id.homevTvDistance);
		homeTvCalorie = (TextView)view.findViewById(R.id.homeTvCalorie);
		homeTvAvgStep = (TextView)view.findViewById(R.id.homeTvAvgStep);
		if(BaseApplication.getInstance().getStep()>0){
			homeTvStep.setText(BaseApplication.getInstance().getStep()+"");
		}
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
				BaseApplication.mac  = deviceAddress;
				mDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(deviceAddress);
				Log.d(TAG, "... onActivityResultdevice.address==" + mDevice + "mserviceValue" + mService);
				isConnect = mService.connect(deviceAddress);
			}


		}

		public void onServiceDisconnected(ComponentName classname) {
			mService = null;
		}
	};

	private final BroadcastReceiver UARTStatusChangeReceiver = new BroadcastReceiver() {

		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			//*********************//
			if (action.equals(UartService.ACTION_GATT_CONNECTED)) {
				getActivity().runOnUiThread(new Runnable() {
					public void run() {
						isConnect = true;
						Log.d(TAG, "UART_CONNECT_MSG");
					}
				});
			}

			//*********************//
			if (action.equals(UartService.ACTION_GATT_DISCONNECTED)) {
				getActivity().runOnUiThread(new Runnable() {
					public void run() {
						isConnect = false;
						progressdialog.dismiss();
						Log.d(TAG, "UART_DISCONNECT_MSG");
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
							if(requestNum>=30){ //只能保存30天内的数据
								upLoadStep();
								requestNum=0;
								return;
							}
							requestNum++; 
							int lastStep = BaseApplication.getInstance().getStep();
							String asyncDate = BaseApplication.getInstance().getAsyncDate();
							String text = new String(txValue, "UTF-8");
							//							Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
							stepInfo+=text;
							deviceInfo = stepInfo.split(",");
							if(deviceInfo!=null&&"BF#".equals(deviceInfo[deviceInfo.length-1])){
								deviceInfo = stepInfo.split(",");
								String step = deviceInfo[3];
//								String step = "100";
								String power = deviceInfo[5];
								//保存电量
								BaseApplication.getInstance().setPower(power);
								if(!TextUtils.isEmpty(asyncDate)){//如果有上次同步时间点
									if(BaseApplication.isSameday(ss.format(d),asyncDate)){//上次同步时间为今天
										setStep(step, lastStep);
										//保存同步时间
										BaseApplication.getInstance().setAsyncDate(ss.format(d));
										saveStepInfo(step);
										float distances = getDistances(BaseApplication.getInstance().getStep());
										homevTvDistance.setText(distances+"");
										homeTvCalorie.setText(getCalorie(distances));
										//同步完成，上传步数
										upLoadStep();
									}else{
										if(BaseApplication.isSameday(ss.format(d),requestDate)){//请求时间如果是今天
											setStep(step, lastStep);
											//获取当前请求时间前一天时间作为请求时间
											saveStepInfo(step);
											float distances = getDistances(BaseApplication.getInstance().getStep());
											homevTvDistance.setText(distances+"");
											homeTvCalorie.setText(getCalorie(distances));
											requestDate = BaseApplication.getSpecifiedDayBefore(requestDate);
											sendMessage(requestDate);
										}else{
											if(BaseApplication.isSameday(requestDate, asyncDate)){
												//保存同步时间
												BaseApplication.getInstance().setAsyncDate(ss.format(d));
												//同步完成，上传步数
												upLoadStep();
												return ;
											}
											saveStepInfo(step);
											requestDate = BaseApplication.getSpecifiedDayBefore(requestDate);
											sendMessage(requestDate);
										}
									}
								}else{
									setStep(step, lastStep);
									saveStepInfo(step);
									float distances = getDistances(BaseApplication.getInstance().getStep());
									homevTvDistance.setText(distances+"");
									homeTvCalorie.setText(getCalorie(distances));
									//保存同步时间
									BaseApplication.getInstance().setAsyncDate(ss.format(d));
									//上传步数
									upLoadStep();
								}
								//清空步数信息
								stepInfo = "";
							}

						} catch (Exception e) {
							Log.e(TAG, e.toString());
							progressdialog.dismiss();
							Toast.makeText(getActivity(), "同步失败", Toast.LENGTH_SHORT).show();
							stepInfo = "";
						}
					}
				});
			}
			//*********************//
			if (action.equals(UartService.DEVICE_DOES_NOT_SUPPORT_UART)){
				Toast.makeText(getActivity(), "蓝牙不可用", Toast.LENGTH_SHORT).show();
				mService.disconnect();
			}
		}
	};

	private void setStep(String step,int lastStep){
		int currentStep = lastStep +Integer.parseInt(step);
		BaseApplication.getInstance().setStep(currentStep);
		homeTvStep.setText(currentStep+"");
	}
	private void saveStepInfo(String step){
		List<DeviceStepInfo> deviceStepInfos;
		deviceStepInfos = BaseApplication.getInstance().getDeviceStepInfo();
		if(deviceStepInfos==null){
			deviceStepInfos = new ArrayList<DeviceStepInfo>();
		}
		DeviceStepInfo deviceStepInfo = new DeviceStepInfo();
		deviceStepInfo.setDatetime(requestDate);
		deviceStepInfo.setStepcount(step);
		deviceStepInfo.setMac(BaseApplication.mac);
		deviceStepInfo.setUid(BaseApplication.getInstance()
				.getUserData().getData().getUserinfo().getId()+"");
		deviceStepInfos.add(deviceStepInfo);
		BaseApplication.getInstance().setDeviceStepInfo(deviceStepInfos);
	}

	private void upLoadStep(){
		List<DeviceStepInfo> deviceStepInfos = new ArrayList<DeviceStepInfo>();
		List<DeviceStepInfo> datas = BaseApplication.getInstance().getDeviceStepInfo();
		if(datas == null){
			datas = new ArrayList<DeviceStepInfo>();
		}
		for(DeviceStepInfo deviceStepInfo : datas){
			if(deviceStepInfo.getUid().equals(BaseApplication.getInstance()
					.getUserData().getData().getUserinfo().getId()+"")){
				deviceStepInfos.add(deviceStepInfo);
			}
		}
		HttpUtil httpUtil = new HttpUtil();
		httpUtil.postStepInfo(deviceStepInfos, new IOnDataListener<WeeKAvgStep>() {

			@Override
			public void onDataResult(WeeKAvgStep t) {
				// TODO Auto-generated method stub
				progressdialog.dismiss();
				if(t!=null){
					if(t.getCode()==200){
						if(t.getData().isStatus()){
							Toast.makeText(getActivity(), "同步成功", Toast.LENGTH_SHORT).show();
							homeTvAvgStep.setText(t.getData().getStepavg());
							BaseApplication.getInstance().setDeviceStepInfo(null);
							return;
						}
					}
				}
				Toast.makeText(getActivity(), "同步失败", Toast.LENGTH_SHORT).show();
			}
		});
	}

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

	/**
	 * 发送同步信息
	 * @param time yyyy-MM-dd hh:mm:ss
	 */
	private void sendMessage(String time){
		String message = "#BF,request,"+BaseApplication.devicesId
				+","+time.substring(0, 1);
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
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.homeTvSync:
			if(!isConnect){
				Toast.makeText(getActivity(), "未连接设备", Toast.LENGTH_LONG).show();
				Intent intent = new Intent(getActivity(), BTsearchActivity.class);
				getActivity().startActivity(intent);
				return;
			}
			progressdialog.setMessage("正在同步中...");
			progressdialog.show();
			//			Toast.makeText(getActivity(), "正在同步中...", Toast.LENGTH_LONG).show();
			requestDate = ss.format(d);
			sendMessage(requestDate);
			break;

		default:
			break;
		}
	}

	private String getCalorie(float distances){
		float weight = BaseApplication.getInstance()
				.getUserData().getData().getUserinfo().getWeight();
		int Calorie = (int) (weight*distances*1.036);
		return Calorie/1000f+"";
	}

	private float getDistances(int Steps){
		int sex = BaseApplication.getInstance()
				.getUserData().getData().getUserinfo().getSex();
		float distances;
		if(sex == 0){
			distances =  (float) (((float)Steps)*0.65);
		}else{
			distances =  (float) (((float)Steps)*0.75);
		}
		return distances/1000f;
	}

}
