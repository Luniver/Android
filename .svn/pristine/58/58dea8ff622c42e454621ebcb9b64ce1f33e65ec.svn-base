package hrst.sczd.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * 弹窗基类
 * @author glj
 * 2018-01-25
 */
public class BaseDialog extends Dialog{
//  style引用style样式  
  public BaseDialog(Context context, int width, int height, View layout, int style) {  

      super(context, style);  

      setContentView(layout);  

      Window window = getWindow();  

      WindowManager.LayoutParams params = window.getAttributes();  

      params.gravity = Gravity.CENTER;  

      window.setAttributes(params);  
  } 
}
