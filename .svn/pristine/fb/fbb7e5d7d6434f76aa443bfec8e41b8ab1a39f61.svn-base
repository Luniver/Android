package hrst.sczd.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import hrst.sczd.R;

/**
 * 指示灯, 可表示开, 关, 闪烁等状态
 * Created by Sophimp on 2018/12/6.
 */
public class LightView extends ImageView {

    private boolean isBlink = false;

    private int blinkIndex = 0;

    public LightView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
        setImageResource(R.drawable.red_light);
    }

    public LightView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 设置灯的状态
     *
     * @param state 0 - 关, 1 - 开, 2 - 闪烁
     */
    public void setState(int state) {
        switch (state) {
            case 0:
                isBlink = false;
                setImageResource(R.drawable.red_light);
                break;
            case 1:
                isBlink = false;
                setImageResource(R.drawable.green_light);
                break;
            case 2:
                if(!isBlink){
                    isBlink = true;
                    postDelayed(this::blink, 300);
                }
                break;
            default:
                isBlink = false;
                break;
        }
    }

    /**
     * 闪烁操作
     */
    private void blink() {
        if (blinkIndex % 2 == 0) {
            setImageResource(R.drawable.gray_light);
            blinkIndex = 0;
        } else {
            setImageResource(R.drawable.red_light);
        }
        blinkIndex++ ;
        if (isBlink) {
            postDelayed(this::blink, 300);
        }
    }
}
