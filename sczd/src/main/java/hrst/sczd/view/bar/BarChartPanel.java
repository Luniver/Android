package hrst.sczd.view.bar;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import java.util.LinkedList;

import hrst.sczd.R;
import hrst.sczd.ui.manager.SkinSettingManager;

/**
 * @author 沈月美
 * @version 1.1.1.3 修改者，修改日期，修改内容
 * @describe 画柱状图的类
 * @date 2014.05.23
 * @modify by sophimp at 2018.6.02
 */
public class BarChartPanel extends View {
    private static final String TAG = "BarChartPanel";
    private static final String TAG_OVERFLOW = "溢出";
    // Y轴上的字
    String yValue[] = {"-20dbm", "-40dbm", "-60dbm", "-80dbm", "-100dbm", "-120dbm", "目标", "总信号"};
    private LinkedList<DataElement> dataElements;
    private BitmapDrawable drawale = null;
    private Paint myPaint;
    private Context context;
    SkinSettingManager skinManager;
    /**
     * Y轴坐标间距
     */
    private int yUnit;

    /**
     * X轴能量柱间隔
     */
    private int xUnit;

    /**
     * 能量柱宽度
     */
    private int barWidth;

    private int selfWidth;
    private int selfHeight;

    /**
     * 距顶部 和 底部空间
     */
    private int upAndDownMargin;

    /**
     * 距左边空间
     */
    private int leftAndRightMargin;

    /**
     * 最大字宽
     */
    private int maxTextWidth;
    private float density;
    /**
     * Y轴 起点(x, )值
     */
    private int yStartX;

    /**
     * Y轴 终点(,y)值
     */
    private int yStopY;

    /**
     * X轴 起点(,y)值
     */
    private int xStartY;

    /**
     * x轴 终点(x,)值
     */
    private int xStopX;

    /**
     * 字体大小
     */
    private float textSize;
    private int axisColor;

    public BarChartPanel(Context context) {
        this(context, null);
    }

