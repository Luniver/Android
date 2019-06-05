package hrst.sczd.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Scroller;
/**
 * 上下左右滑动关闭activity的布局
 * @author Java狗狗
 *
 */
public class SlidingRelativeLayout extends RelativeLayout {

	private static final String TAG = "SlidingRelativeLayout";
	/**
	 * 滑动退出的Activity
	 */
	private ViewGroup scrollView;
	private Scroller mScroller;
	private int downX;
	private int downY;
	/**
	 * SlidingRelativeLayout的宽度
	 */
	private int ScreenWidth;
	/**
	 * SlidingRelativeLayout的高度
	 */
	private int ScreenHeight;
	/**
	 * 每次滑动停止时x轴停止的x轴坐标作为下次开始移动的downX坐标
	 */
	private int tempX;
	/**
	 * 每次滑动停止时y轴停止的x轴坐标作为下次开始移动的downY坐标
	 */
	private int tempY;
	/**
	 * 触发Activity滑动的最小值
	 */
	private int mTouchSlop;
	/**
	 * 触发Activity水平方向滑动销毁的最小值
	 */
	private int minHTouchSlop;
	/**
	 * 触发Activity垂直方向滑动销毁的最小值
	 */
	private int minVTouchSlop;
	/**
	 * 滑动触发销毁的临界滑动速度
	 */
	private static final int SNAP_VELOCITY = 10;  
	/**
	 * Activity是否水平开始滑动
	 */
	private boolean isHSliding = false;
	/**
	 * Activity是否垂直开始滑动
	 */
	private boolean isVSliding = false;
	/**
	 * 是否锁定方向滑动
	 */
	private static boolean isLock = false;
	/**
	 * Activity是否销毁完毕
	 */
	private boolean isFinish = false;
	/**
	 * 通知activity销毁的监听器
	 */
	private OnTouchActivitySlidingListener listener;
	/**
	 * 速度最终对象
	 */
	private  VelocityTracker velocityTracker;
	
	/**
	 * 当前事件的方向
	 */
	private Direction curDirection;
	/**
	 * Activity滑动方向
	 * @author Java狗狗
	 *
	 */
	private enum Direction{
		Left,Right,Up,Down;
	}

	public SlidingRelativeLayout(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);

