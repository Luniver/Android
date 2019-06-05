package hrst.sczd.view.gifplay;

import hrst.sczd.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Window;
import android.widget.TextView;
/**
 * @author 刘兰
 * @describe 记载窗口
 * @date 2014.05.23
 * @version 1.1.1.3
 * 修改者，修改日期，修改内容
*/
public class MyAlertDialog {
	private static Context context;
	private static MyAlertDialog model;
	private static AlertDialog alert;
	private static Window w;
	private static GifView mGifView;
	private static TextView tvContent;

	/**
	 * @author 刘兰
	 * @describe 弹出窗口
	 * @date 2014.05.23
	 * @version 1.1.1.3 修改者，修改日期，修改内容
	 */
	private MyAlertDialog(Context context) {
		MyAlertDialog.context = context;

	}

	public static MyAlertDialog getInstance(Context context) {
		if (model == null || context != MyAlertDialog.context) {
			model = new MyAlertDialog(context);
		}
		return model;
	}

	/**
	 * 显示loading
	 */
	public void showLoading(String string) {
		if (alert == null) {
			alert = new AlertDialog.Builder(context).create();
			((Dialog) alert).setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失?
			w = alert.getWindow();

			w.setBackgroundDrawableResource(android.R.color.transparent);
			w.setBackgroundDrawable(new BitmapDrawable());

			alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
		}
		try {
			if (!alert.isShowing()) {
				alert.show();
			}

		} catch (Exception e) {
			e.printStackTrace();
			e.printStackTrace();
		}
		w.setContentView(R.layout.updateloading_sczd);

		mGifView = (GifView) w.findViewById(R.id.loading_gif);
		tvContent = (TextView) w.findViewById(R.id.content);
		if (!string.equals("")) {
			tvContent.setText(string);
		}
		mGifView.setGifImage(R.drawable.loadgif);
	}
	
	/**
	 * 修改弹窗显示的消息
	 */
	public void changeMsg (String str) {
		tvContent.setText(str);
	}

	public void loadingDiss() {
		if (alert != null) {
			try {
				alert.dismiss();
				alert = null;
				w.closeAllPanels();
				mGifView.setRun(false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}