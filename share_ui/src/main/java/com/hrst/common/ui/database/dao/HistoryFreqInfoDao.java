package com.hrst.common.ui.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.hrst.common.ui.database.DataAccessJDBC;
import com.hrst.common.ui.database.DatabaseGlobal;
import com.hrst.common.ui.pojo.HistoryFreqInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author glj
 * 2016-07-19
 * 历史频点操作记录
 */
public class HistoryFreqInfoDao {
	
    /** 数据库操作基础类 */
    private static DataAccessJDBC dataAccessJDBC;
    /** 上下文 */
    private Context context;
    
    public HistoryFreqInfoDao (Context context) {
    	this.context = context;
    	openDataBase();
    }
	
    /**
     * 同步历史频点
     * @param freq		频点
     * @param user_id	用户id
     * @param protocol	制式	
     * @param operator	运营商
     * @param isFirst	是否首次
     * @return
     */
    public boolean syncFreq(int freq, int user_id, int protocol, int operator, boolean isFirst) {
    	boolean i = false;
    	System.out.println("protocol + (operator << 16)----->"+(protocol + (operator << 16))+ "\n" +
    			protocol+"\n"+
    			operator);
		try {
		  ContentValues values = new ContentValues();
		
		  values.put(DatabaseGlobal.T_HISTORYFREQ_USER_ID, user_id);
		  values.put(DatabaseGlobal.T_HISTORYFREQ_PROTOCOL_TYPE_SIGN, (protocol + (operator << 16)));
//		  values.put(DatabaseGlobal.T_HISTORYFREQ_OPERATOR,  operator);
		  values.put(DatabaseGlobal.T_HISTORYFREQ_FREQ, freq);
		  values.put(DatabaseGlobal.RECORD_TIME, dateFormat.format(new Date()));
		  values.put(DatabaseGlobal.STATE, 1);
		
		  i = dataAccessJDBC.insert(DatabaseGlobal.T_HISTORYFREQ, values);
		} catch (Exception e) {
		  // 如果是第一次出错就调用
			  if (isFirst) {
				  // 创建表
				  dataAccessJDBC.createTable(DatabaseGlobal.T_HISTORYFREQ_CREATESQL);
				  // 创建表重新执行
				  syncFreq(freq, user_id, protocol, operator, false);        
			  }
		  e.printStackTrace();
		}

		return i;
  	}
	
	/**
	   * 获取历史频点
	   * @param operator
	   * @param protocol
	   * @return
	   */
	  public List<HistoryFreqInfo> getHistoryFreqs(int operator, int protocol) {
	    // 初始化
	    List<HistoryFreqInfo> freqInfos = new LinkedList<HistoryFreqInfo>();

	    try {
	      // 根据条件拼装SQL
	      StringBuilder sqlSb = new StringBuilder();
	      sqlSb.append(" select distinct freq from " + DatabaseGlobal.T_HISTORYFREQ + " where state = 1 ");
	      sqlSb.append(" and " + DatabaseGlobal.T_HISTORYFREQ_PROTOCOL_TYPE_SIGN + " = " + (protocol + (operator << 16)));
//	      sqlSb.append(" and " + DatabaseGlobal.T_HISTORYFREQ_OPERATOR + " = " + operator);
	      sqlSb.append(" and " + DatabaseGlobal.T_HISTORYFREQ_FREQ + " <> 0 ");
	      sqlSb.append(" order by date(" + DatabaseGlobal.RECORD_TIME + ") desc, ");
	      sqlSb.append(" time(" + DatabaseGlobal.RECORD_TIME + ") desc ");
	      sqlSb.append(" limit 0, 20 ");

	      Log.i("sql", sqlSb.toString());

	      // 查询数据库获取数据
	      Cursor cursor = dataAccessJDBC.query(sqlSb.toString(), null);

	      if (cursor != null) {
	        // 循环遍历
	        while (cursor.moveToNext()) {
	          // 初始化
	          HistoryFreqInfo historyFreqInfo = new HistoryFreqInfo();
	          historyFreqInfo.setFreq(cursor.getInt(cursor.getColumnIndex(DatabaseGlobal.T_HISTORYFREQ_FREQ)));
	          freqInfos.add(historyFreqInfo);
	        }
	      }
	    } catch (Exception e) {
	      e.printStackTrace();
	      
	      freqInfos = new LinkedList<HistoryFreqInfo>();
	    } finally {
	    	//dataAccessJDBC.close();
	    }

	    return freqInfos;
	  }
	
	
    /**
     * 打开数据库
     */
    private void openDataBase() {
        dataAccessJDBC = new DataAccessJDBC(context,DatabaseGlobal.DataBaseName);
        dataAccessJDBC
                .createTable(DatabaseGlobal.T_HISTORYFREQ_CREATESQL);
    }
    
    /**
     * 关闭数据库
     */
    public void closeDataBase() {
        dataAccessJDBC.close();
    }
    
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
}
