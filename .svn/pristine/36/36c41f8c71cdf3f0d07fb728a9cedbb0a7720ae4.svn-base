package hrst.sczd.utils;

/**
 * @author 赵耿忠
 * @describe 字节转换的工具类的工具类
 * @date 2014.05.23
 * @version 1.1.1.3
 * 修改者，修改日期，修改内容
*/
public class ByteUtils {
    
    /**
     * 将byte[]转换为字符串
     * 
     * @param arrB 
     * @return String
     * @throws Exception
     */
	public static String byteArr2HexStr(byte[] arrB) throws Exception {
		int iLen = arrB.length;
		// 每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍
		StringBuffer sb = new StringBuffer(iLen * 2);
		for (int i = 0; i < iLen; i++) {
			if(Integer.toHexString(arrB[i]&0xff).length()<2){
				sb.append("0" + Integer.toHexString(arrB[i]&0xff));
			}
			else{
				sb.append(Integer.toHexString(arrB[i]&0xff));
			}
			
		}
		return sb.toString();
	}
	
	/**
	 * 将String转换为Byte【】
	 * 
	 * @param str
	 * @return byte[]
	 * @throws Exception
	 */
	public static byte[] hexs(String str) throws Exception{{
		int iLen = str.length();
		// 两个字符表示一个字节，所以字节数组长度是字符串长度除以2
		byte[] arrOut = new byte[iLen/2];
		for (int i = 0; i < iLen-1; i = i + 2) {
			String strTmp = "" + str.charAt(i) + str.charAt(i+1);
			arrOut[i/2] = (byte) Integer.parseInt(strTmp, 16);
		}
		return arrOut;
		
	}}
}
