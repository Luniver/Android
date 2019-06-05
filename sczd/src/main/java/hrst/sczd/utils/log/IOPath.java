package hrst.sczd.utils.log;

import android.os.Environment;

import java.io.File;

import hrst.sczd.agreement.sczd.Cmd;
import hrst.sczd.agreement.sczd.Cmd433;

/**
 * 文件地址
 *
 * @author glj
 *         2018-01-16
 */
public class IOPath {

    /**
     * Android SD卡 根目录
     */
    private static final File SD_PATH = Environment.getExternalStorageDirectory();

    /**
     * 数传终端总路径
     */
    public static final String SCZD_PATH = SD_PATH.toString() + "/hrst/sczd/";

    /**
     * /hrst/sczd/sendData
     */
    private static final String SEND_DATA_PATH = SCZD_PATH + "sendData";

    /**
     * /hrst/sczd/receviceData
     */
    private static final String RECEVICE_DATA_PATH = SCZD_PATH + "receiveData";

    /**
     * 433固件升级存放目录
     */
    public static final String FIRM_FILE_PATH = SCZD_PATH + "firms/";

    public static final String SENSIBLE_STATISTIC_DATA = SCZD_PATH + "sensible/";

    /**
     * 获取命令通信存储路径
     */
    public static String getLogSavePath(short cmd) {
        String path = SCZD_PATH;
        switch (cmd) {
            case Cmd.FPGA_DT_TO_PDA_HEART:
                path = RECEVICE_DATA_PATH + "/fpga_dt_heart/";
                break;
            case Cmd.FPGA_TO_PDA_HEART:
                path = RECEVICE_DATA_PATH + "/fpga_state_heart/";
                break;
            case Cmd.MCU_TO_PDA_FIRMWARE_STARTUP_ANOMALY:
                path = RECEVICE_DATA_PATH + "/mcu_startup/";
                break;
            case Cmd.MCU_TO_PDA_FIRMWARE_STARTUP_ANOMALY_ACK:
                path = SEND_DATA_PATH + "/mcu_startup_ack/";
                break;
            case Cmd.MCU_TO_PDA_HEART:
                path = RECEVICE_DATA_PATH + "/mcu_heart/";
                break;
            case Cmd.MCU_TO_PDA_HEART_ACK:
                path = SEND_DATA_PATH + "/mcu_heart_ack/";
                break;
            case Cmd.MCU_TO_PDA_LOG:
                path = RECEVICE_DATA_PATH + "/mcu_log/";
                break;
            case Cmd.MCU_TO_PDA_REPORT_ENERGY:
                path = RECEVICE_DATA_PATH + "/energy_report/";
                break;
            case Cmd.MCU_TO_PDA_TEMPERATURE_AND_ELECTRICITY_STATE:
                path = RECEVICE_DATA_PATH + "/temperature_electricity/";
                break;
            case Cmd.MCU_TO_PDA_U_DISK_INSERTION:
                path = RECEVICE_DATA_PATH + "/u_disk/insert_state/";
                break;
            case Cmd.PDA_TO_MCU_CHECK_TF_KEEP_NUMBER_SIZE:
                path = SEND_DATA_PATH + "/tf_keep_number/size/";
                break;
            case Cmd.PDA_TO_MCU_CHECK_TF_KEEP_NUMBER_SIZE_ACK:
                path = RECEVICE_DATA_PATH + "/tf_Keep_number/size_ack/";
                break;
            case Cmd.PDA_TO_MCU_DELETE_THE_SPECIFIED_FILE_FOR_TF:
                path = SEND_DATA_PATH + "/tf_keep_number/delete_select/";
                break;
            case Cmd.PDA_TO_MCU_DELETE_THE_SPECIFIED_FILE_FOR_TF_ACK:
                path = RECEVICE_DATA_PATH + "/tf_keep_number/delete_select_ack/";
                break;
            case Cmd.PDA_TO_MCU_DT_MODE_SETTING:
                path = SEND_DATA_PATH + "/dt_mode/setting/";
                break;
            case Cmd.PDA_TO_MCU_DT_MODE_SETTING_ACK:
                path = RECEVICE_DATA_PATH + "/dt_mode/setting_ack/";
                break;
            case Cmd.PDA_TO_MCU_KEEP_NUMBER_COPY_TO_U_DISK:
                path = SEND_DATA_PATH + "/u_disk/copy_tf_data/";
                break;
            case Cmd.PDA_TO_MCU_KEEP_NUMBER_COPY_TO_U_DISK_ACK:
                path = RECEVICE_DATA_PATH + "/u_disk/copy_tf_data_ack/";
                break;
            case Cmd.PDA_TO_MCU_QUERY_TF_KEEP_NUMBER_FILE_LIST:
                path = SEND_DATA_PATH + "/tf_keep_number/query_files/";
                break;
            case Cmd.PDA_TO_MCU_QUERY_TF_KEEP_NUMBER_FILE_LIST_ACK:
                path = RECEVICE_DATA_PATH + "/tf_keep_number/query_files_ack/";
                break;
            case Cmd.PDA_TO_MCU_RECEIVE_LOG_ACK:
                path = SEND_DATA_PATH + "/mcu_log_ack/";
                break;
            case Cmd.PDA_TO_MCU_REQUEST_CLEAR_TF_KEEP_NUMBER:
                path = SEND_DATA_PATH + "/tf_keep_number/clear/";
                break;
            case Cmd.PDA_TO_MCU_REQUEST_CLEAR_TF_KEEP_NUMBER_ACK:
                path = RECEVICE_DATA_PATH + "/tf_keep_number/clear_ack/";
                break;
            case Cmd.PDA_TO_MCU_REQUEST_DT_GAIN_SETTING:
                path = SEND_DATA_PATH + "/gain/dt_setting/";
                break;
            case Cmd.PDA_TO_MCU_REQUEST_DT_GAIN_SETTING_ACK:
                path = RECEVICE_DATA_PATH + "/gain/dt_setting_ack/";
                break;
            case Cmd.PDA_TO_MCU_REQUEST_GAIN_SETTING:
                path = SEND_DATA_PATH + "/gain/goal_setting/";
                break;
            case Cmd.PDA_TO_MCU_REQUEST_GAIN_SETTING_ACK:
                path = RECEVICE_DATA_PATH + "/gain/goal_setting_ack/";
                break;
            case Cmd.PDA_TO_MCU_REQUEST_KEEP_NUMBER:
                path = SEND_DATA_PATH + "/tf_keep_number/state/";
                break;
            case Cmd.PDA_TO_MCU_REQUEST_KEEP_NUMBER_ACK:
                path = RECEVICE_DATA_PATH + "/tf_keep_number/state_ack/";
                break;
            case Cmd.PDA_TO_MCU_REQUEST_MODE_SETTING:
                path = SEND_DATA_PATH + "/operation_mode/";
                break;
            case Cmd.PDA_TO_MCU_REQUEST_MODE_SETTING_ACK:
                path = RECEVICE_DATA_PATH + "/operation_mode_ack/";
                break;
            case Cmd.PDA_TO_MCU_REQUEST_PEAK_AVERAGE_RATION:
                path = SEND_DATA_PATH + "/peak_average_ration_statistic/";
                break;
            case Cmd.PDA_TO_MCU_REQUEST_PEAK_AVERAGE_RATION_ACK:
                path = RECEVICE_DATA_PATH + "/peak_average_ration_ack/";
                break;
            case Cmd.PDA_TO_MCU_REQUEST_SENSIBLE_VALID_STATISTIC:
                path = SEND_DATA_PATH + "/sensible_statistic/";
                break;
            case Cmd.PDA_TO_MCU_REQUEST_SENSIBLE_VALID_STATISTIC_ACK:
                path = RECEVICE_DATA_PATH + "/sensible_statistic_ack/";
                break;
            case Cmd.PDA_TO_MCU_REQUEST_UPDATE_FIRMWARE:
                path = SEND_DATA_PATH + "/firmware_update/";
                break;
            case Cmd.PDA_TO_MCU_REQUEST_UPDATE_FIRMWARE_ACK:
                path = RECEVICE_DATA_PATH + "/firmware_update_ack/";
                break;
            case Cmd.PDA_TO_MCU_REQUEST_U_DISK_FIRMWARE_NAME:
                path = SEND_DATA_PATH + "/firmware_name/";
                break;
            case Cmd.PDA_TO_MCU_REQUEST_U_DISK_FIRMWARE_NAME_ACK:
                path = RECEVICE_DATA_PATH + "/firmware_name_ack/";
                break;
            case Cmd.PDA_TO_MCU_REQUEST_U_DISK_SIZE:
                path = SEND_DATA_PATH + "/u_disk/size/";
                break;
            case Cmd.PDA_TO_MCU_REQUEST_U_DISK_SIZE_ACK:
                path = RECEVICE_DATA_PATH + "/u_disk/size_ack/";
                break;
            case Cmd.PDA_TO_MCU_REQUEST_VERSION:
                path = SEND_DATA_PATH + "/request_version/";
                break;
            case Cmd.PDA_TO_MCU_REQUEST_VERSION_ACK:
                path = RECEVICE_DATA_PATH + "/request_version_ack/";
                break;
            case Cmd.PDA_TO_MCU_SELECT_KEEP_NUMBER_FILE_COPY_TO_U_DISK:
                path = SEND_DATA_PATH + "/u_disk/copy_select_file/";
                break;
            case Cmd.PDA_TO_MCU_SELECT_KEEP_NUMBER_FILE_COPY_TO_U_DISK_ACK:
                path = RECEVICE_DATA_PATH + "/u_disk/copy_select_file_ack/";
                break;
            case Cmd.PDA_TO_MCU_STOP_COPY_TO_DISK:
                path = SEND_DATA_PATH + "/u_disk/stop_copy_file/";
                break;
            case Cmd.PDA_TO_MCU_STOP_COPY_TO_DISK_ACK:
                path = RECEVICE_DATA_PATH + "/u_disk/stop_copy_file_ack/";
                break;
            case Cmd.PDA_TO_MCU_TIMING:
                path = SEND_DATA_PATH + "/timing/";
                break;
            case Cmd.PDA_TO_MCU_TIMING_ACK:
                path = RECEVICE_DATA_PATH + "/timing_ack/";
                break;
            case Cmd.PDA_TO_MCU_U_DISK_STATE:
                path = SEND_DATA_PATH + "/u_disk/state/";
                break;
            case Cmd.PDA_TO_MCU_U_DISK_STATE_ACK:
                path = RECEVICE_DATA_PATH + "/u_disk/state_ack/";
                break;
            case Cmd.DISCARD_DATA:
                path = RECEVICE_DATA_PATH + "/discard_bytes/";
                break;
            case Cmd.DISCARD_TEST_START:
                path = RECEVICE_DATA_PATH + "/discard_test/";
                break;
            default:
                path = RECEVICE_DATA_PATH + "/unrecognized/";
                break;
        }

        return path;
    }

