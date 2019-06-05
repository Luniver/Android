package hrst.sczd.ui.manager;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import hrst.sczd.agreement.sczd.vo.FPGA_TO_PDA_HEART;
import hrst.sczd.agreement.sczd.vo.MCU_TO_PDA_HEART;
import hrst.sczd.agreement.sczd.vo.MCU_TO_PDA_TEMPERATURE_AND_ELECTRICITY_STATE;
import hrst.sczd.view.LightView;

/**
 * 设备信息管理类
 *
 * @author glj
 *         2018-01-19
 */
public class DevicesManager {

    /**
     * 心跳
     */
    private final int HEART = 0x001;

    private Context context;

    private TextView tvTemperature;

    private TextView tvElectricity;

    private LightView ivFpga;

    private LightView ivMcu;

    private long lastMcuHeartTime;

    private long lastFpgaHeartTime;

    /**
     * UIHandler
     */
    private Handler mHandler = new Handler(Looper.myLooper()) {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HEART:
                    setHeartState();
                    break;
                default:
                    break;
            }
        }

        ;
    };

    /**
     * 构造函数
     *
     * @param context
     */
    public DevicesManager(Context context) {
        this.context = context;
        //开启心跳监听器
        timer.schedule(task, 6000, 6000);
    }

    /**
     * Timer计时器
     */
    Timer timer = new Timer();

    /**
     * 计时器任务
     */
    TimerTask task = new TimerTask() {
        public void run() {
            mHandler.sendEmptyMessage(HEART);
        }
    };

    public void init(TextView tvTemperature, TextView tvElectricity,
                     LightView ivFpga, LightView ivMcu) {
        this.tvTemperature = tvTemperature;
        this.tvElectricity = tvElectricity;
        this.ivFpga = ivFpga;
        this.ivMcu = ivMcu;
    }

    /**
     * 处理温度电量信息
     *
     * @param obj
     */
    public void showTemperatureAndElectricity(
            MCU_TO_PDA_TEMPERATURE_AND_ELECTRICITY_STATE obj) {
        short digTemp = obj.getDigitalTemperature_a();
        short rfTemp = obj.getrFTemperature_b();
        short electricity = obj.getElectricity_c();

        //设置数字版温度|RF温度
//		tvTemperature.setText(digTemp + "°C | " + rfTemp + "°C");
        tvTemperature.setText(digTemp + "°C ");

        //设置电量值
        tvElectricity.setText(electricity + "%");
    }

    /**
     * 显示Mcu心跳信息
     */
    public void showMcuHeart(MCU_TO_PDA_HEART obj) {
        lastMcuHeartTime = System.currentTimeMillis() / 1000;
//        ivMcu.setImageResource(R.drawable.green_light);
    }

    /**
     * 显示Fpga心跳信息
     */
    public void showFpgaHeart(FPGA_TO_PDA_HEART obj) {
        lastFpgaHeartTime = System.currentTimeMillis() / 1000;
        ivFpga.setState(1);
    }

    /**
     * 设置心跳状态
     */
    private void setHeartState() {
        long nowTime = System.currentTimeMillis() / 1000;

        //Mcu心跳/超过6秒设置状态
        if (lastMcuHeartTime == 0 || (nowTime - lastMcuHeartTime) >= 6) {
            ivMcu.setState(0);
        } else {
            ivMcu.setState(1);
        }

        //Fpga心跳/超过6秒设置状态
        if (lastFpgaHeartTime == 0 || ((nowTime - lastFpgaHeartTime) >= 6)) {
            ivFpga.setState(0);
        } else {
            ivFpga.setState(1);
        }
    }

    public void destory() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
