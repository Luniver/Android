package hrst.sczd.utils.log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 数据文件存储工具类
 * @author glj
 * 2018/01/16
 */
public class LogWriteUtil {
    public static FileUtil fileUtil;

    //-------------------ACK-----------------------
	/** MCU对PDA的心跳应答 */
	public static String PDA_TO_MCU_HEART_ACK = "";
	
	/** PDA授时请求*/
	public static String PDA_TO_MCU_TIMING_ACK = "";
	
	/** PDA请求固件版本号 */
	public static String PDA_TO_MCU_REQUEST_VERSION_ACK = "";
	
	/** PDA请求U盘状态 */
	public static String PDA_TO_MCU_U_DISK_STATE_ACK = "";
	
	/** PDA请求U盘中固件文件名*/
	public static String PDA_TO_MCU_REQUEST_U_DISK_FIRMWARE_NAME_ACK = "";
	
	/** PDA请求U盘大小*/
	public static String PDA_TO_MCU_REQUEST_U_DISK_SIZE_ACK = "";
	
	/** PDA请求升级固件 */
	public static String PDA_TO_MCU_REQUEST_UPDATE_FIRMWARE_ACK = "";
	
	/** 设备上报固件启动异常应答*/
	public static String PDA_TO_MCU_FIRMWARE_STARTUP_ANOMALY_ACK = "";
	
	/** PDA 查询TF卡存数容量*/
	public static String PDA_TO_MCU_CHECK_TF_KEEP_NUMBER_SIZE_ACK = "";
	
	/** PDA 请求存数*/
	public static String PDA_TO_MCU_REQUEST_KEEP_NUMBER_ACK = "";
	
	/** PDA把存数复制到U盘 */
	public static String PDA_TO_MCU_KEEP_NUMBER_COPY_TO_U_DISK_ACK = "";
	
	/** PDA请求清除TF存数 */
	public static String PDA_TO_MCU_REQUEST_CLEAR_TF_KEEP_NUMBER_ACK = "";
	
	/** PDA查询TF卡中的存数文件列表*/
	public static String PDA_TO_MCU_QUERY_TF_KEEP_NUMBER_FILE_LIST_ACK = "";
	
	/** PDA把指定的存数文件复制到U盘*/
	public static String PDA_TO_MCU_SELECT_KEEP_NUMBER_FILE_COPY_TO_U_DISK_ACK = "";
	
	/** PDA在TF卡中删除指定的存数文件*/
	public static String PDA_TO_MCU_DELETE_THE_SPECIFIED_FILE_FOR_TF_ACK = "";
	
	/** PDA请求制式设置 */
	public static String PDA_TO_MCU_REQUEST_MODE_SETTING_ACK = "";
	
	/** PDA数传模式设置*/
	public static String PDA_TO_MCU_DT_MODE_SETTING_ACK = "";
	
	/** PDA请求增益设置*/
	public static String PDA_TO_MCU_REQUEST_GAIN_SETTING_ACK = "";
    //----------------------------------------------
    
    
    //-------------------RECV-----------------------
	/** MCU对PDA的心跳 */
	public static String MCU_TO_PDA_HEART_RECEVICE = "";
	
	/** FPGA对PDA的心跳 */
	public static String FPGA_TO_PDA_HEART_RECEVICE = "";
	
	/** PDA授时应答*/
	public static String MCU_TO_PDA_TIMING_RECEVICE = "";
	
	/** PDA请求固件版本号应答 */
	public static String MCU_TO_PDA_REQUEST_VERSION_RECEVICE = "";
	
	/** PDA请求U盘状态应答*/
	public static String MCU_TO_PDA_U_DISK_STATE_RECEVICE = "";
	
	/** PDA请求U盘中固件文件名应答 */
	public static String MCU_TO_PDA_REQUEST_U_DISK_FIRMWARE_NAME_RECEVICE = "";
	
	/** PDA请求U盘大小应答 */
	public static String MCU_TO_PDA_REQUEST_U_DISK_SIZE_RECEVICE = "";
	
	/** 设备通知PDA有入盘插入 */
	public static String MCU_TO_PDA_U_DISK_INSERTION_RECEVICE = "";
	
	/** PDA请求升级固件应答 */
	public static String MCU_TO_PDA_REQUEST_UPDATE_FIRMWARE_RECEVICE = "";
	
