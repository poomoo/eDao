package com.poomoo.edao.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;

import android.app.ProgressDialog;
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
import android.provider.MediaStore.Images.Media;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.poomoo.edao.R;
import com.poomoo.edao.config.eDaoClientConfig;
import com.poomoo.edao.popupwindow.Upload_Pics_PopupWindow;
import com.poomoo.edao.util.Utity;

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
	private static final int PHOTORESOULT = 2;// 结果

	private static final String IMAGE_UNSPECIFIED = "image/*";
	private Bitmap photo;
	private int flag = 0;

	private String userId = "", path1 = "", path2 = "", path3 = "", path4 = "";
	private ProgressDialog progressDialog;
	private File file1 = null, file2 = null, file3 = null, file4 = null;
	private Bitmap bitmap = null;

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
			showProgressDialog();
			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO 自动生成的方法存根
					upload();
				}
			}).start();
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
								.getExternalStorageDirectory(), flag + ".jpg")));
				startActivityForResult(intent1, PHOTOHRAPH);
				break;

			case R.id.popup_select_pic_res_photograph:
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
				intent.setDataAndType(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
						IMAGE_UNSPECIFIED);
				intent.putExtra("return-data", true);
				// startActivityForResult(intent, PHOTOZOOM);
				startActivityForResult(intent, PHOTORESOULT);
				break;
			}
		}
	};

	private File saveBitmap(Bitmap bitmap, String imageName) {

		File f = new File(Environment.getExternalStorageDirectory(), imageName);
		if (f.exists()) {
			f.delete();
		}

		try {
			FileOutputStream out = new FileOutputStream(f);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return f;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == NONE)
			return;
		// 拍照
		if (requestCode == PHOTOHRAPH) {
			// 设置文件保存路径这里放在跟目录下
			String imagePath = Environment.getExternalStorageDirectory() + "/"
					+ flag + ".jpg";
			setImage(imagePath);
		}

		if (data == null)
			return;

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
							mImageCaptureUri, new String[] { Media.DATA },
							null, null, null);
					cursor.moveToFirst();
					int columnIndex = cursor.getColumnIndex(Media.DATA);
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
			bitmap = Utity.revitionImageSize(path);
			imageView_identitycard_front.setImageBitmap(bitmap);
			file1 = saveBitmap(bitmap, "1.jpg");
			break;
		case 2:
			textView_identitycard_back.setText("");
			bitmap = Utity.revitionImageSize(path);
			imageView_identitycard_back.setImageBitmap(bitmap);
			file2 = saveBitmap(bitmap, "2.jpg");
			break;
		case 3:
			textView_identitycard_inhand.setText("");
			bitmap = Utity.revitionImageSize(path);
			imageView_identitycard_inhand.setImageBitmap(bitmap);
			file3 = saveBitmap(bitmap, "3.jpg");
			break;
		case 4:
			textView_business_license.setText("");
			bitmap = Utity.revitionImageSize(path);
			imageView_business_license.setImageBitmap(bitmap);
			file4 = saveBitmap(bitmap, "4.jpg");
			break;
		}
	}

	private void upload() {

		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(eDaoClientConfig.imageurl); // 根据Post参数,实例化一个Post对象
		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
				1 * 10 * 1000);
		client.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 1 * 10 * 1000);

		MultipartEntity entity = new MultipartEntity(); // 实例化请求实体,请求正文
		List<File> list = new ArrayList<File>();
		// 创建File
		list.add(file1);
		list.add(file2);
		for (int i = 0; i < list.size(); i++) {
			ContentBody body = new FileBody(list.get(i));
			entity.addPart("file", body); // 表单字段名
		}

		post.setEntity(entity); // 将请求实体保存到Post的实体参数中
		Message message = new Message();
		try {
			entity.addPart("userId",
					new StringBody(userId, Charset.forName("utf-8")));
			HttpResponse response;
			response = client.execute(post);
			// 执行Post方法
			if (response.getStatusLine().getStatusCode() == 200)
				message.what = 1;
			else
				message.what = 2;
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

	private void test() {
		RequestParams params = new RequestParams();
		// params.addHeader("name", "value");
		// params.addQueryStringParameter("userId", userId);

		// 只包含字符串参数时默认使用BodyParamsEntity，
		// 类似于UrlEncodedFormEntity（"application/x-www-form-urlencoded"）。
		params.addBodyParameter("userId", userId);

		// 加入文件参数后默认使用MultipartEntity（"multipart/form-data"），
		// 如需"multipart/related"，xUtils中提供的MultipartEntity支持设置subType为"related"。
		// 使用params.setBodyEntity(httpEntity)可设置更多类型的HttpEntity（如：
		// MultipartEntity,BodyParamsEntity,FileUploadEntity,InputStreamUploadEntity,StringEntity）。
		// 例如发送json参数：params.setBodyEntity(new StringEntity(jsonStr,charset));
		params.addBodyParameter("file", file1);
		params.addBodyParameter("file", file2);
		final DecimalFormat df = new DecimalFormat("0.00");
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, eDaoClientConfig.imageurl,
				params, new RequestCallBack<String>() {

					@Override
					public void onStart() {
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						if (isUploading) {
							System.out.println("上传进度:"
									+ df.format(current / total) + "%");
						} else {
							System.out.println("上传结束");
						}
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						Toast.makeText(getApplicationContext(), "上传成功",
								Toast.LENGTH_LONG).show();
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						Toast.makeText(getApplicationContext(), "上传失败",
								Toast.LENGTH_LONG).show();
					}
				});
	}

	Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			closeProgressDialog();
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

	/**
	 * 
	 * 
	 * @Title: showProgressDialog
	 * @Description: TODO 显示进度对话框
	 * @author 李苜菲
	 * @return
	 * @return void
	 * @throws
	 * @date 2015-8-12下午1:23:53
	 */
	private void showProgressDialog() {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("上传中...");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}

	/**
	 * 
	 * 
	 * @Title: closeProgressDialog
	 * @Description: TODO 关闭进度对话框
	 * @author 李苜菲
	 * @return
	 * @return void
	 * @throws
	 * @date 2015-8-12下午1:24:43
	 */
	private void closeProgressDialog() {
		if (progressDialog != null)
			progressDialog.dismiss();
	}

}
