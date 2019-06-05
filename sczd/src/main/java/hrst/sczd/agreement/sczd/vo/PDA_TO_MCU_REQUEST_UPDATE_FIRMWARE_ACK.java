package hrst.sczd.agreement.sczd.vo;

/**
 * PDA请求U盘中固件文件名应答
 * @author glj
 * 2018-01-15
 */
public class PDA_TO_MCU_REQUEST_UPDATE_FIRMWARE_ACK {
	
/*	0xFF文件校验失败
	0xFE 固件升级失败
	0 ~ 100(%)固件升级进度*/
	
	private byte cResult_a;

	public byte getcResult_a() {
		return cResult_a;
	}

	public void setcResult_a(byte cResult_a) {
		this.cResult_a = cResult_a;
	}

	@Override
	public String toString() {
		return "PDA_TO_MCU_REQUEST_UPDATE_FIRMWARE_ACK [cResult_a=" + cResult_a
				+ "]";
	}
	
}
