package hrst.sczd.ui.vo;

public interface SharedPreferencesValues {
	/**
	 * GSM目标监听状态值
	 * 0	搜索中
	 * 1	监听中
	 * */
	public String GSM_TARGET_MONITOR_STATE_VALUE = "gsm_target_monitor_state_value";	
	/**
	 * CDMA目标监听状态值
	 * 0	搜索中
	 * 1	监听中 
	 * */
	public String CDMA_TARGET_MONITOR_STATE_VALUE = "cdma_target_monitor_state_value";
	
	/**
	 * LTE小区搜索捕获模式
	 * 0	新捕获
	 * 1	常规捕获
	 * 2	精准捕获
	 */
	public String LTE_SEARCH_CELL_MODE = "lte_search_cell_mode";
	
	/**
	 * 记录上一次目标号码和记录时间
	 */
	public String LAST_TARGET_NUMBER = "last_target_number_record_time";
	
	/**
	 * 频点历史记录开关
	 */
	public String LTE_ADD_FREQ_TO_HISTORY = "lte_add_freq_to_history";
	
	//----------------------系统设置---------------------------------------------
	/** 电量过低  */
	public String HOST_DEFICIENCY_VALUE_SCZD = "host_deficiency_value_sczd";
	
	/** 温度过高 */
	public String TEMP_HIGH_VALUE_SCZD = "temp_high_value_sczd";
	
	/** 目标能量语音报数提示开关 */
	public String VOICE_COUNT_OFF_SCZD = "voice_count_off_sczd";
	
	/** 接近目标语音提示开关  */
	public String NEAR_TARGET_OFF_SCZD = "near_target_off_sczd";
	
	/** 温度过高语音提示开关 */
	public String TEMP_HIGH_OFF_SCZD = "temp_high_off_sczd";
	
	/** 电量过低语音提示开关 */
	public String HOST_DEFICIENCY_OFF_SCZD = "host_deficiency_sczd";
	
	/** 设备自检开关 */
	public String DEVICE_SELF_CHECKING_OFF_SCZD = "device_self_checking_off";
	
}
