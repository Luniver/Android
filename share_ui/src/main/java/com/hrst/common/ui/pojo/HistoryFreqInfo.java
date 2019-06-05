package com.hrst.common.ui.pojo;

public class HistoryFreqInfo {

	private int id;

	// 用户id
	private int user_id;

	// 制式类型
	private int protocol_type_sign;

	// 频点
	private int freq;

	// 描述
	private String describe;

	// 记录时间
	private String record_time;

	// 状态
	private int state;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public int getProtocol_type_sign() {
		return protocol_type_sign;
	}

	public void setProtocol_type_sign(int protocol_type_sign) {
		this.protocol_type_sign = protocol_type_sign;
	}

	public int getFreq() {
		return freq;
	}

	public void setFreq(int freq) {
		this.freq = freq;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public String getRecord_time() {
		return record_time;
	}

	public void setRecord_time(String record_time) {
		this.record_time = record_time;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
}
