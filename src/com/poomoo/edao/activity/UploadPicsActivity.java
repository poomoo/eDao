package com.poomoo.edao.activity;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.poomoo.edao.R;
import com.poomoo.edao.config.eDaoClientConfig;
import com.poomoo.edao.popupwindow.Upload_Pics_PopupWindow;

public class UploadPicsActivity extends BaseActivity implements OnClickListener {
	private Button button_upload;
	private FrameLayout frameLayout_identitycard_front,
			frameLayout_identitycard_back, frameLayout_identitycard_inhand,
			frameLayout_business_license;
	private ImageView imageView_identitycard_front,
			imageView_identitycard_back, imageView_identitycard_inhand,
			imageView_business_license;
	private TextView textView_identitycard_front, textView_identitycard_back,
			textView_identitycard_inhand, textView_business_license;

	private Upload_Pics_PopupWindow upload_Pics_PopupWindow;

	private static final int NONE = 0;
	private static final int PHOTOHRAPH = 1;// 拍照
	private static final int PHOTOZOOM = 2; // 缩放
	private static final int PHOTORESOULT = 3;// 结果

	private static final String IMAGE_UNSPECIFIED = "image/*";
	private Bitmap photo;
	private int flag = 0;

	private String userId = "", path1 = "", path2 = "", path3 = "", path4 = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_uploadpics);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.navigation_fragment));
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根
		frameLayout_identitycard_front = (FrameLayout) findViewById(R.id.uploadpics_layout_identitycard_front);
		frameLayout_identitycard_back = (FrameLayout) findViewById(R.id.uploadpics_layout_identitycard_back);
		frameLayout_identitycard_inhand = (FrameLayout) findViewById(R.id.uploadpics_layout_identitycard_inhand);
		frameLayout_business_license = (FrameLayout) findViewById(R.id.uploadpics_layout_businesss_license);

		imageView_identitycard_front = (ImageView) findViewById(R.id.uploadpics_imageView_identitycard_front);
		imageView_identitycard_back = (ImageView) findViewById(R.id.uploadpics_imageView_identitycard_back);
		imageView_identitycard_inhand = (ImageView) findViewById(R.id.uploadpics_imageView_identitycard_inhand);
		imageView_business_license = (ImageView) findViewById(R.id.uploadpics_imageView_businesss_license);

		textView_identitycard_front = (TextView) findViewById(R.id.uploadpics_textView_identitycard_front);
		textView_identitycard_back = (TextView) findViewById(R.id.uploadpics_textView_identitycard_back);
		textView_identitycard_inhand = (TextView) findViewById(R.id.uploadpics_textView_identitycard_inhand);
		textView_business_license = (TextView) findViewById(R.id.uploadpics_textView_businesss_license);

		button_upload = (Button) findViewById(R.id.uploadpics_btn_upload);

		frameLayout_identitycard_front.setOnClickListener(this);
		frameLayout_identitycard_back.setOnClickListener(this);
		frameLayout_identitycard_inhand.setOnClickListener(this);
		frameLayout_business_license.setOnClickListener(this);
		button_upload.setOnClickListener(this);

		SharedPreferences sp = getSharedPreferences("userInfo",
				Context.MODE_PRIVATE);
		userId = sp.getString("userId", "");

	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.uploadpics_layout_identitycard_front:
			flag = 1;
			select_pics();
			break;
		case R.id.uploadpics_layout_identitycard_back:
			flag = 2;
			select_pics();
			break;
		case R.id.uploadpics_layout_identitycard_inhand:
			flag = 3;
			select_pics();
			break;
		case R.id.uploadpics_layout_businesss_license:
			flag = 4;
			select_pics();
			break;
		case R.id.uploadpics_btn_upload:
			// uploadUserInfo();
			// upload();
			doPost();
			break;
		}

	}

	private void select_pics() {
		// 实例化SelectPicPopupWindow
		upload_Pics_PopupWindow = new Upload_Pics_PopupWindow(
				UploadPicsActivity.this, itemsOnClick);
		// 显示窗口
		upload_Pics_PopupWindow.showAtLocation(UploadPicsActivity.this
				.findViewById(R.id.activity_uploadpics_layout), Gravity.CENTER,
				0, 0); // 设置layout在PopupWindow中显示的位置
	}

	// 为弹出窗口实现监听类
	private OnClickListener itemsOnClick = new OnClickListener() {

		@Override
		public void onClick(View view) {
			upload_Pics_PopupWindow.dismiss();
			switch (view.getId()) {
			case R.id.popup_select_pic_res_camera:
				Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent1.putExtra(MediaStore.EXTRA_OUTPUT, Uri
						.fromFile(new File(Environment
								.getExternalStorageDirectory(), "temp.jpg")));
				startActivityForResult(intent1, PHOTOHRAPH);
				break;

			case R.id.popup_select_pic_res_photograph:
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
				intent.setDataAndType(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
						IMAGE_UNSPECIFIED);
				// intent.putExtra("return-data", true);
				// startActivityForResult(intent, PHOTOZOOM);
				startActivityForResult(intent, PHOTORESOULT);
				break;
			}
		}
	};

	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 2);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 600);
		intent.putExtra("outputY", 300);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, PHOTORESOULT);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == NONE)
			return;
		// 拍照
		if (requestCode == PHOTOHRAPH) {
			// 设置文件保存路径这里放在跟目录下
			File picture = new File(Environment.getExternalStorageDirectory()
					+ "/temp.jpg");
			startPhotoZoom(Uri.fromFile(picture));
		}

		if (data == null)
			return;

		// 读取相册缩放图片
		if (requestCode == PHOTOZOOM) {
			startPhotoZoom(data.getData());
		}
		// 处理结果
		if (requestCode == PHOTORESOULT) {
			// 取得返回的Uri,基本上选择照片的时候返回的是以Uri形式，但是在拍照中有得机子呢Uri是空的，所以要特别注意
			Uri mImageCaptureUri = data.getData();
			System.out.println("mImageCaptureUri:" + mImageCaptureUri);
			// 返回的Uri不为空时，那么图片信息数据都会在Uri中获得。如果为空，那么我们就进行下面的方式获取
			if (mImageCaptureUri != null) {
				try {
					// 这个方法是根据Uri获取Bitmap图片的静态方法
					photo = MediaStore.Images.Media.getBitmap(
							this.getContentResolver(), mImageCaptureUri);
					String imagePath;
					Cursor cursor = getContentResolver().query(
							mImageCaptureUri,
							new String[] { MediaStore.Images.Media.DATA },
							null, null, null);
					cursor.moveToFirst();
					int columnIndex = cursor
							.getColumnIndex(MediaStore.Images.Media.DATA);
					imagePath = cursor.getString(columnIndex); // 从内容提供者这里获取到图片的路径
					cursor.close();
					if (photo != null) {
						System.out.println("进入setImage:" + imagePath);
						setImage(imagePath);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			super.onActivityResult(requestCode, resultCode, data);
		}

	}

	private void setImage(String path) {
		switch (flag) {
		case 1:
			textView_identitycard_front.setText("");
			imageView_identitycard_front.setImageBitmap(photo);
			path1 = path;
			break;
		case 2:
			textView_identitycard_back.setText("");
			imageView_identitycard_back.setImageBitmap(photo);
			path2 = path;
			break;
		case 3:
			textView_identitycard_inhand.setText("");
			imageView_identitycard_inhand.setImageBitmap(photo);
			path3 = path;
			break;
		case 4:
			textView_business_license.setText("");
			imageView_business_license.setImageBitmap(photo);
			path4 = path;
			break;
		}
	}

	private boolean uploadUserInfo() {
		RequestParams params = new RequestParams();
		JSONObject jsonObject = new JSONObject();

		try {
			// jsonObject.put("bizName", "10000");
			// jsonObject.put("method", "10018");
			// jsonObject.put("userId", userId);
			// jsonObject.put("imageType", "1");
			//
			// String strJson = new String(jsonObject.toString().getBytes(),
			// "UTF-8");

			params.put("userId", userId);
			File file1 = new File(path1);
			File file2 = new File(path2);
			params.put("file1", file1);
			params.put("file2", file2);

			System.out.println("url:" + eDaoClientConfig.imageurl + "\n"
					+ "params:" + params);

			// 异步的客户端对象
			AsyncHttpClient client = new AsyncHttpClient();
			client.post(eDaoClientConfig.imageurl, params,
					new AsyncHttpResponseHandler() {
						Message message = new Message();

						@Override
						public void onFailure(Throwable arg0, String arg1) {
							message.what = 2;
							myHandler.sendMessage(message);
						}

						@Override
						public void onSuccess(String arg0) {
							message.what = 1;
							myHandler.sendMessage(message);
							System.out.println(arg0);
						}

					});
		} catch (Exception e) {
			System.out.println("异常" + e.getMessage().toString());
		}
		return true;
	}

	private void upload() {
		HttpParams params = new BasicHttpParams(); // 实例化Post参数对象
		HttpConnectionParams.setConnectionTimeout(params, 1 * 10 * 1000); // 设置请求超时
		HttpConnectionParams.setSoTimeout(params, 1 * 10 * 1000); // 设置响应超时
		HttpClient client = new DefaultHttpClient(params); // 实例化一个连接对象
		HttpPost post = new HttpPost(eDaoClientConfig.imageurl); // 根据Post参数,实例化一个Post对象

		MultipartEntity entity = new MultipartEntity(); // 实例化请求实体,请求正文
		List<File> list = new ArrayList<File>();
		list.add(new File(path1)); // 创建File
		list.add(new File(path2)); // 创建File

		for (int i = 0; i < list.size(); i++) {
			ContentBody body = new FileBody(list.get(i));
			entity.addPart("file", body); // 表单字段名
		}
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		nameValuePair.add(new BasicNameValuePair("userId", userId));
		Message message = new Message();
		try {
			HttpEntity entity1 = new UrlEncodedFormEntity(nameValuePair,
					HTTP.UTF_8);
			System.out.println("entity1:" + entity1.toString());
			post.setEntity(entity1);
			// post.setEntity(entity); // 将请求实体保存到Post的实体参数中
			System.out.println("post:" + post.toString());

			HttpResponse response = client.execute(post); // 执行Post方法
			if (response.getStatusLine().getStatusCode() == 200)
				message.what = 1;
			else
				message.what = 2;
			// return EntityUtils.toString(response.getEntity(), "UTF-8"); //
			// 根据字符编码返回字符串
		} catch (Exception e) {
			message.what = 2;
			System.out.println("Exception:" + e.getMessage());
		} finally {
			client.getConnectionManager().shutdown(); // 释放连接所有资源
			myHandler.sendMessage(message);
		}
	}

	private void doPost() {
		// 获取HttpClient对象
		HttpClient httpClient = new DefaultHttpClient();
		// 新建HttpPost对象
		HttpPost httpPost = new HttpPost(eDaoClientConfig.imageurl);
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();

		nameValuePair.add(new BasicNameValuePair("userId", userId));
		// 设置字符集
		HttpEntity entity;
		Message message = new Message();
		try {
			entity = new UrlEncodedFormEntity(nameValuePair, HTTP.UTF_8);

			// 设置参数实体
			httpPost.setEntity(entity);

			HttpResponse httpResp = httpClient.execute(httpPost);
			// 判断是够请求成功
			if (httpResp.getStatusLine().getStatusCode() == 200) {
				// 获取返回的数据
				String result = EntityUtils.toString(httpResp.getEntity(),
						"UTF-8");
				message.what = 1;
			} else {
				// Log.i("HttpPost", "HttpPost方式请求失败");
				message.what = 2;
			}
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			message.what = 2;
			System.out.println("Exception:" + e.getMessage());
		}

	}

	Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1) {

				Toast.makeText(getApplicationContext(), "上传成功",
						Toast.LENGTH_LONG).show();
			} else {

				Toast.makeText(getApplicationContext(), "上传失败",
						Toast.LENGTH_LONG).show();
			}
			super.handleMessage(msg);
		}
	};

}
