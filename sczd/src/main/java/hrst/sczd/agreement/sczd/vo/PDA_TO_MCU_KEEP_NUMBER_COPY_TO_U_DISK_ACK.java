package hrst.sczd.agreement.sczd.vo;

public class PDA_TO_MCU_KEEP_NUMBER_COPY_TO_U_DISK_ACK {
/*	0xFF　U盘空间不足
	0xFE  复制失败
	0 ~ 100(%)复制进度*/
	
	private byte cResult_a;

	public byte getcResult_a() {
		return cResult_a;
	}

	public void setcResult_a(byte cResult_a) {
		this.cResult_a = cResult_a;
	}

	@Override
	public String toString() {
		return "PDA_TO_MCU_KEEP_NUMBER_COPY_TO_U_DISK_ACK [cResult_a="
				+ cResult_a + "]";
	}

}
