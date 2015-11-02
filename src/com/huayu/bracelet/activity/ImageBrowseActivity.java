package com.huayu.bracelet.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.GridView;
import android.widget.Toast;

import com.huayu.bracelet.R;
import com.huayu.bracelet.adapter.MyAdapter;


public class ImageBrowseActivity extends PActivity{

	private ProgressDialog mProgressDialog;

	/**
	 * 存储文件夹中的图片数量
	 */
	private int mPicsSize;
	/**
	 * 所有的图片
	 */
	private List<String> mImgs;

	private GridView mGirdView;
	private MyAdapter mAdapter;
	/**
	 * 临时的辅助类，用于防止同一个文件夹的多次扫描
	 */
	//	private HashSet<String> mDirPaths = new HashSet<String>();

	/**
	 * 扫描拿到所有的图片文件夹
	 */
	//	private List<ImageFloder> mImageFloders = new ArrayList<ImageFloder>();

	//	private RelativeLayout mBottomLy;
	//
	//	private TextView mChooseDir;
	//	private TextView mImageCount;
	int totalCount = 0;

	//	private int mScreenHeight;

	private Handler mHandler = new Handler()
	{
		public void handleMessage(android.os.Message msg)
		{
			mProgressDialog.dismiss();
			// 为View绑定数据
			data2View();
		}
	};


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_image_browsw);
		DisplayMetrics outMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
		//		mScreenHeight = outMetrics.heightPixels;
		mImgs = new ArrayList<String>();
		initView();
		getImages();
	}

	/**
	 * 初始化View
	 */
	private void initView()
	{
		mGirdView = (GridView) findViewById(R.id.id_gridView);

	}



	/**
	 * 为View绑定数据
	 */
	private void data2View(){
		if (mImgs.size()<=0){
			Toast.makeText(getApplicationContext(), "图片没扫描到",
					Toast.LENGTH_SHORT).show();
			return;
		}
		/**
		 * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
		 */
		mAdapter = new MyAdapter(getApplicationContext(), mImgs,
				R.layout.grid_item);
		mGirdView.setAdapter(mAdapter);
	};

	private void getImages(){
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)){
			Toast.makeText(this, "暂无外部存储", Toast.LENGTH_SHORT).show();
			return;
		}
		// 显示进度条
		mProgressDialog = ProgressDialog.show(this, null, "正在加载...");
		new Thread(new Runnable(){
			@Override
			public void run(){
				String firstImage = null;
				Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				ContentResolver mContentResolver = ImageBrowseActivity.this.getContentResolver();
				// 只查询jpeg和png的图片
				Cursor mCursor = mContentResolver.query(mImageUri, null,
						MediaStore.Images.Media.MIME_TYPE + "=? or "
								+ MediaStore.Images.Media.MIME_TYPE + "=?",
								new String[] { "image/jpeg", "image/png" },
								MediaStore.Images.Media.DATE_MODIFIED);

				Log.e("TAG", mCursor.getCount() + "");
				while (mCursor.moveToNext()){
					mImgs.add(mCursor.getString(1));// 将图片路径添加到list中  
				}
				mCursor.close();
				// 通知Handler扫描图片完成
				mHandler.sendEmptyMessage(0x110);
			}
		}).start();

	}


}
