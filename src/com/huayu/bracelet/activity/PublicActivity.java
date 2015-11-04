package com.huayu.bracelet.activity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.huayu.bracelet.BaseApplication;
import com.huayu.bracelet.R;
import com.huayu.bracelet.adapter.MyAdapter;
import com.huayu.bracelet.adapter.NoScrollGridAdapter;
import com.huayu.bracelet.http.HttpUtil;
import com.huayu.bracelet.view.NoScrollGridView;
import com.huayu.bracelet.vo.ZoonInfo;

public class PublicActivity extends PActivity implements OnClickListener{

	private ImageView pubIvPhoto;
	private TextView pubTvPublish;
	private EditText pubEtTalk;
	private NoScrollGridView pubGVImages;
	private NoScrollGridAdapter adapter ;
	private List<String> iamgePaths;
	private List<String> showImagePaths;
	private File tempFile = new File(Environment.getExternalStorageDirectory(),
			"huayu/"+getPhotoFileName());

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_publish);
		pubIvPhoto = (ImageView)findViewById(R.id.pubIvPhoto);
		pubTvPublish = (TextView)findViewById(R.id.pubTvPublish);
		pubEtTalk = (EditText)findViewById(R.id.pubEtTalk);
		pubGVImages = (NoScrollGridView)findViewById(R.id.pubGVImages);
		pubIvPhoto.setOnClickListener(this);
		pubTvPublish.setOnClickListener(this);
		iamgePaths = new ArrayList<String>();
		showImagePaths = new ArrayList<String>();
		adapter =  new NoScrollGridAdapter(this,showImagePaths);
		pubGVImages.setAdapter(adapter);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(MyAdapter.mSelectedImage!=null&&MyAdapter.mSelectedImage.size()>0){
			iamgePaths.clear();
			showImagePaths.clear();
			iamgePaths.addAll(iamgePaths);
			for(String path : iamgePaths){
				showImagePaths.add("file://" +path);
			}
			adapter.notifyDataSetChanged();
		}
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
					intent = new Intent(PublicActivity.this, ImageBrowseActivity.class);
					startActivity(intent);
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

	// 使用系统当前日期加以调整作为照片的名称
	public static String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) { 
			iamgePaths.add(tempFile.getAbsolutePath());
			showImagePaths.add("file://" +tempFile.getAbsolutePath());
			adapter.notifyDataSetChanged();
			tempFile = new File(Environment.getExternalStorageDirectory(),getPhotoFileName());
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.pubIvPhoto:
			showPhotoDialog();
			break;
		case R.id.pubTvPublish:
			if(pubEtTalk.getText().toString().length()>0){
				HttpUtil httpUtil = new HttpUtil();
				httpUtil.postFriendTalk(BaseApplication.getInstance().getUserData().
						getData().getUserinfo().getId()+"",
						pubEtTalk.getText().toString(),
						iamgePaths, new IOnDataListener<ZoonInfo>() {
							
							@Override
							public void onDataResult(ZoonInfo t) {
								// TODO Auto-generated method stub
								if(t!=null){
									if(t.getCode()==200){
										Intent intent = new Intent(PublicActivity.this, MainActivity.class);
										intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
										startActivity(intent);
									}
								}
							}
						});
			}
			break;
		default:
			break;
		}
	}

}
