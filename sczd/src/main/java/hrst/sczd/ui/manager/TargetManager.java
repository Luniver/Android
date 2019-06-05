package hrst.sczd.ui.manager;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.hrst.common.ui.InduceConfigure;
import com.hrst.common.util.ToastUtils;
import com.hrst.induce.annotation.TargetFormat;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;

import hrst.sczd.R;
import hrst.sczd.agreement.sczd.vo.FPGA_DT_TO_PDA_HEART;
import hrst.sczd.agreement.sczd.vo.MCU_TO_PDA_REPORT_ENERGY;
import hrst.sczd.systemsettings.SettingUtils;
import hrst.sczd.ui.model.CommunityModel;
import hrst.sczd.utils.FileUtils;
import hrst.sczd.utils.SaveData_withPreferences;
import hrst.sczd.utils.ThreadPoolExecutors;
import hrst.sczd.view.bar.BarChartPanel;
import hrst.sczd.view.bar.DataElement;

import static hrst.sczd.ui.activity.GainWayActivity.KEY_DES_GAIN;
import static hrst.sczd.ui.activity.GainWayActivity.KEY_DT_GAIN;

/**
 * 目标信息管理类
 *
 * @author glj 2018-01-18
 */
public class TargetManager {

    private static final String TAG = "goo-TargetManager";

    private String[] formats = {"十进制(16bit)", "十进制(32bit)", "十六进制(16bit)", "十六进制(32bit)"};

    private Activity activity;

    private TextView lacTxt;
    private TextView cidTxt;
    private TextView freqTxt;
    private TextView powerTxt;
    private TextView distanceTxt;
    private TextView snrTxt;
    private TextView standardTxt;
    private TextView gainTxt;
    private TextView bidNumTxt;
    private BarChartPanel bar_s;
    private TextView dtTxt;

    /**
     * 文件工具类
     */
    private FileUtils fileUtils;

    private SaveData_withPreferences sPreferences;

    /**
     * 能量转距离数组
     */
    private String[] fileStr;

    private String targetFormat = TargetFormat.TARGET_FORMAT_LTE;

    /**
     * 音量
     */
    private float volumnRatio;

    /**
     * cid显示格式
     * 0: "十进制(16bit)"
     * 1: "十进制(32bit)"
     * 2: "十六进制(16bit)"
     * 3: "十六进制(32bit)"
     */
    private int cidFormat = 0;

    /**
     * 记录上次Cid
     */
    private long lastCid;

    /**
     * 能量柱实体类
     */
    private DataElement data;

    /**
     * 场强分析model类
     */
    private CommunityModel model;

    /**
     * 存放能量柱信息
     */
    private LinkedList<DataElement> dataElements = new LinkedList<DataElement>();

    /**
     * 记录捕获目标次数
     */
    private int hitTimes;

    /**
     * dt心跳计数
     */
    private int dtCount;

    /**
     * dt 溢出计数
     */
    private int dtOverflow;

    /**
     * Timer计时器
     */
    Timer timer;
//    ScheduledExecutorService executorService;
    /**
     * 统计10次fpga心跳间隔, 取平均值
     */
    private int statisticCount;

    private long statisticTime;

    /**
     * 收到fpga心跳平均间隔
     * 校准取前10次有效值的平均值
     * 2018.5.18
     * 延时主要因素是蓝牙模块, 所以晶振校准意义不大
     * 继续采取5.2秒周期采集
     */
    private long heartbeatInterval = 5200;

    private long lastHeartbeat;

    private Handler uiHandler = new Handler(Looper.getMainLooper());

    /**
     * 当前cid显示
     */
    private long curCidValue;
    private int desGain;
    private boolean isNearTarget = false;

    /**
     * 待播放声音池缓冲
     */
    private Queue<Integer> soundsBuf = new LinkedBlockingQueue<>();

    /**
     * 正在播放语音
     */
    private boolean isPlayingSound = false;
    private boolean voiceCountSwitch;
    private boolean nearTargetSwitch;

    private void setHeartState() {
        int ret = 0;
//        Log.d(TAG, "setHeartState");
//        if (hasCalibrated) {
        ret = dtCount * 10;
        dtCount = 0;
//        } else {
//            if (dtCount > 0) {
//                ret = statisticCount / dtCount * 100;
//            }
//        }
        if (ret > 100) {
            ret = 100;
        }
        if (ret > 50) {
            dtTxt.setTextColor(Color.GREEN);
        } else {
            dtTxt.setTextColor(Color.RED);
        }
        // 大于3次, 显示溢出
        if (dtOverflow > 3) {
            dtTxt.setText("DT: " + ret + "%(溢出)");
        } else {
            dtTxt.setText("DT: " + ret + "%");
        }
        dtOverflow = 0;
    }

