package com.huayu.bracelet.activity;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.huayu.bracelet.R;


public class MainActivity extends FragmentActivity {

	private Fragment[] mFragments;  
	private FragmentManager fragmentManager;  
	private FragmentTransaction fragmentTransaction;  
	private RadioGroup mainRbtGroup;  
	private RadioButton mainRbtHome;
	private RadioButton mainRbtCricle;
	private RadioButton mainRbtSetting;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		mainRbtGroup = (RadioGroup)findViewById(R.id.mianRbtGroup);
		mainRbtHome = (RadioButton)findViewById(R.id.mainRbtHome);
		mainRbtCricle = (RadioButton)findViewById(R.id.mainRbtCricle);
		mainRbtSetting = (RadioButton)findViewById(R.id.mainRbtSetting);
		mFragments = new Fragment[3];  
		fragmentManager = getSupportFragmentManager();  
		mFragments[0] = fragmentManager.findFragmentById(R.id.fragmentHome);  
//		Bundle bundle = new Bundle();
//		bundle.putString(BluetoothDevice.EXTRA_DEVICE, 
//				getIntent().getStringExtra(BluetoothDevice.EXTRA_DEVICE));
//		mFragments[0].setArguments(bundle);
		mFragments[1] = fragmentManager.findFragmentById(R.id.fragmentFriend);  
		mFragments[2] = fragmentManager.findFragmentById(R.id.fragmentMe);
		fragmentTransaction = fragmentManager.beginTransaction()  
				.hide(mFragments[0]).hide(mFragments[1]).hide(mFragments[2]); 
		mainRbtGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {  

			@Override  
			public void onCheckedChanged(RadioGroup group, int checkedId) {  
				fragmentTransaction = fragmentManager.beginTransaction()  
						.hide(mFragments[0]).hide(mFragments[1])  
						.hide(mFragments[2]);  
				mainRbtHome.setTextColor(getResources().getColor(R.color.grap));
				mainRbtCricle.setTextColor(getResources().getColor(R.color.grap));
				mainRbtSetting.setTextColor(getResources().getColor(R.color.grap));
				mainRbtHome.setCompoundDrawablesWithIntrinsicBounds(null, 
						getResources().getDrawable(R.drawable.ico_home), null, null);
				mainRbtCricle.setCompoundDrawablesWithIntrinsicBounds(null, 
						getResources().getDrawable(R.drawable.ico_friendsgroup), null, null);
				mainRbtSetting.setCompoundDrawablesWithIntrinsicBounds(null, 
						getResources().getDrawable(R.drawable.ico_me), null, null);
				switch (checkedId) {  
				case R.id.mainRbtHome: 
					mainRbtHome.setTextColor(getResources().getColor(R.color.red));
					mainRbtHome.setCompoundDrawablesWithIntrinsicBounds(null, 
							getResources().getDrawable(R.drawable.ico_home_press), null, null);
					fragmentTransaction.show(mFragments[0]).commit();  
					break;  

				case R.id.mainRbtCricle:  
					mainRbtCricle.setTextColor(getResources().getColor(R.color.red));
					mainRbtCricle.setCompoundDrawablesWithIntrinsicBounds(null, 
							getResources().getDrawable(R.drawable.ico_friendsgroup_press), null, null);
					fragmentTransaction.show(mFragments[1]).commit();  
					break;  

				case R.id.mainRbtSetting:  
					mainRbtSetting.setTextColor(getResources().getColor(R.color.red));
					mainRbtSetting.setCompoundDrawablesWithIntrinsicBounds(null, 
							getResources().getDrawable(R.drawable.ico_me_press), null, null);
					fragmentTransaction.show(mFragments[2]).commit();  
					break;  

				default:  
					break;  
				}  
			}  
		}); 

		fragmentTransaction.show(mFragments[0]).commit();  
		mainRbtHome.setChecked(true);
	}

	
}
