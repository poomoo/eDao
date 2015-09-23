package com.poomoo.edao.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;

import com.poomoo.edao.R;
import com.poomoo.edao.application.eDaoClientApplication;
import com.poomoo.edao.config.eDaoClientConfig;
import com.poomoo.edao.popupwindow.Upload_Pics_PopupWindow;
import com.poomoo.edao.util.Utity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class UploadPicsActivity extends BaseActivity implements OnClickListener {
	private Button button_upload;
	private FrameLayout frameLayout_identitycard_front, frameLayout_identitycard_back, frameLayout_identitycard_inhand,
			frameLayout_business_license;
	private ImageView imageView_identitycard_front, imageView_identitycard_back, imageView_identitycard_inhand,
			imageView_business_license;
	private TextView textView_identitycard_front, textView_identitycard_back, textView_identitycard_inhand,
			textView_business_license;

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
	private int uploadCount = 0;
	private List<File> filelist = null;
	private eDaoClientApplication applicaiton = null;
	private SharedPreferences sharedPreferences_certificaitonInfo = null;
	private Editor editor = null;
	private final static String image_capture_path = Environment.getExternalStorageDirectory() + "/" + "edao.temp";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_uploadpics);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.navigation_fragment));
		applicaiton = (eDaoClientApplication) getApplication();
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

		userId = applicaiton.getUserId();

		sharedPreferences_certificaitonInfo = getSharedPreferences("certificaitonInfo", Context.MODE_PRIVATE);
		path1 = sharedPreferences_certificaitonInfo.getString("imagepath1", "");
		path2 = sharedPreferences_certificaitonInfo.getString("imagepath2", "");
		path3 = sharedPreferences_certificaitonInfo.getString("imagepath3", "");
		path4 = sharedPreferences_certificaitonInfo.getString("imagepath4", "");

		if (!TextUtils.isEmpty(path1)) {
			imageView_identitycard_front.setImageBitmap(Utity.revitionImageSize(path1));
			textView_identitycard_front.setText("");
		}

		if (!TextUtils.isEmpty(path2)) {
			imageView_identitycard_back.setImageBitmap(Utity.revitionImageSize(path2));
			textView_identitycard_back.setText("");
		}

		if (!TextUtils.isEmpty(path3)) {
			imageView_identitycard_inhand.setImageBitmap(Utity.revitionImageSize(path3));
			textView_identitycard_inhand.setText("");
		}

		if (!TextUtils.isEmpty(path4)) {
			imageView_business_license.setImageBitmap(Utity.revitionImageSize(path4));
			textView_business_license.setText("");
		}

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

			if (checkInput()) {
				showProgressDialog();
				// test();
				new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO 自动生成的方法存根
						uploadCount = 0;
						upload(filelist.get(uploadCount));
					}
				}).start();
			}
			break;
		}

	}

	private boolean checkInput() {
		// TODO 自动生成的方法存根
		if (file1 == null) {
			Utity.showToast(getApplicationContext(), "请选择身份证正面照");
			return false;
		}
		if (file2 == null) {
			Utity.showToast(getApplicationContext(), "请选择身份证背面照");
			return false;
		}
		if (file3 == null) {
			Utity.showToast(getApplicationContext(), "请选择手持证件照");
			return false;
		}
		filelist = new ArrayList<File>();
		filelist.add(file1);
		filelist.add(file2);
		filelist.add(file3);
		if (file4 != null) {
			// Utity.showToast(getApplicationContext(), "请选择营业执照");
			// return false;
			filelist.add(file4);
		}

		return true;
	}

	private void select_pics() {
		// 实例化SelectPicPopupWindow
		upload_Pics_PopupWindow = new Upload_Pics_PopupWindow(UploadPicsActivity.this, itemsOnClick);
		// 显示窗口
		upload_Pics_PopupWindow.showAtLocation(UploadPicsActivity.this.findViewById(R.id.activity_uploadpics_layout),
				Gravity.CENTER, 0, 0); // 设置layout在PopupWindow中显示的位置
	}

	// 为弹出窗口实现监听类
	private OnClickListener itemsOnClick = new OnClickListener() {

		@Override
		public void onClick(View view) {
			upload_Pics_PopupWindow.dismiss();
			switch (view.getId()) {
			case R.id.popup_select_pic_res_camera:
				Intent intent1 = new Intent("android.media.action.IMAGE_CAPTURE");
				intent1.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(image_capture_path)));
				startActivityForResult(intent1, PHOTOHRAPH);
				break;

			case R.id.popup_select_pic_res_photograph:
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
				intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
				intent.putExtra("return-data", true);
				startActivityForResult(intent, PHOTORESOULT);
				break;
			}
		}
	};

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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == NONE)
			return;
		// 拍照
		if (requestCode == PHOTOHRAPH) {
			System.out.println("拍照返回");
			setImage(image_capture_path);
		}
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
					setImage(imagePath);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			super.onActivityResult(requestCode, resultCode, data);
		}

	}

	private void setImage(String path) {
		photo = null;
		switch (flag) {
		case 1:
			textView_identitycard_front.setText("");
			// photo = Utity.getBitMap(path);
			bitmap = Utity.revitionImageSize(path);
			imageView_identitycard_front.setImageBitmap(bitmap);
			path1 = Environment.getExternalStorageDirectory() + "/" + "edao1.jpg";
			file1 = saveBitmap(bitmap, path1);
			break;
		case 2:
			textView_identitycard_back.setText("");
			// photo = Utity.getBitMap(path);
			bitmap = Utity.revitionImageSize(path);
			imageView_identitycard_back.setImageBitmap(bitmap);
			path2 = Environment.getExternalStorageDirectory() + "/" + "edao2.jpg";
			file2 = saveBitmap(bitmap, path2);
			break;
		case 3:
			textView_identitycard_inhand.setText("");
			// photo = Utity.getBitMap(path);
			bitmap = Utity.revitionImageSize(path);
			System.out.println("bitmap:" + bitmap);
			imageView_identitycard_inhand.setImageBitmap(bitmap);
			path3 = Environment.getExternalStorageDirectory() + "/" + "edao3.jpg";
			file3 = saveBitmap(bitmap, path3);
			break;
		case 4:
			textView_business_license.setText("");
			// photo = Utity.getBitMap(path);
			bitmap = Utity.revitionImageSize(path);
			imageView_business_license.setImageBitmap(bitmap);
			path4 = Environment.getExternalStorageDirectory() + "/" + "edao4.jpg";
			file4 = saveBitmap(bitmap, path4);
			break;
		}
	}

	private void upload(File file) {
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(eDaoClientConfig.imageurl); // 根据Post参数,实例化一个Post对象
		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, eDaoClientConfig.timeout);
		client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, eDaoClientConfig.timeout);

		MultipartEntity entity = new MultipartEntity(); // 实例化请求实体,请求正文
		// List<File> list = new ArrayList<File>();
		// // 创建File
		// list.add(file1);
		// list.add(file2);
		// list.add(file3);
		// list.add(file4);
		// System.out.println("list.size():" + list.size());
		// for (int i = 0; i < list.size(); i++) {
		// ContentBody body = new FileBody(list.get(i));
		// entity.addPart("file", body); // 表单字段名
		// }
		System.out.println("进入upload：" + "uploadCount" + ":" + uploadCount + "filelist.size" + ":" + filelist.size());
		entity.addPart("file", new FileBody(file));

		post.setEntity(entity); // 将请求实体保存到Post的实体参数中
		Message message = new Message();
		try {
			entity.addPart("imageType", new StringBody(String.valueOf(uploadCount + 1), Charset.forName("utf-8")));
			entity.addPart("userId", new StringBody(userId, Charset.forName("utf-8")));
			HttpResponse response;
			response = client.execute(post);
			// 执行Post方法
			if (response.getStatusLine().getStatusCode() == 200) {
				uploadCount++;
				message.what = 1;
			} else
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

	Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {

			if (msg.what == 1) {
				if (uploadCount < filelist.size())
					new Thread(new Runnable() {
						@Override
						public void run() {
							// TODO 自动生成的方法存根
							upload(filelist.get(uploadCount));
						}
					}).start();
				else {
					closeProgressDialog();
					editor = sharedPreferences_certificaitonInfo.edit();
					editor.putString("imagepath1", path1);
					editor.putString("imagepath2", path2);
					editor.putString("imagepath3", path3);
					editor.putString("imagepath4", path4);
					editor.commit();
					Toast.makeText(getApplicationContext(), "上传成功", Toast.LENGTH_LONG).show();
					finish();
				}

			} else {
				closeProgressDialog();
				Toast.makeText(getApplicationContext(), "上传失败", Toast.LENGTH_LONG).show();
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
	 * @throws @date
	 *             2015-8-12下午1:23:53
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
	 * @throws @date
	 *             2015-8-12下午1:24:43
	 */
	private void closeProgressDialog() {
		if (progressDialog != null)
			progressDialog.dismiss();
	}

}
