package hrst.sczd.agreement.sczd.vo;

/**
 * PDA请求数传增益设置应答
 * @author glj
 * 2018-01-15
 */
public class PDA_TO_MCU_REQUEST_DT_GAIN_SETTING_ACK {
/*	参数	设置成功: 0
		设置失败:1	1字节*/
	
	private byte cResult_a;

	public byte getcResult_a() {
		return cResult_a;
	}

	public void setcResult_a(byte cResult_a) {
		this.cResult_a = cResult_a;
	}

	@Override
	public String toString() {
		return "PDA_TO_MCU_REQUEST_DT_GAIN_SETTING_ACK{" +
				"cResult_a=" + cResult_a +
				'}';
	}

}
