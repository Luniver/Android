package hrst.sczd.agreement.sczd.vo;

import java.util.Arrays;

/**
 * PDA查询TF卡中的存数文件列表应答
 * 
 * @author glj 2018-01-15
 */
public class PDA_TO_MCU_QUERY_TF_KEEP_NUMBER_FILE_LIST_ACK {
	/*
	 * 参数 总共文件数 
	 * 参数 n>0: 第n个文件
	 *
	 *  文件名 
	 *  文件名字符(不含路径) 20字节
	 */

	private short totalFile_a;

	private short fileNumber_b;

	private byte[] cFileName_c = new byte[20];

	public short getTotalFile_a() {
		return totalFile_a;
	}

	public void setTotalFile_a(short totalFile_a) {
		this.totalFile_a = totalFile_a;
	}

	public short getFileNumber_b() {
		return fileNumber_b;
	}

	public void setFileNumber_b(short fileNumber_b) {
		this.fileNumber_b = fileNumber_b;
	}

	public byte[] getcFileName_c() {
		return cFileName_c;
	}

	public void setcFileName_c(byte[] cFileName_c) {
		this.cFileName_c = cFileName_c;
	}

	@Override
	public String toString() {
		return "PDA_TO_MCU_QUERY_TF_KEEP_NUMBER_FILE_LIST_ACK{"
				+ "totalFile_a=" + totalFile_a + ", fileNumber_b="
				+ fileNumber_b + ", cFileName_c="
				+ Arrays.toString(cFileName_c) + '}';
	}

}