    /**
     * 构造函数
     *
     * @param activity
     */
    public TargetManager(Activity activity) {
        this.activity = activity;
    }

    /**
     * 绑定控件
     */
    public void initView(TextView lacTxt, TextView cidTxt, TextView freqTxt,
                         TextView powerTxt, TextView distanceTxt, TextView snrTxt,
                         TextView standardTxt, TextView gainTxt, TextView bidNumTxt,
                         BarChartPanel bar_s, TextView dtTxt) {
        this.lacTxt = lacTxt;
        this.cidTxt = cidTxt;
        this.freqTxt = freqTxt;
        this.powerTxt = powerTxt;
        this.distanceTxt = distanceTxt;
        this.snrTxt = snrTxt;
        this.standardTxt = standardTxt;
        this.gainTxt = gainTxt;
        this.bidNumTxt = bidNumTxt;
        this.bar_s = bar_s;
        this.dtTxt = dtTxt;

        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
//        curCidValue = 3492837471892873749L;
//        cidTxt.setText(getCidStr());
        // 设置当前制式
        setStandard();

        // 初始化目标小区信息
        initCellData();

//		long startTime = System.currentTimeMillis();
        // 初始化工具
        initUtils();
//		Log.d(TAG, "initData: init util tims:" + (System.currentTimeMillis() - startTime));
    }

    /**
     * 初始化目标小区信息
     */
    private void initCellData() {
        // mock data
//        for (int i = 0; i < 8; i++) {
//            DataElement element = new DataElement(-15 - i, Color.RED);
//            element.setOverflow((short) (RandomUtils.randFloat() * 100 % 2 + 2));
//            dataElements.add(element);
//        }
        // 还原柱子
        if (dataElements.size() > 0) {
            uiHandler.postDelayed(() -> bar_s.setBar(dataElements, 7), 500);
        }
    }

    /**
     * 初始化工具
     */
    private void initUtils() {
        // 初始化文件工具类
        fileUtils = new FileUtils();
        // 获取能量转距离对比方案
        String str = fileUtils.readAssetsFile("distant.txt", activity);
        String[] fileStr = str.split("\n");
//		System.out.println("goo fileStr: " + Arrays.toString(fileStr));
        // 获取音频管理器
        AudioManager am = (AudioManager) activity
                .getSystemService(activity.AUDIO_SERVICE);
        float audioMaxVolumn = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float audioCurrentVolumn = am
                .getStreamVolume(AudioManager.STREAM_MUSIC);
        // 设置音量
        volumnRatio = audioCurrentVolumn / audioMaxVolumn;

        new Thread(() -> model = CommunityModel.getInstance(activity)).start();

        sPreferences = SaveData_withPreferences.getInstance(activity);

        voiceCountSwitch = sPreferences.getData_Boolean(SettingUtils.VoiceCountOff, false);
        nearTargetSwitch = sPreferences.getData_Boolean(SettingUtils.NearTarget, false);
    }

    /**
     * 设置当前制式
     */
    private void setStandard() {
        targetFormat = InduceConfigure.targetInduceFormat;
        switch (InduceConfigure.targetInduceFormat) {
            case TargetFormat.TARGET_FORMAT_GSM:
                standardTxt.setText("制式: GSM");
                break;
            case TargetFormat.TARGET_FORMAT_CDMA:
                standardTxt.setText("制式: CDMA");
                break;
            case TargetFormat.TARGET_FORMAT_WCDMA:
                standardTxt.setText("制式: WCDMA");
                break;
            case TargetFormat.TARGET_FORMAT_TDSCDMA:
                standardTxt.setText("制式: TD-SCDMA");
                break;
            case TargetFormat.TARGET_FORMAT_LTE:
                standardTxt.setText("制式: LTE");
                break;
        }
    }

