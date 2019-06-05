package hrst.sczd.agreement.sczd.vo;

/**
 * PDA 请求存数 
 * @author glj
 * 2018-01-15
 */
public class PDA_TO_MCU_REQUEST_KEEP_NUMBER {

    /**
     * 0x00 常规
     * 0x01 同步
     * 0xff 关闭
     */
	private int mode_a;
	
	private int length_b;

	public int getMode_a() {
		return mode_a;
	}

	public void setMode_a(int mode_a) {
		this.mode_a = mode_a;
	}

	public int getLength_b() {
		return length_b;
	}

	public void setLength_b(int length_b) {
		this.length_b = length_b;
	}

	@Override
	public String toString() {
		return "PDA_TO_MCU_REQUEST_KEEP_NUMBER [mode_a=" + mode_a
				+ ", length_b=" + length_b + "]";
	}
	
}
