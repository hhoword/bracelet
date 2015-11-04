package com.huayu.bracelet.adapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.huayu.bracelet.R;

public class PublicImageAdapter extends BaseAdapter {

	private Context context;
	private List<String> imageUrls;

	public PublicImageAdapter(Context context , List<String> imageUrls) {
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

	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		View view = View.inflate(context, R.layout.friend_grid_item, null);
		ImageView imageView = (ImageView) view.findViewById(R.id.friendIvPhoto);
		try {
			imageView.setImageBitmap(getSmallImage(imageUrls.get(position)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			imageView.setImageResource(R.drawable.pictures_no);
		}
		return view;
	}
	
	public static Bitmap getSmallImage(final String path)
			throws FileNotFoundException {
		FileInputStream fis;
		fis = new FileInputStream(new File(path));
		BitmapDrawable drawable = new BitmapDrawable(fis);
		Bitmap bitmap = drawable.getBitmap();
		// 利用Bitmap对象创建缩略图
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, 100, 100);
		return bitmap;
	}

}