package hrst.sczd.utils;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.widget.Toast;
/**
 * @author 赵耿忠
 * @describe 用于判断手机号码
 * @date 2014.10.10
 * @version 1.1.1.3
 * 修改者，修改日期，修改内容
*/
public class SIMCardInfo {
    private TelephonyManager telephonyManager;
    private Context mContext;
    private String IMSI;
    private static SIMCardInfo model;
    public SIMCardInfo(Context context){
        mContext=context;
        telephonyManager=(TelephonyManager)context.getSystemService(context.TELEPHONY_SERVICE);
    }
    
    public static SIMCardInfo getInstance(Context context){
        if(model==null){
            model=new SIMCardInfo(context);
        }
        return model;
    }
    
    /**
     * 判断手机号码是否正确
     * 
     * @return
     */
    public Boolean isPhoneNumber(final String phoneNumber,final boolean toast,final String toastContent) {
        if (phoneNumber.equals("")) {
            if(toast){
                Toast.makeText(mContext, "请输入" + toastContent + "！", Toast.LENGTH_SHORT).show();
            }
            return false;
        } else if (!phoneNumber.startsWith("1") || phoneNumber.length() < 11) {
            if(toast){
                Toast.makeText(mContext, "请输入正确的" + toastContent + "！", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
        return true;
    }
    
    /**
     * 获取手机号码
     * @return
     */
    public String getPhoneNumber(){
        return telephonyManager.getLine1Number();
    }
    
    /**
     * 获取运营商信息
     * 需要权限<uses-permission android:name="android.permission.READ_PHONE_STATE" />
     * @return ，0=移动，1=联通，2=电信，3=无制式
     */
    public int getProvidersName(){
        int providersNameString=0;
        //返回Imsi
        IMSI=telephonyManager.getSubscriberId();
        //IMSI号前面3位460是国家，紧接着两个00，02是中国移动，01是中国联通，03是中国电信
        if(IMSI.startsWith("46000") || IMSI.startsWith("46002")){
            providersNameString=0;
        }
        else if(IMSI.startsWith("46001")){
            providersNameString=1;
        }
        else if(IMSI.startsWith("46003")){
            providersNameString=2;
        }
        else{
            providersNameString=3;
        }
        return providersNameString;
    }
    
    /**
     * 获取号码制式 返回0，代表移动; 1代表联通; 2代表电信; 3代表无制式
     * 
     * @param strPhoneNumber
     *            手机号码
     */
    public static int getPhoneFormat(String strPhoneNumber) {
    	
    	if (strPhoneNumber == null)
    	{
    		return -1;
    	}
    	
    	if (strPhoneNumber.length() < 4)
    	{
    	    return -1;	
    	}
    	
    	if (strPhoneNumber.length() != 11)
    	{
    		  return 3;
    	}
    	
        // 获取前三位号码
		String strTopThree = strPhoneNumber.substring(0, 3);
        String strTopFour = strPhoneNumber.substring(0, 4); //这里是新加的 2016.8.11
        int iPhoneFormat = 0;
        //17开头的是4G
        //14开头的事上网卡
        if (strTopThree.equals("131") || strTopThree.equals("130")
                || strTopThree.equals("132") || strTopThree.equals("155")
                || strTopThree.equals("156") || strTopThree.equals("186")
                
                || strTopThree.equals("185") || strTopThree.equals("176")
                || strTopThree.equals("145") //这里是新加的 7.25
                || strTopThree.equals("175") //2016-11-29
                //虚拟运营商
                || strTopFour.equals("1709") //这里是新加的 2016.8.11
                || strTopFour.equals("1704") //2016-11-29
                || strTopFour.equals("1707") //2016-11-29
                || strTopFour.equals("1708") //2016-11-29
                || strTopFour.equals("1709") //2016-11-29
                || strTopThree.equals("171") //2016-11-29
                || strTopThree.equals("166") //2018-06-01
                ) {// 判断是否为2G联通,3G联通
            iPhoneFormat = 1;
        } else if (strTopThree.equals("133") || strTopThree.equals("153")
                || strTopThree.equals("189")
                
                || strTopThree.equals("180") || strTopThree.equals("181")
                || strTopThree.equals("177") //这里是新加的 7.25
                || strTopFour.equals("1700") //这里是新加的 2016.8.11
                || strTopThree.equals("649") //这里是新加的 电信物联卡2016.8.11
                || strTopThree.equals("173") //2016-11-29
                || strTopThree.equals("149") //2016-11-29
                //虚拟运营商
                || strTopFour.equals("1701") //2016-11-29
                || strTopFour.equals("1702") //2016-11-29
                ) {// 判断是否为电信
            iPhoneFormat = 2;
        } else if (strTopThree.equals("134") || strTopThree.equals("135")
                || strTopThree.equals("136") || strTopThree.equals("137")
                || strTopThree.equals("138") || strTopThree.equals("139")
                || strTopThree.equals("150") || strTopThree.equals("151")
                || strTopThree.equals("152") || strTopThree.equals("158")
                || strTopThree.equals("159") || strTopThree.equals("182")
                || strTopThree.equals("183") || strTopThree.equals("187")
                || strTopThree.equals("157") || strTopThree.equals("188")
              
                || strTopThree.equals("184") || strTopThree.equals("178")
                || strTopThree.equals("147")   //这里是新加的 7.25
                || strTopFour.equals("1705")   //这里是新加的 2016.8.11
                
                || strTopFour.equals("1703") //2016-11-29
                || strTopFour.equals("1706") //2016-11-29
                ) {// 判断是否为2G移动,3G移动
            iPhoneFormat = 0;
        } else {
            iPhoneFormat = 3;
        }
        return iPhoneFormat;
    }
}
