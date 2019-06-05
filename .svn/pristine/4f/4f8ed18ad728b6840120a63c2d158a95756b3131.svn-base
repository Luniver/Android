package hrst.sczd.utils;

import hrst.sczd.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.PopupWindow;

/**
 * 弹出窗口
 * 
 * @Author 罗深志
 * @Date 2015-5-22
 */
public class WindowUtils {

	/**
	 * 显示对话框
	 * @param rootView : 显示的内容
	 * @param width    : 窗口宽
	 * @param height   : 窗口高
	 * @param childView : 指定的控件
	 * @param x         : X 轴的偏移量
	 * @param y         : Y 轴的偏移量
	 * @return
	 */
	public static PopupWindow showPopupWindow(View rootView, int width,
			int height, View childView, int x, int y) {
		PopupWindow popupWindow = new PopupWindow(rootView, width, height);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setFocusable(true);        // 失去焦点隐藏
		popupWindow.setOutsideTouchable(true); // 支持返回键删除
		popupWindow.showAsDropDown(childView, x, y);
		return popupWindow;
	}

	/**
	 * 自定义对话框
	 */
	public static Dialog showDiyDialog(Context context, View view) {
		// 使用自定义的主题
		Dialog dialog = new Dialog(context, R.style.MyDialogTheme);
		dialog.setCancelable(false);
		dialog.setContentView(view);
		return dialog;
	}
	
	// 对话框
	public static void showAlterDialog(Context context, String title, String message, DialogInterface.OnClickListener onClickListener){
		new AlertDialog.Builder(context).setTitle(title).setMessage(message).setPositiveButton("确定", onClickListener).show();
	}
}
