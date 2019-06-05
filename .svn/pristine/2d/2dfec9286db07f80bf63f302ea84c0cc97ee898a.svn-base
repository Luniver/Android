package hrst.sczd.broadcast;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import com.hrst.common.util.DualSimUtils;
import com.orhanobut.logger.Logger;

import de.greenrobot.event.EventBus;
import hrst.sczd.ui.model.SimCardAbsent;

import static android.telephony.TelephonyManager.SIM_STATE_ABSENT;

/**
 * Created by Sophimp on 2018/11/27.
 */

public class SimStateBroadcast extends BroadcastReceiver {
    private int receiveCounter = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        Logger.d("sis action: " + intent.getAction());
        if ("android.intent.action.SIM_STATE_CHANGED".equals(intent.getAction())) {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
            if (null != telephonyManager) {
                int state = telephonyManager.getSimState();
                Logger.d("sis state: " + state);
                if (TelephonyManager.SIM_STATE_READY == state) {
                    receiveCounter++;
                    // 这里会连续接收到好几次广播， 保险起见， 连续读两次状态
                    if (receiveCounter < 3){
                        DualSimUtils.readSimOperator(context);
                    }
                } else {
                    if (state == SIM_STATE_ABSENT) { //sim 卡拔出 通知重新配置参数
                        DualSimUtils.clearState();
                        receiveCounter = 0;
                        EventBus.getDefault().post(new SimCardAbsent());
                    }
                }
            }
        }
    }
}