		initSlidingRelativeLayout(context);
	}

	public SlidingRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);

		initSlidingRelativeLayout(context);
	}

	public SlidingRelativeLayout(Context context) {
		super(context);

		initSlidingRelativeLayout(context);
	}

	public void initSlidingRelativeLayout(Context context) {
		mScroller = new Scroller(context);
		mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {

		if (changed) {
			scrollView = (ViewGroup) this.getParent();
			ScreenWidth = this.getWidth();
			ScreenHeight = this.getHeight();
			minHTouchSlop = this.getWidth() / 5;
			minVTouchSlop = this.getHeight() / 5;
		}
		super.onLayout(changed, l, t, r, b);
	}
	
	/**
	 * 添加用户的速度追踪器
	 * @param ev
	 */
	private void addVelocityTracker(MotionEvent ev) {

		if(velocityTracker==null){
			velocityTracker=VelocityTracker.obtain();
		}
		velocityTracker.addMovement(ev);
	}
	
	/**
	 * 移除用户速度追踪器
	 */
	private void recycleVelocityTracker() {

		if(velocityTracker!=null){
			velocityTracker.recycle();
			velocityTracker=null;
		}
	}
	
	/**
	 * 获取X方向的滑动速度,大于0向右滑动，反之向左 
	 * @return
	 */
	private int getHVelocity() { 
		velocityTracker.computeCurrentVelocity(1000);  
		int xvelocity = (int) velocityTracker.getXVelocity();
		return xvelocity;
	}
	
	/**
	 * 获取Y方向的滑动速度,大于0向下滑动，反之向上
	 * @return
	 */
	private int getVVelocity() { 
		velocityTracker.computeCurrentVelocity(1000);  
		int yvelocity = (int) velocityTracker.getYVelocity();
		return yvelocity;
	}


	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {

		switch (ev.getAction()) {
		
		case MotionEvent.ACTION_DOWN:	
			downX = tempX = (int) ev.getRawX();
			downY = tempY = (int) ev.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			int moveX = (int) ev.getRawX();
			int moveY = (int) ev.getRawY();
			int distanceX = downX - moveX;
			int distanceY = downY - moveY;
			//如果达到滑动开启的临界，将子控件的触摸事件取消
			if (Math.abs(distanceX) > mTouchSlop
					|| Math.abs(distanceY) > mTouchSlop) {
				return true;
			}

			break;
		}
		return super.onInterceptTouchEvent(ev);
	}

	

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			addVelocityTracker(event); 
			break;
		case MotionEvent.ACTION_MOVE:
			int moveX = (int) event.getRawX();
			int moveY = (int) event.getRawY();
			int distanceX = tempX - moveX;
			int distanceY = tempY - moveY;
			tempX = moveX;
			tempY = moveY;
			int isDistanceX=distanceX;
			if (Math.abs(distanceX) > mTouchSlop
					|| Math.abs(distanceY) > mTouchSlop) {
				//检查用户是否已经开启了滑动,确保用户执行完滑动周期后才能执行另外步骤，同步滑动方向
				if (!isLock) {
					if (Math.abs(distanceX) > Math.abs(distanceY)) {
						if (isDistanceX<0) {
							isLock = true;
							isHSliding = true;
						}else {
							isLock=false;
							isHSliding = false;
						}
						
						isVSliding = false;
						
					} else {
						isVSliding = false;
						isHSliding = false;
					}
					
				}
			}
			if (isHSliding) {
				scrollView.scrollBy(distanceX, 0);
			} else if (isVSliding) {
				scrollView.scrollBy(0, distanceY);
			}
			break;
		case MotionEvent.ACTION_UP:
			/**
			 * 获取用户手指在屏幕上水平方向滑动时，视图在水平滚动的相对位移 
			 * 负值--表示向右滑动
			 * 正值--表示向左滑动
			 */
			int mScrollX = scrollView.getScrollX();
			/**
			 * 获取用户手指在屏幕上垂直方向滑动时，视图在垂直滚动的相对位移 
			 * 负值--表示向下滑动
			 * 正值--表示向上滑动
			 */
			int mScrollY = scrollView.getScrollY();
			//检查是否允许滑动销毁activity
			if(listener!=null){
				handleAction(mScrollX, mScrollY);
			}else{
				isFinish=false;
				checkDirection(mScrollX, mScrollY);
			}
			
			int scrollWidthtoRight = (ScreenWidth + scrollView.getScrollX());
			int scrollWidthtoLeft=(ScreenWidth - scrollView.getScrollX());
			int scrollWidthtoUp= (ScreenHeight - scrollView.getScrollY());
			int scrollWidthtoDown= (ScreenHeight + scrollView.getScrollY());
			//判断是否执行销毁
			if(isFinish){
				switch (curDirection) {
				case Left:
					swipeHDirection(mScrollX,scrollWidthtoLeft-1,scrollWidthtoLeft);
					break;
				case Right:
					swipeHDirection(mScrollX,-scrollWidthtoRight+1,scrollWidthtoRight);
					break;
				case Up:
					swipeVDirection(mScrollY,scrollWidthtoUp-1,scrollWidthtoUp);
					break;
				case Down:
					swipeVDirection(mScrollY,-scrollWidthtoDown+1,scrollWidthtoDown);
					break;
				}
			}else{
//				Log.i(TAG, TAG+"：没有达到销毁的条件=------>执行返回滚动");
				swipeOrigin(scrollView.getScrollX(), scrollView.getScrollY(), mScrollX,mScrollY);
			}
			break;
		}
		recycleVelocityTracker(); 
		return true;
	}
	
	

	/**
	 * 动作处理
	 * @param mScrollX 当前窗口的可见视图在水平方向偏移的相对位移
	 * @param mScrollY 当前窗口的可见视图在垂直方向偏移的相对位移
	 */
	public void handleAction(int mScrollX,int mScrollY){
		if (Math.abs(mScrollX) > minHTouchSlop || Math.abs(mScrollY) > minVTouchSlop) {
			if (Math.abs(mScrollX) > Math.abs(mScrollY)) {
				// activity的显示内容在屏幕的水平方向滑动
				if (mScrollX > minHTouchSlop ) {
					isFinish = true;
					curDirection=Direction.Left;
					
				} else if (mScrollX < -minHTouchSlop) {
					System.out.println("handleAction:动作处理");
					isFinish = true;
					curDirection=Direction.Right;
					
				} else {
					isFinish=false;
					if(mScrollX >0){
						curDirection=Direction.Left;
					}else{
						curDirection=Direction.Right;
					}	
				}
			} else {
				// activity的显示内容在屏幕的垂直方向滑动
				if (mScrollY> minVTouchSlop) {
					isFinish = true;
					curDirection=Direction.Up;
					
				} else if (mScrollY < -minVTouchSlop) {
					isFinish = true;
					curDirection=Direction.Down;
					
				} else {
					isFinish=false;
					if(mScrollY>0){
						curDirection=Direction.Up;
					}else{
						curDirection=Direction.Down;
					}	
				}
			}
		}else{
			isFinish=false;
			checkDirection(mScrollX, mScrollY);
		}
	}
	
	/**
	 * 检查出用户滑动的方向
	 * @param mScrollX 当前窗口的可见视图在水平方向偏移的相对位移
	 * @param mScrollY 当前窗口的可见视图在垂直方向偏移的相对位移
	 */
	public void checkDirection(int mScrollX,int mScrollY){
		if (Math.abs(mScrollX) > Math.abs(mScrollY)) {
			if (mScrollX > 0) {
				curDirection=Direction.Left;
			} else{
				curDirection=Direction.Right;
			} 
		} else {
			if (mScrollY > 0) {
				curDirection=Direction.Up;
			} else {
				curDirection=Direction.Down;
			}
		}
	}
	
	/**
	 * 返回相应的初始位置
	 * @param startX
	 * @param startY
	 * @param dx
	 * @param dy
	 * @param duration
	 */
	public void swipeOrigin(int startX, int startY, int durationX,int durationY){
//		Log.i(TAG, TAG+"：根据滑动方向反向滚动返回相应的初始位置");
		switch (curDirection) {
		case Left:
			swipeHDirection(startX,-startX,durationX);
			break;
		case Right:
			swipeHDirection(startX,-startX,durationX);
			break;
		case Up:
			swipeVDirection(startY,-startY,durationY);
			break;
		case Down:
			swipeVDirection(startY,-startY,durationY);
			break;
		}
	}
	
	/**
	 * 水平方向滑动配置
	 * @param startX  滑动起始的X轴坐标
	 * @param dx	  滑动的相对距离
	 * @param duration  滑动效果的时间
	 */
	public void swipeHDirection(int startX,int dx,int duration) {
		Log.i(TAG, TAG+"：--->SwipeHDirection---->");
		mScroller.startScroll(startX, 0, dx, 0,Math.abs(duration));
		postInvalidate();
		isLock = false;
	}

	/**
	 * 垂直方向滑动配置
	 * @param startY 滑动起始的Y轴坐标
	 * @param dy	 滑动的相对距离
	 * @param duration 滑动效果的时间
	 */
	public void swipeVDirection(int startY,int dy, int duration) {
//		Log.i(TAG, TAG+"：--->SwipeVDirection---->");
//		mScroller.startScroll(0, startY, 0, dy,Math.abs(duration)*2);
//		postInvalidate();
//		isLock = false;
	}
	
	/**
	 * 在mScroller.startScroll()方法被调用之后调用此方法
	 */
	@Override
	public void computeScroll() {
		

		/**
		 * 如果还在滚动就返回true，反之返回false
		 */
		if(mScroller.computeScrollOffset()){
//			System.out.println("4==1=computeScroll");
			scrollView.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
			Log.i(TAG, "mScroller.isFinished()="+mScroller.isFinished());
			if(mScroller.isFinished()&&isFinish){	
				System.out.println("4==2=computeScroll");
				if(listener!=null){
					Log.i(TAG, TAG+":进入销毁--->");
					listener.SlidingFinish();
				}
			}
		}
	}

	public interface OnTouchActivitySlidingListener {
		public void SlidingFinish();
	}

	public void SetOnTouchActivitySlidingListener(
			OnTouchActivitySlidingListener listener) {
		this.listener = listener;
	}
	
}
