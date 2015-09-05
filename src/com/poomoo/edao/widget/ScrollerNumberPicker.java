package com.poomoo.edao.widget;

import java.util.ArrayList;
import java.util.Vector;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader.TileMode;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.poomoo.edao.R;
import com.poomoo.edao.model.database.AreaInfo;
import com.poomoo.edao.model.database.CityInfo;
import com.poomoo.edao.model.database.ProvinceInfo;

/**
 * 
 * 滑动选择
 * 
 * @author zihao
 * 
 */
public class ScrollerNumberPicker extends View {
	/** 控件宽度 */
	private float controlWidth;
	/** 控件高度 */
	private float controlHeight;
	/** 是否滑动中 */
	private boolean isScrolling = false;
	/** 选择的内容 */
	private ArrayList<ItemObject> itemList = new ArrayList<ScrollerNumberPicker.ItemObject>();
	/** 设置数据 */
	private ArrayList<ProvinceInfo> provinceList = new ArrayList<ProvinceInfo>();
	private ArrayList<CityInfo> cityList = new ArrayList<CityInfo>();
	private ArrayList<AreaInfo> areaList = new ArrayList<AreaInfo>();
	/** 按下的坐标 */
	private int downY;
	/** 按下的时间 */
	private long downTime = 0;
	/** 短促移动 */
	private long goonTime = 200;
	/** 短促移动距离 */
	private int goonDistence = 100;
	/** 画线画笔 */
	private Paint linePaint;
	/** 线的默认颜色 */
	private int lineColor = 0xff000000;
	/** 默认字体 */
	private float normalFont = 14.0f;
	/** 长度超过6位时字体 */
	private float overLengthFont = 12.0f;
	/** 选中的时候字体 */
	private float selectedFont = 22.0f;
	/** 单元格高度 */
	private int unitHeight = 50;
	/** 显示多少个内容 */
	private int itemNumber = 7;
	/** 默认字体颜色 */
	private int normalColor = 0xff000000;
	/** 选中时候的字体颜色 */
	private int selectedColor = 0xffff0000;
	/** 蒙板高度 */
	private float maskHight = 48.0f;
	/** 选择监听 */
	private OnSelectListener onSelectListener;
	/** 是否可用 */
	private boolean isEnable = true;
	/** 刷新界面 */
	private static final int REFRESH_VIEW = 0x001;
	/** 移动距离 */
	private static final int MOVE_NUMBER = 5;
	/** 是否允许选空 */
	private boolean noEmpty = false;

	/** 正在修改数据，避免ConcurrentModificationException异常 */
	private boolean isClearing = false;

	public ScrollerNumberPicker(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init(context, attrs);
		// initData();
	}

