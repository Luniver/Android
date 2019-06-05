package hrst.sczd.agreement.sczd;


/**
 * @author 赵耿忠
 * @describe 保存MCU报头信息的实体类
 * @date 2014.05.23
 * @version 1.1.1.3
 * 修改者，修改日期，修改内容
*/
//参考mcu3页报文头
public class McuHeader {
	// uint32
	public long uiSynHead_a = 3704445605l;
	// uint16
	public char usSegLength_b;
	// uint8
	public short ucPro_c = 0x16;
	// uint8
	public short ucCmd_d;

	public long getUiSynHead_a() {
		return uiSynHead_a;
	}

	public void setUiSynHead_a(long uiSynHead_a) {
		this.uiSynHead_a = uiSynHead_a;
	}

	public char getUsSegLength_b() {
		return usSegLength_b;
	}

	public void setUsSegLength_b(char usSegLength_b) {
		this.usSegLength_b = usSegLength_b;
	}

	public short getUcPro_c() {
		return ucPro_c;
	}

	public void setUcPro_c(short ucPro_c) {
		this.ucPro_c = ucPro_c;
	}

	public short getUcCmd_d() {
		return ucCmd_d;
	}

	public void setUcCmd_d(short ucCmd_d) {
		this.ucCmd_d = ucCmd_d;
	}

	@Override
	public String toString() {
		return "McuHeader [uiSynHead_a=" + uiSynHead_a + ", usSegLength_b="
				+ usSegLength_b + ", ucPro_c=" + ucPro_c + ", ucCmd_d="
				+ ucCmd_d + "]";
	}

}