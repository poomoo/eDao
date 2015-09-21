package com.poomoo.edao.adapter;

import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.poomoo.edao.R;
import com.poomoo.edao.model.StoreData;

import android.content.Context;
import android.graphics.Bitmap.Config;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class Shop_List_ListViewAdapter extends BaseAdapter {

	private List<StoreData> list;
	private LayoutInflater inflater;

	public Shop_List_ListViewAdapter(Context context, List<StoreData> list) {
		super();
		this.list = list;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO 自动生成的方法存根
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO 自动生成的方法存根
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO 自动生成的方法存根
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO 自动生成的方法存根
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_listview_shop_list, parent, false);
			holder = new ViewHolder();
			holder.imageView = (ImageView) convertView.findViewById(R.id.shop_list_item_imageView);
			holder.textView_shopname = (TextView) convertView.findViewById(R.id.shop_list_item_textView_name);
			holder.textView_owner = (TextView) convertView.findViewById(R.id.shop_list_item_textView_owner);
			holder.textView_tel = (TextView) convertView.findViewById(R.id.shop_list_item_textView_tel);
			holder.textView_distance = (TextView) convertView.findViewById(R.id.shop_list_item_textView_distance);
			holder.bar = (RatingBar) convertView.findViewById(R.id.shop_list_item_ratingBar);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// 使用ImageLoader加载网络图片
		DisplayImageOptions options = new DisplayImageOptions.Builder()//
				.showImageOnLoading(R.drawable.ic_launcher) // 加载中显示的默认图片
				.showImageOnFail(R.drawable.ic_launcher) // 设置加载失败的默认图片
				.cacheInMemory(true) // 内存缓存
				.cacheOnDisk(true) // sdcard缓存
				.bitmapConfig(Config.RGB_565)// 设置最低配置
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)// 缩放图片
				.build();
		ImageLoader.getInstance().displayImage(list.get(position).getPictures(), holder.imageView, options);

		holder.textView_shopname.setText(list.get(position).getShopName());
		holder.textView_owner.setText(list.get(position).getRealName());
		holder.textView_tel.setText(list.get(position).getTel());
		holder.textView_distance.setText("距离:　　" + list.get(position).getDistance());
		holder.bar.setRating(list.get(position).getAvgScore());
		return convertView;
	}

	private class ViewHolder {
		private ImageView imageView;
		private TextView textView_shopname, textView_owner, textView_tel, textView_distance;
		private RatingBar bar;
	}

}
