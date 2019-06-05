package hrst.sczd.agreement.manager;

import android.text.TextUtils;

import com.hrst.BluetoothCommHelper;
import com.hrst.mcu.ConvertUtil;
import com.hrst.mcu.McuUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

import hrst.sczd.agreement.sczd.Cmd;
import hrst.sczd.agreement.sczd.Cmd433;
import hrst.sczd.agreement.sczd.McuDataDispatcher;
import hrst.sczd.agreement.sczd.vo.FPGA_DT_TO_PDA_HEART;
import hrst.sczd.agreement.sczd.vo.MCU_TO_PDA_FIRMWARE_STARTUP_ANOMALY_ACK;
import hrst.sczd.agreement.sczd.vo.MCU_TO_PDA_HEART_ACK;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_CHECK_TF_KEEP_NUMBER_SIZE;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_DELETE_THE_SPECIFIED_FILE_FOR_TF;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_DT_MODE_SETTING;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_KEEP_NUMBER_COPY_TO_U_DISK;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_QUERY_TF_KEEP_NUMBER_FILE_LIST;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_REQUEST_CLEAR_TF_KEEP_NUMBER;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_REQUEST_DT_GAIN_SETTING;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_REQUEST_GAIN_SETTING;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_REQUEST_KEEP_NUMBER;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_REQUEST_MODE_SETTING;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_REQUEST_PEAK_AVERAGE_RATION;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_REQUEST_SENSIBLE_VALID_STATISTIC;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_REQUEST_STOP_COPY_TO_UDISK;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_REQUEST_UPDATE_FIRMWARE;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_REQUEST_U_DISK_FIRMWARE_NAME;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_REQUEST_U_DISK_SIZE;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_REQUEST_VERSION;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_SELECT_KEEP_NUMBER_FILE_COPY_TO_U_DISK;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_TIMING;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_U_DISK_STATE;
import hrst.sczd.agreement.sczd.vo.REQUEST_FIRM_UPGRADE;
import hrst.sczd.agreement.sczd.vo.REQUEST_FREQUENCY_SETTING;
import hrst.sczd.agreement.sczd.vo.REQUEST_QUERY_VERSION;
import hrst.sczd.agreement.sczd.vo.REQUEST_SYSTEM_SELF_CHECK;
import hrst.sczd.utils.log.IOPath;
import hrst.sczd.utils.log.LogWriteUtil;

/**
 * 数据转发中间管理器
 *
 * @author glj
 */
public class InteractiveManager {
    private static final String TAG = "goo-InteractManager";

    /**
     * 私有对象实例
     */
    private static InteractiveManager instance = null;
    private String logFileName = LogWriteUtil.getDateFileName();

    /**
     * 私有构造函数
     */
    private InteractiveManager() {
    }

    /**
     * 获取对象实例
     */
    public static InteractiveManager getInstance() {

        if (instance == null) {
            instance = new InteractiveManager();
        }

        return instance;
    }

    /**
     * PDA对MCU的心跳应答
     */
    public void sendHeartAck() {
        encapsulateDataAndSend(new MCU_TO_PDA_HEART_ACK(), Cmd.MCU_TO_PDA_HEART_ACK, McuDataDispatcher.PROPERTY_SC);
    }

    /**
     * PDA授时请求
     */
    public void sendTiming() {
        //时间(16进制年月日时分0000)，如:2018010415540000
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss00");//设置日期格式
        String dateStr = df.format(new Date()).toString();
//		dateStr = "2018120215380000";
        PDA_TO_MCU_TIMING timing = new PDA_TO_MCU_TIMING();
        timing.setAsTime_a(dateStr);
        encapsulateDataAndSend(timing, Cmd.PDA_TO_MCU_TIMING, McuDataDispatcher.PROPERTY_SC);
    }

    /**
     * PDA请求固件版本号
     */
    public void sendRequestVersion() {
        encapsulateDataAndSend(new PDA_TO_MCU_REQUEST_VERSION(), Cmd.PDA_TO_MCU_REQUEST_VERSION, McuDataDispatcher.PROPERTY_SC);
    }

    /**
     * PDA请求U盘状态
     */
    public void sendRequestUDiskState() {
        encapsulateDataAndSend(new PDA_TO_MCU_U_DISK_STATE(), Cmd.PDA_TO_MCU_U_DISK_STATE, McuDataDispatcher.PROPERTY_SC);
    }

    /**
     * PDA请求U盘中固件文件名
     */
    public void sendRequestUDiskFirmwareName() {
        encapsulateDataAndSend(new PDA_TO_MCU_REQUEST_U_DISK_FIRMWARE_NAME(), Cmd.PDA_TO_MCU_REQUEST_U_DISK_FIRMWARE_NAME, McuDataDispatcher.PROPERTY_SC);
    }

