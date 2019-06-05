package hrst.sczd.agreement.sczd.vo;

import java.util.Arrays;

/**
 * PDA在TF卡中删除指定的存数文件
 * @author glj
 * 2018-01-15
 */
public class PDA_TO_MCU_DELETE_THE_SPECIFIED_FILE_FOR_TF {
	
	/*参数	
	 * 要复制的文件总数     2字节
	 * 文件名字符串	20字节*/
	private short ucTotalFile_a;
	
	private byte[] cfileName_b = new byte[20];

	public short getUcTotalFile_a() {
		return ucTotalFile_a;
	}

	public void setUcTotalFile_a(short ucTotalFile_a) {
		this.ucTotalFile_a = ucTotalFile_a;
	}

	public byte[] getCfileName_b() {
		return cfileName_b;
	}

	public void setCfileName_b(byte[] cfileName_b) {
		this.cfileName_b = cfileName_b;
	}

	@Override
	public String toString() {
		return "PDA_TO_MCU_DELETE_THE_SPECIFIED_FILE_FOR_TF [ucTotalFile_a="
				+ ucTotalFile_a + ", cfileName_b="
				+ Arrays.toString(cfileName_b) + "]";
	}
	
}
