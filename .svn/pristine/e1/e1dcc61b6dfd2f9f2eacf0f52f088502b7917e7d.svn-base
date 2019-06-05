package hrst.sczd.agreement.sczd.vo;

import hrst.sczd.utils.ByteUtils;

/**
 * PDA授时请求
 * @author glj
 * 2018-01-15
 */
public class PDA_TO_MCU_TIMING {
	
	/** 时间(16进制年月日_时分0000)，如:20180104_15540000 */
	private String asTime_a = "";

	public String getAsTime_a() {
		return asTime_a;
	}

	public void setAsTime_a(String asTime_a) {
		/**
		 * 需要转成16进制
		 */
		try {
			this.asTime_a = new String(ByteUtils.hexs(asTime_a));
		} catch (Exception e) {
			e.printStackTrace();
		}
//		System.out.println("goo time str:" + toString());
	}

	@Override
	public String toString() {
		return "PDA_TO_MCU_TIMING [asTime_a=" + asTime_a + "]";
	}
	
}