    /**
     * PDA请求U盘大小
     */
    public void sendRequestUDiskSize() {
        encapsulateDataAndSend(new PDA_TO_MCU_REQUEST_U_DISK_SIZE(), Cmd.PDA_TO_MCU_REQUEST_U_DISK_SIZE, McuDataDispatcher.PROPERTY_SC);
    }

    /**
     * PDA请求升级固件
     */
    public void sendRequestUpdateFirmware() {
        encapsulateDataAndSend(new PDA_TO_MCU_REQUEST_UPDATE_FIRMWARE(), Cmd.PDA_TO_MCU_REQUEST_UPDATE_FIRMWARE, McuDataDispatcher.PROPERTY_SC);
    }

    /**
     * 设备上报固件启动异常应答
     */
    public void sendFirmwareStartupAnomalyAck() {
        encapsulateDataAndSend(new MCU_TO_PDA_FIRMWARE_STARTUP_ANOMALY_ACK(), Cmd.MCU_TO_PDA_FIRMWARE_STARTUP_ANOMALY_ACK, McuDataDispatcher.PROPERTY_SC);
    }

    /**
     * PDA 查询TF卡存数容量
     */
    public void sendCheckTfKeepNumberSize() {
        encapsulateDataAndSend(new PDA_TO_MCU_CHECK_TF_KEEP_NUMBER_SIZE(), Cmd.PDA_TO_MCU_CHECK_TF_KEEP_NUMBER_SIZE, McuDataDispatcher.PROPERTY_SC);
    }

    /**
     * PDA 请求存数
     */
    public void sendRequestKeepNumber(PDA_TO_MCU_REQUEST_KEEP_NUMBER obj) {
        encapsulateDataAndSend(obj, Cmd.PDA_TO_MCU_REQUEST_KEEP_NUMBER, McuDataDispatcher.PROPERTY_SC);
    }

    /**
     * PDA把存数复制U盘
     */
    public void sendKeepNumberCopyToUDisk() {
        encapsulateDataAndSend(new PDA_TO_MCU_KEEP_NUMBER_COPY_TO_U_DISK(), Cmd.PDA_TO_MCU_KEEP_NUMBER_COPY_TO_U_DISK, McuDataDispatcher.PROPERTY_SC);
    }

    /**
     * PDA请求清除TF存数
     */
    public void sendRequestClearTfKeepNumber() {
        encapsulateDataAndSend(new PDA_TO_MCU_REQUEST_CLEAR_TF_KEEP_NUMBER(), Cmd.PDA_TO_MCU_REQUEST_CLEAR_TF_KEEP_NUMBER, McuDataDispatcher.PROPERTY_SC);
    }

    /**
     * PDA查询TF卡中的存数文件列表
     */
    public void sendQueryTfKeepNumberFileList(PDA_TO_MCU_QUERY_TF_KEEP_NUMBER_FILE_LIST obj) {
        encapsulateDataAndSend(obj, Cmd.PDA_TO_MCU_QUERY_TF_KEEP_NUMBER_FILE_LIST, McuDataDispatcher.PROPERTY_SC);
    }

    /**
     * PDA把指定的存数文件复制到Ｕ盘
     */
    public void sendSelectKeepNumberFileCopyToUDisk(PDA_TO_MCU_SELECT_KEEP_NUMBER_FILE_COPY_TO_U_DISK obj) {
        encapsulateDataAndSend(obj, Cmd.PDA_TO_MCU_SELECT_KEEP_NUMBER_FILE_COPY_TO_U_DISK, McuDataDispatcher.PROPERTY_SC);
    }

    /**
     * PDA把停止存数文件复制到Ｕ盘
     */
    public void sendStopKeepNumberFileCopyToUDisk() {
        encapsulateDataAndSend(new PDA_TO_MCU_REQUEST_STOP_COPY_TO_UDISK(), Cmd.PDA_TO_MCU_STOP_COPY_TO_DISK, McuDataDispatcher.PROPERTY_SC);
    }

    /**
     * PDA在TF卡中删除指定的存数文件
     */
    public void sendDeleteTheSpecifiedFileForTf(PDA_TO_MCU_DELETE_THE_SPECIFIED_FILE_FOR_TF obj) {
        encapsulateDataAndSend(obj, Cmd.PDA_TO_MCU_DELETE_THE_SPECIFIED_FILE_FOR_TF, McuDataDispatcher.PROPERTY_SC);
    }

    /**
     * PDA请求制式设置
     */
    public void sendRequestModeSetting(PDA_TO_MCU_REQUEST_MODE_SETTING obj) {
        encapsulateDataAndSend(obj, Cmd.PDA_TO_MCU_REQUEST_MODE_SETTING, McuDataDispatcher.PROPERTY_SC);
    }

