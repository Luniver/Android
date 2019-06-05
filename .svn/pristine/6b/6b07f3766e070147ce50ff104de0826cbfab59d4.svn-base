package hrst.sczd.utils;

import hrst.sczd.ExitApplication;
import hrst.sczd.R;

import java.lang.reflect.Method;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
/**
 * @author 沈月美
 * @describe 网络连接
 * @date 2014.05.23
 * @version 1.1.1.3
 * 修改者，修改日期，修改内容
*/
public class CheckNetWork {

	Context context; // 上下文对象
	ConnectivityManager connectivityManager;
	WifiManager wifiManager; // wifi管理对象
	LocationManager locationManager; // 位置管理类对象
	Class cmClass;
	
	Button btnNetWorkCancel,btnNetWorkSet;

	/**
	 * 构造方法
	 * 
	 * @param context
	 */
	public CheckNetWork(Context context) {
		this.context = context;
		connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		cmClass = connectivityManager.getClass();
	}

	/**
	 * 判断连接网络的是否是GPRS，如果是则返回true，否则返回false
	 * 
	 * @return
	 */
	public boolean isConnectGPRS() {
		if (context != null) {

			Class[] argClasses = null;
			Object[] argObject = null;

			Boolean isOpen = false;
			try {
				Method method = cmClass.getMethod("getMobileDataEnabled",
						argClasses);

				isOpen = (Boolean) method
						.invoke(connectivityManager, argObject);
			} catch (Exception e) {
				return false;
			}

			return isOpen;
		}
		return false;

	}

	/**
	 * 用GPRS的方式连接网路，如果连接成功则返回true，否则返回false
	 * 
	 * @return
	 */
	public boolean connectGPRS() {
		Object[] argObjects = null;

		boolean isOpen = isConnectGPRS();
		if (isOpen == !true) {
			Class[] argClasses = new Class[1];
			argClasses[0] = boolean.class;
			try {
				Method method = cmClass.getMethod("setMobileDataEnabled",
						argClasses);
				method.invoke(connectivityManager, true);
			} catch (Exception e) {
				
				return false;
			}
		}

		return isOpen;
	}

	/**
	 * 关闭使用GPRS连接网络，如果关闭成功则返回true，否则返回false
	 * 
	 * @return
	 */
	public boolean disconnectGPRS() {
		Object[] argObjects = null;
		boolean isOpen = isConnectGPRS();
		if (isOpen == !false) {
			Class[] argClasses = new Class[1];
			argClasses[0] = boolean.class;

			try {
				Method method = cmClass.getMethod("setMobileDataEnabled",
						argClasses);
				method.invoke(connectivityManager, false);
			} catch (Exception e) {
				return false;
			}
		}
		return isOpen;

	}

	/**
	 *  开启/关闭GPRS
	 *  
	 * @param methodName 方法名
	 * @param isEnable 开打/关闭
	 */
	private void setGprsEnable(String methodName, boolean isEnable) {
		Class[] argClasses = new Class[1];
		argClasses[0] = boolean.class;

		try {
			Method method = cmClass.getMethod(methodName, argClasses);
			method.invoke(connectivityManager, isEnable);
		} catch (Exception e) {
		}
	}

	/**
	 * 判断是否是使用无线网络连接，如果是则返回true，否则返回false
	 * 
	 * @return
	 */
	public boolean isConnectWLAN() {
		if (context != null) {
			connectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo wiFiNetworkInfo = connectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (wiFiNetworkInfo != null) {
				return wiFiNetworkInfo.isAvailable();
			}
		}
		return false;

	}

	/**
	 * 使用WIFI连接到网络，如果连接成功，则返回true，否则返回false
	 * 
	 * @return
	 */
	public boolean connectWLAN() {
		try {
			wifiManager.setWifiEnabled(true);
		} catch (Exception e) {
			return false;
		}
		return true;

	}

	/**
	 * 断开使用wifi的方式连接网络，如果断开成功则返回true，否则返回flase
	 * 
	 * @return
	 */
	public boolean disconnectWLAN() {
		try {
			wifiManager.setWifiEnabled(false);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 判断是否打开了GPS，如果打开了则返回true，否则返回false
	 * 
	 * @return
	 */
	public boolean isOpenGPS() {
		return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}

	/**
	 * 打开移动终端的GPS，如果打开成功则返回true，否则返回false
	 * 
	 * @return
	 */
	public boolean openGPS() {

		Intent GPSIntent = new Intent();
		GPSIntent.setClassName("com.android.settings",
				"com.android.settings.widget.SettingsAppWidgetProvider");
		GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
		GPSIntent.setData(Uri.parse("custom:3"));
		try {
			PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
		} catch (CanceledException e) {
			return false;
		}

		return true;

	}

	/**
	 * 关闭移动终端的GPS，如果关闭成功则返回true，否则返回false
	 * 
	 * @return
	 */
	public boolean closeGPS() {
		return false;

	}

	/**
	 * 不管以何种方式，只要是网络连接状态则返回true，关闭状态则返回false
	 * 
	 * @return
	 */
	public boolean isConnectWeb() {
		if (context != null) {
			NetworkInfo networkInfo = connectivityManager
					.getActiveNetworkInfo();
			if (networkInfo != null) {
				return networkInfo.isAvailable();
			}
		}
		return false;

	}
	/**
	 * 網絡異常對話框
	 * 
	 * @param isexit 是否已退出
	 */
	public void netWorkDialog(final boolean isexit) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.dialog_network, null);
		final AlertDialog netWorkDialog = new AlertDialog.Builder(context).create();
		netWorkDialog.setView(view);
		btnNetWorkCancel = (Button) view.findViewById(R.id.btn_network_cancel);
		btnNetWorkSet = (Button) view.findViewById(R.id.btn_network_set);
		btnNetWorkCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {

				if (isexit) {
					ExitApplication.getInstance().exit();
				}else {
					netWorkDialog.dismiss();
				}
			}
		});
		btnNetWorkSet.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {

				if (android.os.Build.VERSION.SDK_INT > 10) {
					context.startActivity(new Intent(
							android.provider.Settings.ACTION_SETTINGS));
				} else {
					context.startActivity(new Intent(
							android.provider.Settings.ACTION_WIRELESS_SETTINGS));
				}
				if (isexit) {
					ExitApplication.getInstance().exit();
				}else {
					netWorkDialog.dismiss();
				}
				
			}
		});
		netWorkDialog.show();
	}
}
