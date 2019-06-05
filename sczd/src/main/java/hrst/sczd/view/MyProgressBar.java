 package hrst.sczd.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ProgressBar;
/**
 * @author 刘兰
 * @describe 自定义进度条
 * @date 2014.05.23
 * @version 1.1.1.3 修改者，修改日期，修改内容
 */
public class MyProgressBar extends ProgressBar {  
    private String text_progress;  
    private Paint mPaint;//画笔
  
    public MyProgressBar(Context context) {  
        super(context);  
    }  
    public MyProgressBar(Context context, AttributeSet attrs) {  
        super(context, attrs);  
    }  
    public MyProgressBar(Context context, AttributeSet attrs, int defStyle) {  
        super(context, attrs, defStyle);  
    }  
      
    @Override  
    public synchronized void setProgress(int progress) {  
        super.setProgress(progress);  
    }  
    @Override  
    protected synchronized void onDraw(Canvas canvas) {  

        super.onDraw(canvas);  
    }  
}  