package com.huayu.bracelet.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap.Config;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huayu.bracelet.R;
import com.huayu.bracelet.activity.ImagePagerActivity;
import com.huayu.bracelet.view.NoScrollGridView;
import com.huayu.bracelet.vo.ListUserZoonInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

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
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.friend_item, null);
            holder.iv_avatar = (ImageView) convertView
                    .findViewById(R.id.iv_avatar);
            holder.tv_title = (TextView) convertView
                    .findViewById(R.id.tv_title);
            holder.tv_content = (TextView) convertView
                    .findViewById(R.id.tv_content);
            holder.gridview = (NoScrollGridView) convertView
                    .findViewById(R.id.gridview);
            holder.tvDate= (TextView) convertView
            		.findViewById(R.id.tvDate);
            holder.tvLevel = (TextView) convertView
                    .findViewById(R.id.tvLevel);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ListUserZoonInfo userZoonInfo = items.get(position);
        holder.tv_title.setText(userZoonInfo.getZooninfo().getUsername());
        holder.tv_content.setText(userZoonInfo.getZooninfo().getText());
        holder.tvDate.setText(userZoonInfo.getZooninfo().getDatatime());
        holder.tvLevel.setText(userZoonInfo.getZooninfo().getUser_level()+"");
        // 使用ImageLoader加载网络图片
        ImageLoader.getInstance().displayImage(userZoonInfo.getZooninfo().getUser_img_url(),
                holder.iv_avatar);
        final List<String> imageUrls = userZoonInfo.getZooninfo().getImg_url();
        if (imageUrls == null || imageUrls.size() == 0) { // 没有图片资源就隐藏GridView
            holder.gridview.setVisibility(View.GONE);
        } else {
            holder.gridview.setAdapter(new NoScrollGridAdapter(mContext,
                    imageUrls));
        }
        // 点击回帖九宫格，查看大图
        holder.gridview.setOnItemClickListener(new OnItemClickListener() {
 
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                // TODO Auto-generated method stub
                imageBrower(position, imageUrls);
            }
        });
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
    class ViewHolder {
        private ImageView iv_avatar;
        private TextView tv_title;
        private TextView tv_content;
        private TextView tvLevel;
        private TextView tvDate;
        private NoScrollGridView gridview;
    }
}
