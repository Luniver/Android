package hrst.sczd.agreement.sczd.vo;

/**
 * PDA请求U盘大小应答
 * @author glj
 * 2018-01-15
 */
public class PDA_TO_MCU_REQUEST_U_DISK_SIZE_ACK {
/*	参数1	U盘容量大小(单位M)	4字节
	参数2	U盘剩余容量大小(单位M)	4字节*/
	
	private int UDiskSize_a;
	
	private int UDiskRestSize_b;

	public int getUDiskSize_a() {
		return UDiskSize_a;
	}

	public void setUDiskSize_a(int uDiskSize_a) {
		UDiskSize_a = uDiskSize_a;
	}

	public int getUDiskRestSize_b() {
		return UDiskRestSize_b;
	}

	public void setUDiskRestSize_b(int uDiskRestSize_b) {
		UDiskRestSize_b = uDiskRestSize_b;
	}

	@Override
	public String toString() {
		return "PDA_TO_MCU_REQUEST_U_DISK_SIZE_ACK [UDiskSize_a=" + UDiskSize_a
				+ ", UDiskRestSize_b=" + UDiskRestSize_b + "]";
	}
}
