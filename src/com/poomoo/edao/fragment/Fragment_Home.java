package com.poomoo.edao.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.poomoo.edao.R;
import com.poomoo.edao.activity.CooperationActivity;
import com.poomoo.edao.activity.CreditManageActivity;
import com.poomoo.edao.activity.DealDetailActivity;
import com.poomoo.edao.activity.LoveFundActivity;
import com.poomoo.edao.activity.MapActivity;
import com.poomoo.edao.activity.MywalletActivity;
import com.poomoo.edao.activity.NavigationActivity;
import com.poomoo.edao.activity.PurchaseAndGetDetailActivity;
import com.poomoo.edao.activity.RebateActivity;
import com.poomoo.edao.activity.TransferActivity1;
import com.poomoo.edao.activity.WebViewActivity;
import com.poomoo.edao.adapter.Fragment_Home_GridViewAdapter;
import com.poomoo.edao.application.eDaoClientApplication;
import com.poomoo.edao.config.eDaoClientConfig;
import com.poomoo.edao.model.ResponseData;
import com.poomoo.edao.util.HttpCallbackListener;
import com.poomoo.edao.util.HttpUtil;
import com.poomoo.edao.util.Utity;
import com.poomoo.edao.widget.SideBar;
import com.poomoo.edao.widget.PhotoView.ImagePagerActivity;
import com.poomoo.edao.widget.PhotoView.MyGestureListener;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap.Config;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.ViewFlipper;

/**
 * 
 * @ClassName Fragment_Home
 * @Description TODO 首页
 * @author 李苜菲
 * @date 2015-8-4 下午3:59:10
 */
public class Fragment_Home extends Fragment implements OnClickListener, OnItemClickListener {
	private TextView textView_inform, textView_ecoin, textView_goldcoin, textView_point, textView_indicator;
	private LinearLayout layout_user, layout_map;
	private RadioButton radioButton_shop;
	private GridView gridView;
	private SideBar sidebar;
	private ViewFlipper flipper;
	private GestureDetector mGestureDetector;

	private Fragment_Home_GridViewAdapter gridViewAdapter;
	private static final String[] list_name = { "普惠全民", "消费领取", "我的钱包", "交易明细", "信用管理", "转账支付", "合作加盟", "爱心基金",
			"乐意道商城" };
	private static final int[] list_image = { R.drawable.ic_rebate, R.drawable.ic_get_detail, R.drawable.ic_my_wallet,
			R.drawable.ic_purchase_history, R.drawable.ic_credit_manage, R.drawable.ic_transfer_payment,
			R.drawable.ic_cooperation, R.drawable.ic_love_fund, R.drawable.ic_shop };

	private static final Class[] outIntent = { RebateActivity.class, PurchaseAndGetDetailActivity.class,
			MywalletActivity.class, DealDetailActivity.class, CreditManageActivity.class, TransferActivity1.class,
			CooperationActivity.class, LoveFundActivity.class, Fragment_Store.class };

	private eDaoClientApplication application = null;
	private Gson gson = new Gson();
	private ArrayList<String> imageUrlsList = null;
	private static final int[] pics = { R.drawable.a01, R.drawable.a02, R.drawable.a03, R.drawable.a04 };
	private int advCount = 0;// 广告数量
	public static Handler handler = null;
	private NavigationActivity mActivity = null;
	public boolean hasPics = false, inform = false;// 广告图片加载标志,通知标志

