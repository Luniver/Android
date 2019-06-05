package hrst.sczd.view;

import hrst.sczd.R;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 自定义Layout界面(暂只显示)
 * @author glj
 * 2018-01-29
 */
public class CustomToast {
	
	private static TextView messageTxt;
	
	
	/**
	 * 显示自定义layout toast
	 * @param context
	 * 			上下文
	 * @param layout
	 * 			界面
	 * @param message
	 * 			提示消息
	 */
    public static void showToast(Context context, int layout, String message) {  
        //加载Toast布局  
        View toastRoot = LayoutInflater.from(context).inflate(layout, null);  
        messageTxt = (TextView) toastRoot.findViewById(R.id.txt_toast_message);
        messageTxt.setText(message);
        //Toast的初始化  
        Toast toastStart = new Toast(context);  
        //获取屏幕高度  
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);  
        int height = wm.getDefaultDisplay().getHeight();  
        //Toast的Y坐标是屏幕高度的1/3，不会出现不适配的问题  
        toastStart.setGravity(Gravity.TOP, 0, height / 3);  
        toastStart.setDuration(Toast.LENGTH_LONG);  
        toastStart.setView(toastRoot);  
        toastStart.show();  
    }  
}
