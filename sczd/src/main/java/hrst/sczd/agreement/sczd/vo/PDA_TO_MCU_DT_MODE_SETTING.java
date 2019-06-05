package hrst.sczd.agreement.sczd.vo;

import java.util.Arrays;

/**
 * PDA数传模式设置 
 * @author glj
 * 2018-01-15
 */
public class PDA_TO_MCU_DT_MODE_SETTING {
	/*	float fFreq;   
	uint8 ucDtMode; 
	uint8 ucTranMode; 
	uint8 ucRsvd[2];*/
	
	private float fFreq_a;

	/**
	 * 0- 关闭 1-自主数传 2-数传模块(已弃用)
 	 */
	private short ucDtMode_b;

	/**
	 * 0 发送模式
	 * 1 接收模式
	 */
	private short ucTranMode_c;
	
	private short[] ucRsvd_d = new short[2];

	public float getfFreq_a() {
		return fFreq_a;
	}

	public void setfFreq_a(float fFreq_a) {
		this.fFreq_a = fFreq_a;
	}

	public short getUcDtMode_b() {
		return ucDtMode_b;
	}

	public void setUcDtMode_b(short ucDtMode_b) {
		this.ucDtMode_b = ucDtMode_b;
	}

	public short getUcTranMode_c() {
		return ucTranMode_c;
	}

	public void setUcTranMode_c(short ucTranMode_c) {
		this.ucTranMode_c = ucTranMode_c;
	}

	public short[] getUcRsvd_d() {
		return ucRsvd_d;
	}

	public void setUcRsvd_d(short[] ucRsvd_d) {
		this.ucRsvd_d = ucRsvd_d;
	}

	@Override
	public String toString() {
		return "PDA_TO_MCU_DT_MODE_SETTING{" +
				"fFreq_a=" + fFreq_a +
				", ucDtMode_b=" + ucDtMode_b +
				", ucTranMode_c=" + ucTranMode_c +
				", ucRsvd_d=" + Arrays.toString(ucRsvd_d) +
				'}';
	}
}
