package com.hrst.common.ui.animation;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

/**
 * @author 赵耿忠
 * @describe 常用动画(移动，旋转，放大缩小）
 * @date 2014.05.23
 * @version 1.1.1.3
 * 修改者，修改日期，修改内容
*/

public class CommonAnimation {
	/**
	 * 移动动画
	 * 
	 * @param context      上下问
	 * @param x1                             开始时的x坐标
	 * @param x2                             结束时的x坐标
	 * @param y1                             开始时的y坐标
	 * @param y2                             结束时的y坐标
	 * @param v            要执行动画的view
	 * @param durationTime         持续时间
	 * @param startTime    开始时间
	 * @param fillAfter       是否保留
	 * @param repeatCount      重复执行次数次，-1=无限循环
	 */
	public static void translate(final Context context, final float x1,
			final float x2, final float y1, final float y2, final View v,
			final int durationTime, final int startTime,
			final boolean fillAfter, final int repeatCount) {
		Animation translateAnimation = new TranslateAnimation(x1, x2, y1, y2); // 移动动画
		translateAnimation.setDuration(durationTime); // 持续时间
		translateAnimation.setStartOffset(startTime); // 开始时间
		translateAnimation.setFillAfter(fillAfter); // 是否保留̬
		translateAnimation.setInterpolator(AnimationUtils.loadInterpolator(
				context, android.R.anim.linear_interpolator)); // 均速
		if (repeatCount == -1) {
			translateAnimation.setRepeatCount(Animation.INFINITE);// 无限
		} else {
			translateAnimation.setRepeatCount(repeatCount); // 重复执行次数
		}
		translateAnimation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation arg0) {

				if (fillAfter == false) {
					v.clearAnimation();
				}
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationStart(Animation animation) {
			}
		});
		v.startAnimation(translateAnimation);
	}

	/**
	 * 放大缩小动画
	 * 
	 * @param context       上下文
	 * @param x1                               开始时的x比例
	 * @param x2                               结束时的x比例
	 * @param y1                               开始时的y比例
	 * @param y2                               结束时的y比例
	 * @param v             要执行动画的view
	 * @param durationTime          持续时间
	 * @param startTime     开始时间
	 * @param fillAfter        是否保留
	 * @param repeatCount       重复执行次数次，-1=无限循环
	 */
	public static void scale(final Context context, final float x1,
			final float x2, final float y1, final float y2, final View v,
			final int durationTime, final int startTime,
			final boolean fillAfter, final int repeatCount) {
		Animation scaleAnimation = new ScaleAnimation(x1, x2, y1, y2,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f); // 缩放大小
		scaleAnimation.setDuration(durationTime); // 持续时间
		scaleAnimation.setStartOffset(startTime); // 开始时间
		scaleAnimation.setFillAfter(fillAfter); // 是否保留̬
		scaleAnimation.setInterpolator(AnimationUtils.loadInterpolator(context,
				android.R.anim.linear_interpolator)); // 均速
		if (repeatCount == -1) {
			scaleAnimation.setRepeatCount(Animation.INFINITE);// 无限
		} else {
			scaleAnimation.setRepeatCount(repeatCount); // 重复执行次数
		}
		scaleAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationEnd(Animation arg0) {

				if (fillAfter == false) {
					v.clearAnimation();
				}
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationStart(Animation animation) {
			}
		});
		v.startAnimation(scaleAnimation);
	}

	/**
	 * 旋转动画
	 * 
	 * @param context      上下文
	 * @param v         要执行动画的view
	 * @param durationTime 持续时间
	 * @param repeatCount  执行次数
	 */
	public static void rotate(final Context context, final View v,
			final int durationTime, final int repeatCount) {
		RotateAnimation rotateAnimation = new RotateAnimation(0f, 360f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rotateAnimation.setDuration(durationTime);
		if (repeatCount == -1) {
			rotateAnimation.setRepeatCount(Animation.INFINITE);// 无限
		} else {
			rotateAnimation.setRepeatCount(repeatCount); // 重复执行次数
		}
		rotateAnimation.setInterpolator(AnimationUtils.loadInterpolator(
				context, android.R.anim.linear_interpolator)); // 均速
		rotateAnimation.setFillAfter(false);
		v.startAnimation(rotateAnimation);
	}
}
