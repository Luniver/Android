package hrst.sczd.agreement.sczd.vo;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * PDA请求固件版本号应答
 * @author glj
 * 2018-01-15
 */
public class PDA_TO_MCU_REQUEST_VERSION_ACK {
	/**
     * 协议已修改: ( 因为一次性传90个字节很容易丢包)
     * 参数1 :  type 1 字节
     * 0: Linux系统固件
     * 1: FPGA固件版本
     * 2: Linux APP版本
     * 3: 硬件版本
     * 参数2:  版本信息 (64字节)
     */
    private byte cVersionType_a;
    private byte[] versionMessage_b = new byte[64];
	public byte getcVersionType_a() {
		return cVersionType_a;
	}
	public void setcVersionType_a(byte cVersionType_a) {
		this.cVersionType_a = cVersionType_a;
	}
	public byte[] getVersionMessage_b() {
		return versionMessage_b;
	}
	public void setVersionMessage_b(byte[] versionMessage_b) {
		this.versionMessage_b = versionMessage_b;
	}
	@Override
	public String toString() {
		try {
			return "PDA_TO_MCU_REQUEST_VERSION_ACK [cVersionType_a="
                    + cVersionType_a + ", versionMessage_b="
                    + new String(versionMessage_b, "utf-8") + "]";
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "PDA_TO_MCU_REQUEST_VERSION_ACK [cVersionType_a="
				+ cVersionType_a + ", versionMessage_b="
				+ Arrays.toString(versionMessage_b) + "]";
	}
 }
