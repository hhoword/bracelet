package com.huayu.bracelet.activity;

import com.huayu.bracelet.BaseApplication;
import com.huayu.bracelet.R;
import com.huayu.bracelet.http.HttpUtil;
import com.huayu.bracelet.vo.UserData;
import com.huayu.bracelet.vo.UserInfo;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class EditInfoActivity extends PActivity{

	private ImageView editIvBack;
	private Button editBtnConfirm;
	private EditText editEtEdit;
	private int type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_edit_info);
		Intent intent = getIntent();
		type = intent.getIntExtra("type", 0);
		editIvBack = (ImageView)findViewById(R.id.editIvBack);
		editEtEdit = (EditText)findViewById(R.id.editEtEdit);
		editBtnConfirm = (Button)findViewById(R.id.editBtnConfirm);
		switch (type) {
		case 0:
			editEtEdit.setInputType(InputType.TYPE_CLASS_TEXT);
			break;
		case 1:
		case 2:
			editEtEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
			break;

		default:
			break;
		}
		
		editIvBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		editBtnConfirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(TextUtils.isEmpty(editEtEdit.getText().toString())){
					Toast.makeText(EditInfoActivity.this, "请输入后再提交",
							Toast.LENGTH_SHORT).show();
					return;
				}
				UserInfo userInfo = BaseApplication.getInstance().getUserData().getData().getUserinfo();
				switch (type) {
				case 0:
					userInfo.setName(editEtEdit.getText().toString());
					break;
				case 1:
					userInfo.setHeight(Integer.parseInt(editEtEdit.getText().toString()));
					break;
				case 2:
					userInfo.setWeight(Integer.parseInt(editEtEdit.getText().toString()));
					break;

				default:
					break;
				}
				HttpUtil httpUtil = new HttpUtil();
				httpUtil.postUserInfo(userInfo, new IOnDataListener<UserData>() {
					
					@Override
					public void onDataResult(UserData t) {
						// TODO Auto-generated method stub
						if(t!=null){
							if(t.getCode()==200){
								BaseApplication.getInstance().setUserInfo(t);
							}
						}
						finish();
					}
				});
			}
		});
	}

}
