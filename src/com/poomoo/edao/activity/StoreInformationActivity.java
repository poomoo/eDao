package com.poomoo.edao.activity;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.poomoo.edao.R;
import com.poomoo.edao.model.StoreData;

import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * 
 * @ClassName StoreInformationActivity
 * @Description TODO 商铺详情
 * @author 李苜菲
 * @date 2015年8月3日 下午9:38:57
 */
public class StoreInformationActivity extends BaseActivity implements OnClickListener {
	private TextView textView_store_name, textView_owner_name, textView_distance, textView_address, textView_tel,
			textView_more;
	private ImageView imageView;
	private RatingBar ratingBar_all;

	private StoreData listData;
	// private List<StoreEvaluationData> list;
	// private Gson gson = new Gson();
	// private ProgressDialog progressDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_information);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.navigation_fragment));
		listData = (StoreData) getIntent().getSerializableExtra("data");
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根
		textView_store_name = (TextView) findViewById(R.id.store_information_textView_store_name);
		textView_owner_name = (TextView) findViewById(R.id.store_information_textView_owner);
		textView_distance = (TextView) findViewById(R.id.store_information_textView_distance);
		textView_address = (TextView) findViewById(R.id.store_information_textView_address);
		textView_tel = (TextView) findViewById(R.id.store_information_textView_tel);
		textView_more = (TextView) findViewById(R.id.store_information_textView_more);

		imageView = (ImageView) findViewById(R.id.store_information_imageView_pic);

		ratingBar_all = (RatingBar) findViewById(R.id.store_information_ratingBar);

		textView_store_name.setText(listData.getShopName());
		textView_owner_name.setText(listData.getRealName());
		textView_distance.setText("距离:" + listData.getDistance());
		textView_address.setText(listData.getAddress());
		textView_tel.setText(listData.getTel());
		ratingBar_all.setRating(listData.getAvgScore());
		// 使用ImageLoader加载网络图片
		DisplayImageOptions options = new DisplayImageOptions.Builder()//
				.showImageOnLoading(R.drawable.ic_launcher) // 加载中显示的默认图片
				.showImageOnFail(R.drawable.ic_launcher) // 设置加载失败的默认图片
				.cacheInMemory(true) // 内存缓存
				.cacheOnDisk(true) // sdcard缓存
				.bitmapConfig(Config.RGB_565)// 设置最低配置
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)// 缩放图片
				.build();
		ImageLoader.getInstance().displayImage(listData.getPictures(), imageView, options);

		textView_more.setOnClickListener(this);
		// textView_info.setText(data.getShopName());
		// list = new ArrayList<StoreEvaluationData>();
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.store_information_textView_more:
			Bundle pBundle = new Bundle();
			// pBundle.putSerializable("list", (Serializable) list);
			System.out.println("listData.getAvgScore():"+listData.getAvgScore());
			pBundle.putFloat("score", listData.getAvgScore());
			openActivity(EvaluationListActivity.class, pBundle);
			break;
		}
	}

}
