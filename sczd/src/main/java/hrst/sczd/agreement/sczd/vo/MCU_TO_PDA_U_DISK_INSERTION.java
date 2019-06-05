package hrst.sczd.agreement.sczd.vo;

/**
 * 设备通知PDA有入盘插入
 * @author glj
 * 2018-01-15
 */
public class MCU_TO_PDA_U_DISK_INSERTION {
	
/*	参数	0:  U盘插入
		1:	U盘拔出	1字节*/
	
	public byte cResult_a;

	public byte getcResult_a() {
		return cResult_a;
	}

	public void setcResult_a(byte cResult_a) {
		this.cResult_a = cResult_a;
	}

	@Override
	public String toString() {
		return "MCU_TO_PDA_U_DISK_INSERTION [cResult_a=" + cResult_a + "]";
	}
}
