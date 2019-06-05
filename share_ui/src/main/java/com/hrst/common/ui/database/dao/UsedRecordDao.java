package com.hrst.common.ui.database.dao;

import android.content.ContentValues;
import android.content.Context;

import com.hrst.common.ui.database.DataAccessJDBC;
import com.hrst.common.ui.database.DatabaseGlobal;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 赵耿忠
 * @describe 使用记录操作Dao
 * @date 2014.05.23
 * @version 1.1.1.3 修改者，修改日期，修改内容
 */
public class UsedRecordDao {
    /** 数据库操作基础类 */
    private DataAccessJDBC dataAccessJDBC;
    /** 上下文 */
    private Context context;
    // 开始日期
    public static String targetDate;
    // 开始时间
    public static String startTime;
    // 开始总秒数
    public static int startSecond;

    /**
     * 构造方法
     * 
     * @param context
     *            上下文
     */
    public UsedRecordDao(Context context) {
        this.context = context;
        openDataBase();
    }

    /**
     * 添加使用记录
     * 
     */
    public boolean saveusedRecord(final String phoneNumber) {
        if (targetDate != null && !targetDate.equals("") && phoneNumber != null
                && !phoneNumber.equals("null") && !phoneNumber.equals("")
                && phoneNumber.length() == 11) {
            ContentValues values = new ContentValues();
            values.put(DatabaseGlobal.usedRecordField[1], phoneNumber);
            values.put(DatabaseGlobal.usedRecordField[2], targetDate);
            values.put(DatabaseGlobal.usedRecordField[3], startTime);
            values.put(DatabaseGlobal.usedRecordField[4], getStopTime());
            values.put(DatabaseGlobal.usedRecordField[5], 1);
            boolean saveBoo = dataAccessJDBC.insert(
                    DatabaseGlobal.usedRecordTable, values);
            UsedRecordDao.targetDate = null;
            return saveBoo;
        } else {
            return false;
        }
    }
    
    /**
     * 更新号码使用记录的时间
     * @param phoneNumber			目标手机号码
     */
//    public void updateUserRecord (final String phoneNumber, String useTime) {
//    	String[] data = new String[]{phoneNumber};
//    	String sql = "select * from "+DatabaseGlobal.usedRecordTable+" where "
//    				+DatabaseGlobal.usedRecordField[1]+"=?";
//    	//查询是否存在当前号码
//    	Cursor cursor = dataAccessJDBC.query(sql, data);
//    	//存在
//    	if (cursor.getCount() > 0) {
//    		//修改记录
//    		System.out.println("存在"+cursor.getCount());
//    		while (cursor.moveToNext()) {
//				cursor.getString(cursor.getColumnIndex(""));
//				
//			}
//    	} 
//    	//不存在
//    	else {
//    		//添加记录
//    		saveusedRecord(phoneNumber);
//    	}
//    	
//    }

    /**
     * 计算持续时间
     * 
     * @return String 返回持续时间文本
     */
    private String getStopTime() {
        String stopTime = "";
        Calendar c = Calendar.getInstance();
        int stop = (int) c.getTimeInMillis() / 1000 - startSecond;
        if (stop / 3600 != 0) {
            stopTime = stopTime + stop / 3600 + "时";
            stop = stop / 3600;
        }
        if (stop / 60 != 0) {
            stopTime = stopTime + stop / 60 + "分";
            stop = stop / 60;
        }
        if (stop != 0) {
            stopTime = stopTime + stop + "秒";
        }
        if (stopTime.equals("")) {
            stopTime = "0秒";
        }
        return stopTime;
    }

    /**
     * 设置开始时间
     * 
     */
    public static void setStartTime() {
        Date date = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        targetDate = sf.format(date);
        sf = new SimpleDateFormat("HH:mm:ss");
        startTime = sf.format(date);
        Calendar c = Calendar.getInstance();
        startSecond = (int) c.getTimeInMillis() / 1000;
    }

    /**
     * 获取使用记录时间分组与个数
     * 
     * @param phoneNumber
     *            手机号码
     * @return List 返回列表
     */
    public List<Map<String, Object>> getusedRecordGroup(String phoneNumber) {
        // 字段 某一天（个数)
        String[] field = new String[] { "sum(1)",
                DatabaseGlobal.usedRecordField[2] };
        String where = null;
        String[] whereargs = null;
        // 如果有号码，则按号码获取
        if (phoneNumber != null) {
            where = DatabaseGlobal.usedRecordField[1] + "=?";
            whereargs = new String[] { phoneNumber };
        }
        // 数据
        List<Map<String, Object>> list = dataAccessJDBC.query(
                DatabaseGlobal.usedRecordTable, field, where, whereargs,
                DatabaseGlobal.usedRecordField[2],
                DatabaseGlobal.usedRecordField[0] + " desc");
        return list;
    }

    /**
     * 获取某一天的使用记录信息
     * 
     * @param date
     *            某一天的时间
     * @param phoneNumber
     *            手机号码
     * @return List 使用记录列表
     */
    public List<Map<String, Object>> getTimeUsedRecord(String date,
            String phoneNumber) {
        String where = DatabaseGlobal.usedRecordField[2] + "=?";
        String[] whereargs = new String[] { date };
        // 如果有号码，则按号码获取
        if (phoneNumber != null) {
            where = where + " and " + DatabaseGlobal.usedRecordField[1] + "=?";
            whereargs = new String[] { date, phoneNumber };
        }
        List<Map<String, Object>> list = dataAccessJDBC.query(
                DatabaseGlobal.usedRecordTable, DatabaseGlobal.usedRecordField,
                where, whereargs, null, DatabaseGlobal.usedRecordField[0]
                        + " desc");
        return list;
    }

    /**
     * 获取指定时间内的使用记录数据
     * 
     * @param date
     *            指定时间
     * @param phoneNumber
     *            手机号码
     * @return List 使用记录列表
     */
    public List<Map<String, Object>> getWithinUsedRecord(String date,
            String phoneNumber) {
        String where = null;
        String[] whereargs = null;
        // 选择全部则不需要条件
        if (date != null) {
            where = "datetime(TargetDate)>=datetime(?)";
            whereargs = new String[] { date };
        }
        // 如果有号码，则按号码获取
        if (phoneNumber != null) {
            if (where != null) {
                where = where + " and ";
                where = where + DatabaseGlobal.usedRecordField[1] + "=?";
                whereargs = new String[] { date, phoneNumber };
            } else {
                where = DatabaseGlobal.usedRecordField[1] + "=?";
                whereargs = new String[] { phoneNumber };
            }
        }

        List<Map<String, Object>> list = dataAccessJDBC.query(
                DatabaseGlobal.usedRecordTable, DatabaseGlobal.usedRecordField,
                where, whereargs, null, DatabaseGlobal.usedRecordField[0]
                        + " desc");
        return list;
    }

    /**
     * 打开数据库
     * 
     */
    private void openDataBase() {
        dataAccessJDBC = new DataAccessJDBC(context,DatabaseGlobal.DataBaseName);
        boolean groupcreate = dataAccessJDBC
                .createTable(DatabaseGlobal.usedRecordCreate);
    }

    /**
     * 关闭数据库
     * 
     */
    public void closeDataBase() {
        dataAccessJDBC.close();
    }
}
