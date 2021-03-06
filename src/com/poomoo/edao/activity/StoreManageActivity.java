package com.poomoo.edao.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.poomoo.edao.R;
import com.poomoo.edao.adapter.ChannelSpinnerAdapter;
import com.poomoo.edao.application.eDaoClientApplication;
import com.poomoo.edao.config.eDaoClientConfig;
import com.poomoo.edao.model.ResponseData;
import com.poomoo.edao.model.StoreData;
import com.poomoo.edao.model.UserInfoData;
import com.poomoo.edao.model.database.AreaInfo;
import com.poomoo.edao.model.database.CityInfo;
import com.poomoo.edao.model.database.ProvinceInfo;
import com.poomoo.edao.popupwindow.Select_City_PopupWindow;
import com.poomoo.edao.util.HttpCallbackListener;
import com.poomoo.edao.util.HttpUtil;
import com.poomoo.edao.util.Utity;
import com.poomoo.edao.widget.CityPicker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

/**
 * 
 * @ClassName StoreManageActivity
 * @Description TODO 店铺管理
 * @author 李苜菲
 * @date 2015年8月31日 下午10:47:53
 */
public class StoreManageActivity extends BaseActivity implements OnClickListener {
	private LinearLayout layout_logo, layout_logo_center, layout_zone, layout_store_class;
	private TextView textView_zone, textView_store_class;
	private EditText editText_store_name, editText_address;
	private Button button_confirm;

	private Select_City_PopupWindow select_City_PopupWindow;

	private PopupWindow popupWindow;
	private View layout;
	private ChannelSpinnerAdapter adapter;
	private List<HashMap<String, String>> list;
	private ListView listView;

	private String curProvince = "", curCity = "", curArea = "", path = "", store_class_id = "", store_name = "",
			address = "";
	private eDaoClientApplication application = null;
	private static final String IMAGE_UNSPECIFIED = "image/*";
	private static final int NONE = 0;
	private static final int PHOTORESOULT = 1;// 结果
	private File file = null;
	private Bitmap bitmap = null;
	private ArrayList<ProvinceInfo> provinceList = new ArrayList<ProvinceInfo>();
	private ArrayList<CityInfo> cityList = new ArrayList<CityInfo>();
	private ArrayList<AreaInfo> areaList = new ArrayList<AreaInfo>();

