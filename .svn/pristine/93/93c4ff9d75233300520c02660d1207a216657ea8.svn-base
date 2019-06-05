package hrst.sczd.agreement.sczd;

import android.util.Log;

import com.hrst.BluetoothCommHelper;
import com.hrst.mcu.ConvertUtil;
import com.orhanobut.logger.Logger;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot.event.EventBus;
import hrst.sczd.agreement.manager.vo.QueryTfKeepNumberFile;
import hrst.sczd.agreement.manager.vo.UDiskFirmwareNameInfo;
import hrst.sczd.agreement.sczd.vo.FPGA_DT_TO_PDA_HEART;
import hrst.sczd.agreement.sczd.vo.FPGA_TO_PDA_HEART;
import hrst.sczd.agreement.sczd.vo.MCU_TO_PDA_FIRMWARE_STARTUP_ANOMALY;
import hrst.sczd.agreement.sczd.vo.MCU_TO_PDA_HEART;
import hrst.sczd.agreement.sczd.vo.MCU_TO_PDA_REPORT_ENERGY;
import hrst.sczd.agreement.sczd.vo.MCU_TO_PDA_TEMPERATURE_AND_ELECTRICITY_STATE;
import hrst.sczd.agreement.sczd.vo.MCU_TO_PDA_U_DISK_INSERTION;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_CHECK_TF_KEEP_NUMBER_SIZE_ACK;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_DELETE_THE_SPECIFIED_FILE_FOR_TF_ACK;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_DT_MODE_SETTING_ACK;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_KEEP_NUMBER_COPY_TO_U_DISK_ACK;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_QUERY_TF_KEEP_NUMBER_FILE_LIST_ACK;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_REQUEST_CLEAR_TF_KEEP_NUMBER_ACK;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_REQUEST_DT_GAIN_SETTING_ACK;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_REQUEST_GAIN_SETTING_ACK;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_REQUEST_KEEP_NUMBER_ACK;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_REQUEST_MODE_SETTING_ACK;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_REQUEST_PEAK_AVERAGE_RATION_ACK;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_REQUEST_SENSIBLE_VALID_STATISTIC_ACK;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_REQUEST_STOP_COPY_TO_UDISK_ACK;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_REQUEST_UPDATE_FIRMWARE_ACK;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_REQUEST_U_DISK_SIZE_ACK;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_REQUEST_VERSION_ACK;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_SELECT_KEEP_NUMBER_FILE_COPY_TO_U_DISK_ACK;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_TIMING_ACK;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_U_DISK_STATE_ACK;
import hrst.sczd.agreement.sczd.vo.REQUEST_FIRM_UPGRADE_ACK;
import hrst.sczd.agreement.sczd.vo.REQUEST_FREQUENCY_SETTING_ACK;
import hrst.sczd.agreement.sczd.vo.REQUEST_QUERY_FLASH_VERSION_ACK;
import hrst.sczd.agreement.sczd.vo.REQUEST_QUERY_VERSION_ACK;
import hrst.sczd.agreement.sczd.vo.REQUEST_SYSTEM_SELF_CHECK_ACK;
import hrst.sczd.ui.model.ConnectBroken;
import hrst.sczd.utils.log.IOPath;
import hrst.sczd.utils.log.LogWriteUtil;

/**
 * hrst.sczd.agreement
 * 通信数据集散分发器
 * by Sophimp
 * on 2018/4/27
 */
public class McuDataDispatcher {

    private static final String TAG = "goo-McuDataDispatcher";

    private static String fileName = LogWriteUtil.getDateFileName();

    /**
     * 数传终端mcu属性字
     */
    public static final short PROPERTY_SC = 0x18;

    /**
     * 433模块属性字
     */
    public static final short PROPERTY_433 = 0x1A;

    private static boolean isTesting = false;

    // 记录解析帧数
    private static long countFrame = 0;
    private static String startTime;
    private static String endTime;

    private static final long CONNECT_CHECK_PERIOD_MS = 5000;

    private static long lastHeartTime = 0;

