package com.huayu.bracelet.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap.Config;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.huayu.bracelet.R;
import com.huayu.bracelet.http.HttpUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class NoScrollGridAdapter extends BaseAdapter {

	private Context context;
	private List<String> imageUrls;
	
	public NoScrollGridAdapter(Context context , List<String> imageUrls) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.imageUrls = imageUrls;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return imageUrls.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		View view = View.inflate(context, R.layout.friend_grid_item, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.friendIvPhoto);
        DisplayImageOptions options = new DisplayImageOptions.Builder()//
		        .showImageForEmptyUri(R.drawable.pictures_no)	// 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.pictures_no)		// 设置图片加载或解码过程中发生错误显示的图片	
                .cacheInMemory(true)//
                .cacheOnDisk(true)//
                .bitmapConfig(Config.RGB_565)//
                .build();
        ImageLoader.getInstance().displayImage(HttpUtil.url+"/"+
                imageUrls.get(position),imageView, options);
        return view;
	}

}
