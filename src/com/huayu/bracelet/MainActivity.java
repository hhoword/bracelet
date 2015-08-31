package com.huayu.bracelet;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;


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
		setContentView(R.layout.activity_main);
		mainRbtGroup = (RadioGroup)findViewById(R.id.mianRbtGroup);
		mFragments = new Fragment[3];  
		fragmentManager = getSupportFragmentManager();  
		mFragments[0] = fragmentManager.findFragmentById(R.id.fragmentHome);  
		mFragments[1] = fragmentManager.findFragmentById(R.id.fragmentFriend);  
		mFragments[2] = fragmentManager.findFragmentById(R.id.fragmentMe);
		fragmentTransaction = fragmentManager.beginTransaction()  
				.hide(mFragments[0]).hide(mFragments[1]).hide(mFragments[2]);  
		fragmentTransaction.show(mFragments[0]).commit();  
		mainRbtGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {  
  
            @Override  
            public void onCheckedChanged(RadioGroup group, int checkedId) {  
                fragmentTransaction = fragmentManager.beginTransaction()  
                        .hide(mFragments[0]).hide(mFragments[1])  
                        .hide(mFragments[2]);  
                switch (checkedId) {  
                case R.id.mainRbtHome:  
                    fragmentTransaction.show(mFragments[0]).commit();  
                    break;  
  
                case R.id.mainRbtCricle:  
                    fragmentTransaction.show(mFragments[1]).commit();  
                    break;  
  
                case R.id.mainRbtSetting:  
                    fragmentTransaction.show(mFragments[2]).commit();  
                    break;  
  
                default:  
                    break;  
                }  
            }  
        });  
	}

}
