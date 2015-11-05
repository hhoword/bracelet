package com.huayu.bracelet.activity;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huayu.bracelet.BaseApplication;
import com.huayu.bracelet.R;
import com.huayu.bracelet.http.HttpUtil;
import com.huayu.bracelet.vo.UserData;
import com.huayu.bracelet.vo.UserInfo;
import com.huayu.bracelet.vo.UserPic;

public class UserInfoActivity extends PActivity implements OnClickListener{

	private TextView userTvName;
	private TextView userTvSex;
	private TextView userTvHeight;
	private TextView userTvWeight;
	//	private TextView userTvName;
	private ImageView userIvBack;
	private ImageView userIvPic;
	private LinearLayout userLyLogout;
	private LinearLayout UserLayoutPic;
	private LinearLayout userLyName;
	private LinearLayout userLySex;
	private LinearLayout userLyHeight;
	private LinearLayout userLyWeight;
	private ProgressDialog progressDialog;
	private File tempFile;
	private HttpUtil httpUtil;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_user_info);
		userTvName = (TextView)findViewById(R.id.userTvName);
		userTvSex = (TextView)findViewById(R.id.userTvSex);
		userTvHeight = (TextView)findViewById(R.id.userTvHeight);
		userTvWeight = (TextView)findViewById(R.id.userTvWeight);
		userIvBack= (ImageView)findViewById(R.id.userIvBack);
		userIvPic= (ImageView)findViewById(R.id.userIvPic);
		userLyLogout = (LinearLayout)findViewById(R.id.userLyLogout);

		UserLayoutPic = (LinearLayout)findViewById(R.id.UserLayoutPic);
		userLyName = (LinearLayout)findViewById(R.id.userLyName);
		userLyHeight = (LinearLayout)findViewById(R.id.userLyHeight);
		userLySex = (LinearLayout)findViewById(R.id.userLySex);
		userLyWeight = (LinearLayout)findViewById(R.id.userLyWeight);
		//		userTvName = (TextView)findViewById(R.id.userTvName);
		progressDialog = new ProgressDialog(this);
		progressDialog.setCancelable(false);
		userIvBack.setOnClickListener(this);
		userLyLogout.setOnClickListener(this);

		UserLayoutPic.setOnClickListener(this);
		userLyName.setOnClickListener(this);
		userLyHeight.setOnClickListener(this);
		userLySex.setOnClickListener(this);
		userLyWeight.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		UserData info = BaseApplication.getInstance().getUserData();
		if(info!=null){
			userTvName.setText(info.getData().getUserinfo().getName());
			if(info.getData().getUserinfo().getSex()==0){
				userTvSex.setText("女");
			}else{
				userTvSex.setText("男");
			}
			BaseApplication.getImageByloader(this, HttpUtil.url+"/"+info.getData().getUserinfo()
					.getProfile_img_url(), userIvPic, R.drawable.ico_sex_male_100);
			userTvHeight.setText(info.getData().getUserinfo().getHeight()+"");
			userTvWeight.setText(info.getData().getUserinfo().getWeight()+"");
		}
		File file = new File(Environment.getExternalStorageDirectory(),"huayu");
		httpUtil = new HttpUtil();
		if(!file.isDirectory()){
			file.mkdirs();
		}
		tempFile = new File(Environment.getExternalStorageDirectory(),
				"huayu/"+BaseApplication.getPhotoFileName());
	}

	private void logoutDialog(){
		new AlertDialog.Builder(this)
		.setTitle("提示")
		.setMessage("确认退出登录吗？")
		.setNegativeButton("取消", null)
		.setPositiveButton("确认",  new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				BaseApplication.getInstance().setUserInfo(null);
				Intent intent = new Intent(UserInfoActivity.this, LoginActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();
			}
		})
		.create()
		.show();
	}

	private void showPhotoDialog(){
		new AlertDialog.Builder(this)
		.setItems(new String[]{"相册中获取","拍照"}, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int which) {
				// TODO Auto-generated method stub
				Intent intent;
				switch (which) {
				case 0:
					Intent picture = new Intent(Intent.ACTION_PICK,android.provider
							.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					startActivityForResult(picture, 2);
					break;
				case 1:
					intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  
					intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
					startActivityForResult(intent, 1); 
					break;
				default:
					break;
				}
			}
		})
		.create()
		.show();
	}

	private void sexDialog(){
		new AlertDialog.Builder(this)
		.setTitle("性别选择")
		.setItems(new String[]{"女","男"}, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				UserInfo userInfo = BaseApplication.getInstance().getUserData().getData().getUserinfo();
				userInfo.setSex(which);
				if(which==0){
					userTvSex.setText("女");
				}else{
					userTvSex.setText("男");
				}
				httpUtil.postUserInfo(userInfo, new IOnDataListener<UserData>() {

					@Override
					public void onDataResult(UserData t) {
						// TODO Auto-generated method stub
						if(t!=null){
							if(t.getCode() == 200){
								BaseApplication.getInstance().setUserInfo(t);
							}
						}
					}
				});
			}
		})
		.create()
		.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) { 
			//			iamgePaths.add(tempFile.getAbsolutePath());
			//			MyAdapter.mSelectedImage.add(tempFile.getAbsolutePath());
			//			adapter.notifyDataSetChanged();
			String file = "";
			if(requestCode==1){
				file = tempFile.getAbsolutePath();
			}
			if(requestCode==2){
				Uri selectedImage = data.getData();
				String[] filePathColumns={MediaStore.Images.Media.DATA};
				Cursor c = this.getContentResolver().query(selectedImage, filePathColumns, null,null, null);
				c.moveToFirst();
				int columnIndex = c.getColumnIndex(filePathColumns[0]);
				file= c.getString(columnIndex);
				c.close();
			}
			if(!TextUtils.isEmpty(file)){
				progressDialog.setMessage("正在上传图片");
				progressDialog.show();
				httpUtil.postUserPic(BaseApplication.getInstance().getUserData().
						getData().getUserinfo().getId()+"", file, new IOnDataListener<UserPic>() {

					@Override
					public void onDataResult(UserPic t) {
						// TODO Auto-generated method stub
						progressDialog.dismiss();
						if(t!=null){
							if(t.getCode() == 200){
								BaseApplication.getImageByloader(UserInfoActivity.this, HttpUtil.url+"/"+t.getData(), 
										userIvPic, R.drawable.ico_sex_male_100);
								UserData data = BaseApplication.getInstance().getUserData();
								data.getData().getUserinfo().setProfile_img_url(t.getData());
								BaseApplication.getInstance().setUserInfo(data);
								Toast.makeText(UserInfoActivity.this, "上传成功",
										Toast.LENGTH_SHORT).show();
							}
						}else{
							Toast.makeText(UserInfoActivity.this, "连接失败，请重试",
									Toast.LENGTH_SHORT).show();
						}
					}
				});
			}
			tempFile = new File(Environment.getExternalStorageDirectory(),
					BaseApplication.getPhotoFileName());
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(UserInfoActivity.this, EditInfoActivity.class);
		switch (v.getId()) {
		case R.id.userIvBack:
			finish();
			break;
		case R.id.userLyLogout:
			logoutDialog();
			break;

		case R.id.UserLayoutPic:
			showPhotoDialog();
			break;
		case R.id.userLyName:
			intent.putExtra("type", 0);
			startActivity(intent);
			break;
		case R.id.userLyHeight:
			intent.putExtra("type", 1);
			startActivity(intent);
			break;
		case R.id.userLySex:
			sexDialog();
			break;
		case R.id.userLyWeight:
			intent.putExtra("type", 2);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

}
