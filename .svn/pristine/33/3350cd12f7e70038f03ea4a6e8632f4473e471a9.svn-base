package hrst.sczd.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

public class CustomHorizontalScrollView extends HorizontalScrollView{
	
   public CustomHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);

	}
   
	private float mDownPosX = 0;    
	private float mDownPosY = 0;  
	
	@Override    
    public boolean onInterceptTouchEvent(MotionEvent ev) {    
        final float x = ev.getX();    
        final float y = ev.getY();    
           
        final int action = ev.getAction();    
        switch (action) {    
        case MotionEvent.ACTION_DOWN:    
            mDownPosX = x;    
            mDownPosY = y;    
            break;    
        case MotionEvent.ACTION_MOVE:    
            final float deltaX = Math.abs(x - mDownPosX);    
            final float deltaY = Math.abs(y - mDownPosY);    
            if (deltaX > deltaY) {    
                return false;    
            }    
        }    
        return super.onInterceptTouchEvent(ev);    
   } 
}
