package hrst.sczd.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;
/**
 * @author 刘兰
 * @describe 解决跟fragment滑动的冲突
 * @date 2014.05.23
 * @version 1.1.1.3 修改者，修改日期，修改内容
 */

public class MyListView extends ListView {
	// 滑动距离及坐标
	private float xDistance, yDistance, xLast, yLast;

	public MyListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyListView(Context context) {
		super(context);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			xDistance = yDistance = 0f;
			xLast = ev.getX();
			yLast = ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			final float curX = ev.getX();
			final float curY = ev.getY();

			xDistance += Math.abs(curX - xLast);
			yDistance += Math.abs(curY - yLast);
			xLast = curX;
			yLast = curY;

			if (xDistance > yDistance) {
				return false; // 表示向下传递事件
			}
		}

		return super.onInterceptTouchEvent(ev);
	}
}
