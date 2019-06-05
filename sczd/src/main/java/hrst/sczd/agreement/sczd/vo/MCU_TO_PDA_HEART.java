package hrst.sczd.agreement.sczd.vo;

import java.util.Arrays;

/**
 * MCU对PDA心跳
 * @author glj
 * 2018-1-15
 */
public class MCU_TO_PDA_HEART {
	
	/** 系统正常: 0 	MCU启动中:1  FPGA不在线:2*/
	private byte cHeartState_a;

	private byte[] deviceState_b = new byte[8];
	public byte getcHeartState_a() {
		return cHeartState_a;
	}

	public void setcHeartState_a(byte cHeartState_a) {
		this.cHeartState_a = cHeartState_a;
	}

	public byte[] getDeviceState_b() {
		return deviceState_b;
	}

	public void setDeviceState_b(byte[] deviceState_b) {
		this.deviceState_b = deviceState_b;
	}

	@Override
	public String toString() {
		return "MCU_TO_PDA_HEART [cHeartState_a=" + cHeartState_a
				+ ", deviceState_b=" + Arrays.toString(deviceState_b) + "]";
	}

}
