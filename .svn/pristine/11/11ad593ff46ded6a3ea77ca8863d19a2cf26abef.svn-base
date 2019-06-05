package hrst.sczd.agreement.sczd.vo;

import java.util.Arrays;

/**
 * PDA请求U盘中固件文件名应答
 * @author glj
 * 2018-01-15
 */
public class PDA_TO_MCU_REQUEST_U_DISK_FIRMWARE_NAME_ACK {

/*	1: 整体固件文件名
	2: 固件文件不存在*/
	
	private byte cResult_a;
	
	/*固件文件名描述字符串*/
	private byte[] cDescribe_b = new byte[64];

	public byte getcResult_a() {
		return cResult_a;
	}

	public void setcResult_a(byte cResult_a) {
		this.cResult_a = cResult_a;
	}

	public byte[] getcDescribe_b() {
		return cDescribe_b;
	}

	public void setcDescribe_b(byte[] cDescribe_b) {
		this.cDescribe_b = cDescribe_b;
	}

	@Override
	public String toString() {
		return "PDA_TO_MCU_REQUEST_U_DISK_FIRMWARE_NAME_ACK [cResult_a="
				+ cResult_a + ", cDescribe_b=" + Arrays.toString(cDescribe_b)
				+ "]";
	}
}