	public static Fragment_Home instance = null;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onActivityCreated(savedInstanceState);
		// 实现沉浸式状态栏效果
		setImmerseLayout(getView().findViewById(R.id.fragment_home_layout));
		application = (eDaoClientApplication) getActivity().getApplication();
		instance = this;
		init();
		// 查询通知
		getInformData();
		// 查询广告
		getAdvData();
		handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO 自动生成的方法存根
				super.handleMessage(msg);
				if (msg.what == eDaoClientConfig.freshFlag) {
					setUserInfo();
				}
			}

		};
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		return inflater.inflate(R.layout.fragment_home, container, false);
	}

	@Override
	public void onStart() {
		// TODO 自动生成的方法存根
		super.onStart();
		setUserInfo();
	}

	private void init() {
		// TODO 自动生成的方法存根
		flipper = (ViewFlipper) getView().findViewById(R.id.fragment_home_viewFlipper);
		textView_inform = (TextView) getView().findViewById(R.id.main_textView_inform);
		textView_ecoin = (TextView) getView().findViewById(R.id.main_textView_ecoin);
		textView_goldcoin = (TextView) getView().findViewById(R.id.main_textView_goldcoin);
		textView_point = (TextView) getView().findViewById(R.id.main_textView_point);
		layout_user = (LinearLayout) getView().findViewById(R.id.main_layout_user);
		layout_map = (LinearLayout) getView().findViewById(R.id.main_layout_map);
		textView_indicator = (TextView) getView().findViewById(R.id.fragment_home_indicator);
		gridView = (GridView) getView().findViewById(R.id.main_gridView);

		gridViewAdapter = new Fragment_Home_GridViewAdapter(getActivity(), list_name, list_image, gridView);
		gridView.setAdapter(gridViewAdapter);
		gridView.setOnItemClickListener(this);

		sidebar = NavigationActivity.sideBar;
		radioButton_shop = (RadioButton) NavigationActivity.radioButton_shop;

		layout_user.setOnClickListener(this);
		layout_map.setOnClickListener(this);
		textView_inform.setOnClickListener(this);
	}

	private void setFlipper() {
		// TODO 自动生成的方法存根
		flipper.setInAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in));
		flipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out));
		flipper.setAutoStart(true); // 设置自动播放功能（点击事件，前自动播放）
		flipper.setFlipInterval(eDaoClientConfig.advTime);
		mGestureDetector = new GestureDetector(getActivity(),
				new MyGestureListener(getActivity(), flipper, textView_indicator, advCount));
		if (flipper.isAutoStart() && !flipper.isFlipping()) {
			flipper.startFlipping();
		}

		flipper.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// System.out.println("flipper:" + flipper.getInAnimation());
				v.getParent().requestDisallowInterceptTouchEvent(true);// 防止与父类的onTouch冲突
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
				System.out.println("onAnimationStart");
				CharSequence text = getString(R.string.viewpager_indicator, flipper.getDisplayedChild() + 1, advCount);
				textView_indicator.setText(text);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO 自动生成的方法存根
				System.out.println("onAnimationRepeat");
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO 自动生成的方法存根
				System.out.println("onAnimationEnd");
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.main_textView_inform:
			Intent intent = new Intent(getActivity(), WebViewActivity.class);
			intent.putExtra("from", "inform");
			startActivity(intent);
			break;
		case R.id.main_layout_user:
			sidebar.toggle();
			break;
		case R.id.main_layout_map:
			startActivity(new Intent(getActivity(), MapActivity.class));
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO 自动生成的方法存根
		showActivity(arg2);
	}

	private void showActivity(int arg2) {
		// TODO 自动生成的方法存根
		if (arg2 == 4) {
			Utity.showToast(getActivity().getApplicationContext(), eDaoClientConfig.notDevelop);
			return;
		} else if (arg2 == 6) {
			if (application.getJoinStatus().equals("1"))
				Utity.showToast(getActivity().getApplication(), "已经加盟");
			else
				startActivity(new Intent(getActivity(), outIntent[arg2]));
		} else if (arg2 == 8) {
			if (NavigationActivity.fragment_Store == null)
				NavigationActivity.fragment_Store = new Fragment_Store();
			NavigationActivity.instance.switchFragment(NavigationActivity.fragment_Store);
			NavigationActivity.curFragment = NavigationActivity.fragment_Store;
			radioButton_shop.setChecked(true);
		} else
			startActivity(new Intent(getActivity(), outIntent[arg2]));
	}

	public void getInformData() {
		Map<String, String> data = new HashMap<String, String>();
		data.put("bizName", "70000");
		data.put("method", "70003");
		HttpUtil.SendPostRequest(gson.toJson(data), eDaoClientConfig.url, new HttpCallbackListener() {

			@Override
			public void onFinish(final ResponseData responseData) {
				// TODO 自动生成的方法存根
				System.out.println("getInformData" + getActivity());
				if (getActivity() != null)
					getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							// TODO 自动生成的方法存根
							if (responseData.getRsCode() == 1) {
								System.out.println("getRsCode");
								try {
									JSONObject result = new JSONObject(responseData.getJsonData().toString());
									textView_inform.setText(result.getString("title"));
									inform = true;
								} catch (JSONException e) {
									// TODO 自动生成的 catch 块
									e.printStackTrace();
								}
							} else {
								Utity.showToast(getActivity().getApplicationContext(),
										"查询通知失败" + responseData.getMsg());
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
							// Utity.showToast(getActivity().getApplicationContext(),
							// eDaoClientConfig.checkNet);
						}

					});
			}
		});
	}

	public void getAdvData() {
		Map<String, String> data = new HashMap<String, String>();
		data.put("bizName", "70000");
		data.put("method", "70002");
		data.put("type", "1");
		data.put("position", "1");// 1-首页 2-商铺
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
									System.out.println("advCount:" + advCount);
									imageUrlsList = new ArrayList<String>();
									flipper.removeAllViews();
									for (int i = 0; i < advCount; i++) {
										imageUrlsList.add(array.getJSONObject(i).getString("picture"));
										flipper.addView(addImageById(imageUrlsList.get(i)));
									}
									CharSequence text = getString(R.string.viewpager_indicator, 1, advCount);
									textView_indicator.setText(text);
									textView_indicator.setVisibility(View.VISIBLE);
									hasPics = true;
									setFlipper();
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
							// Utity.showToast(getActivity().getApplicationContext(),
							// eDaoClientConfig.checkNet);
							// getAdvData();
							setFlipper();
						}
					});
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

	private void setUserInfo() {
		textView_ecoin.setText("" + application.getTotalEb());
		textView_goldcoin.setText("" + application.getTotalGold());
		textView_point.setText("" + application.getTotalIntegral());
	}

}