    static {
        // 生命周期在整个应用使用期间, 退出应用, 自动回收
        Timer timer = new Timer("mcu_heart");
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // 设置连接状态
                if (lastHeartTime == 0) {
                    lastHeartTime = System.currentTimeMillis();
                } else {
                    ConnectBroken connectBroken = new ConnectBroken();
                    if (System.currentTimeMillis() - lastHeartTime < CONNECT_CHECK_PERIOD_MS) {
                        connectBroken.setBroken(false);
                        BluetoothCommHelper.get().setConnected(true);
                    } else {
                        connectBroken.setBroken(true);
                        BluetoothCommHelper.get().setConnected(false);
                    }
                    EventBus.getDefault().post(connectBroken);
                }
//                Logger.d("connecting: " + BluetoothCommHelper.get().isConnected());
            }
        }, 0, CONNECT_CHECK_PERIOD_MS);
    }

    /**
     * 数据接收监听
     */
    public static void dataReceiveListener(short cmd, short property, byte[] data) {
        switch (property) {
            case PROPERTY_SC:
                dispatchSCData(cmd, data);
                break;
            case PROPERTY_433:
                dispatch433Data(cmd, data);
                break;
        }

    }

    /**
     * 发送433 模块协议数据
     *
     * @param cmd  数据包命令字
     * @param data 数据内容(去掉协议头, 属性字等协议通用信息, 只包含命令内容)
     */
    private static void dispatch433Data(short cmd, byte[] data) {
        Object postObj = null;
        // 日志存储信息
        String logStr = "cmd: " + Integer.toHexString(cmd & 0xff) + " - " + ConvertUtil.toHexString(data) + "\n";
//        Logger.d("cmd: " + Integer.toHexString(cmd & 0xff) + " - " + ConvertUtil.toHexString(data) + "\n");
        switch (cmd) {
            case Cmd433.MCU_TO_PDA_FREQUENCY_SETTING_ACK:
                postObj = new REQUEST_FREQUENCY_SETTING_ACK();
                break;
            case Cmd433.MCU_TO_PDA_SYSTEM_SELF_CHECK_ACK:
                postObj = new REQUEST_SYSTEM_SELF_CHECK_ACK();
                break;
            case Cmd433.MCU_TO_PDA_FIRM_UPGRADE_ACK:
                postObj = new REQUEST_FIRM_UPGRADE_ACK();
                break;
            case Cmd433.MCU_TO_PDA_QUERY_VERSION_ACK:
                postObj = new REQUEST_QUERY_VERSION_ACK();
                break;
            case Cmd433.MCU_TO_PDA_QUERY_FLASH_VERSION_ACK:
                postObj = new REQUEST_QUERY_FLASH_VERSION_ACK();
                break;
        }
        if (null != postObj) {
            // 解析数据
            ConvertUtil.setDecompByte(data, new Object[]{postObj});
            EventBus.getDefault().post(postObj);
            logStr += postObj.toString();
        }
        // 数据日志本地保存
        LogWriteUtil.getWrite(IOPath.get433LogSavePath(cmd),
                fileName, logStr + "\n", true);

        // 本地测试验证
        if (isTesting) {
            countFrame++;
        } else if (countFrame > 0) {
            LogWriteUtil.getWrite(IOPath.getLogSavePath(Cmd.DISCARD_TEST_START),
                    fileName, "|----------------------| \n " +
                            "duration: \n" +
                            startTime + " -- \n" +
                            endTime + "\n本次测试共解析 " + countFrame + " 帧数据\n" +
                            "|----------------------| \n ", false);
            countFrame = 0;
        }
    }

    /**
     * 发送数传mcu协议数据
     *
     * @param cmd  数据包命令字
     * @param data 数据内容(去掉协议头, 属性字等协议通用信息, 只包含命令内容)
     */
    private static void dispatchSCData(short cmd, byte[] data) {
        // 只要收到信息就更新, 不限于心跳, 这里不会耗费多少性能
        lastHeartTime = System.currentTimeMillis();
        Object postObj = null;
        // 日志存储信息
        String logStr = "cmd: " + Integer.toHexString(cmd & 0xff) + " - " + ConvertUtil.toHexString(data) + "\n";
        switch (cmd) {
            //MCU对PDA的心跳
            case Cmd.MCU_TO_PDA_HEART:
                postObj = new MCU_TO_PDA_HEART();
                break;
            //FPGA 数传对PDA的心跳
            case Cmd.FPGA_DT_TO_PDA_HEART:
                postObj = new FPGA_DT_TO_PDA_HEART();
                break;
            //FPGA 状态对PDA心跳
            case Cmd.FPGA_TO_PDA_HEART:
                postObj = new FPGA_TO_PDA_HEART();
                break;
            //PDA授时应答
            case Cmd.PDA_TO_MCU_TIMING_ACK:
                postObj = new PDA_TO_MCU_TIMING_ACK();
                break;
            //设备向PDA请求固件版本号的应答
            case Cmd.PDA_TO_MCU_REQUEST_VERSION_ACK:
                postObj = new PDA_TO_MCU_REQUEST_VERSION_ACK();
                break;
            //PDA请求U盘状态应答
            case Cmd.PDA_TO_MCU_U_DISK_STATE_ACK:
                postObj = new PDA_TO_MCU_U_DISK_STATE_ACK();
                break;
            //PDA请求U盘中固件文件名应答
            case Cmd.PDA_TO_MCU_REQUEST_U_DISK_FIRMWARE_NAME_ACK:
                // 字符串动态解析, 兼容固件版本
                logStr = uDiskFirmwareNameAck(data);
                break;
            //PDA请求U盘大小应答
            case Cmd.PDA_TO_MCU_REQUEST_U_DISK_SIZE_ACK:
                postObj = new PDA_TO_MCU_REQUEST_U_DISK_SIZE_ACK();
                break;
            //设备通知PDA有入盘插入
            case Cmd.MCU_TO_PDA_U_DISK_INSERTION:
                postObj = new MCU_TO_PDA_U_DISK_INSERTION();
                break;
            //PDA请求升级固件应答
            case Cmd.PDA_TO_MCU_REQUEST_UPDATE_FIRMWARE_ACK:
                postObj = new PDA_TO_MCU_REQUEST_UPDATE_FIRMWARE_ACK();
                break;
            //设备上报固件启动异常
            case Cmd.MCU_TO_PDA_FIRMWARE_STARTUP_ANOMALY:
                postObj = new MCU_TO_PDA_FIRMWARE_STARTUP_ANOMALY();
                break;
            case Cmd.MCU_TO_PDA_LOG:
                logStr = mcuLogMessageSave(cmd, data);
                break;
            //PDA 查询TF卡存数容量应答
            case Cmd.PDA_TO_MCU_CHECK_TF_KEEP_NUMBER_SIZE_ACK:
                postObj = new PDA_TO_MCU_CHECK_TF_KEEP_NUMBER_SIZE_ACK();
                break;
            //PDA 请求存数应答
            case Cmd.PDA_TO_MCU_REQUEST_KEEP_NUMBER_ACK:
                postObj = new PDA_TO_MCU_REQUEST_KEEP_NUMBER_ACK();
                break;
            //PDA把存数复制到Ｕ盘应答
            case Cmd.PDA_TO_MCU_KEEP_NUMBER_COPY_TO_U_DISK_ACK:
                postObj = new PDA_TO_MCU_KEEP_NUMBER_COPY_TO_U_DISK_ACK();
                break;
            // PDA 发送停止复制的指令应答
            case Cmd.PDA_TO_MCU_STOP_COPY_TO_DISK_ACK:
                postObj = new PDA_TO_MCU_REQUEST_STOP_COPY_TO_UDISK_ACK();
                break;
            //PDA请求清除TF存数应答
            case Cmd.PDA_TO_MCU_REQUEST_CLEAR_TF_KEEP_NUMBER_ACK:
                postObj = new PDA_TO_MCU_REQUEST_CLEAR_TF_KEEP_NUMBER_ACK();
                break;
            //PDA查询TF卡中的存数文件列表应答
            case Cmd.PDA_TO_MCU_QUERY_TF_KEEP_NUMBER_FILE_LIST_ACK:
                logStr = queryTfKeepNumberAck(data);
                break;
            //PDA把指定的存数文件复制到Ｕ盘应答
            case Cmd.PDA_TO_MCU_SELECT_KEEP_NUMBER_FILE_COPY_TO_U_DISK_ACK:
                postObj = new PDA_TO_MCU_SELECT_KEEP_NUMBER_FILE_COPY_TO_U_DISK_ACK();
                break;
            //PDA在TF卡中删除指定的存数文件应答
            case Cmd.PDA_TO_MCU_DELETE_THE_SPECIFIED_FILE_FOR_TF_ACK:
                postObj = new PDA_TO_MCU_DELETE_THE_SPECIFIED_FILE_FOR_TF_ACK();
                break;
            //设备主动上报能量
            case Cmd.MCU_TO_PDA_REPORT_ENERGY:
                postObj = new MCU_TO_PDA_REPORT_ENERGY();
                break;
            //设备主动上报温度/电池容量状态
            case Cmd.MCU_TO_PDA_TEMPERATURE_AND_ELECTRICITY_STATE:
                postObj = new MCU_TO_PDA_TEMPERATURE_AND_ELECTRICITY_STATE();
                break;
            //PDA请求制式设置应答
            case Cmd.PDA_TO_MCU_REQUEST_MODE_SETTING_ACK:
                postObj = new PDA_TO_MCU_REQUEST_MODE_SETTING_ACK();
                break;
            //PDA数传模式设置应答
            case Cmd.PDA_TO_MCU_DT_MODE_SETTING_ACK:
                postObj = new PDA_TO_MCU_DT_MODE_SETTING_ACK();
                break;
            //PDA请求增益设置应答
            case Cmd.PDA_TO_MCU_REQUEST_GAIN_SETTING_ACK:
                postObj = new PDA_TO_MCU_REQUEST_GAIN_SETTING_ACK();
                break;
            //PDA请求数传增益设置应答
            case Cmd.PDA_TO_MCU_REQUEST_DT_GAIN_SETTING_ACK:
                postObj = new PDA_TO_MCU_REQUEST_DT_GAIN_SETTING_ACK();
                break;
            //MCU 应答峰均比
            case Cmd.PDA_TO_MCU_REQUEST_PEAK_AVERAGE_RATION_ACK:
                postObj = new PDA_TO_MCU_REQUEST_PEAK_AVERAGE_RATION_ACK();
                break;
            //MCU 应答灵敏度
            case Cmd.PDA_TO_MCU_REQUEST_SENSIBLE_VALID_STATISTIC_ACK:
                postObj = new PDA_TO_MCU_REQUEST_SENSIBLE_VALID_STATISTIC_ACK();
                break;
            // 自定义命令, 用来存储抛弃数据包及解析错误数据包日志
            case Cmd.DISCARD_DATA:
                logStr = new String(data);
                break;
            default:
                Log.d(TAG, "未处理: " + Integer.toHexString(0xff & cmd) + "---" + ConvertUtil.toHexString(data));
                break;
        }
        if (null != postObj) {
            // 解析数据
            ConvertUtil.setDecompByte(data, new Object[]{postObj});
            EventBus.getDefault().post(postObj);
            logStr += postObj.toString();
        }
        // 数据日志本地保存
        LogWriteUtil.getWrite(IOPath.getLogSavePath(cmd),
                fileName, logStr + "\n", true);

        // 本地测试验证
        if (isTesting) {
            countFrame++;
        } else if (countFrame > 0) {
            LogWriteUtil.getWrite(IOPath.getLogSavePath(Cmd.DISCARD_TEST_START),
                    fileName, "|----------------------| \n " +
                            "duration: \n" +
                            startTime + " -- \n" +
                            endTime + "\n本次测试共解析 " + countFrame + " 帧数据\n" +
                            "|----------------------| \n ", false);
            countFrame = 0;
        }
    }

    public static void setTesting(boolean testing) {
        isTesting = testing;
        if (testing) {
            startTime = LogWriteUtil.completeSf.format(new Date());
        } else {
            endTime = LogWriteUtil.completeSf.format(new Date());
        }
    }

    /**
     * PDA请求U盘中固件文件名应答
     *
     * @return 日志信息
     */
    private static String uDiskFirmwareNameAck(byte[] data) {

        // 为了兼容arm 端修改了接口的情况
        String describeStr = ConvertUtil.decodeStringDataWithState(data);
        int state = data[0] & 0xff;
        UDiskFirmwareNameInfo info = new UDiskFirmwareNameInfo();
        info.setResult(state);
        info.setDescribeStr(describeStr);

        // 校验Crc
        EventBus.getDefault().post(info);
        // 数据日志
        String logStr = ConvertUtil.toHexString(data) + " - content: " + describeStr;
        return logStr;
    }

    /**
     * 保存mcu端日志信息
     */
    private static String mcuLogMessageSave(short cmd, byte[] data) {
//        MCU_TO_PDA_LOG obj = new MCU_TO_PDA_LOG();
        // 解析数据
//        ConvertUtil.setDecompByte(data, new Object[] { obj });
        // 数据日志
//        String logStr = new String(obj.getLogMessage_a());
        String logStr = new String(data);
        Log.d("goo", "cmd: " + cmd + "logStr: " + logStr);
        return logStr;
    }

    /**
     * PDA查询TF卡中的存数文件列表应答
     *
     * @return 日志打印
     */
    private static String queryTfKeepNumberAck(byte[] data) {

        PDA_TO_MCU_QUERY_TF_KEEP_NUMBER_FILE_LIST_ACK obj = new PDA_TO_MCU_QUERY_TF_KEEP_NUMBER_FILE_LIST_ACK();

        // 解析数据
        ConvertUtil.setDecompByte(data, new Object[]{obj});

        String fileName = null;
        try {
            fileName = new String(obj.getcFileName_c(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        QueryTfKeepNumberFile info = new QueryTfKeepNumberFile();
        info.setTotalFile(obj.getTotalFile_a());
        info.setFileName(fileName);
        info.setFileNum(obj.getFileNumber_b());

        // 数据日志
        String logStr = "ucTotalFile_a = " + obj.getTotalFile_a() + "\n"
                + "ucFileNumber_b = " + obj.getFileNumber_b() + "\n"
                + "cFileName_c = " + fileName;


        // 校验Crc
        EventBus.getDefault().post(info);
        return logStr;
    }

}