	/** 设备上报固件启动异常 */
	public static String MCU_TO_PDA_FIRMWARE_STARTUP_ANOMALY_RECEVICE = "";
	
	/** PDA 查询TF卡存数容量应答 */
	public static String MCU_TO_PDA_CHECK_TF_KEEP_NUMBER_SIZE_RECEVICE = "";
	
	/** PDA 请求存数应答 */
	public static String MCU_TO_PDA_REQUEST_KEEP_NUMBER_RECEVICE = "";
	
	/** PDA把存数复制到Ｕ盘应答 */
	public static String MCU_TO_PDA_KEEP_NUMBER_COPY_TO_U_DISK_RECEVICE = "";
	
	/** PDA请求清除TF存数应答 */
	public static String MCU_TO_PDA_REQUEST_CLEAR_TF_KEEP_NUMBER_RECEVICE = "";
	
	/** PDA查询TF卡中的存数文件列表应答 */
	public static String MCU_TO_PDA_QUERY_TF_KEEP_NUMBER_FILE_LIST_RECEVICE = "";
	
	/** PDA把指定的存数文件复制到Ｕ盘应答 */
	public static String MCU_TO_PDA_SELECT_KEEP_NUMBER_FILE_COPY_TO_U_DISK_RECEVICE = "";
	
	/** PDA在TF卡中删除指定的存数文件应答 */
	public static String MCU_TO_PDA_DELETE_THE_SPECIFIED_FILE_FOR_TF_RECEVICE = "";
	
	/** 设备主动上报能量 */
	public static String MCU_TO_PDA_REPORT_ENERGY_RECEVICE = "";
	
	/** 设备主动上报温度/电池容量状态 */
	public static String MCU_TO_PDA_TEMPERATURE_AND_ELECTRICITY_STATE_RECEVICE = "";
	
	/** PDA请求制式设置应答 */
	public static String MCU_TO_PDA_REQUEST_MODE_SETTING_RECEVICE = "";
	
	/** PDA数传模式设置应答 */
	public static String MCU_TO_PDA_DT_MODE_SETTING_RECEVICE = "";
	
	/** PDA请求增益设置应答 */
	public static String MCU_TO_PDA_REQUEST_GAIN_SETTING_RECEVICE = "";

	/**
	 * MCU -> PDA 日志信息存储
	 */
	public static String MCU_TO_PDA_LOG_RECEIVE = "";

	/**
	 * pda -> mcu 日志应答
	 */
	public static String PDA_TO_MCU_RECEIVE_LOG_ACK_NAME;
    //--------------------------------------------------
    
    
    public static String strDate;
    
    public static SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHH");
    public static SimpleDateFormat completeSf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


