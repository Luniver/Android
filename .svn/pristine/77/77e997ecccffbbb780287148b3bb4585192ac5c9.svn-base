package com.hrst.common.ui.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.hrst.common.ui.database.DataAccessJDBC;
import com.hrst.common.ui.database.DatabaseGlobal;
import com.hrst.common.ui.pojo.OperateRecordInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author 关玲基
 * @describe 操作记录Dao
 * @date 2016.04.27
 */
public class OperateRecordDao {
	private static final String TAG = "OperateRecordDao";
    /** 数据库操作基础类 */
    private DataAccessJDBC dataAccessJDBC;
    /** 上下文 */
    private Context context;
    
    /**
     * 构造方法
     * 
     * @param context
     *            上下文
     */
    public OperateRecordDao(Context context){
    	this.context = context;
    	openDataBase();
    }
    
    /**
     * 添加操作记录
     * @param function
     * 			功能
     * @param standard
     * 			制式
     * @param targetPhoneNumber
     * 			目标号码
     * @param inducedPhoneNumber
     * 			诱发号码
     * @param ESN
     * 			ESN码
     * @param IMSI
     * 			IMSI码
     * @param IMEI
     * 			IMEI码
     * @return
     */
    public void insertOperateRecord(String function, String standard,
    		String targetPhoneNumber, String inducedPhoneNumber,String ESN, String IMSI,
    		String IMEI){
    	ContentValues values = new ContentValues();
    	values.put(DatabaseGlobal.operateRecordField[1], setRecordTime());
    	values.put(DatabaseGlobal.operateRecordField[2], function);
    	values.put(DatabaseGlobal.operateRecordField[3], standard);
    	values.put(DatabaseGlobal.operateRecordField[4], targetPhoneNumber);
    	values.put(DatabaseGlobal.operateRecordField[5], inducedPhoneNumber);
    	values.put(DatabaseGlobal.operateRecordField[6], ESN);
    	values.put(DatabaseGlobal.operateRecordField[7], IMSI);
    	values.put(DatabaseGlobal.operateRecordField[8], IMEI);
    	dataAccessJDBC.insert(DatabaseGlobal.operateRecordTable, values);
    }
    

    private List<OperateRecordInfo> data = null;
    private OperateRecordInfo mOperateRecordInfo;
    /**
     * 获取所有操作记录
     */
    public List<OperateRecordInfo> queryOperateRecord(){
    	data = new ArrayList<OperateRecordInfo>();
    	Cursor cursor = dataAccessJDBC.queryAlldata("select * from " + DatabaseGlobal.operateRecordTable + " order by " + DatabaseGlobal.operateRecordField[1] + " desc", null);
    	while (cursor.moveToNext()) {
    		mOperateRecordInfo = new OperateRecordInfo();
    		mOperateRecordInfo.setRecordDate(cursor.getString(cursor.getColumnIndex(DatabaseGlobal.operateRecordField[1])));
    		mOperateRecordInfo.setFunction(cursor.getString(cursor.getColumnIndex(DatabaseGlobal.operateRecordField[2])));
    		mOperateRecordInfo.setStandard(cursor.getString(cursor.getColumnIndex(DatabaseGlobal.operateRecordField[3])));
    		mOperateRecordInfo.setTargetPhoneNumber(cursor.getString(cursor.getColumnIndex(DatabaseGlobal.operateRecordField[4])));
    		mOperateRecordInfo.setInducedPhoneNumber(cursor.getString(cursor.getColumnIndex(DatabaseGlobal.operateRecordField[5])));
    		mOperateRecordInfo.setESN(cursor.getString(cursor.getColumnIndex(DatabaseGlobal.operateRecordField[6])));
    		mOperateRecordInfo.setIMIS(cursor.getString(cursor.getColumnIndex(DatabaseGlobal.operateRecordField[7])));
    		mOperateRecordInfo.setIMEI(cursor.getString(cursor.getColumnIndex(DatabaseGlobal.operateRecordField[8])));
    		data.add(mOperateRecordInfo);
		}
    	cursor.close();
    	return data;
    }
    
    public List<OperateRecordInfo> querySelectOperateRecord(String startTime, String endTime){
    	data = new ArrayList<OperateRecordInfo>();
    	
//    	select * from ReturnGoodsMains where datetime(ReturnDate)>='2012-12-02 00:00:00' and 
//    	datetime(ReturnDate)<'2012-12-03 00:00:00' ;
    	
//    	select * from operateRecord where datetime(Date)>='2016/04/27 11:07:13'
//    	and dat order by Date datetime(Date)<='2016/04/28 11:07:13'
    	Cursor cursor = dataAccessJDBC.queryAlldata(
		"select * from " + DatabaseGlobal.operateRecordTable +" where "
		+ DatabaseGlobal.operateRecordField[1] + ">='" + startTime +"' and "
		+ "" + DatabaseGlobal.operateRecordField[1] + "<='" + endTime + "'"  +
		" order by " + DatabaseGlobal.operateRecordField[1] + " desc" 
		, null);
    	while (cursor.moveToNext()) {
    		mOperateRecordInfo = new OperateRecordInfo();
    		mOperateRecordInfo.setRecordDate(cursor.getString(cursor.getColumnIndex(DatabaseGlobal.operateRecordField[1])));
    		mOperateRecordInfo.setFunction(cursor.getString(cursor.getColumnIndex(DatabaseGlobal.operateRecordField[2])));
    		mOperateRecordInfo.setStandard(cursor.getString(cursor.getColumnIndex(DatabaseGlobal.operateRecordField[3])));
    		mOperateRecordInfo.setTargetPhoneNumber(cursor.getString(cursor.getColumnIndex(DatabaseGlobal.operateRecordField[4])));
    		mOperateRecordInfo.setInducedPhoneNumber(cursor.getString(cursor.getColumnIndex(DatabaseGlobal.operateRecordField[5])));
    		mOperateRecordInfo.setESN(cursor.getString(cursor.getColumnIndex(DatabaseGlobal.operateRecordField[6])));
    		mOperateRecordInfo.setIMIS(cursor.getString(cursor.getColumnIndex(DatabaseGlobal.operateRecordField[7])));
    		mOperateRecordInfo.setIMEI(cursor.getString(cursor.getColumnIndex(DatabaseGlobal.operateRecordField[8])));
    		data.add(mOperateRecordInfo);
		}
    	cursor.close();
    	return data;
    }   
    
    /**
     * 设置操作记录的时间
     * 格式：年/月/日  时:分:秒
     */
    private String setRecordTime() {
    	//获取系统当前时间
        Date date = new Date();
        //设置日期格式
        SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return sf.format(date).toString();
    }
    
    /**
     * 打开数据库
     * 
     */
    private void openDataBase() {
        dataAccessJDBC = new DataAccessJDBC(context,DatabaseGlobal.DataBaseName);
        boolean groupcreate = dataAccessJDBC
                .createTable(DatabaseGlobal.operateRecordCreate);
    }

    /**
     * 关闭数据库
     * 
     */
    public void closeDataBase() {
        dataAccessJDBC.close();
    }
}