    /**
     * 设置目标定位信息
     *
     * @param obj
     */
    public void setTargetInfo(MCU_TO_PDA_REPORT_ENERGY obj) {
        // 记录中标次数
        hitTimes++;
//		Log.d("goo", "goo target: " + obj.toString());
        bidNumTxt.setText(hitTimes + "");
        lacTxt.setText(obj.getUsTargetTAC_b() + "");

        // 先赋值
        curCidValue = obj.getUiTargetCID_c();

        cidTxt.setText(getCidStr());

        freqTxt.setText(obj.getUsTargetARFCN_a() > 0 ? (obj.getUsTargetARFCN_a() + "") : "N/A");
//        distanceTxt.setText(getDistant(obj.getsRxLevInDbm_g()) + "米");
        distanceTxt.setText(convertPowerToDistance(obj.getsRxLevInDbm_g()) + "米");
        snrTxt.setText(obj.getcSNR_i() + "");
        data = getLteDataElement((int) obj.getsRxLevInDbm_g(),
                obj.getbIsPowerOverFlow_j(),
                checkCidChange(obj.getUiTargetCID_c()));
        Log.d("goo", "goo element" + data.toString());
        dataElements.addFirst(data);
//		dataElements.add(0,new DataElement(-80, Color.RED));
//		bar_s.setBar(dataElements, data, 7, hitTimes, true);
        bar_s.setBar(dataElements, 7);
    }

    /**
     * 能量转称距离
     */
    private String convertPowerToDistance(int power) {
        isNearTarget = false;
        String distance = "-";
        switch (InduceConfigure.targetInduceFormat) {
            case TargetFormat.TARGET_FORMAT_GSM:
                if (power >= -15) {
                    isNearTarget = true;
                    distance = "<=5";
                } else if (power >= -40) {
                    isNearTarget = true;
                    distance = "<=50";
                } else if (power >= -70) {
                    distance = "<=200";
                } else if (power >= -80) {
                    distance = "<=400";
                } else {
                    distance = ">400";
                }
                break;
            case TargetFormat.TARGET_FORMAT_CDMA:
                if (power >= -20) {
                    isNearTarget = true;
                    distance = "<=5";
                } else if (power >= -40) {
                    isNearTarget = true;
                    distance = "<=50";
                } else if (power >= -60) {
                    isNearTarget = true;
                    distance = "<=100";
                } else if (power >= -80) {
                    isNearTarget = true;
                    distance = "<=200";
                } else {
                    distance = ">200";
                }
                break;
            case TargetFormat.TARGET_FORMAT_TDSCDMA:
                break;
            case TargetFormat.TARGET_FORMAT_WCDMA:
            case TargetFormat.TARGET_FORMAT_LTE:
                if (power >= -30) {
                    isNearTarget = true;
                    distance = "<=5";
                } else if (power >= -75) {
                    isNearTarget = true;
                    distance = "<=50";
                } else if (power >= -90) {
                    distance = "<=100";
                } else if (power >= -105) {
                    distance = "<=200";
                } else {
                    distance = ">200";
                }
                break;
        }
        return distance;
    }

    private String getCidStr() {
        String cidStr = "";
        // 十进制/16bit显示
        switch (cidFormat) {
            //10进制(16bit)
            case 0:
                cidStr = (curCidValue & 0xffff) + "";
                break;
            //10进制(32bit)
            case 1:
                cidStr = curCidValue + "";
                break;
            //16进制(16bit)
            case 2:
                cidStr = Long.toHexString(curCidValue & 0xffff);
                break;
            //16进制(32bit)
            case 3:
                cidStr = Long.toHexString(curCidValue);
                break;
        }
        return cidStr;
    }

