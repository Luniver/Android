package hrst.sczd.agreement.sczd.vo;

/**
 * PDA把指定的存数文件复制到Ｕ盘应答 
 * @author glj
 * 2018-01-15
 */
public class PDA_TO_MCU_SELECT_KEEP_NUMBER_FILE_COPY_TO_U_DISK_ACK {

	/*	参数	0xFF　U盘空间不足
			0xFE  复制失败
			0xFD  TF卡中文件不存在
			0xFC  确定接收到请求
			0 ~ 100(%)复制进度	1字节*/
	
	private byte cResult_a;

	public byte getcResult_a() {
		return cResult_a;
	}

	public void setcResult_a(byte cResult_a) {
		this.cResult_a = cResult_a;
	}

	@Override
	public String toString() {
		return "PDA_TO_MCU_SELECT_KEEP_NUMBER_FILE_COPY_TO_U_DISK_ACK [cResult_a="
				+ cResult_a + "]";
	}

}
