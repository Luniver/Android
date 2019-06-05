package hrst.sczd.utils;

import java.util.Random;
import java.util.UUID;

/**
 * 
 * @author glj
 * 获取随机短信内容
 * 2016-09-01
 *
 */
public class SmsContent {
	//获取随机内容
	public static String getSmsContent () {
		UUID uuid = UUID.randomUUID();
		String str = uuid.toString();
		//去掉"-"符号
		String temp = str.substring(0, 8); 
		
		return temp;
	}
	
    //生成随机数字和字母,  
    public static String getStringRandom(int length) {  
          
        String val = "";  
        Random random = new Random();  
          
        //参数length，表示生成几位随机数  
        for(int i = 0; i < length; i++) {  
              
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";  
            //输出字母还是数字  
            if( "char".equalsIgnoreCase(charOrNum) ) {  
                //输出是大写字母还是小写字母  
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;  
                val += (char)(random.nextInt(26) + temp);  
            } else if( "num".equalsIgnoreCase(charOrNum) ) {  
                val += String.valueOf(random.nextInt(10));  
            }  
        }  
        return val;  
    } 
}