	public ScrollerNumberPicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(context, attrs);
		// initData();
	}

	public ScrollerNumberPicker(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		// initData();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub

		if (!isEnable)
			return true;
		int y = (int) event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			isScrolling = true;
			downY = (int) event.getY();
			downTime = System.currentTimeMillis();
			break;
		case MotionEvent.ACTION_MOVE:
			actionMove(y - downY);
			onSelectListener();
			break;
		case MotionEvent.ACTION_UP:

			// 移动距离的绝对值
			int move = (y - downY);
			move = move > 0 ? move : move * (-1);
			// 判断段时间移动的距离
			if (System.currentTimeMillis() - downTime < goonTime
					&& move > goonDistence) {
				goonMove(y - downY);
			} else {
				actionUp(y - downY);
			}
			noEmpty();
			isScrolling = false;
			break;
		default:
			break;
		}
		return true;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);

		drawLine(canvas);
		drawList(canvas);
		// drawMask(canvas);
	}

	private synchronized void drawList(Canvas canvas) {
		if (isClearing)
			return;
		try {
			for (ItemObject itemObject : itemList) {
				itemObject.drawSelf(canvas);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		// TODO Auto-generated method stub
		super.onLayout(changed, left, top, right, bottom);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		controlWidth = getWidth();
		if (controlWidth != 0) {
			setMeasuredDimension(getWidth(), itemNumber * unitHeight);
			controlWidth = getWidth();
		}

	}

	/**
	 * 继续移动一定距离
	 */
	private synchronized void goonMove(final int move) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				int distence = 0;
				while (distence < unitHeight * MOVE_NUMBER) {
					try {
						Thread.sleep(5);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					actionThreadMove(move > 0 ? distence : distence * (-1));
					distence += 10;

				}
				actionUp(move > 0 ? distence - 10 : distence * (-1) + 10);
				noEmpty();
			}
		}).start();
	}

	/**
	 * 不能为空，必须有选项
	 */
	private void noEmpty() {
		if (!noEmpty)
			return;
		for (ItemObject item : itemList) {
			if (item.isSelected())
				return;
		}
		int move = (int) itemList.get(0).moveToSelected();
		if (move < 0) {
			defaultMove(move);
		} else {
			defaultMove((int) itemList.get(itemList.size() - 1)
					.moveToSelected());
		}
		for (ItemObject item : itemList) {
			if (item.isSelected()) {
				if (onSelectListener != null)
					onSelectListener.endSelect(item.id, item.itemText);
				break;
			}
		}
	}

	/**
	 * 初始化省份数据
	 */
	private void initProvinceData() {
		isClearing = true;
		itemList.clear();
		for (int i = 0; i < provinceList.size(); i++) {
			ItemObject itmItemObject = new ItemObject();
			itmItemObject.id = i;
			itmItemObject.itemId = provinceList.get(i).getProvince_id();
			itmItemObject.itemText = provinceList.get(i).getProvince_name();
			itmItemObject.x = 0;
			itmItemObject.y = i * unitHeight;
			itemList.add(itmItemObject);
		}
		isClearing = false;

	}

	/**
	 * 初始化城市数据
	 */
	private void initCityData() {
		isClearing = true;
		itemList.clear();
		if (cityList.size() == 0) {
			ItemObject itmItemObject = new ItemObject();
			itmItemObject.id = 0;
			itmItemObject.itemId = -1 + "";
			itmItemObject.itemText = "";
			itmItemObject.x = 0;
			itmItemObject.y = 0 * unitHeight;
			itemList.add(itmItemObject);
		} else
			for (int i = 0; i < cityList.size(); i++) {
				ItemObject itmItemObject = new ItemObject();
				itmItemObject.id = i;
				itmItemObject.itemId = cityList.get(i).getCity_id();
				itmItemObject.itemText = cityList.get(i).getCity_name();
				itmItemObject.x = 0;
				itmItemObject.y = i * unitHeight;
				itemList.add(itmItemObject);
			}
		isClearing = false;

	}

	/**
	 * 初始化区域数据
	 */
	private void initAreaData() {
		isClearing = true;
		itemList.clear();
		if (areaList.size() == 0) {
			ItemObject itmItemObject = new ItemObject();
			itmItemObject.id = 0;
			itmItemObject.itemId = -1 + "";
			itmItemObject.itemText = "";
			itmItemObject.x = 0;
			itmItemObject.y = 0 * unitHeight;
			itemList.add(itmItemObject);
		} else
			for (int i = 0; i < areaList.size(); i++) {
				ItemObject itmItemObject = new ItemObject();
				itmItemObject.id = i;
				itmItemObject.itemId = areaList.get(i).getArea_id();
				itmItemObject.itemText = areaList.get(i).getArea_name();
				itmItemObject.x = 0;
				itmItemObject.y = i * unitHeight;
				itemList.add(itmItemObject);
			}
		isClearing = false;

	}

	/**
	 * 移动的时候
	 * 
	 * @param move
	 */
	private void actionMove(int move) {
		for (ItemObject item : itemList) {
			item.move(move);
		}
		invalidate();
	}

	/**
	 * 移动，线程中调用
	 * 
	 * @param move
	 */
	private void actionThreadMove(int move) {
		for (ItemObject item : itemList) {
			item.move(move);
		}
		Message rMessage = new Message();
		rMessage.what = REFRESH_VIEW;
		handler.sendMessage(rMessage);
	}

	/**
	 * 松开的时候
	 * 
	 * @param move
	 */
	private void actionUp(int move) {
		int newMove = 0;
		if (move > 0) {
			for (int i = 0; i < itemList.size(); i++) {
				if (itemList.get(i).isSelected()) {
					newMove = (int) itemList.get(i).moveToSelected();
					if (onSelectListener != null)
						onSelectListener.endSelect(itemList.get(i).id,
								itemList.get(i).itemText);
					break;
				}
			}
		} else {
			for (int i = itemList.size() - 1; i >= 0; i--) {
				if (itemList.get(i).isSelected()) {
					newMove = (int) itemList.get(i).moveToSelected();
					if (onSelectListener != null)
						onSelectListener.endSelect(itemList.get(i).id,
								itemList.get(i).itemText);
					break;
				}
			}
		}
		for (ItemObject item : itemList) {
			item.newY(move + 0);
		}
		slowMove(newMove);
		Message rMessage = new Message();
		rMessage.what = REFRESH_VIEW;
		handler.sendMessage(rMessage);

	}

	/**
	 * 缓慢移动
	 * 
	 * @param move
	 */
	private synchronized void slowMove(final int move) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// 判断政府
				int m = move > 0 ? move : move * (-1);
				int i = move > 0 ? 1 : (-1);
				// 移动速度
				int speed = 1;
				while (true) {
					m = m - speed;
					if (m <= 0) {
						for (ItemObject item : itemList) {
							item.newY(m * i);
						}
						Message rMessage = new Message();
						rMessage.what = REFRESH_VIEW;
						handler.sendMessage(rMessage);
						try {
							Thread.sleep(2);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;
					}
					for (ItemObject item : itemList) {
						item.newY(speed * i);
					}
					Message rMessage = new Message();
					rMessage.what = REFRESH_VIEW;
					handler.sendMessage(rMessage);
					try {
						Thread.sleep(2);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				for (ItemObject item : itemList) {
					if (item.isSelected()) {
						if (onSelectListener != null)
							onSelectListener.endSelect(item.id, item.itemText);
						break;
					}
				}

			}
		}).start();
	}

	/**
	 * 移动到默认位置
	 * 
	 * @param move
	 */
	private void defaultMove(int move) {
		for (ItemObject item : itemList) {
			item.newY(move);
		}
		Message rMessage = new Message();
		rMessage.what = REFRESH_VIEW;
		handler.sendMessage(rMessage);
	}

	/**
	 * 滑动监听
	 */
	private void onSelectListener() {
		if (onSelectListener == null)
			return;
		for (ItemObject item : itemList) {
			if (item.isSelected()) {
				onSelectListener.selecting(item.id, item.itemText);
			}
		}
	}

	/**
	 * 绘制线条
	 * 
	 * @param canvas
	 */
	private void drawLine(Canvas canvas) {

		if (linePaint == null) {
			linePaint = new Paint();
			linePaint.setColor(lineColor);
			linePaint.setAntiAlias(true);
			linePaint.setStrokeWidth(1f);
		}

		canvas.drawLine(0, controlHeight / 2 - unitHeight / 2 + 2,
				controlWidth, controlHeight / 2 - unitHeight / 2 + 2, linePaint);
		canvas.drawLine(0, controlHeight / 2 + unitHeight / 2 - 2,
				controlWidth, controlHeight / 2 + unitHeight / 2 - 2, linePaint);
	}

	/**
	 * 绘制遮盖板
	 * 
	 * @param canvas
	 */
	private void drawMask(Canvas canvas) {
		LinearGradient lg = new LinearGradient(0, 0, 0, maskHight, 0x00f2f2f2,
				0x00f2f2f2, TileMode.MIRROR);
		Paint paint = new Paint();
		paint.setShader(lg);
		canvas.drawRect(0, 0, controlWidth, maskHight, paint);

		LinearGradient lg2 = new LinearGradient(0, controlHeight - maskHight,
				0, controlHeight, 0x00f2f2f2, 0x00f2f2f2, TileMode.MIRROR);
		Paint paint2 = new Paint();
		paint2.setShader(lg2);
		canvas.drawRect(0, controlHeight - maskHight, controlWidth,
				controlHeight, paint2);

	}

	/**
	 * 初始化，获取设置的属性
	 * 
	 * @param context
	 * @param attrs
	 */
	private void init(Context context, AttributeSet attrs) {
		TypedArray attribute = context.obtainStyledAttributes(attrs,
				R.styleable.NumberPicker);
		unitHeight = (int) attribute.getDimension(
				R.styleable.NumberPicker_unitHight, 32);
		normalFont = attribute.getDimension(
				R.styleable.NumberPicker_normalTextSize, 14.0f);
		overLengthFont = attribute.getDimension(
				R.styleable.NumberPicker_overLengthTextSize, 12.0f);
		selectedFont = attribute.getDimension(
				R.styleable.NumberPicker_selecredTextSize, 22.0f);
		itemNumber = attribute.getInt(R.styleable.NumberPicker_itemNumber, 7);
		normalColor = attribute.getColor(
				R.styleable.NumberPicker_normalTextColor, 0xff000000);
		selectedColor = attribute.getColor(
				R.styleable.NumberPicker_selecredTextColor, 0xffff0000);
		lineColor = attribute.getColor(R.styleable.NumberPicker_lineColor,
				0xff000000);
		maskHight = attribute.getDimension(R.styleable.NumberPicker_maskHight,
				48.0f);
		noEmpty = attribute.getBoolean(R.styleable.NumberPicker_noEmpty, false);
		isEnable = attribute
				.getBoolean(R.styleable.NumberPicker_isEnable, true);
		attribute.recycle();

		controlHeight = itemNumber * unitHeight;

	}

	/**
	 * 设置省份数据
	 * 
	 * @param data
	 */
	public void setProcinceData(ArrayList<ProvinceInfo> data) {
		this.provinceList = data;
		initProvinceData();
	}

	/**
	 * 设置省份数据
	 * 
	 * @param data
	 */
	public void setCityData(ArrayList<CityInfo> data) {
		this.cityList = data;
		initCityData();
	}

	/**
	 * 设置省份数据
	 * 
	 * @param data
	 */
	public void setAreaData(ArrayList<AreaInfo> data) {
		this.areaList = data;
		initAreaData();
	}

	/**
	 * 获取返回项
	 * 
	 * @return
	 */
	public int getSelected() {
		for (ItemObject item : itemList) {
			if (item.isSelected())
				return item.id;
		}
		return -1;
	}

	/**
	 * 获取返回的内容
	 * 
	 * @return
	 */
	public String getSelectedText() {
		for (ItemObject item : itemList) {
			if (item.isSelected())
				return item.itemText;
		}
		return "";
	}

	/**
	 * 是否正在滑动
	 * 
	 * @return
	 */
	public boolean isScrolling() {
		return isScrolling;
	}

	/**
	 * 是否可用
	 * 
	 * @return
	 */
	public boolean isEnable() {
		return isEnable;
	}

	/**
	 * 设置是否可用
	 * 
	 * @param isEnable
	 */
	public void setEnable(boolean isEnable) {
		this.isEnable = isEnable;
	}

	/**
	 * 设置默认选项
	 * 
	 * @param index
	 */
	public void setDefault(int index) {
		float move = itemList.get(index).moveToSelected();
		defaultMove((int) move);
	}

	/**
	 * 获取列表大小
	 * 
	 * @return
	 */
	public int getListSize() {
		if (itemList == null)
			return 0;
		return itemList.size();
	}

	/**
	 * 获取某项的内容
	 * 
	 * @param index
	 * @return
	 */
	public String getItemText(int index) {
		if (itemList == null)
			return "";
		return itemList.get(index).itemText;
	}

	/**
	 * 获取某项的id
	 * 
	 * @param index
	 * @return
	 */
	public String getItemId(int index) {
		if (itemList == null)
			return "";
		return itemList.get(index).itemId;
	}

	/**
	 * 监听
	 * 
	 * @param onSelectListener
	 */
	public void setOnSelectListener(OnSelectListener onSelectListener) {
		this.onSelectListener = onSelectListener;
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case REFRESH_VIEW:
				invalidate();
				break;
			default:
				break;
			}
		}

	};

	/**
	 * 单条内容
	 * 
	 * @author limufei
	 */
	private class ItemObject {
		/** id */
		public int id = 0;
		/** 内容 */
		public String itemId = "";
		public String itemText = "";
		/** x坐标 */
		public int x = 0;
		/** y坐标 */
		public int y = 0;
		/** 移动距离 */
		public int move = 0;
		/** 字体画笔 */
		private Paint textPaint;
		/** 字体范围矩形 */
		private Rect textRect;

		public ItemObject() {
			super();
		}

		/**
		 * 绘制自身
		 * 
		 * @param canvas
		 */
		public void drawSelf(Canvas canvas) {

			if (textPaint == null) {
				textPaint = new Paint();
				textPaint.setAntiAlias(true);
			}

			if (textRect == null)
				textRect = new Rect();

			Vector<String> m_String = new Vector<String>();
			int textWidth = itemText.length();

			if (itemText.length() > 6 && !itemText.contains("(")) {
				m_String.addElement(itemText.substring(0, 6));
				m_String.addElement(itemText.substring(6, itemText.length()));
				textWidth = 6;
			}

			// 判断是否被选择
			if (isSelected()) {
				textPaint.setColor(selectedColor);
				// 获取距离标准位置的距离
				float moveToSelect = moveToSelected();
				moveToSelect = moveToSelect > 0 ? moveToSelect : moveToSelect
						* (-1);
				// 计算当前字体大小
				if (itemText.length() < 5) {
					float textSize = (float) normalFont
							+ ((float) (selectedFont - normalFont) * (1.0f - (float) moveToSelect
									/ (float) unitHeight));
					textPaint.setTextSize(textSize);
				} else if (itemText.length() < 7)
					textPaint.setTextSize(normalFont);
				else
					textPaint.setTextSize(overLengthFont);
			} else {
				textPaint.setColor(normalColor);
				if (itemText.length() < 7)
					textPaint.setTextSize(normalFont);
				else
					textPaint.setTextSize(overLengthFont);

			}

			// 返回包围整个字符串的最小的一个Rect区域
			textPaint.getTextBounds(itemText, 0, textWidth, textRect);
			// 判断是否可视
			if (!isInView())
				return;

			// 绘制内容
			if (m_String.size() > 0)
				for (int i = 0; i < m_String.size(); i++)
					canvas.drawText(m_String.elementAt(i), x + controlWidth / 2
							- textRect.width() / 2, y + move + unitHeight / 3
							+ textRect.height() / 2 + (unitHeight / 3) * i,
							textPaint);
			else
				canvas.drawText(itemText,
						x + controlWidth / 2 - textRect.width() / 2, y + move
								+ unitHeight / 2 + textRect.height() / 2,
						textPaint);

		}

		/**
		 * 是否在可视界面内
		 * 
		 * @param rect
		 * @return
		 */
		public boolean isInView() {
			if (y + move > controlHeight
					|| (y + move + unitHeight / 2 + textRect.height() / 2) < 0)
				return false;
			return true;
		}

		/**
		 * 移动距离
		 * 
		 * @param _move
		 */
		public void move(int _move) {
			this.move = _move;
		}

		/**
		 * 设置新的坐标
		 * 
		 * @param move
		 */
		public void newY(int _move) {
			this.move = 0;
			this.y = y + _move;
		}

		/**
		 * 判断是否在选择区域内
		 * 
		 * @return
		 */
		public boolean isSelected() {
			if ((y + move) >= controlHeight / 2 - unitHeight / 2 + 2
					&& (y + move) <= controlHeight / 2 + unitHeight / 2 - 2)
				return true;
			if ((y + move + unitHeight) >= controlHeight / 2 - unitHeight / 2
					+ 2
					&& (y + move + unitHeight) <= controlHeight / 2
							+ unitHeight / 2 - 2)
				return true;
			if ((y + move) <= controlHeight / 2 - unitHeight / 2 + 2
					&& (y + move + unitHeight) >= controlHeight / 2
							+ unitHeight / 2 - 2)
				return true;
			return false;
		}

		/**
		 * 获取移动到标准位置需要的距离
		 */
		public float moveToSelected() {
			return (controlHeight / 2 - unitHeight / 2) - (y + move);
		}
	}

	/**
	 * 选择监听监听
	 * 
	 * @author limufei
	 * 
	 */
	public interface OnSelectListener {

		/**
		 * 结束选择
		 * 
		 * @param id
		 * @param text
		 */
		public void endSelect(int id, String text);

		/**
		 * 选中的内容
		 * 
		 * @param id
		 * @param text
		 */
		public void selecting(int id, String text);

	}
}
