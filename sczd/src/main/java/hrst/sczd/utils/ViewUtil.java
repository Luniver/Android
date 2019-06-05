package hrst.sczd.utils;

import android.content.Context;
import android.util.TypedValue;

/**
 * View dp/px互转工具
 * @author glj
 * 2018/01/25
 */
public class ViewUtil {
    /**
     * dp 转px
     * @param context
     * @param dpValue
     * @return
     */
    public static int dp2px(Context context,int dpValue){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dpValue,context.getResources().getDisplayMetrics());
    }

    /**
     * sp 转 px
     * @param context
     * @param spValue
     * @return
     */
    public static int sp2px(Context context,int spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,spValue,context.getResources().getDisplayMetrics());
    }
}
