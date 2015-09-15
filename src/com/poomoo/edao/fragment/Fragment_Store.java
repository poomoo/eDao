package com.poomoo.edao.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap.Config;
import android.os.Build;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.poomoo.edao.R;
import com.poomoo.edao.activity.MapActivity;
import com.poomoo.edao.activity.ShopListActivity;
import com.poomoo.edao.adapter.Fragment_Store_GridViewAdapter;
import com.poomoo.edao.config.eDaoClientConfig;
import com.poomoo.edao.model.ResponseData;
import com.poomoo.edao.util.HttpCallbackListener;
import com.poomoo.edao.util.HttpUtil;
import com.poomoo.edao.util.Utity;
import com.poomoo.edao.widget.PhotoView.ImagePagerActivity;
import com.poomoo.edao.widget.PhotoView.MyGestureListener;

public class Fragment_Store extends Fragment implements OnItemClickListener, OnClickListener {
	private TextView textView_indicator;
	private EditText editText_keywords;
	private LinearLayout layout_position, layout_map;
	private ViewFlipper flipper;
	private GridView gridView;
	private Fragment_Store_GridViewAdapter gridViewAdapter;

	private final int[] list_image = { R.drawable.ic_store_jewelry, R.drawable.ic_store_hotel, R.drawable.ic_store_food,
			R.drawable.ic_store_clothing, R.drawable.ic_store_super_market, R.drawable.ic_store_travel,
			R.drawable.ic_store_beauty, R.drawable.ic_store_advertisement, R.drawable.ic_store_electronic,
			R.drawable.ic_store_bags, R.drawable.ic_store_wine, R.drawable.ic_store_relaxing, R.drawable.ic_store_car,
			R.drawable.ic_store_education, R.drawable.ic_store_agricultural, R.drawable.ic_store_medicine,
			R.drawable.ic_store_traffic, R.drawable.ic_store_office, R.drawable.ic_store_housing,
			R.drawable.ic_store_machine };
	private Gson gson = new Gson();
	private ArrayList<String> imageUrlsList = null;
	private GestureDetector mGestureDetector;
	private int advCount = 0;// 广告数量
	public boolean hasPics = false;// 广告图片加载标志
	public static Fragment_Store instance = null;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onActivityCreated(savedInstanceState);
		// 实现沉浸式状态栏效果
		setImmerseLayout(getView().findViewById(R.id.fragment_store_layout));
		instance = this;
		init();
		// 查询广告
		getAdvData();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		return inflater.inflate(R.layout.fragment_store, container, false);
	}

	private void init() {
		// TODO 自动生成的方法存根
		textView_indicator = (TextView) getView().findViewById(R.id.fragment_store_indicator);
		flipper = (ViewFlipper) getView().findViewById(R.id.fragment_store_viewFlipper);
		layout_position = (LinearLayout) getView().findViewById(R.id.fragment_store_layout_position);
		layout_map = (LinearLayout) getView().findViewById(R.id.fragment_store_layout_map);
		editText_keywords = (EditText) getView().findViewById(R.id.fragment_store_editText_keywords);
		gridView = (GridView) getView().findViewById(R.id.fragment_store_gridView);

		gridViewAdapter = new Fragment_Store_GridViewAdapter(getActivity(), eDaoClientConfig.store_class, list_image,
				gridView);
		gridView.setAdapter(gridViewAdapter);
		gridView.setOnItemClickListener(this);
		layout_map.setOnClickListener(this);
	}

	public void getAdvData() {
		Map<String, String> data = new HashMap<String, String>();
		data.put("bizName", "70000");
		data.put("method", "70002");
		data.put("type", "1");
		data.put("position", "2");// 1-首页 2-商铺
		HttpUtil.SendPostRequest(gson.toJson(data), eDaoClientConfig.url, new HttpCallbackListener() {

			@Override
			public void onFinish(final ResponseData responseData) {
				// TODO 自动生成的方法存根
				if (getActivity() != null)
					getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							// TODO 自动生成的方法存根
							if (responseData.getRsCode() == 1) {
								try {
									JSONObject result = new JSONObject(responseData.getJsonData().toString());
									JSONArray array = result.getJSONArray("records");
									advCount = array.length();
									imageUrlsList = new ArrayList<String>();
									flipper.removeAllViews();
									for (int i = 0; i < advCount; i++) {
										imageUrlsList.add(array.getJSONObject(i).getString("picture"));
										flipper.addView(addImageById(imageUrlsList.get(i)));
									}
									CharSequence text = getString(R.string.viewpager_indicator, 1, advCount);
									textView_indicator.setText(text);
									textView_indicator.setVisibility(View.VISIBLE);
									setFlipper();
									hasPics = true;
								} catch (JSONException e) {
									// TODO 自动生成的 catch 块
									e.printStackTrace();
								}
							} else {
								Utity.showToast(getActivity().getApplicationContext(),
										"查询广告失败" + responseData.getMsg());
							}
						}

					});
			}

			@Override
			public void onError(Exception e) {
				// TODO 自动生成的方法存根
				if (getActivity() != null)
					getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							// TODO 自动生成的方法存根
							Utity.showToast(getActivity().getApplicationContext(), eDaoClientConfig.checkNet);
						}

					});
			}
		});
	}

	private void setFlipper() {
		// TODO 自动生成的方法存根
		//
		mGestureDetector = new GestureDetector(getActivity(),
				new MyGestureListener(getActivity(), flipper, textView_indicator, advCount));

		flipper.setInAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in));
		flipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out));
		flipper.setAutoStart(true); // 设置自动播放功能（点击事件，前自动播放）
		flipper.setFlipInterval(eDaoClientConfig.advTime);
		if (flipper.isAutoStart() && !flipper.isFlipping()) {
			flipper.startFlipping();
		}

		flipper.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.getParent().requestDisallowInterceptTouchEvent(true);
				return mGestureDetector.onTouchEvent(event);
			}
		});

		flipper.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int position = flipper.getDisplayedChild();
				// imageBrower(position, imageUrlsList);
			}
		});
		flipper.getInAnimation().setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO 自动生成的方法存根
				CharSequence text = getString(R.string.viewpager_indicator, flipper.getDisplayedChild() + 1, advCount);
				textView_indicator.setText(text);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO 自动生成的方法存根

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO 自动生成的方法存根

			}
		});
	}

	protected void imageBrower(int position, ArrayList<String> urls2) {
		Intent intent = new Intent(getActivity(), ImagePagerActivity.class);
		// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		startActivity(intent);
	}

	public View addImageById(String url) {
		final ImageView iv = new ImageView(getActivity());
		// 使用ImageLoader加载网络图片
		DisplayImageOptions options = new DisplayImageOptions.Builder()//
				.showImageOnLoading(R.drawable.ic_launcher) // 加载中显示的默认图片
				.showImageOnFail(R.drawable.ic_launcher) // 设置加载失败的默认图片
				.cacheInMemory(true) // 内存缓存
				.cacheOnDisk(true) // sdcard缓存
				.bitmapConfig(Config.RGB_565)// 设置最低配置
				.build();//
		ImageLoader.getInstance().displayImage(url, iv, options);
		iv.setScaleType(ScaleType.FIT_XY);
		return iv;
	}

	protected void setImmerseLayout(View view) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			Window window = getActivity().getWindow();
			window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

			int statusBarHeight = getStatusBarHeight(getActivity().getBaseContext());
			view.setPadding(0, statusBarHeight, 0, 0);
		}
	}

	protected int getStatusBarHeight(Context context) {
		int result = 0;
		int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = context.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO 自动生成的方法存根
		Intent intent = new Intent(getActivity(), ShopListActivity.class);
		intent.putExtra("fromFlag", "store");
		intent.putExtra("categoryId", Integer.toString(arg2 + 1));
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.fragment_store_layout_map:
			startActivity(new Intent(getActivity(), MapActivity.class));
			break;
		}
	}
}
