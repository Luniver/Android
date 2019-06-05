package com.hrst.common.ui.errorlog;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.hrst.common.ui.HrstApplication;
import com.hrst.common.util.SharedPreferencesUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 黄水祥
 * @describe UncaughtException处理类,当程序发生Uncaught异常的时候,
 *           有该类来接管程序,并记录发送错误报告.
 * @date 2014.05.23
 * @version 1.1.1.3
 * 修改者，修改日期，修改内容
*/
public class CrashHandler implements UncaughtExceptionHandler {

	public static final String TAG = "CrashHandler";

	// 系统默认的UncaughtException处理类
	private UncaughtExceptionHandler mDefaultHandler;
	// CrashHandler实例
	private static CrashHandler INSTANCE = new CrashHandler();
	// 程序的Context对象
	private Context mContext;
	private HrstApplication appl;
	public static final int SHOW = 8;
	public static final int SECCESS = 0;
	public static final int DISS = 10;
	private CrashHandlerCallBack callBack;
	private int fileNum;
	
	// 用来存储设备信息和异常信息
	private Map<String, String> infos = new HashMap<String, String>();

	// 用于格式化日期,作为日志文件名的一部分
	private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

	/** 保证只有一个CrashHandler实例 */
	private CrashHandler() {
	}

	/** 获取CrashHandler实例 ,单例模式 */
	public static CrashHandler getInstance() {
		return INSTANCE;
	}

	/**
	 * 初始化
	 * 
	 * @param context
	 */
	public void init(Context context) {
		mContext = context;
		SharedPreferencesUtils.init(context);
		appl = (HrstApplication) mContext.getApplicationContext();
		// 获取系统默认的UncaughtException处理器
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		// 设置该CrashHandler为程序的默认处理器
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	/**
	 * 当UncaughtException发生时会转入该函数来处理
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		ex.printStackTrace();
		// 收集设备参数信息
		collectDeviceInfo(mContext);
		// 保存日志文件
		saveCrashInfo2File(ex);

		if (!handleException(ex) && mDefaultHandler != null) {
			// 如果用户没有处理则让系统默认的异常处理器来处理
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				Log.e(TAG, "error : ", e);
			}
		}
	}
 

	/**
	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
	 * 
	 * @param ex
	 * @return true:如果处理了该异常信息;否则返回false.
	 */
	private boolean handleException(Throwable ex) {
		if (ex == null) {
			return false;
		}
		// 使用Toast来显示异常信息
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				Looper.loop();
			}
		}.start();
		return true;
	}

	/**
	 * 收集设备参数信息
	 * 
	 * @param ctx
	 */
	public void collectDeviceInfo(Context ctx) {
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
					PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				String versionName = pi.versionName == null ? "null"
						: pi.versionName;
				String versionCode = pi.versionCode + "";
				infos.put("versionName", versionName);
				infos.put("versionCode", versionCode);
			}
		} catch (NameNotFoundException e) {
			Log.e(TAG, "an error occured when collect package info", e);
		}
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				infos.put(field.getName(), field.get(null).toString());
				Log.d(TAG, field.getName() + " : " + field.get(null));
			} catch (Exception e) {
				Log.e(TAG, "an error occured when collect crash info", e);
			}
		}
	}

	/**
	 * 保存错误信息到文件中
	 * 
	 * @param ex
	 * @return 返回文件名称,便于将文件传送到服务器
	 */
	private String saveCrashInfo2File(Throwable ex) {

		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, String> entry : infos.entrySet()) {
			String key = entry.getKey();
			String value = "";
			if(key.equals("TIME")){
				//把报错时间报错到文件中去
				value = System.currentTimeMillis()+"";
			}else{
				value = entry.getValue();
			}
			sb.append(key + "=" + value + "\n");
		}
		
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		String result = writer.toString();
		sb.append(result);
		
		try {
			long timestamp = System.currentTimeMillis();
			String time = formatter.format(new Date());
			//动态文件名
			
			String fileName = mContext.getPackageName()+"-" + time  + ".txt";
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				File cache = null;
				// todo 这里待定制, 针对不同项目传入不同的路径
				cache = new File(
						Environment.getExternalStorageDirectory(), "hrst/sczd/errorLog");
				if (!cache.exists()) {
					cache.mkdirs();
				}
				FileOutputStream fos = new FileOutputStream(cache + "/" + fileName);
				
				//新建一个数字标签，用以在内存读取中分辨多个文件
				fileNum= SharedPreferencesUtils.getInt("fileNum");
				//使用过一次的数字标签，自增1
				SharedPreferencesUtils.put("fileNum", fileNum+1);
				fos.write(sb.toString().getBytes());
				fos.close();
			}
			return fileName;
		} catch (Exception e) {
			Log.e(TAG, "an error occured while writing file...", e);
		}
		return null;
	}

	private void setFileNum() {
		fileNum = SharedPreferencesUtils.getInt("fileNum");
		fileNum-=1;
		if(fileNum>0){
			//fileNum > 0后,表示还有报错文件未上传，回调继续上传
			SharedPreferencesUtils.put("fileNum", fileNum);
			callBack.goOnUpLoad();
		}else{
			//fileNum = 0后，表示是最后一个文件上传完毕，不再继续上传
			SharedPreferencesUtils.put("fileNum", fileNum);
		}
		
	};
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SECCESS:{ //
				//上传成功之后清内存文件名
				setFileNum();
				break;
			}
			case SHOW: {
				break;
			}
			case DISS: {
				break;
			}
			}
		}
	};
	
}
