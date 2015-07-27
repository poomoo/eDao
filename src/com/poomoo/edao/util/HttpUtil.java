package com.poomoo.edao.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {
	/**
	 * 
	 * 
	 * @Title: SendPostRequest
	 * @Description: TODO
	 * @author 李苜菲
	 * @return
	 * @return void
	 * @throws
	 * @date 2015-7-24下午3:07:36
	 */
	public void SendPostRequest(final String json, final String address,
			final HttpCallbackListener listener) {
		new Thread(new Runnable() {
			public void run() {
				HttpURLConnection connection = null;
				try {
					URL url = new URL(address);
					connection = (HttpURLConnection) url.openConnection();
					connection.setDoOutput(true);
					connection.setDoInput(true);
					connection.setRequestMethod("POST");
					// connection.setReadTimeout(5000);
					connection.setUseCaches(false);
					connection.setInstanceFollowRedirects(true);
					connection.setRequestProperty("Content-Type",
							"application/json");
					connection.setConnectTimeout(1 * 60 * 1000);
					connection.connect();

					if (json != null && json.trim().length() > 0) {
						DataOutputStream out = new DataOutputStream(
								connection.getOutputStream());
						byte[] content = json.getBytes("utf-8");
						out.write(content, 0, content.length);
						out.flush();
						out.close();
					}

					InputStream inputStream = connection.getInputStream();
					BufferedReader bufferedReader = new BufferedReader(
							new InputStreamReader(inputStream));

					StringBuilder response = new StringBuilder();
					String line = "";
					while ((line = bufferedReader.readLine()) != null) {
						response.append(line);
					}

					if (listener != null) {
						listener.onFinish(response.toString());
					}
				} catch (Exception e) {
					if (listener != null) {
						listener.onError(e);
					}
				} finally {
					if (connection != null)
						connection.disconnect();
				}
			}
		});
	}

	/**
	 * 
	 * 
	 * @Title: SendGetRequest
	 * @Description: TODO 
	 * @author 李苜菲
	 * @return
	 * @return void
	 * @throws
	 * @date 2015-7-24下午3:07:47
	 */
	public static void SendGetRequest(final String address,
			final HttpCallbackListener listener) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				HttpURLConnection connection = null;
				try {
					URL url = new URL(address);
					connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(8000);
					connection.setReadTimeout(8000);
					InputStream in = connection.getInputStream();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(in));
					StringBuilder response = new StringBuilder();
					String line;
					while ((line = reader.readLine()) != null) {
						response.append(line);
					}
					if (listener != null) {
						listener.onFinish(response.toString());
					}
				} catch (Exception e) {
					if (listener != null) {
						listener.onError(e);
					}
				} finally {
					if (connection != null) {
						connection.disconnect();
					}
				}
			}
		}).start();
	}
}
