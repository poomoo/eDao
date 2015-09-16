package com.poomoo.edao.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.conn.util.InetAddressUtils;
import org.litepal.crud.DataSupport;

import com.poomoo.edao.application.eDaoClientApplication;
import com.poomoo.edao.model.database.AreaInfo;
import com.poomoo.edao.model.database.CityInfo;
import com.poomoo.edao.model.database.ProvinceInfo;
import com.tencent.mm.sdk.openapi.IWXAPI;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @ClassName Utity
 * @Description TODO 工具类
 * @author 李苜菲
 * @date 2015-8-13 下午3:45:08
 */
public class Utity {
	/**
	 * 
	 * 
	 * @Title: showToast
	 * @Description: TODO 吐司
	 * @author 李苜菲
	 * @return
	 * @return void
	 * @throws @date
	 *             2015-8-13下午3:46:02
	 */
	public static void showToast(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 
	 * 
	 * @Title: getProvinceList
	 * @Description: TODO 获取省份列表
	 * @author 李苜菲
	 * @return
	 * @return ArrayList<ProvinceInfo>
	 * @throws @date
	 *             2015-8-17上午10:38:47
	 */
	public static ArrayList<ProvinceInfo> getProvinceList() {
		// ProvinceInfo provinceInfo =
		// DataSupport.findFirst(ProvinceInfo.class);
		// System.out.println(provinceInfo.getProvince_name());
		List<ProvinceInfo> provinceList = DataSupport.findAll(ProvinceInfo.class);
		return (ArrayList<ProvinceInfo>) provinceList;
	}

	/**
	 * 
	 * 
	 * @Title: getCityList
	 * @Description: TODO 获取城市列表
	 * @author 李苜菲
	 * @return
	 * @return ArrayList<CityInfo>
	 * @throws @date
	 *             2015-8-17上午10:39:44
	 */
	public static ArrayList<CityInfo> getCityList(String province_id) {
		List<CityInfo> cityList = DataSupport.where("provinceinfo_id = ?", province_id).find(CityInfo.class);
		return (ArrayList<CityInfo>) cityList;
	}

	/**
	 * 
	 * 
	 * @Title: getAreaList
	 * @Description: TODO 获取区域列表
	 * @author 李苜菲
	 * @return
	 * @return ArrayList<AreaInfo>
	 * @throws @date
	 *             2015-8-17上午10:40:43
	 */
	public static ArrayList<AreaInfo> getAreaList(String city_id) {
		List<AreaInfo> areaList = DataSupport.where("cityinfo_id = ?", city_id).find(AreaInfo.class);
		return (ArrayList<AreaInfo>) areaList;
	}

	/**
	 * 
	 * 
	 * @Title: getProvincePosition
	 * @Description: TODO 查找某省份所在表中位置
	 * @author 李苜菲
	 * @return
	 * @return int
	 * @throws @date
	 *             2015年8月17日下午9:53:53
	 */
	public static int getProvincePosition(ArrayList<ProvinceInfo> provinceList, String province) {
		System.out.println("getProvincePosition:" + province);
		int i = 0;
		for (ProvinceInfo provinceInfo : provinceList) {
			i++;
			System.out.println("provinceInfo.getProvince_name():" + provinceInfo.getProvince_name() + "i:" + i);
			if (provinceInfo.getProvince_name().equals(province))
				return i - 1;
		}
		return 0;
	}

	/**
	 * 
	 * 
	 * @Title: getCityPosition
	 * @Description: TODO 查找某城市所在表中位置
	 * @author 李苜菲
	 * @return
	 * @return int
	 * @throws @date
	 *             2015年8月17日下午9:57:59
	 */
	public static int getCityPosition(ArrayList<CityInfo> cityList, String city) {
		System.out.println("getCityPosition:" + city);
		int i = 0;
		for (CityInfo cityInfo : cityList) {
			i++;
			System.out.println("cityInfo.getCity_name():" + cityInfo.getCity_name() + "i:" + i);
			if (cityInfo.getCity_name().equals(city))
				return i - 1;
		}
		return 0;
	}

	/**
	 * 
	 * 
	 * @Title: getAreaPosition
	 * @Description: TODO 查找某区域所在表中位置
	 * @author 李苜菲
	 * @return
	 * @return int
	 * @throws @date
	 *             2015年8月31日下午11:41:41
	 */
	public static int getAreaPosition(ArrayList<AreaInfo> areaList, String area) {
		int i = 0;
		for (AreaInfo areaInfo : areaList) {
			i++;
			System.out.println("areaInfo.getArea_name():" + areaInfo.getArea_name() + "  " + area);
			if (areaInfo.getArea_name().equals(area))
				return i - 1;
		}
		return 0;
	}

	/**
	 * 
	 * 
	 * @Title: setOnTextChanged
	 * @Description: TODO 输入的数字每隔4位加一个空格
	 * @author 李苜菲
	 * @return
	 * @return void
	 * @throws @date
	 *             2015-8-18下午3:14:34
	 */
	public static void setOnTextChanged(final EditText et) {
		et.addTextChangedListener(new TextWatcher() {
			int beforeTextLength = 0;
			int onTextLength = 0;
			boolean isChanged = false;

			int location = 0;// 记录光标的位置
			private char[] tempChar;
			private StringBuffer buffer = new StringBuffer();
			int konggeNumberB = 0;

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				onTextLength = s.length();
				buffer.append(s.toString());
				if (onTextLength == beforeTextLength || onTextLength <= 3 || isChanged) {
					isChanged = false;
					return;
				}
				isChanged = true;
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				beforeTextLength = s.length();
				if (buffer.length() > 0) {
					buffer.delete(0, buffer.length());
				}
				konggeNumberB = 0;
				for (int i = 0; i < s.length(); i++) {
					if (s.charAt(i) == ' ') {
						konggeNumberB++;
					}
				}
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (isChanged) {
					location = et.getSelectionEnd();
					int index = 0;
					while (index < buffer.length()) {
						if (buffer.charAt(index) == ' ') {
							buffer.deleteCharAt(index);
						} else {
							index++;
						}
					}

					index = 0;
					int konggeNumberC = 0;
					while (index < buffer.length()) {
						if (index % 5 == 4) {
							buffer.insert(index, ' ');
							konggeNumberC++;
						}
						index++;
					}

					if (konggeNumberC > konggeNumberB) {
						location += (konggeNumberC - konggeNumberB);
					}

					tempChar = new char[buffer.length()];
					buffer.getChars(0, buffer.length(), tempChar, 0);
					String str = buffer.toString();
					if (location > str.length()) {
						location = str.length();
					} else if (location < 0) {
						location = 0;
					}

					et.setText(str);
					Editable etable = et.getText();
					Selection.setSelection(etable, location);
					isChanged = false;
				}
			}
		});
	}

