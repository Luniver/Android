package hrst.sczd.agreement.sczd.vo;

/**
 * PDA请求制式设置
 * @author glj
 * 2018-01-15
 */
public class PDA_TO_MCU_REQUEST_MODE_SETTING {

/*	参数	1: LTE
		2: CDMA
		3: GSM
		4: WCDMA
		5: TD	1字节*/
	private byte cMode_a;

	public byte getcMode_a() {
		return cMode_a;
	}

	public void setcMode_a(byte cMode_a) {
		this.cMode_a = cMode_a;
	}

	@Override
	public String toString() {
		return "PDA_TO_MCU_REQUEST_MODE_SETTING [cMode_a=" + cMode_a + "]";
	}
	
}