    /**
     * PDA数传模式设置
     */
    public void sendDtModeSetting(PDA_TO_MCU_DT_MODE_SETTING obj) {
        encapsulateDataAndSend(obj, Cmd.PDA_TO_MCU_DT_MODE_SETTING, McuDataDispatcher.PROPERTY_SC);
    }

    /**
     * PDA请求目标增益设置
     */
    public void sendRequestGainSetting(PDA_TO_MCU_REQUEST_GAIN_SETTING obj) {
        encapsulateDataAndSend(obj, Cmd.PDA_TO_MCU_REQUEST_GAIN_SETTING, McuDataDispatcher.PROPERTY_SC);
    }


    /**
     * PDA请求数传增益设置
     */
    public void sendRequestDtGainSetting(PDA_TO_MCU_REQUEST_DT_GAIN_SETTING obj) {
        encapsulateDataAndSend(obj, Cmd.PDA_TO_MCU_REQUEST_DT_GAIN_SETTING, McuDataDispatcher.PROPERTY_SC);
    }


    /**
     * PDA 请求峰均比测试
     */
    public void sendRequestStartThresholdStatistic(PDA_TO_MCU_REQUEST_PEAK_AVERAGE_RATION obj) {
        encapsulateDataAndSend(obj, Cmd.PDA_TO_MCU_REQUEST_PEAK_AVERAGE_RATION, McuDataDispatcher.PROPERTY_SC);
    }

    /**
     * PDA 请求灵敏度有效率测试
     */
    public void sendRequestStartSensibleStatistic() {
        encapsulateDataAndSend(new PDA_TO_MCU_REQUEST_SENSIBLE_VALID_STATISTIC(), Cmd.PDA_TO_MCU_REQUEST_SENSIBLE_VALID_STATISTIC, McuDataDispatcher.PROPERTY_SC);
    }

    /**
     * 调试开关
     */
    public void sendDebugging(boolean debug) {
        FPGA_DT_TO_PDA_HEART debugging = new FPGA_DT_TO_PDA_HEART();
        debugging.setOverflow_a(debug ? (byte) 1 : 0);
        encapsulateDataAndSend(debugging, Cmd.MCU_LOG_DEBUG_SWITCH, McuDataDispatcher.PROPERTY_SC);
    }

    public void encapsulateDataAndSend(Object obj, short cmd, short property) {
        // 把数据转换成data
        byte[] data = ConvertUtil.getArrayByte(new Object[]{obj});

        //发送消息
        byte[] allData = McuUtil.packingMcuProtocol(cmd, property, data);

        String logStr = "send cmd : " + Integer.toHexString(0xff & cmd) + "\n" + obj.toString() + "\n";

        // 测试命令不需要保存
        if (cmd == Cmd.DISCARD_TEST_START) logStr = "";

        if (BluetoothCommHelper.get().isClassicBluetooth()) {
            // 经典蓝牙发送方式
            BluetoothCommHelper.get().write(allData);
        } else {
            // ble 蓝牙通信方式
            BluetoothCommHelper.get().write(null, null, allData);
        }
//        Log.d(TAG, "encapsulateDataAndSend:" + " \n " + logStr);
        //写入日志
        if (!TextUtils.isEmpty(logStr)) {
            LogWriteUtil.getWrite(IOPath.getLogSavePath(cmd), logFileName, logStr, true);
        }
    }

    /**
     * 433模块频点设置
     * @param freq
     */
    public void sendFrequencySetting433(short freq) {
        encapsulateDataAndSend(new REQUEST_FREQUENCY_SETTING(freq), Cmd433.PDA_TO_MCU_REQUEST_FREQUENCY_SETTING, McuDataDispatcher.PROPERTY_433);
    }

    /**
     * 433模块系统自检
     */
    public void sendSystemSelfCheck433(REQUEST_SYSTEM_SELF_CHECK obj){
        encapsulateDataAndSend(obj, Cmd433.PDA_TO_MCU_REQUEST_SYSTEM_SELF_CHECK, McuDataDispatcher.PROPERTY_433);
    }

    /**
     * 固件升级
     */
    public void sendFirmUpgrade433(REQUEST_FIRM_UPGRADE obj){
        encapsulateDataAndSend(obj, Cmd433.PDA_TO_MCU_REQUEST_FIRM_UPGRADE, McuDataDispatcher.PROPERTY_433);
    }

    /**
     * 版本查询指令
     */
    public void sendQueryVersion433(REQUEST_QUERY_VERSION obj){
        encapsulateDataAndSend(obj, Cmd433.PDA_TO_MCU_REQEUST_QUERY_VERSION, McuDataDispatcher.PROPERTY_433);
    }

    /**
     * flash版本查询
     */
    public void sendQueryFlashVersion433() {
        encapsulateDataAndSend(new REQUEST_QUERY_VERSION(), Cmd433.PDA_TO_MCU_REQEUST_QUERY_FLASH_VERSION, McuDataDispatcher.PROPERTY_433);
    }
}