    public BarChartPanel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        skinManager = new SkinSettingManager((Activity) context);
        // 初始化
        init();
    }

    private void init() {
        textSize = 25;
        // draw background
        myPaint = new Paint();
        myPaint.setStrokeWidth(2);
        axisColor = getResources().getColor(R.color.light_blue);
        // draw XY Axis 绘制x,y轴3
        myPaint.setColor(axisColor);
        myPaint.setStrokeWidth(3);// 设置线宽
        myPaint.setTextSize(textSize);

        // 计算 x,y轴刻度最大长度
        maxTextWidth = Math.max((int) myPaint.measureText("-220dbm"), (int) myPaint.measureText("溢出"));

        // 获取四周的空白, 按density适配
        WindowManager wm = (WindowManager) this.getContext().getSystemService(
                Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        density = metrics.density;
        leftAndRightMargin = (int) (metrics.density * 10);
        upAndDownMargin = (int) (metrics.density * 10);

        yStartX = (int) (leftAndRightMargin + maxTextWidth + 3 * density) + 1;
    }

    public BarChartPanel(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 清空画笔
     */
    public void clearPaint() {
        myPaint = null;
        invalidate();
    }

    /**
     * 获取柱子信息，并更新柱子
     *
     * @param dataElements 存放柱子的集合
     * @param lines        显示最多柱子个数
     */
    public void setBar(final LinkedList<DataElement> dataElements, int lines) {
        this.dataElements = dataElements;
//		if (hitTimes > dataElements.size() && player) {// 先判断是否捕获到信息
//			dataElements.addFirst(data);
//		}

        // 计算柱体宽度和间隔
        xUnit = (selfWidth - yStartX - 2 * leftAndRightMargin) / lines;
        barWidth = xUnit / 2;
        Log.d(TAG, "selfWidth: " + selfWidth + " -yStartX: " + yStartX);
        if (dataElements.size() > lines) {
            dataElements.removeLast();
        }
        Resources resources = this.getContext().getResources();
        if (dataElements.size() > 1) {
            if (dataElements.get(0).getValue() >= dataElements.get(1)
                    .getValue()) {
                if (skinManager.getCurrentSkinRes() == 0) {
                    drawale = (BitmapDrawable) resources
                            .getDrawable(R.drawable.arrow_up_white);
                } else {
                    drawale = (BitmapDrawable) resources
                            .getDrawable(R.drawable.arrow_up_black);
                }

//                dataElements.get(0).setArrow(drawale);
//                dataElements.get(1).setArrow(null);
            } else {
                if (skinManager.getCurrentSkinRes() == 0) {
                    drawale = (BitmapDrawable) resources
                            .getDrawable(R.drawable.arrow_down_white);
                } else {
                    drawale = (BitmapDrawable) resources
                            .getDrawable(R.drawable.arrow_down_blak);
                }

//                dataElements.get(0).setArrow(drawale);
//                dataElements.get(1).setArrow(null);
            }
        }
        postInvalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        selfWidth = getMeasuredWidth();
        selfHeight = getMeasuredHeight();
//        Log.d(TAG, "onMeasure: " + selfWidth + " x " + selfHeight);
        yUnit = (selfHeight - 4 * upAndDownMargin) / yValue.length;
        yStopY = selfHeight - 2 * upAndDownMargin;

        // 从下向上, 在第三个刻度处
        xStartY = selfHeight - 2 * upAndDownMargin - 3 * yUnit;
        xStopX = selfWidth - leftAndRightMargin;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 设置线宽
        myPaint.setStrokeWidth(2 * density);
        myPaint.setColor(axisColor);
        // Y轴
        canvas.drawLine(yStartX, upAndDownMargin, yStartX, yStopY, myPaint);
        // X轴
        canvas.drawLine(yStartX, xStartY, xStopX, xStartY, myPaint);

        // 画y轴方向上背景刻度线
        myPaint.setStrokeWidth(1);
        myPaint.setColor(Color.LTGRAY);

        if (skinManager.getCurrentSkinRes() == 0) {
            myPaint.setColor(Color.WHITE);
        } else {
            myPaint.setColor(Color.BLACK);
        }
        // 画y轴方向上背景刻度线和值
        myPaint.setStrokeWidth(1);
        myPaint.setTextSize(25);
        myPaint.setPathEffect(null);
        for (int i = 0; i < yValue.length; i++) {
            // 最后两个刻度不需要线, X轴已经画过了, 所以 减3
            int y = 2 * upAndDownMargin + i * yUnit;
            if (i < yValue.length - 3) {
                // 画背景虚线
                canvas.drawLine(yStartX, y, selfWidth - leftAndRightMargin, y, myPaint);
            }
            // 刻度值
            canvas.drawText(yValue[i], leftAndRightMargin, y + textSize / 2, myPaint);

        }

        // 画能量柱
        if (dataElements != null) {
            int count = dataElements.size();
            // draw bar chart now
            myPaint.setStyle(Style.FILL);
            myPaint.setStrokeWidth(2);

            // y轴每个间距刻度值是-20
            float ymarkers = -20.0f;
            for (int i = 0; i < count; i++) {
                int left = yStartX + leftAndRightMargin + barWidth + xUnit * i;
                int right = left + barWidth;
                myPaint.setColor(dataElements.get(i).getColor());
                int top = 0;
                // 小于零的时候按比例来, 大于0 直接从顶开始画
                if (dataElements.get(i).getValue() <= 0) {
                    // 与刻度保持一致
                    top = 2 * upAndDownMargin;
                    // 从-20dm开始, 要减1
                    int barHeight = (int) ((dataElements.get(i).getValue() / ymarkers - 1.0f) * yUnit);
                    top += barHeight;
                }
                int bottom = xStartY;
                if (top > bottom) {
                    top = bottom;
                }
                canvas.drawRect(left, top, right, bottom, myPaint);

                // 绘制x轴上面的横坐标
                if (skinManager.getCurrentSkinRes() == 0) {
                    myPaint.setColor(Color.WHITE);
                } else {
                    myPaint.setColor(Color.BLACK);
                }
                myPaint.setTextSize(textSize);
                myPaint.setStrokeWidth(5);
                myPaint.setPathEffect(null);
                // x轴上的值，居中显示
                String valText = dataElements.get(i).getValue() + "";
                float xStart = ((left + right) / 2 - myPaint.measureText(valText) / 2);
                // 能量值
                canvas.drawText(valText,
                        xStart,
                        xStartY + textSize + density,
                        myPaint);
                // 溢出值
                short overFlow = dataElements.get(i).getOverflow();
                int y = -1;
                xStart = ((left + right) / 2 - myPaint.measureText(TAG_OVERFLOW) / 2);
                // bit1 为1 是目标溢出
                if ((overFlow & 0x2) == 0x2) {
                    y = 2 * upAndDownMargin + (yValue.length - 2) * yUnit;
                    canvas.drawText(TAG_OVERFLOW, xStart, y + textSize / 2, myPaint);
                }
                // bit0 为1 是干扰溢出
                if ((overFlow & 0x1) == 0x1) {
                    y = 2 * upAndDownMargin + (yValue.length - 1) * yUnit;
                    canvas.drawText(TAG_OVERFLOW, xStart, y + textSize / 2, myPaint);
                }
                if (0 == i) {
                    // 加载图片资源文件
                    try {
                        Log.d(TAG, "onDraw: " + drawale);
                        if (drawale != null) {

                            // 创建内存中的一张图片
                            int arrowLeft = (left + right) / 2 - barWidth / 4;
                            int arrowRight = (left + right) / 2 + barWidth / 4;
                            int arrowTop = 2 * upAndDownMargin + 2 * yUnit;
                            int arrowBottom = arrowTop + yUnit;
                            drawale.setBounds(arrowLeft, arrowTop, arrowRight, arrowBottom);
                            // 画到View上
                            drawale.draw(canvas);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "" + e.getMessage());
                    }
                }
            }
        }
    }
}
