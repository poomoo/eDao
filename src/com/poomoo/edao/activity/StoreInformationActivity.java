package com.poomoo.edao.activity;

import java.util.ArrayList;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.poomoo.edao.R;
import com.poomoo.edao.model.StoreData;
import com.poomoo.edao.util.Utity;
import com.poomoo.edao.widget.photoview.ImagePagerActivity;

import android.content.Intent;
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
	private ArrayList<String> imageUrls = new ArrayList<>();

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
		textView_owner_name.setText(Utity.addStarByNameWithLastOne(listData.getRealName()));
		textView_distance.setText("距离:" + listData.getDistance());
		textView_address.setText(listData.getAddress());
		textView_tel.setText(listData.getTel());
		ratingBar_all.setRating(listData.getAvgScore());
		// 使用ImageLoader加载网络图片
		// DisplayImageOptions options = new DisplayImageOptions.Builder()//
		// .showImageOnLoading(R.drawable.ic_launcher) // 加载中显示的默认图片
		// .showImageOnFail(R.drawable.ic_launcher) // 设置加载失败的默认图片
		// .cacheInMemory(true) // 内存缓存
		// .cacheOnDisk(true) // sdcard缓存
		// .bitmapConfig(Config.RGB_565)// 设置最低配置
		// .imageScaleType(ImageScaleType.EXACTLY)// 缩放图片
		// .build();
		ImageLoader.getInstance().displayImage(listData.getPictures(), imageView);

		textView_more.setOnClickListener(this);
		imageView.setOnClickListener(this);
		// textView_info.setText(data.getShopName());
		// list = new ArrayList<StoreEvaluationData>();
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.store_information_imageView_pic:
			// Bundle pBundle = new Bundle();
			// pBundle.putString("path", listData.getPictures());
			// openActivity(CatBigPictureActivity.class, pBundle);
			imageUrls.add(listData.getPictures());
			imageBrower(0, imageUrls);
			break;
		case R.id.store_information_textView_more:
			Bundle pBundle2 = new Bundle();
			// pBundle.putSerializable("list", (Serializable) list);
			System.out.println("listData.getAvgScore():" + listData.getAvgScore());
			pBundle2.putFloat("score", listData.getAvgScore());
			pBundle2.putString("shopId", listData.getShopId());
			openActivity(EvaluationListActivity.class, pBundle2);
			break;
		}
	}

	/**
	 * 打开图片查看器
	 * 
	 * @param position
	 * @param urls2
	 */
	protected void imageBrower(int position, ArrayList<String> urls2) {
		Intent intent = new Intent(this, ImagePagerActivity.class);
		// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		this.startActivity(intent);
	}
}
