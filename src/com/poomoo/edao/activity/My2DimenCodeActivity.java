package com.poomoo.edao.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.poomoo.edao.R;
import com.poomoo.edao.application.eDaoClientApplication;
import com.poomoo.edao.config.eDaoClientConfig;
import com.poomoo.edao.util.Utity;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

/**
 * 
 * @ClassName My2DimenCodeActivity
 * @Description TODO 我的二维码
 * @author 李苜菲
 * @date 2015年9月9日 下午8:13:09
 */
public class My2DimenCodeActivity extends BaseActivity implements OnClickListener {
	private ImageView imageView_return, imageView_2code;
	private Button button_save;
	private static final String path = Environment.getExternalStorageDirectory() + "/my2code.jpg";
	private static final File my2code = new File(path);
	private eDaoClientApplication application = null;
	private Bitmap bitmap = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my2dimencode);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.my2dimencode_layout));
		application = (eDaoClientApplication) getApplication();
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根
		imageView_return = (ImageView) findViewById(R.id.my2dimencode_imageView_return);
		imageView_2code = (ImageView) findViewById(R.id.my2dimencode_imageView_2code);

		button_save = (Button) findViewById(R.id.my2dimencode_btn_save);

		imageView_return.setOnClickListener(this);
		button_save.setOnClickListener(this);

		// 使用ImageLoader加载网络图片
		DisplayImageOptions options = new DisplayImageOptions.Builder()//
				.showImageOnLoading(R.drawable.ic_launcher) // 加载中显示的默认图片
				.showImageOnFail(R.drawable.ic_launcher) // 设置加载失败的默认图片
				.cacheInMemory(false) // 内存缓存
				.cacheOnDisk(true) // sdcard缓存
				.bitmapConfig(Config.RGB_565)// 设置最低配置
				.build();
		bitmap = ImageLoader.getInstance().loadImageSync(application.getQuickmarkPic(), options);
		System.out.println("url:" + application.getQuickmarkPic() + "bitmap:" + bitmap);
		imageView_2code.setImageBitmap(bitmap);
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.my2dimencode_imageView_return:
			finish();
			break;
		case R.id.my2dimencode_btn_save:
			saveBitmap(bitmap);
			break;
		}
	}

	private void saveBitmap(Bitmap bitmap) {
		if (bitmap == null) {
			Utity.showToast(getApplicationContext(), "图片不存在");
			return;
		}
		if (my2code.exists()) {
			my2code.delete();
		}

		try {
			FileOutputStream out = new FileOutputStream(my2code);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
			out.flush();
			out.close();
			Utity.showToast(getApplicationContext(), eDaoClientConfig.save2code + path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
