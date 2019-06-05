
package hrst.sczd.utils;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
/**
 * @author 刘兰
 * @describe 保存数据到内存
 * @date 2014.05.23
 * @version 1.1.1.3
 * 修改者，修改日期，修改内容
*/
public class SaveData_withPreferences extends Activity {
	private Context context;
	private static SaveData_withPreferences model;

	/**
	 * 构造方法
	 * 
	 * @param context 上下文
	 */
	private SaveData_withPreferences(Context context) {
		this.context = context;
	}

	/**
	 * 单例
	 * 
	 * @param context 上下文
	 * @return SaveData_withPreferences
	 */
	public static SaveData_withPreferences getInstance(Context context) {
		if (model == null) {
			model = new SaveData_withPreferences(context);
		}
		return model;
	}

	/**
	 * 保存数据
	 * 
	 * @param key 
	 * @param value
	 */
	public void saveDatas(String key, int value) {
		SharedPreferences uiState = context
				.getSharedPreferences("user_info", 0);
		SharedPreferences.Editor editor = uiState.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	/**
	 * 保存boolean类型的数据
	 * 
	 * @param key
	 * @param value
	 */
	public void saveDatas_Boolean(String key, boolean value) {
		SharedPreferences uiState = context
				.getSharedPreferences("user_info", 0);
		SharedPreferences.Editor editor = uiState.edit();

		editor.putBoolean(key, value);

		editor.commit();
	}

	/**
	 * 保存手机号码
	 * 
	 * @param key
	 * @param value
	 */
	public void saveDatas_phone(String key, String value) {
		SharedPreferences uiState = context
				.getSharedPreferences("user_info", 0);
		SharedPreferences.Editor editor = uiState.edit();
		editor.putString(key, value);
		editor.commit();
	}

	/**
	 * 获取手机号码
	 * 
	 * @param key
	 * @param defValue
	 * @return String 手机号码
	 */
	public String getDatas_phone(String key, String defValue) {

		SharedPreferences share = context.getSharedPreferences("user_info", 0);

		return share.getString(key, defValue);
	}

	/**
	 * 保存String类型的数据
	 * 
	 * @param key
	 * @param value
	 */
	public void saveDatas_String(String key, String value) {
		SharedPreferences uiState = context
				.getSharedPreferences("user_info", 0);
		SharedPreferences.Editor editor = uiState.edit();

		editor.putString(key, value);

		editor.commit();
	}

	/**
	 * 保存long类型的数据
	 * 
	 * @param key
	 * @param value
	 */
	public void saveDatas_long(String key, Long value) {
		SharedPreferences uiState = context
				.getSharedPreferences("user_info", 0);
		SharedPreferences.Editor editor = uiState.edit();

		editor.putLong(key, value);

		editor.commit();
	}
	
	/**
	 * 保存字符串数据
	 */
	public void saveDatas_StringArray (String key, String[] str) {
		SharedPreferences uiState = context
				.getSharedPreferences("user_info", 0);
		String regularEx = "#";
		String data = "";
		if (str!=null && str.length > 0) {
			
		}
		for (String string : str) {
			data += string;
			data += regularEx;
		}
		
		SharedPreferences.Editor editor = uiState.edit();
		
		editor.putString(key, data);
		
		editor.commit();
	}

	public String[] getDatas_StringArray(String key) {
		String regularEx = "#";
		String[] str = null;
		SharedPreferences sp = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
		String values; 
		values = sp.getString(key, ""); 
		str = values.split(regularEx);
		return str;
	}
	
	/**
	 * 获取long类型的数据
	 * 
	 * @param key
	 * @return Long
	 */
	public Long getData_long(String key) {
		SharedPreferences share = context.getSharedPreferences("user_info", 0);

		return share.getLong(key, 0);
	}

	/**
	 * 获取long类型的数据，有默认值
	 * 
	 * @param key
	 * @param dufault 默认值
	 * @return Long
	 */
	public Long getData_long(String key, long dufault) {
		SharedPreferences share = context.getSharedPreferences("user_info", 0);
		return share.getLong(key, dufault);
	}

	/**
	 * 获取int类型的数据
	 * 
	 * @param key
	 * @return int
	 */
	public int getData_int(String key) {
		SharedPreferences share = context.getSharedPreferences("user_info", 0);

		return share.getInt(key, 0);
	}

	public boolean getData_Boolean(String key) {
	    return getData_Boolean(key, false);
    }
	/**
	 * 获取Boolean类型的数据
	 * 
	 * @param key
	 * @return Boolean
	 */
	public boolean getData_Boolean(String key, boolean defValue) {
		try {
			SharedPreferences share = context.getSharedPreferences("user_info", 0);
			return share.getBoolean(key, defValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 获取String类型的数据
	 * 
	 * @param key
	 * @return String
	 */
	public String getData_String(String key) {
		return getData_String(key, "");
	}

	public String getData_String(String key, String defaultValue) {
		SharedPreferences share = context.getSharedPreferences("user_info", 0);
		return share.getString(key, defaultValue);
	}
}
