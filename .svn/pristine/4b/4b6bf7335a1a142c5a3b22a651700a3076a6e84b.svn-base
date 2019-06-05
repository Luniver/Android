package com.hrst.common.ui.view.gifplay;

import android.graphics.Bitmap;

/**
 * @author 刘兰
 * @describe 存放gif结构数据
 * @date 2014.05.23
 * @version 1.1.1.3
 * 修改者，修改日期，修改内容
*/
public class GifFrame {
	/**
	 * 构造函数
	 * 
	 * @param im
	 *            图片
	 * @param del
	 *            延时
	 */
	public GifFrame(Bitmap im, int del) {
		image = im;
		delay = del;
	}

	public GifFrame(String name, int del) {
		imageName = name;
		delay = del;
	}

	/** 图片 */
	public Bitmap image;
	/** 延时 */
	public int delay;
	/** 当图片存成文件时的文件名 */
	public String imageName = null;

	/** 下一帧 */
	public GifFrame nextFrame = null;
}
