package com.poomoo.edao.activity;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.poomoo.edao.R;
import com.poomoo.edao.widget.photoview.PhotoViewAttacher;
import com.poomoo.edao.widget.photoview.PhotoViewAttacher.OnPhotoTapListener;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

/**
 * 
 * @ClassName CatBigPictureActivity
 * @Description TODO 查看大图
 * @author 李苜菲
 * @date 2015年10月26日 上午11:43:41
 */
public class CatBigPictureActivity extends BaseActivity {
	private ImageView imageView;
	private PhotoViewAttacher mAttacher;

	private String path = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cat_big_picture);
		path = getIntent().getStringExtra("path");
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根
		imageView = (ImageView) findViewById(R.id.cat_big_picture_imageview);
		mAttacher = new PhotoViewAttacher(imageView);

		mAttacher.setOnPhotoTapListener(new OnPhotoTapListener() {

			@Override
			public void onPhotoTap(View arg0, float arg1, float arg2) {
				finish();
			}
		});

		// 使用ImageLoader加载网络图片
		// DisplayImageOptions options = new DisplayImageOptions.Builder()//
		// .showImageOnLoading(R.drawable.ic_launcher) // 加载中显示的默认图片
		// .showImageOnFail(R.drawable.ic_launcher) // 设置加载失败的默认图片
		// .cacheInMemory(true) // 内存缓存
		// .cacheOnDisk(true) // sdcard缓存
		// .bitmapConfig(Config.RGB_565)// 设置最低配置
		// .imageScaleType(ImageScaleType.EXACTLY)// 缩放图片
		// .build();
		ImageLoader.getInstance().displayImage(path, imageView, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				mAttacher.update();
			}
		});
	}

}
