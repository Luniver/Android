package hrst.sczd.view.animation;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import hrst.sczd.R;

/**
 * @author 赵耿忠
 * @describe 下推动画
 * @date 2014.05.23
 * @version 1.1.1.3
 * 修改者，修改日期，修改内容
*/
public class PushDownAnimation extends Animation {
	private View mAnimatedView;
	private LayoutParams mViewLayoutParams;
	private int mMarginStart, mMarginEnd;
	private boolean mIsVisibleAfter = false;
	private boolean mWasEndedAlready = false;
	
	private static boolean isFirst = false;//是否第一次启动动画

	/**
	 * 构造方法
	 * 
	 * @param context       上下文
	 * @param view          下推的View
	 * @param duration      持续时间
	 * @param childHeight   高度
	 */
	public PushDownAnimation(Context context, View view, int duration,
			int childHeight) {

		setDuration(duration);
		setInterpolator(AnimationUtils.loadInterpolator(context,
				android.R.anim.linear_interpolator)); //
		mAnimatedView = view;
		mViewLayoutParams = (LayoutParams) view.getLayoutParams();

		// decide to show or hide the view
		mIsVisibleAfter = (view.getVisibility() == View.VISIBLE);
		if (mIsVisibleAfter == true) {
			mMarginStart = 0;
		} else {
			mMarginStart = -childHeight;
		}
		mMarginEnd = (mMarginStart == 0 ? (0 - view.getHeight()) : 0);
		mViewLayoutParams.bottomMargin = mMarginStart;
		view.setVisibility(View.VISIBLE);
	}

	/**
	 * 在下推过程中不断运行，不断重置其高度
	 * 
	 * @param interpolatedTime
	 * @param t
	 */
	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		super.applyTransformation(interpolatedTime, t);
		if (interpolatedTime < 1.0f) {

			// Calculating the new bottom margin, and setting it
			mViewLayoutParams.bottomMargin = mMarginStart
					+ (int) ((mMarginEnd - mMarginStart) * interpolatedTime);

			// Invalidating the layout, making us seeing the changes we made
			mAnimatedView.requestLayout();
			// Making sure we didn't run the ending before (it happens!)
		} else if (!mWasEndedAlready) {
			mViewLayoutParams.bottomMargin = mMarginEnd;
			mAnimatedView.requestLayout();

			if (mIsVisibleAfter) {
				mAnimatedView.setVisibility(View.GONE);
			}
			mWasEndedAlready = true;
		}
	}
	
	/**
	 * 启动下推动画，需要父的layout使用RelativeLayout效果比较好
	 * 注意的问题：动画所在的layout要有足够的空间进行动画，不然会出现动画不全的异常界面
	 * 
	 * @param relativeLayout 需要隐藏和显示的layout
	 * @param layout         下方的layout
	 * @param durationMillis 动画时间
	 * @param high           需要隐藏和显示的layout的高度
	 */
	public static void putDownAnimation(LinearLayout relativeLayout,View layout,long durationMillis,int high){
		
		TranslateAnimation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF,0.0f);
		mShowAction.setDuration(durationMillis);
		if(layout == null){
			relativeLayout.setVisibility(View.VISIBLE);
			relativeLayout.startAnimation(mShowAction);
			return;
		}
		
		layout.setVisibility(View.GONE);
		relativeLayout.setVisibility(View.VISIBLE);
		relativeLayout.startAnimation(mShowAction);
		
		if(isFirst){
			TranslateAnimation mShowAction1 = new TranslateAnimation(0,0,-high,0);
			mShowAction1.setDuration(durationMillis);
			layout.startAnimation(mShowAction1);
			isFirst = false;
		}else{
			TranslateAnimation mShowAction1 = new TranslateAnimation(0,0,-(relativeLayout.getBottom()-relativeLayout.getTop()),0);
			mShowAction1.setDuration(durationMillis);
			layout.startAnimation(mShowAction1);
		}
		layout.setVisibility(View.VISIBLE);
		
	}
	
	/**
	 * 隐藏下推动画
	 * @param relativeLayout 需要隐藏和显示的layout
	 * @param layout         下方的layout
	 * @param durationMillis 动画时间
	 */
	public static void putUpAnimation(LinearLayout relativeLayout,View layout,long durationMillis){
		
		TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,-1.0f);
		mHiddenAction.setDuration(durationMillis);
		if(layout == null){
			relativeLayout.startAnimation(mHiddenAction);
			relativeLayout.setVisibility(View.GONE);
			return;
		}
		layout.setVisibility(View.GONE);
		TranslateAnimation mShowAction1 = new TranslateAnimation(0,0,(relativeLayout.getBottom()-relativeLayout.getTop()),0);
		mShowAction1.setDuration(durationMillis);
		layout.startAnimation(mShowAction1);
		
		relativeLayout.startAnimation(mHiddenAction);
		layout.setVisibility(View.VISIBLE);
		relativeLayout.setVisibility(View.GONE);
		
	}
	
	/**
	 * 箭头旋转动画
	 * @param context
	 * @param imageView
	 */
	public static void startAnimation(Context context,ImageView imageView){
		Animation animation = AnimationUtils.loadAnimation(context, R.anim.arrowrote);
		imageView.setAnimation(animation);
	}
}