    public void showRadioDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("请选择CID转换格式");
        //    设置一个单项选择下拉框
        builder.setSingleChoiceItems(formats, cidFormat,
                (dialog, which) -> {
                    ToastUtils.showToast("格式为：" + formats[which]);
                    cidFormat = which;
                });
        builder.setPositiveButton("确定", (dialog, which) -> cidTxt.setText(getCidStr()));
        builder.setNegativeButton("取消", (dialog, which) -> {
        });
        builder.show();
    }

    /**
     * 能量柱
     */
    private DataElement getLteDataElement(final int value, byte powerOverFlow,
                                          boolean isChangeCell) {
        // 目前能量值没有从 distant 转换， 传的是绝对值
        // 又被改回来了， 可传负数
        int realValue = value;
        int color = colorConfig(realValue, isChangeCell);
        powerTxt.setText(realValue + "dbm");

        // 检测增益档位是否需要调节
        checkGainStatus(realValue);

        DataElement element = new DataElement(realValue, color);
        element.setOverflow(powerOverFlow);
        playSound(value);
        return element;
    }

    /**
     * 播放能量信息
     *
     * @param value 能量值
     */
    private void playSound(int value) {
        voiceCountSwitch = sPreferences.getData_Boolean(SettingUtils.VoiceCountOff, false);
        nearTargetSwitch = sPreferences.getData_Boolean(SettingUtils.NearTarget, false);
        // 实例化声音资源类
        int first = Math.abs(value) / 100;
        int second = (Math.abs(value) % 100) / 10;
        int third = Math.abs(value) % 10;
        if (voiceCountSwitch) {
            // 负数
            soundsBuf.add(10);
            if (first > 0) {
                soundsBuf.add(first);
            }
            if (first > 0 || second > 0) {
                soundsBuf.add(second);
            }
            soundsBuf.add(third);
        }
        if (isNearTarget && nearTargetSwitch) {
            soundsBuf.add(11);
        }
        if (!isPlayingSound) {
            ThreadPoolExecutors.getThread().execute(playSoundThread);
        }
    }

    private Runnable playSoundThread = () -> {
        while (!soundsBuf.isEmpty() && (voiceCountSwitch || nearTargetSwitch)) {
            isPlayingSound = true;
            int soundIndex = soundsBuf.poll();
            playSound(soundIndex, 0, volumnRatio);
            try {
                Thread.sleep(350);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        isPlayingSound = false;
    };

    /**
     * 检测增益档位是否需要调节
     */
    private void checkGainStatus(int realValue) {
        boolean showToast = false;
        if (desGain == 1) { // 低中益
            switch (InduceConfigure.targetInduceFormat) {
                case TargetFormat.TARGET_FORMAT_CDMA:
                case TargetFormat.TARGET_FORMAT_GSM:
                    if (realValue < -65) showToast = true;
                    break;
                case TargetFormat.TARGET_FORMAT_LTE:
                    if (realValue < -90) showToast = true;
                    break;
            }
        } else if (desGain == 4) { //调试增益
            switch (InduceConfigure.targetInduceFormat) {
                case TargetFormat.TARGET_FORMAT_CDMA:
                case TargetFormat.TARGET_FORMAT_GSM:
                    if (realValue < -45) showToast = true;
                    break;
                case TargetFormat.TARGET_FORMAT_LTE:
                    if (realValue < -70) showToast = true;
                    break;
            }
        }
        if (showToast) {
            ToastUtils.showToast("当前目标增益档位过低,请调节!", Toast.LENGTH_LONG);
        }
    }

    /**
     * 能量柱颜色配置
     *
     * @param value
     * @param isChangeCell
     * @return
     */
    private int colorConfig(int value, boolean isChangeCell) {
        int color = 0;
        //* 当前制式 1->GSM 2->CDMA 3->WCDMA 4->TD-SCDMA 5->LTE
        // LTE能量柱颜色
        switch (InduceConfigure.targetInduceFormat) {
            case TargetFormat.TARGET_FORMAT_LTE:
                // 切换小区时为绿色
                if (isChangeCell) {
                    color = activity.getResources().getColor(R.color.green);
                } else {
                    if (value > -90) {
                        color = activity.getResources().getColor(R.color.bar_red);
                    } else {
                        color = activity.getResources().getColor(R.color.bar_blue);
                    }
                }
                break;
            case TargetFormat.TARGET_FORMAT_WCDMA:
            case TargetFormat.TARGET_FORMAT_TDSCDMA:
                if (value > -90) {
                    color = activity.getResources().getColor(R.color.bar_red);
                } else {
                    color = activity.getResources().getColor(R.color.bar_blue);
                }
                break;
            case TargetFormat.TARGET_FORMAT_CDMA:
                if (value > -85) {
                    color = activity.getResources().getColor(R.color.bar_red);
                } else {
                    color = activity.getResources().getColor(R.color.bar_blue);
                }
                break;
            case TargetFormat.TARGET_FORMAT_GSM:
                // 获取频点
                String strFeq = freqTxt.getText().toString();
                if (TextUtils.isEmpty(strFeq) || "N/A".equals(strFeq)) {
                    strFeq = "0";
                }
                int feq = Integer.valueOf(strFeq);
                // 低频
                if (feq <= 124) {
                    if (value > -80) {
                        color = activity.getResources()
                                .getColor(R.color.bar_purple);
                    } else {
                        color = activity.getResources().getColor(R.color.bar_blue);
                    }
                }
                // 高频
                else {
                    if (value > -80) {
                        color = activity.getResources().getColor(R.color.bar_red);
                    } else {
                        color = activity.getResources().getColor(R.color.bar_blue);
                    }
                }
                break;
            default:
                // 其他制式能量柱颜色设置
                if (value <= -95) {
                    color = activity.getResources().getColor(R.color.bar_blue);
                } else if (value > -95) {
                    color = activity.getResources().getColor(R.color.bar_red);
                }
                break;

        }
        return color;
    }

    /**
     * 播报能量柱的方法
     *
     * @param sound
     * @param number
     */
    private void playSound(int sound, int number, float volumnRatio) {

        if (activity == null) {
            return;
        }

        if (model.getSoundPool() == null) {

        }
        model.getSoundPool().play(model.getSoundHashMap().get(sound),
                volumnRatio, volumnRatio, 1, number, 1);
    }

    /**
     * 检测是否切换小区
     *
     * @param cid
     * @return
     */
    private boolean checkCidChange(long cid) {
        if (lastCid == 0) {
            lastCid = cid;
        }

        boolean isChangeCid = false;
        if (lastCid != cid) {
            isChangeCid = true;
        } else {
            isChangeCid = false;
        }

        lastCid = cid;
        return isChangeCid;
    }

    /**
     * 计算距离
     *
     * @param power 能量
     * @return
     */
    private String getDistant(short power) {
        if (null == fileStr) return "0";
        for (int i = 0; i < fileStr.length; i++) {
            String[] distantStr = fileStr[i].split(" ");
            if (distantStr[0].equals(">=")) {
                if (power >= Integer.parseInt(distantStr[1])) {
                    return distantStr[2];
                }
            } else if (distantStr[0].equals("<")) {
                if (power < Integer.parseInt(distantStr[1])) {
                    return distantStr[2];
                }
            }
        }
        return "0";
    }

    /**
     * 显示增益方式
     */
    public void showGrowType() {
        desGain = sPreferences.getData_int(KEY_DES_GAIN);
        int dtGain = sPreferences.getData_int(KEY_DT_GAIN);
        // 辅助档位的值在第7位
        int aux = (desGain & 0x80) >> 7;
        // 取出普通档位的值
        desGain = desGain & 0x7f;
        String str = "";
        switch (desGain) {
            case 1:
                str = "低增益";
                break;
            case 2:
                str = "中增益";
                break;
            case 3:
                str = "高增益";
                break;
            case 4:
                str = "调试增益";
                break;
            default:
                str = "中增益";
                break;
        }
        switch (dtGain) {
            case 1:
                str += " | 低增益";
                break;
            case 2:
                str += " | 中增益";
                break;
            case 3:
                str += " | 高增益";
                break;
            default:
                str += " | 中增益";
                break;
        }
        gainTxt.setText(str);
    }

    public void showFpgaHeart(FPGA_DT_TO_PDA_HEART obj) {
        dtCount++;
        if (obj.getOverflow_a() == 1) {
            dtOverflow++;
        }
        // 开启心跳监听器
        // 第一次状态为100%
        if (null == timer) {
            dtCount = 10;
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    uiHandler.post(() -> setHeartState());
                }
            }, 0, heartbeatInterval);
        }
        // 先统计10次校准
//        if (!hasCalibrated && statisticCount < 10) {
//            calculateHeartbeatInterval();
//            setHeartState();
//        } else {
//            if (!hasCalibrated) {
//                hasCalibrated = true;
//                executorService.scheduleAtFixedRate(this::setHeartState,
//                        heartbeatInterval,
//                        heartbeatInterval,
//                        TimeUnit.MILLISECONDS);
//            }
//        }
    }

    /**
     * 计算心跳平均间隔
     * 校准实际心跳间隔
     */
    private void calculateHeartbeatInterval() {
        long curTime = System.currentTimeMillis();
        if (lastHeartbeat > 0 && curTime > lastHeartbeat) {
            long interval = curTime - lastHeartbeat;
//            Log.d(TAG, "interval: " + interval);
            if (interval > 400 && interval < 600) {
                statisticCount++;
                statisticTime += interval;
                heartbeatInterval = statisticTime / statisticCount * 10;
                Log.d(TAG, "calculateHeartbeatInterval: " + statisticCount + " 次平均:" + heartbeatInterval);
            }
        }
        lastHeartbeat = curTime;
    }

    public void onDesTory() {
        if (timer != null) {
            timer.cancel();
        }
    }
}