	/**
	 * 
	 * 
	 * @Title: revitionImageSize
	 * @Description: TODO 缩小图片显示
	 * @author 李苜菲
	 * @return
	 * @return Bitmap
	 * @throws @date
	 *             2015年8月18日下午10:45:00
	 */
	public static Bitmap revitionImageSize(String path) {
		System.out.println("revitionImageSize:" + path);
		BufferedInputStream in;
		Bitmap bitmap = null;
		try {
			in = new BufferedInputStream(new FileInputStream(new File(path)));

			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(in, null, options);
			in.close();
			int i = 0;

			while (true) {
				if ((options.outWidth >> i <= 1000) && (options.outHeight >> i <= 1000)) {
					in = new BufferedInputStream(new FileInputStream(new File(path)));
					options.inSampleSize = (int) Math.pow(2.0D, i);
					// options.inSampleSize = calculateInSampleSize(options,
					// 480, 800);
					options.inJustDecodeBounds = false;
					bitmap = BitmapFactory.decodeStream(in, null, options);
					break;
				}
				i += 1;
			}

		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			System.out.println("revitionImageSize Exception：" + e.getMessage());
		} finally {
			System.out.println("revitionImageSize结束 bitmap:" + bitmap);
			return bitmap;
		}
	}

	/**
	 * 
	 * 
	 * @Title: getBitMap
	 * @Description: TODO 获取Bitmap
	 * @author 李苜菲
	 * @return
	 * @return Bitmap
	 * @throws @date
	 *             2015-8-19下午3:28:53
	 */
	public static Bitmap getBitMap(String path) {
		System.out.println("getBitMap path:" + path);
		Bitmap bitmap = null;
		BufferedInputStream in;

		try {
			in = new BufferedInputStream(new FileInputStream(new File(path)));

			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			bitmap = BitmapFactory.decodeStream(in, null, options);
			in.close();
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * 
	 * 
	 * @Title: trimAll
	 * @Description: TODO 去掉所有空格
	 * @author 李苜菲
	 * @return
	 * @return String
	 * @throws @date
	 *             2015-8-19下午3:01:50
	 */
	public static String trimAll(String string) {
		return string.replaceAll(" ", "");

	}

	/**
	 * 
	 * 
	 * @Title: bankAccount
	 * @Description: TODO 数字加*
	 * @author 李苜菲
	 * @return
	 * @return String
	 * @throws @date
	 *             2015-8-21下午3:31:32
	 */
	public static String addStarByNum(int begin, int end, String str) {
		if (TextUtils.isEmpty(str))
			return "";
		if (str.length() != 11 && str.length() != 19)
			return str;
		String temp = "";
		temp = str.substring(0, begin) + str.substring(begin, end).replaceAll("[0123456789]", "*")
				+ str.substring(end, str.length());
		return temp;
	}

	/**
	 * 
	 * 
	 * @Title: addStarByName
	 * @Description: TODO 名字加*号
	 * @author 李苜菲
	 * @return
	 * @return String
	 * @throws @date
	 *             2015-8-21下午3:41:44
	 */
	public static String addStarByName(String str) {
		if (TextUtils.isEmpty(str))
			return "";
		String temp = "";
		temp = "*" + str.substring(1, str.length());
		return temp;
	}

	/**
	 * 
	 * 
	 * @Title: getLocalHostIp
	 * @Description: TODO 获取设备IP地址
	 * @author 李苜菲
	 * @return
	 * @return String
	 * @throws @date
	 *             2015-8-29下午4:56:18
	 */
	public static String getLocalHostIp() {
		String ipaddress = "";
		try {
			Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
			// 遍历所用的网络接口
			while (en.hasMoreElements()) {
				NetworkInterface nif = en.nextElement();// 得到每一个网络接口绑定的所有ip
				Enumeration<InetAddress> inet = nif.getInetAddresses();
				// 遍历每一个接口绑定的所有ip
				while (inet.hasMoreElements()) {
					InetAddress ip = inet.nextElement();
					if (!ip.isLoopbackAddress() && InetAddressUtils.isIPv4Address(ip.getHostAddress())) {
						return ipaddress = ip.getHostAddress();
					}
				}

			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return ipaddress;

	}

	/**
	 * 
	 * 
	 * @Title: subZeroAndDot
	 * @Description: TODO 去掉多余的.和0
	 * @author 李苜菲
	 * @return
	 * @return String
	 * @throws @date
	 *             2015-8-29下午6:06:02
	 */
	public static String subZeroAndDot(String s) {
		if (s.indexOf(".") > 0) {
			s = s.replaceAll("0+?$", "");// 去掉多余的0
			s = s.replaceAll("[.]$", "");// 如最后一位是.则去掉
		}
		return s;
	}

	/**
	 * 
	 * 
	 * @Title: getLastNum
	 * @Description: TODO 获取一个整数的个位数
	 * @author 李苜菲
	 * @return
	 * @return int
	 * @throws @date
	 *             2015年8月30日上午12:51:58
	 */
	public static int getLastNum(int num) {
		String temp = String.valueOf(num);
		char ch = temp.charAt(temp.length() - 1);
		int re = Integer.parseInt(ch + "");
		System.out.println("getLastNum" + num + ":" + re);
		return re;
	}

	/**
	 * 
	 * 
	 * @Title: isWXAppInstalledAndSupported
	 * @Description: TODO 判断微信是否安装
	 * @author 李苜菲
	 * @return
	 * @return boolean
	 * @throws @date
	 *             2015年8月30日下午2:38:31
	 */
	public static boolean isWXAppInstalledAndSupported(Context context, IWXAPI api) {
		System.out.println("检查微信是否安装 api:" + api + "isWXAppInstalled：" + api.isWXAppInstalled() + "isWXAppSupportAPI:"
				+ api.isWXAppSupportAPI());
		boolean sIsWXAppInstalledAndSupported = api.isWXAppInstalled() && api.isWXAppSupportAPI();
		return sIsWXAppInstalledAndSupported;
	}

	/**
	 * 
	 * 
	 * @Title: setUserAndTel
	 * @Description: TODO 设置用户名字和手机号
	 * @author 李苜菲
	 * @return
	 * @return void
	 * @throws @date
	 *             2015年8月30日下午3:17:16
	 */
	public static void setUserAndTel(TextView textView_user, TextView textView_tel, eDaoClientApplication application) {
		textView_user.setText(Utity.addStarByName(application.getRealName()));
		textView_tel.setText(Utity.addStarByNum(3, 7, application.getTel()));
	}

	/**
	 * 
	 * 
	 * @Title: isSmaller
	 * @Description: TODO 时间比较大小
	 * @author 李苜菲
	 * @return
	 * @return boolean
	 * @throws @date
	 *             2015年9月9日下午11:05:24
	 */
	public static boolean isSmaller(String startDate, String endDate) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar_start = Calendar.getInstance();
		Calendar calendar_end = Calendar.getInstance();
		try {
			calendar_start.setTime(format.parse(startDate));
			calendar_end.setTime(format.parse(endDate));
		} catch (ParseException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

		int result = calendar_start.compareTo(calendar_end);
		if (result > 0)
			return false;
		return true;
	}

	/**
	 * 
	 * 
	 * @Title: encrypt
	 * @Description: TODO 加密
	 * @author 李苜菲
	 * @return
	 * @return String
	 * @throws @date
	 *             2015年9月16日上午11:55:59
	 */
	public static String encrypt(String seed, String cleartext) throws Exception {
		byte[] rawKey = getRawKey(seed.getBytes());
		byte[] result = encrypt(rawKey, cleartext.getBytes());
		return toHex(result);
	}

	/**
	 * 
	 * 
	 * @Title: decrypt
	 * @Description: TODO 解密
	 * @author 李苜菲
	 * @return
	 * @return String
	 * @throws @date
	 *             2015年9月16日上午11:56:09
	 */
	public static String decrypt(String seed, String encrypted) throws Exception {
		byte[] rawKey = getRawKey(seed.getBytes());
		byte[] enc = toByte(encrypted);
		byte[] result = decrypt(rawKey, enc);
		return new String(result);
	}

	private static byte[] getRawKey(byte[] seed) throws Exception {
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		// SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");
		sr.setSeed(seed);
		kgen.init(128, sr); // 192 and 256 bits may not be available
		SecretKey skey = kgen.generateKey();
		byte[] raw = skey.getEncoded();
		return raw;
	}

	private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		byte[] encrypted = cipher.doFinal(clear);
		return encrypted;
	}

	private static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		byte[] decrypted = cipher.doFinal(encrypted);
		return decrypted;
	}

	private static String toHex(String txt) {
		return toHex(txt.getBytes());
	}

	private static String fromHex(String hex) {
		return new String(toByte(hex));
	}

	private static byte[] toByte(String hexString) {
		int len = hexString.length() / 2;
		byte[] result = new byte[len];
		for (int i = 0; i < len; i++)
			result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
		return result;
	}

	private static String toHex(byte[] buf) {
		if (buf == null)
			return "";
		StringBuffer result = new StringBuffer(2 * buf.length);
		for (int i = 0; i < buf.length; i++) {
			appendHex(result, buf[i]);
		}
		return result.toString();
	}

	private final static String HEX = "0123456789ABCDEF";

	private static void appendHex(StringBuffer sb, byte b) {
		sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
	}
}
