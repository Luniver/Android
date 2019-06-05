package hrst.sczd.agreement.sczd;

/**
 * 数传终端 MCU 命令字
 *
 * @author glj
 *         2018-01-15
 */
public interface Cmd {

//--------------------普通事务------------------------

    /**
     * MCU对PDA的心跳 	MCU->PDA
     */
    short MCU_TO_PDA_HEART = 0x01;

    /**
     * PDA -> MCU 的心跳应答 	PDA->MCU
     */
    short MCU_TO_PDA_HEART_ACK = 0x81;

    /**
     * 数传对PDA心跳	 FPGA数传信号->PDA
     */
    short FPGA_DT_TO_PDA_HEART = 0x02;

    /**
     * PDA授时请求 	PDA->MCU
     */
    short PDA_TO_MCU_TIMING = 0x03;

    /**
     * MCU 日志存储 MCU -> PDA
     */
    short MCU_TO_PDA_LOG = 0x04;
    /**
     * 日志存储应答 PDA -> MUC
     */
    short PDA_TO_MCU_RECEIVE_LOG_ACK = 0x04 + 0x80;

    /**
     * FPGA状态对PDA的心跳	MCU->PDA
     */
    short FPGA_TO_PDA_HEART = 0x05;

    /**
     * PDA授时应答 	MCU->PDA
     */
    short PDA_TO_MCU_TIMING_ACK = 0x83;

//--------------------版本管理------------------------

    /**
     * PDA请求固件版本号 PDA->MCU
     */
    short PDA_TO_MCU_REQUEST_VERSION = 0x10;

    /**
     * PDA请求固件版本号应答	MCU->PDA
     */
    short PDA_TO_MCU_REQUEST_VERSION_ACK = 0x90;

//--------------------U盘相关------------------------

    /**
     * PDA请求U盘状态  PDA->MCU
     */
    short PDA_TO_MCU_U_DISK_STATE = 0x21;

    /**
     * PDA请求U盘状态应答 MCU->PDA
     */
    short PDA_TO_MCU_U_DISK_STATE_ACK = 0x21 + 0x80;

    /**
     * PDA请求U盘中固件文件名 PDA->MCU
     */
    short PDA_TO_MCU_REQUEST_U_DISK_FIRMWARE_NAME = 0x23;

    /**
     * PDA请求U盘中固件文件名应答 MCU->PDA
     */
    short PDA_TO_MCU_REQUEST_U_DISK_FIRMWARE_NAME_ACK = 0x23 + 0x80;

    /**
     * PDA请求U盘大小 PDA->MCU
     */
    short PDA_TO_MCU_REQUEST_U_DISK_SIZE = 0x25;

    /**
     * PDA请求U盘大小应答 MCU->PDA
     */
    short PDA_TO_MCU_REQUEST_U_DISK_SIZE_ACK = 0x25 + 0x80;

    /**
     * 设备通知PDA有入盘插入 MCU->PDA
     */
    short MCU_TO_PDA_U_DISK_INSERTION = 0x26;

//--------------------固件升级------------------------

    /**
     * PDA请求升级固件  PDA->MCU
     */
    short PDA_TO_MCU_REQUEST_UPDATE_FIRMWARE = 0x30;

    /**
     * PDA请求升级固件应答 MCU->PDA
     */
    short PDA_TO_MCU_REQUEST_UPDATE_FIRMWARE_ACK = 0x30 + 0x80;

    /**
     * 设备上报固件启动异常 MCU->PDA
     */
    short MCU_TO_PDA_FIRMWARE_STARTUP_ANOMALY = 0x31;

    /**
     * 设备上报固件启动异常应答 PDA->MCU
     */
    short MCU_TO_PDA_FIRMWARE_STARTUP_ANOMALY_ACK = 0x31 + 0x80;

//--------------------U盘存数------------------------

    /**
     * PDA 查询TF卡存数容量	PDA->MCU
     */
    short PDA_TO_MCU_CHECK_TF_KEEP_NUMBER_SIZE = 0x40;

    /**
     * PDA 查询TF卡存数容量应答 		MCU->PDA
     */
    short PDA_TO_MCU_CHECK_TF_KEEP_NUMBER_SIZE_ACK = 0x40 + 0x80;

    /**
     * PDA 请求存数 	PDA->MCU
     */
    short PDA_TO_MCU_REQUEST_KEEP_NUMBER = 0x42;

    /**
     * PDA 请求存数应答	 MCU->PDA
     */
    short PDA_TO_MCU_REQUEST_KEEP_NUMBER_ACK = 0x42 + 0x80;

    /**
     * PDA把存数复制到Ｕ盘 	PDA->MCU
     */
    short PDA_TO_MCU_KEEP_NUMBER_COPY_TO_U_DISK = 0x44;

    /**
     * PDA把存数复制到Ｕ盘应答	 MCU->PDA
     */
    short PDA_TO_MCU_KEEP_NUMBER_COPY_TO_U_DISK_ACK = 0x44 + 0x80;

    /**
     * PDF发送停止 复制到U盘的指令
     */
    short PDA_TO_MCU_STOP_COPY_TO_DISK = 0X41;
    /**
     * PDF发送停止 复制到U盘的指令应答
     */
    short PDA_TO_MCU_STOP_COPY_TO_DISK_ACK = 0X41 + 0x80;