    /**
     * 获取433通信数据日志存储路径
     */
    public static String get433LogSavePath(short cmd) {
        String path = SCZD_PATH;
        switch (cmd) {
            case Cmd433.PDA_TO_MCU_REQUEST_FREQUENCY_SETTING:
                path = SEND_DATA_PATH + "433/frequency_setting/";
                break;
            case Cmd433.MCU_TO_PDA_FREQUENCY_SETTING_ACK:
                path = RECEVICE_DATA_PATH + "433/frequency_setting_ack/";
                break;
            case Cmd433.PDA_TO_MCU_REQUEST_SYSTEM_SELF_CHECK:
                path = SEND_DATA_PATH + "433/system_self_check/";
                break;
            case Cmd433.MCU_TO_PDA_SYSTEM_SELF_CHECK_ACK:
                path = RECEVICE_DATA_PATH + "433/system_self_check_ack/";
                break;
            case Cmd433.PDA_TO_MCU_REQUEST_FIRM_UPGRADE:
                path = SEND_DATA_PATH + "433/firm_upgrade/";
                break;
            case Cmd433.MCU_TO_PDA_FIRM_UPGRADE_ACK:
                path = RECEVICE_DATA_PATH + "433/firm_upgrade_ack/";
                break;
            case Cmd433.PDA_TO_MCU_REQEUST_QUERY_VERSION:
                path = SEND_DATA_PATH + "433/version/";
                break;
            case Cmd433.MCU_TO_PDA_QUERY_VERSION_ACK:
                path = RECEVICE_DATA_PATH + "433/version_ack/";
                break;
            case Cmd433.MCU_TO_PDA_QUERY_FLASH_VERSION_ACK:
                path = RECEVICE_DATA_PATH + "433/flash_version_ack/";
                break;
        }

        return path;
    }
}
