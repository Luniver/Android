package hrst.sczd.agreement.sczd.vo;

/**
 * PDA在TF卡中删除指定的存数文件应答
 * @author glj
 * 2018-01-15
 */
public class PDA_TO_MCU_DELETE_THE_SPECIFIED_FILE_FOR_TF_ACK {

	/*	参数	0xFF　删除失败
			0xFE  TF卡中文件不存在
			0xFC  确定接收命令 	
			0 ~ 100 总的删除进度       1字节*/
	
	private byte cResult_a;

	public byte getcResult_a() {
		return cResult_a;
	}

	public void setcResult_a(byte cResult_a) {
		this.cResult_a = cResult_a;
	}

	@Override
	public String toString() {
		return "PDA_TO_MCU_DELETE_THE_SPECIFIED_FILE_FOR_TF_ACK [cResult_a="
				+ cResult_a + "]";
	}

}