    /**
     * PDA请求清除TF存数 	PDA->MCU
     */
    short PDA_TO_MCU_REQUEST_CLEAR_TF_KEEP_NUMBER = 0x46;

    /**
     * PDA请求清除TF存数应答 	MCU->PDA
     */
    short PDA_TO_MCU_REQUEST_CLEAR_TF_KEEP_NUMBER_ACK = 0x46 + 0x80;

    /**
     * PDA查询TF卡中的存数文件列表	 PDA->MCU
     */
    short PDA_TO_MCU_QUERY_TF_KEEP_NUMBER_FILE_LIST = 0x47;

    /**
     * PDA查询TF卡中的存数文件列表应答	 MCU->PDA
     */
    short PDA_TO_MCU_QUERY_TF_KEEP_NUMBER_FILE_LIST_ACK = 0x47 + 0x80;

    /**
     * PDA把指定的存数文件复制到Ｕ盘 	PDA->MCU
     */
    short PDA_TO_MCU_SELECT_KEEP_NUMBER_FILE_COPY_TO_U_DISK = 0x48;

    /**
     * PDA把指定的存数文件复制到Ｕ盘应答	 MCU->PDA
     */
    short PDA_TO_MCU_SELECT_KEEP_NUMBER_FILE_COPY_TO_U_DISK_ACK = 0x48 + 0x80;

    /**
     * PDA在TF卡中删除指定的存数文件 	PDA->MCU
     */
    short PDA_TO_MCU_DELETE_THE_SPECIFIED_FILE_FOR_TF = 0x49;

    /**
     * PDA在TF卡中删除指定的存数文件应答 	MCU->PDA
     */
    short PDA_TO_MCU_DELETE_THE_SPECIFIED_FILE_FOR_TF_ACK = 0x49 + 0x80;


//--------------------能量上报------------------------

    /**
     * 设备主动上报能量 	MCU->PDA
     */
    short MCU_TO_PDA_REPORT_ENERGY = 0x51;

//--------------------上报温度和电池状态------------------------

    /**
     * 设备主动上报温度/电池容量状态	MCU->PDA
     */
    short MCU_TO_PDA_TEMPERATURE_AND_ELECTRICITY_STATE = 0x60;

//--------------------制式/数传模式/增益设置--------------------

    /**
     * PDA请求制式设置 	PDA->MCU
     * enum{
     * LTE_STANDARD    = (0x01)
     * ,CDMA_STANDARD  = (0x02)
     * ,GSM_STANDARD   = (0x03)
     * ,WCDMA_STANDARD = (0x04)
     * ,TD_STANDARD    = (0x05)
     * };
     */
    short PDA_TO_MCU_REQUEST_MODE_SETTING = 0x70;

    /**
     * PDA请求制式设置应答 		MCU->PDA
     * 链路配置失败：-4
     * 资源配置失败：-3
     * 设备未就绪：-2
     * 设置失败：-1
     * 设置成功: 0
     * 无需切换：1
     * 资源配置成功：3
     * 链路配置成功：4
     */
    short PDA_TO_MCU_REQUEST_MODE_SETTING_ACK = 0x70 + 0x80;

    /**
     * PDA数传模式设置 	PDA->MCU
     */
    short PDA_TO_MCU_DT_MODE_SETTING = 0x72;

    /**
     * PDA数传模式设置应答 		MCU->PDA
     */
    short PDA_TO_MCU_DT_MODE_SETTING_ACK = 0x72 + 0x80;

    /**
     * PDA请求目标增益设置 	PDA->MCU
     */
    short PDA_TO_MCU_REQUEST_GAIN_SETTING = 0x74;

    /**
     * PDA请求目标增益设置应答 		MCU->PDA
     */
    short PDA_TO_MCU_REQUEST_GAIN_SETTING_ACK = 0x74 + 0x80;

    /**
     * PDA请求数传增益设置 	PDA->MCU
     */
    short PDA_TO_MCU_REQUEST_DT_GAIN_SETTING = 0x75;

    /**
     * PDA请求数传增益设置应答 		MCU->PDA
     */
    short PDA_TO_MCU_REQUEST_DT_GAIN_SETTING_ACK = 0x75 + 0x80;

    /**
     * PDA请求峰均比门限设置		PDA -> MCU
     */
    short PDA_TO_MCU_REQUEST_PEAK_AVERAGE_RATION = 0x76;

    /**
     * MCU 上报峰均比门限		MCU -> PDA
     */
    short PDA_TO_MCU_REQUEST_PEAK_AVERAGE_RATION_ACK = 0x76 + 0x80;

    /**
     * PDA请求灵敏度有效率统计		PDA -> MCU
     */
    short PDA_TO_MCU_REQUEST_SENSIBLE_VALID_STATISTIC = 0x77;

    /**
     * MCU 上报灵敏度有效率统计结果		MCU -> PDA
     */
    short PDA_TO_MCU_REQUEST_SENSIBLE_VALID_STATISTIC_ACK = 0x77 + 0x80;

    short DISCARD_DATA = 0x7fff;

    /**
     * todo 测试命令, 用来统计实际的丢包率
     */
    short DISCARD_TEST_START = 0x11;

    /**
     * mcu_log 调试信息开关
     * (未调试, 与李尚对接)
     */
    short MCU_LOG_DEBUG_SWITCH = 0x12;

    short MCU_LOG_DEBUG_SWITCH_ACK = 0x12 + 0x80;

}
