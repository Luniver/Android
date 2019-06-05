package hrst.sczd.view;

import android.annotation.SuppressLint;
import android.content.Context;

import android.graphics.Canvas;

import android.graphics.Movie;

import android.util.AttributeSet;

import android.view.View;

import hrst.sczd.R;

/**
 * @author 刘兰
 * @describe gifView
 * @date 2014.05.23
 * @version 1.1.1.3 修改者，修改日期，修改内容
 */

public class MyGifView extends View {

	private long movieStart;

	private Movie movie;

	// 此处必须重写该构造方法

	@SuppressLint("ResourceType")
	public MyGifView(Context context, AttributeSet attributeSet) {

		super(context, attributeSet);

		// 以文件流（InputStream）读取进gif图片资源

		movie = Movie.decodeStream(getResources().openRawResource(R.drawable.voices));

	}

	@Override
	protected void onDraw(Canvas canvas) {

		long curTime = android.os.SystemClock.uptimeMillis();

		// 第一次播放

		if (movieStart == 0) {

			movieStart = curTime;

		}

		if (movie != null) {

			int duraction = movie.duration();

			int relTime = (int) ((curTime - movieStart) % duraction);

			movie.setTime(relTime);

			movie.draw(canvas, 0, 0);

			// 强制重绘

			invalidate();

		}

		super.onDraw(canvas);

	}

}
