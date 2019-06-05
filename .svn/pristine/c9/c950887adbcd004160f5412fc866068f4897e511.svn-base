
package hrst.sczd.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import android.content.Context;
import android.content.Intent;
/**
 * @author 沈月美
 * @describe 本类是http的访问类主要是控制访问时的操作
 * @date 2014.05.23
 * @version 1.1.1.3
 * 修改者，修改日期，修改内容
*/
public class HttpDownloader {
	private URL url = null;
	private long photo_max;

	/**
	 * 从网络上下载数据
	 * 
	 * @param context 上下文
	 * @param urlStr 路径
	 * @return String 内容
	 */
	public static String download(Context context,String urlStr) {
		StringBuffer sb = new StringBuffer();
		String line = null;
		BufferedReader buffer = null;
		byte[] all = null;
		try {
			// 创建一个URL对象
			URL url = new URL(urlStr);
			UserConfig.p("", "更新流程：" + url.toString());
			HttpURLConnection urlConn = (HttpURLConnection) url
					.openConnection();
			urlConn.setConnectTimeout(15000);
			urlConn.setReadTimeout(20000);
			InputStreamReader inputStreamReader = new InputStreamReader(
					urlConn.getInputStream());

			buffer = new BufferedReader(inputStreamReader);

			while ((line = buffer.readLine()) != null) {
				sb.append(line);
			}
			// Message message = new Message();

			buffer.close();

		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			Intent intent=new Intent("finish");
			context.sendBroadcast(intent);
			return "false";
		} catch (IOException e) {
			return "false";

		}
		return sb.toString();
	}
	

	
	/**
	 * 根据URL得到输入流
	 * 
	 * @param urlStr
	 * @return InputStream 流文件
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public InputStream getInputStreamFromUrl(String urlStr)
			throws MalformedURLException, IOException {

		try {
			url = new URL(urlStr);
		} catch (Exception e) {
			return null;
		}

		HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
		urlConn.setRequestMethod("GET");
		urlConn.setDoInput(true);
		urlConn.setConnectTimeout(15 * 1000);
		photo_max = urlConn.getContentLength();
		InputStream inputStream = urlConn.getInputStream();
		return inputStream;
	}

}