	/**
     * 生成文件名
     */
    public static void getInstance() {
        fileUtil = new FileUtil();
        Date date = new Date();
        strDate = sf.format(date);
        
        
        //---------------ACK-------------------
    	PDA_TO_MCU_HEART_ACK = strDate + ".txt";
    	PDA_TO_MCU_TIMING_ACK = strDate + ".txt";
    	PDA_TO_MCU_REQUEST_VERSION_ACK = strDate + ".txt";
    	PDA_TO_MCU_U_DISK_STATE_ACK = strDate + ".txt";
    	PDA_TO_MCU_REQUEST_U_DISK_FIRMWARE_NAME_ACK = strDate + ".txt";
    	PDA_TO_MCU_REQUEST_U_DISK_SIZE_ACK = strDate + ".txt";
    	PDA_TO_MCU_REQUEST_UPDATE_FIRMWARE_ACK = strDate + ".txt";
    	PDA_TO_MCU_FIRMWARE_STARTUP_ANOMALY_ACK = strDate + ".txt";
    	PDA_TO_MCU_CHECK_TF_KEEP_NUMBER_SIZE_ACK = strDate + ".txt";
    	PDA_TO_MCU_REQUEST_KEEP_NUMBER_ACK = strDate + ".txt";
    	PDA_TO_MCU_KEEP_NUMBER_COPY_TO_U_DISK_ACK = strDate + ".txt";
    	PDA_TO_MCU_REQUEST_CLEAR_TF_KEEP_NUMBER_ACK = strDate + ".txt";
    	PDA_TO_MCU_QUERY_TF_KEEP_NUMBER_FILE_LIST_ACK = strDate + ".txt";
    	PDA_TO_MCU_SELECT_KEEP_NUMBER_FILE_COPY_TO_U_DISK_ACK = strDate + ".txt";
    	PDA_TO_MCU_DELETE_THE_SPECIFIED_FILE_FOR_TF_ACK = strDate + ".txt";
    	PDA_TO_MCU_REQUEST_MODE_SETTING_ACK = strDate + ".txt";
    	PDA_TO_MCU_DT_MODE_SETTING_ACK = strDate + ".txt";
    	PDA_TO_MCU_REQUEST_GAIN_SETTING_ACK = strDate + ".txt";
        
        //---------------RECV-------------------
    	MCU_TO_PDA_HEART_RECEVICE = strDate + ".txt";
    	FPGA_TO_PDA_HEART_RECEVICE = strDate + ".txt";
    	MCU_TO_PDA_TIMING_RECEVICE = strDate + ".txt";
    	MCU_TO_PDA_REQUEST_VERSION_RECEVICE = strDate + ".txt";
    	MCU_TO_PDA_U_DISK_STATE_RECEVICE = strDate + ".txt";
    	MCU_TO_PDA_REQUEST_U_DISK_FIRMWARE_NAME_RECEVICE = strDate + ".txt";
    	MCU_TO_PDA_REQUEST_U_DISK_SIZE_RECEVICE = strDate + ".txt";
    	MCU_TO_PDA_U_DISK_INSERTION_RECEVICE = strDate + ".txt";
    	MCU_TO_PDA_REQUEST_UPDATE_FIRMWARE_RECEVICE = strDate + ".txt";
    	MCU_TO_PDA_FIRMWARE_STARTUP_ANOMALY_RECEVICE = strDate + ".txt";
    	MCU_TO_PDA_CHECK_TF_KEEP_NUMBER_SIZE_RECEVICE = strDate + ".txt";
    	MCU_TO_PDA_REQUEST_KEEP_NUMBER_RECEVICE = strDate + ".txt";
    	MCU_TO_PDA_KEEP_NUMBER_COPY_TO_U_DISK_RECEVICE = strDate + ".txt";
    	MCU_TO_PDA_REQUEST_CLEAR_TF_KEEP_NUMBER_RECEVICE = strDate + ".txt";
    	MCU_TO_PDA_QUERY_TF_KEEP_NUMBER_FILE_LIST_RECEVICE = strDate + ".txt";
    	MCU_TO_PDA_SELECT_KEEP_NUMBER_FILE_COPY_TO_U_DISK_RECEVICE = strDate + ".txt";
    	MCU_TO_PDA_DELETE_THE_SPECIFIED_FILE_FOR_TF_RECEVICE = strDate + ".txt";
    	MCU_TO_PDA_REPORT_ENERGY_RECEVICE = strDate + ".txt";
    	MCU_TO_PDA_TEMPERATURE_AND_ELECTRICITY_STATE_RECEVICE = strDate + ".txt";
    	MCU_TO_PDA_REQUEST_MODE_SETTING_RECEVICE = strDate + ".txt";
    	MCU_TO_PDA_DT_MODE_SETTING_RECEVICE = strDate + ".txt";
    	MCU_TO_PDA_REQUEST_GAIN_SETTING_RECEVICE = strDate + ".txt";

		MCU_TO_PDA_LOG_RECEIVE = strDate + ".txt";

		PDA_TO_MCU_RECEIVE_LOG_ACK_NAME = strDate + ".txt";
    }
    
    /**
     * 写入文件
     * 
     * @param filepath
     *            路径
     * @param filename
     *            文件名
     * @param fileStr
     *            数据内容
     * @param hasTime
     * 			  是否需要时间记录
     */
    public static void getWrite(String filepath, String filename, String fileStr, boolean hasTime) {
        
    	if (fileUtil == null) {
            getInstance();
        }
        
        //路径或文件名为空则不保存数据
        if (filepath == null || filename == null) {
        	return;
        }
        
        if (hasTime) {
            Date date = new Date();
            String targetDate = completeSf.format(date);
            File file = fileUtil.write2SDFromString2(filepath, filename, "time:"+targetDate + "\n  " + fileStr);
        } 
        
        else {
        	File file = fileUtil.write2SDFromString2(filepath, filename, fileStr);
        }

    }

	/**
	 * 获得以日期命名的文件名
	 */
	public static String getDateFileName() {
		Date date = new Date();
		strDate = sf.format(date);
		return strDate + ".txt";
    }
}
