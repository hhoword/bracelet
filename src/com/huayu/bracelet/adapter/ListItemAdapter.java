package com.huayu.bracelet.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huayu.bracelet.BaseApplication;
import com.huayu.bracelet.R;
import com.huayu.bracelet.activity.ImagePagerActivity;
import com.huayu.bracelet.http.HttpUtil;
import com.huayu.bracelet.view.NoScrollGridView;
import com.huayu.bracelet.vo.ListUserZoonInfo;
import com.huayu.bracelet.vo.UserData;

/**
 * 首页ListView的数据适配器
 * 
 * @author Administrator
 * 
 */
public class ListItemAdapter extends BaseAdapter {

	private Context mContext;
	private List<ListUserZoonInfo> items;

	public ListItemAdapter(Context ctx,List<ListUserZoonInfo> items) {
		this.mContext = ctx;
		this.items = items;
	}

	@Override
	public int getCount() {
		return items == null ? 0 : items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//		ViewHolder holder;

		if(position == 0 ){
			convertView = View.inflate(mContext, R.layout.item_user_info, null);
			ImageView friendIvUser= (ImageView)convertView.findViewById(R.id.friendIvUser);
			TextView friendTvName= (TextView)convertView.findViewById(R.id.friendTvName);
			TextView friendTvFans = (TextView)convertView.findViewById(R.id.friendTvFans);
			TextView friendTvConcern= (TextView)convertView.findViewById(R.id.friendTvConcern);
			TextView friendTvLevel = (TextView)convertView.findViewById(R.id.friendTvLevel);
			TextView friendTvHNum= (TextView)convertView.findViewById(R.id.friendTvHNum);
			UserData info = BaseApplication.getInstance().getUserData();
			if(info!=null){
				friendTvName.setText(info.getData().getUserinfo().getName());
				friendTvFans.setText(info.getData().getFollowerCount()+"");
				friendTvConcern.setText(info.getData().getConcernedCount()+"");
				friendTvLevel.setText(info.getData().getUserinfo().getLevel()+"");
				//					friendTvHNum.setText(info.getData().getUserinfo().get);
				BaseApplication.getImageByloader(mContext, HttpUtil.url+"/"+info.getData().getUserinfo().getProfile_img_url(),
						friendIvUser, R.drawable.ico_sex_male_100);
			}
		}else{
			//			if (convertView == null) {
			//				holder = new ViewHolder();
			//				convertView = View.inflate(mContext, R.layout.friend_item, null);
			//				holder.iv_avatar = (ImageView) convertView
			//						.findViewById(R.id.iv_avatar);
			//				holder.tv_title = (TextView) convertView
			//						.findViewById(R.id.tv_title);
			//				holder.tv_content = (TextView) convertView
			//						.findViewById(R.id.tv_content);
			//				holder.gridview = (NoScrollGridView) convertView
			//						.findViewById(R.id.gridview);
			//				holder.tvDate= (TextView) convertView
			//						.findViewById(R.id.tvDate);
			//				holder.tvLevel = (TextView) convertView
			//						.findViewById(R.id.tvLevel);
			//				convertView.setTag(holder);
			//			} else {
			//				holder = (ViewHolder) convertView.getTag();
			//			}
			convertView = View.inflate(mContext, R.layout.friend_item, null);
			ImageView iv_avatar=(ImageView) convertView
					.findViewById(R.id.iv_avatar);
			TextView tv_title=(TextView) convertView
					.findViewById(R.id.tv_title);
			TextView tv_content= (TextView) convertView
					.findViewById(R.id.tv_content);
			TextView tvLevel= (TextView) convertView
					.findViewById(R.id.tvLevel);
			TextView tvDate= (TextView) convertView
					.findViewById(R.id.tvDate);
			NoScrollGridView gridview= (NoScrollGridView) convertView
					.findViewById(R.id.gridview);
			ListUserZoonInfo userZoonInfo = items.get(position);
			tv_title.setText(userZoonInfo.getZooninfo().getUsername());
			tv_content.setText(userZoonInfo.getZooninfo().getText());
			tvDate.setText(userZoonInfo.getZooninfo().getDatatime());
			tvLevel.setText(userZoonInfo.getZooninfo().getUser_level()+"");
			// 使用ImageLoader加载网络图片
			BaseApplication.getImageByloader(mContext, HttpUtil.url+"/"+
					userZoonInfo.getZooninfo().getUser_img_url(),
					iv_avatar, R.drawable.ico_header_default);
			final List<String> imageUrls = userZoonInfo.getZooninfo().getImg_url();
			if (imageUrls == null || imageUrls.size() == 0) { // 没有图片资源就隐藏GridView
				gridview.setVisibility(View.GONE);
			} else {
				gridview.setAdapter(new NoScrollGridAdapter(mContext,
						imageUrls));
			}
			// 点击回帖九宫格，查看大图
			gridview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					imageBrower(position, imageUrls);
				}
			});}
		return convertView;
	}

	/**
	 * 打开图片查看器
	 * 
	 * @param position
	 * @param urls2
	 */
	protected void imageBrower(int position, List<String> urls2) {
		Intent intent = new Intent(mContext, ImagePagerActivity.class);
		// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, (ArrayList<String>)urls2);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		mContext.startActivity(intent);
	}

	/**
	 * listview组件复用，防止“卡顿”
	 * 
	 * @author Administrator
	 * 
	 */
	//	class ViewHolder {
	//		private ImageView iv_avatar;
	//		private TextView tv_title;
	//		private TextView tv_content;
	//		private TextView tvLevel;
	//		private TextView tvDate;
	//		private NoScrollGridView gridview;
	//	}
}