	private Gson gson = new Gson();
	private Editor editor = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_manage);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.navigation_fragment));
		application = (eDaoClientApplication) getApplication();
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根
		textView_store_class = (TextView) findViewById(R.id.store_manage_textView_store_class);
		textView_zone = (TextView) findViewById(R.id.store_manage_textView_zone);

		editText_store_name = (EditText) findViewById(R.id.store_manage_editText_store_name);
		editText_address = (EditText) findViewById(R.id.store_manage_editText_address);

		layout_store_class = (LinearLayout) findViewById(R.id.store_manage_layout_store_class);
		layout_logo = (LinearLayout) findViewById(R.id.store_manage_layout_logo);
		layout_logo_center = (LinearLayout) findViewById(R.id.store_manage_layout_logo_center);
		layout_zone = (LinearLayout) findViewById(R.id.store_manage_layout_zone);

		button_confirm = (Button) findViewById(R.id.store_manage_btn_confirm);

		layout_zone.setOnClickListener(this);
		layout_logo_center.setOnClickListener(this);
		layout_store_class.setOnClickListener(this);
		button_confirm.setOnClickListener(this);

		// 取当前定位
		curProvince = application.getCurProvince();
		curCity = application.getCurCity();
		curArea = application.getCurArea();
		textView_zone.setText(curProvince + "-" + curCity + "-" + curArea);
		CityPicker.province_name = curProvince;
		CityPicker.city_name = curCity;
		CityPicker.area_name = curArea;
		// 设置省份编码
		provinceList = Utity.getProvinceList();
		int position = 0;
		position = Utity.getProvincePosition(provinceList, curProvince);
		CityPicker.province_id = provinceList.get(position).getProvince_id();
		// 设置城市编码
		cityList = Utity.getCityList(CityPicker.province_id);
		position = 0;
		position = Utity.getCityPosition(cityList, curCity);
		CityPicker.city_id = cityList.get(position).getCity_id();
		// 设置区域编码
		areaList = Utity.getAreaList(CityPicker.city_id);
		position = 0;
		position = Utity.getAreaPosition(areaList, curArea);
		CityPicker.area_id = areaList.get(position).getArea_id();

		list = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> data = null;
		int length = eDaoClientConfig.store_class.length;
		for (int i = 0; i < length; i++) {
			data = new HashMap<String, String>();
			data.put("name", eDaoClientConfig.store_class[i]);
			data.put("id", i + 1 + "");
			list.add(data);
		}
		adapter = new ChannelSpinnerAdapter(this, list);
		// 已经添加过店铺时要检查店铺状态
		if (!TextUtils.isEmpty(application.getShopId())) {
			checkShopStatus();
		}

	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.store_manage_layout_logo:
			Intent intent1 = new Intent(Intent.ACTION_PICK, null);
			intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
			intent1.putExtra("return-data", true);
			startActivityForResult(intent1, PHOTORESOULT);
			break;
		case R.id.store_manage_layout_logo_center:
			Intent intent = new Intent(Intent.ACTION_PICK, null);
			intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
			intent.putExtra("return-data", true);
			startActivityForResult(intent, PHOTORESOULT);
			break;
		case R.id.store_manage_layout_store_class:
			showWindow(layout_store_class, listView, list, textView_store_class, adapter);
			break;
		case R.id.store_manage_layout_zone:
			select_city();
			break;
		case R.id.store_manage_btn_confirm:
			confrim();
			break;
		}
	}

	private void confrim() {
		// TODO 自动生成的方法存根
		if (checkInput()) {
			showProgressDialog("上传中...");
			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO 自动生成的方法存根
					upload();
				}
			}).start();
		}
	}

	private boolean checkInput() {
		// TODO 自动生成的方法存根
		if (Double.toString(application.getCurlongitude()).contains("4.9E-324")
				|| Double.toString(application.getCurlatitude()).contains("4.9E-324")) {
			Utity.showToast(getApplicationContext(), "定位失败!请允许该应用获取定位权限,并重新启动应用!");
			return false;
		}
		if (file == null) {
			Utity.showToast(getApplication(), "请选择图片");
			return false;
		}

		store_name = editText_store_name.getText().toString().trim();
		if (TextUtils.isEmpty(store_name)) {
			Utity.showToast(getApplication(), "请输入店铺名称");
			return false;
		}

		if (TextUtils.isEmpty(store_class_id)) {
			Utity.showToast(getApplication(), "请选择店铺类型");
			return false;
		}

		address = editText_address.getText().toString().trim();
		if (TextUtils.isEmpty(address)) {
			Utity.showToast(getApplication(), "请输入详细地址");
			return false;
		}

		return true;
	}

	private void select_city() {
		// 实例化SelectPicPopupWindow
		select_City_PopupWindow = new Select_City_PopupWindow(StoreManageActivity.this, itemsOnClick);
		// 显示窗口
		select_City_PopupWindow.showAtLocation(StoreManageActivity.this.findViewById(R.id.activity_store_manage_layout),
				Gravity.CENTER, 0, 0); // 设置layout在PopupWindow中显示的位置
	}

	// 为弹出窗口实现监听类
	private OnClickListener itemsOnClick = new OnClickListener() {

		@Override
		public void onClick(View view) {
			if (view.getId() == R.id.popup_select_city_btn_confirm) {
				select_City_PopupWindow.dismiss();
				textView_zone.setText(CityPicker.getZone_string());
			}
		}
	};

	public void showWindow(View spinnerlayout, ListView listView, final List<HashMap<String, String>> list,
			final TextView text, final ChannelSpinnerAdapter adapter) {
		layout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.myspinner_dropdown, null);
		listView = (ListView) layout.findViewById(R.id.myspinner_dropdown_listView);
		listView.setAdapter(adapter);
		popupWindow = new PopupWindow(spinnerlayout);
		// 设置弹框的宽度为布局文件的宽
		popupWindow.setWidth(spinnerlayout.getWidth());
		popupWindow.setHeight(400);
		// 设置一个透明的背景，不然无法实现点击弹框外，弹框消失
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		popupWindow.setBackgroundDrawable(dw);
		// 设置点击弹框外部，弹框消失
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.setContentView(layout);
		// 设置弹框出现的位置，在v的正下方横轴偏移textview的宽度，为了对齐~纵轴不偏移
		popupWindow.showAsDropDown(spinnerlayout, 0, 0);
		// 弹出动画
		// popupWindow.setAnimationStyle(R.style.popwin_anim_style);
		popupWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				// spinnerlayout
				// .setBackgroundResource(R.drawable.preference_single_item);
			}

		});
		// listView的item点击事件
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				store_class_id = list.get(arg2).get("id");
				textView_store_class.setText(list.get(arg2).get("name"));
				// 弹框消失
				popupWindow.dismiss();
				popupWindow = null;
			}
		});

	}

	private File saveBitmap(Bitmap bitmap, String path) {

		File f = new File(path);
		if (f.exists()) {
			f.delete();
		}

		try {
			FileOutputStream out = new FileOutputStream(f);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 40, out);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return f;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == NONE)
			return;

		if (data == null) {
			System.out.println("返回为空");
			return;
		}
		// 处理结果
		if (requestCode == PHOTORESOULT) {
			// 取得返回的Uri,基本上选择照片的时候返回的是以Uri形式，但是在拍照中有得机子呢Uri是空的，所以要特别注意
			Uri mImageCaptureUri = data.getData();
			System.out.println("mImageCaptureUri:" + mImageCaptureUri);
			// 返回的Uri不为空时，那么图片信息数据都会在Uri中获得。如果为空，那么我们就进行下面的方式获取
			if (mImageCaptureUri != null) {
				try {
					String imagePath;
					Cursor cursor = getContentResolver().query(mImageCaptureUri, new String[] { Media.DATA }, null,
							null, null);
					cursor.moveToFirst();
					int columnIndex = cursor.getColumnIndex(Media.DATA);
					imagePath = cursor.getString(columnIndex); // 从内容提供者这里获取到图片的路径
					cursor.close();
					bitmap = Utity.revitionImageSize(imagePath);
					Drawable drawable = new BitmapDrawable(bitmap);
					layout_logo_center.setVisibility(View.GONE);
					layout_logo.setBackground(drawable);
					path = Environment.getExternalStorageDirectory() + "/" + "edaoStore.jpg";
					file = saveBitmap(bitmap, path);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	private void getData() {
		Map<String, String> data = new HashMap<String, String>();
		data.put("bizName", "30000");
		data.put("method", "30004");
		data.put("shopId", application.getShopId());
		showProgressDialog("请稍后...");
		HttpUtil.SendPostRequest(gson.toJson(data), eDaoClientConfig.url, new HttpCallbackListener() {

			@Override
			public void onFinish(final ResponseData responseData) {
				// TODO 自动生成的方法存根
				closeProgressDialog();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// TODO 自动生成的方法存根
						if (responseData.getRsCode() == 1) {
							try {
								JSONObject result = new JSONObject(responseData.getJsonData().toString());
								StoreData storeData = new StoreData();
								storeData = gson.fromJson(result.toString(), StoreData.class);
								editText_store_name.setText(storeData.getShopName());
								editText_address.setText(storeData.getAddress());
								textView_store_class.setText(storeData.getCategoryName());
								store_class_id = storeData.getCategoryId();

								// 使用ImageLoader加载网络图片
								DisplayImageOptions options = new DisplayImageOptions.Builder()//
										.cacheInMemory(false) // 内存缓存
										.cacheOnDisk(true) // sdcard缓存
										.bitmapConfig(Config.RGB_565)// 设置最低配置
										.build();
								ImageLoader.getInstance().loadImage(storeData.getPictures(),
										new SimpleImageLoadingListener() {

									@Override
									public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
										// TODO Auto-generated method stub
										super.onLoadingComplete(imageUri, view, loadedImage);
										bitmap = loadedImage;
										Drawable drawable = new BitmapDrawable(bitmap);
										layout_logo_center.setVisibility(View.GONE);
										layout_logo.setBackground(drawable);
										path = Environment.getExternalStorageDirectory() + "/" + "edaoStore.jpg";
										file = saveBitmap(bitmap, path);
									}
								});

							} catch (JSONException e) {
								// TODO 自动生成的 catch 块
								e.printStackTrace();
							}
						} else {
							finish();
							Utity.showToast(getApplicationContext(), responseData.getMsg());
						}
					}

				});
			}

			@Override
			public void onError(Exception e) {
				// TODO 自动生成的方法存根
				closeProgressDialog();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// TODO 自动生成的方法存根
						finish();
						Utity.showToast(getApplicationContext(), eDaoClientConfig.checkNet);
					}

				});
			}
		});
	}

	@SuppressWarnings("deprecation")
	private void upload() {
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(eDaoClientConfig.uploadStoreurl); // 根据Post参数,实例化一个Post对象
		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, eDaoClientConfig.timeout);
		client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, eDaoClientConfig.timeout);
		Message message = new Message();
		System.out.println("province_id:" + CityPicker.getProvince_id() + "city_id:" + CityPicker.getCity_id()
				+ "area_id:" + CityPicker.getArea_id());
		try {
			MultipartEntity entity = new MultipartEntity(); // 实例化请求实体,请求正文
			entity.addPart("bizName", new StringBody("30000"));
			entity.addPart("method", new StringBody("30001"));
			entity.addPart("userId", new StringBody(application.getUserId()));
			entity.addPart("shopName", new StringBody(store_name, Charset.forName("utf-8")));
			entity.addPart("categoryId", new StringBody(store_class_id));
			entity.addPart("provinceId", new StringBody(CityPicker.getProvince_id()));
			entity.addPart("cityId", new StringBody(CityPicker.getCity_id()));
			entity.addPart("areaId", new StringBody(CityPicker.getArea_id()));
			entity.addPart("address", new StringBody(address, Charset.forName("utf-8")));
			entity.addPart("longitude",
					new StringBody(Double.toString((application.getCurlongitude())), Charset.forName("utf-8")));
			entity.addPart("latitude",
					new StringBody(Double.toString(application.getCurlatitude()), Charset.forName("utf-8")));
			entity.addPart("file", new FileBody(file));
			System.out.println("entity:" + entity.toString());
			post.setEntity(entity); // 将请求实体保存到Post的实体参数中
			HttpResponse response;
			response = client.execute(post);
			// 执行Post方法
			if (response.getStatusLine().getStatusCode() == 200) {
				message.what = 1;
			} else {
				message.what = 2;

			}

			// return EntityUtils.toString(response.getEntity(), "UTF-8"); //
			// 根据字符编码返回字符串
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			message.what = 2;
			System.out.println("IOException:" + e.getMessage());
		} finally {
			client.getConnectionManager().shutdown(); // 释放连接所有资源
			myHandler.sendMessage(message);
		}
	}

	Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			closeProgressDialog();
			if (msg.what == 1) {
				Utity.showToast(getApplication(), "上传成功");
				application.setShopId("success");// 上传成功了设置shopid不为空
				finish();
			} else {
				Utity.showToast(getApplication(), "上传失败");
			}
			super.handleMessage(msg);
		}
	};

	private void checkShopStatus() {
		showProgressDialog("查看审核状态中...");
		Map<String, String> data = new HashMap<String, String>();
		data.put("bizName", "10000");
		data.put("method", "10013");
		data.put("userId", application.getUserId());
		HttpUtil.SendPostRequest(gson.toJson(data), eDaoClientConfig.url, new HttpCallbackListener() {

			@Override
			public void onFinish(final ResponseData responseData) {
				// TODO 自动生成的方法存根
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						closeProgressDialog();
						if (responseData.getRsCode() == 1) {
							UserInfoData infoData = new UserInfoData();
							infoData = gson.fromJson(responseData.getJsonData(), UserInfoData.class);
							String shopId = infoData.getShopId();
							String shopStatus = infoData.getShopStatus();
							application.setShopId(shopId);
							application.setShopStatus(shopStatus);

							editor = getSharedPreferences("userInfo", Context.MODE_PRIVATE).edit();
							editor.putString("shopStatus", shopStatus);
							editor.putString("shopId", shopId);
							editor.commit();

							if (application.getShopStatus().equals("0")) {
								Utity.showToast(getApplicationContext(), "店铺正在审核中...");
								finish();
							} else if (application.getShopStatus().equals("1")) {
								Utity.showToast(getApplicationContext(), "店铺添加成功!");
								finish();
							} else {
								// 店铺审核没有通过时回显
								getData();
								layout_logo.setOnClickListener(StoreManageActivity.this);
								Utity.showToast(getApplicationContext(), "店铺审核失败,请重新添加!");
							}

						} else {
							Utity.showToast(getApplicationContext(), responseData.getMsg());
							finish();
						}

					}
				});
			}

			@Override
			public void onError(Exception e) {
				// TODO 自动生成的方法存根
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						closeProgressDialog();
						Utity.showToast(getApplicationContext(), eDaoClientConfig.checkNet);
						finish();
					}

				});
			}
		});
	}
}
