package com.huayu.bracelet.adapter;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.huayu.bracelet.R;
import com.huayu.bracelet.util.ViewHolder;

public class MyAdapter extends CommonAdapter<String>
{
	/**
	 * 用户选择的图片，存储为图片的完整路径
	 */
	public static List<String> mSelectedImage = new LinkedList<String>();
	/**
	 * 文件夹路径
	 */
//	private String mDirPath;

	public MyAdapter(Context context, List<String> mDatas, int itemLayoutId){
		super(context, mDatas, itemLayoutId);
//		this.mDirPath = dirPath;
	}

	@Override
	public void convert(final ViewHolder helper, final String item){
		//设置no_pic
		helper.setImageResource(R.id.id_item_image, R.drawable.pictures_no);
		//设置no_selected
				helper.setImageResource(R.id.id_item_select,
						R.drawable.picture_unselected);
		//设置图片
		helper.setImageByUrl(R.id.id_item_image, "file://" + item);
		
		final ImageView mImageView = helper.getView(R.id.id_item_image);
		final ImageView mSelect = helper.getView(R.id.id_item_select);
		
		mImageView.setColorFilter(null);
		//设置ImageView的点击事件
		mImageView.setOnClickListener(new OnClickListener(){
			//选择，则将图片变暗，反之则反之
			@Override
			public void onClick(View v){
				// 已经选择过该图片
				if (mSelectedImage.contains( item)){
					mSelectedImage.remove( item);
					mSelect.setImageResource(R.drawable.picture_unselected);
					mImageView.setColorFilter(null);
				} else{
					if(mSelectedImage.size()>=9){
						Toast.makeText(mContext, "最多只能选择9张", Toast.LENGTH_SHORT).show();
					}else{
						mSelectedImage.add( item);
						mSelect.setImageResource(R.drawable.pictures_selected);
						mImageView.setColorFilter(Color.parseColor("#77000000"));
					}
				}
			}
		});
		
		/**
		 * 已经选择过的图片，显示出选择过的效果
		 */
		if (mSelectedImage.contains(item)){
			mSelect.setImageResource(R.drawable.pictures_selected);
			mImageView.setColorFilter(Color.parseColor("#77000000"));
		}
	}
}
