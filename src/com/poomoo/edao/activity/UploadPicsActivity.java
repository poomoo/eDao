package com.poomoo.edao.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.poomoo.edao.R;
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
					System.out.println("photo:" + photo);
					// 将图片内容解析成字节数组
					// ContentResolver resolver = getContentResolver();
					// byte[] mContent;
					// mContent = readStream(resolver.openInputStream(Uri
					// .parse(mImageCaptureUri.toString())));
					// // 将字节数组转换为ImageView可调用的Bitmap对象
					// image = getPicFromBytes(mContent, null);

					if (photo != null) {
						System.out.println("进入setImage");
						setImage();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				// Bundle extras = data.getExtras();
				// if (extras != null) {
				// photo = extras.getParcelable("data");
				// setImage();
				// }
			}
			super.onActivityResult(requestCode, resultCode, data);
		}

	}

	private void setImage() {
		switch (flag) {
		case 1:
			textView_identitycard_front.setText("");
			imageView_identitycard_front.setImageBitmap(photo);
			break;
		case 2:
			textView_identitycard_back.setText("");
			imageView_identitycard_back.setImageBitmap(photo);
			break;
		case 3:
			textView_identitycard_inhand.setText("");
			imageView_identitycard_inhand.setImageBitmap(photo);
			break;
		case 4:
			textView_business_license.setText("");
			imageView_business_license.setImageBitmap(photo);
			break;
		}
	}

	public static Bitmap getPicFromBytes(byte[] bytes,
			BitmapFactory.Options opts) {
		if (bytes != null)
			if (opts != null)
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
						opts);
			else
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		return null;
	}

	public static byte[] readStream(InputStream inStream) throws Exception {
		byte[] buffer = new byte[1024];
		int len = -1;
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();
		outStream.close();
		inStream.close();
		return data;

	}
}
