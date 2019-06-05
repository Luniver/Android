package hrst.sczd.agreement.sczd.vo;

/**
 * PDA授时请求应答
 * @author glj
 * 2018-01-15
 */
public class PDA_TO_MCU_TIMING_ACK {
	
	/** 0：授时成功 1：授时失败 */
	private byte cResult_a;

	public byte getcResult_a() {
		return cResult_a;
	}

	public void setcResult_a(byte cResult_a) {
		this.cResult_a = cResult_a;
	}

	@Override
	public String toString() {
		return "PDA_TO_MCU_TIMING_ACK [cResult_a=" + cResult_a + "]";
	}

}
