package hrst.sczd.agreement.sczd.vo;



/**
 * PDA查询TF卡中的存数文件列表
 * @author glj
 * 2018-01-15
 */
public class PDA_TO_MCU_QUERY_TF_KEEP_NUMBER_FILE_LIST {
	
	//参数	查询第几个(从1开始)文件名	2字节
	private short ucFileName_a;

	public short getUcFileName_a() {
		return ucFileName_a;
	}

	public void setUcFileName_a(short ucFileName_a) {
		this.ucFileName_a = ucFileName_a;
	}

	@Override
	public String toString() {
		return "PDA_TO_MCU_QUERY_TF_KEEP_NUMBER_FILE_LIST [ucFileName_a="
				+ ucFileName_a + "]";
	}
}
