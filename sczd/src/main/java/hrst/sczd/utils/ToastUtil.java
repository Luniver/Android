package hrst.sczd.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil
{
	private static Toast mToast = null;
	private static Toast mToastLong = null;
	private static Context mContext;
	
	public static void showToast(Context context,String msg){
		if (null == mToast)
		{
			mContext = context;
			mToast =Toast.makeText(context, msg, Toast.LENGTH_SHORT);
		}
		else
		{
			
			mToast.setText(msg);
		}
		
		mToast.show();
	}
	
	public static void showToastLong(Context context,String msg){
		if (null == mToastLong)
		{
			mContext = context;
			mToastLong =Toast.makeText(context, msg, Toast.LENGTH_LONG);
		}
		else
		{
			
			mToastLong.setText(msg);
		}
		
		mToastLong.show();
	}
	
	public static void showToast(Context context,int resId)
	{
		if (null == mToast)
		{
			mToast =Toast.makeText(context, resId, Toast.LENGTH_SHORT);
		}
		else
		{
			mToast.setText(resId);
		}
		
		mToast.show();
	}
	public static void showToastduan(Context context,String msg){
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}
	public static void print(Context context, String string) {
		System.out.println("我的=======：" + context.getClass().getName() + " : " + string);
	}
}
